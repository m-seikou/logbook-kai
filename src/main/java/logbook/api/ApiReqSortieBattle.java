package logbook.api;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.json.JsonObject;

import logbook.bean.AppCondition;
import logbook.bean.AppConfig;
import logbook.bean.BattleLog;
import logbook.bean.BattleTypes.IFormation;
import logbook.bean.Ship;
import logbook.bean.ShipCollection;
import logbook.bean.SortieBattle;
import logbook.internal.PhaseState;
import logbook.proxy.RequestMetaData;
import logbook.proxy.ResponseMetaData;

/**
 * /kcsapi/api_req_sortie/battle
 *
 */
@API("/kcsapi/api_req_sortie/battle")
public class ApiReqSortieBattle implements APIListenerSpi {

    @Override
    public void accept(JsonObject json, RequestMetaData req, ResponseMetaData res) {
        JsonObject data = json.getJsonObject("api_data");
        if (data == null) {
            return;
        }
        AppCondition condition = AppCondition.get();
        BattleLog log = condition.getBattleResult();
        if (log == null) {
            return;
        }
        condition.setBattleCount(condition.getBattleCount() + 1);
        log.setBattleCount(condition.getBattleCount());
        log.setRoute(condition.getRoute());

        log.setBattle(SortieBattle.toBattle(data));
        // ローデータを設定する
        if (AppConfig.get().isIncludeRawData()) {
            BattleLog.setRawData(log, BattleLog.RawData::setBattle, data, req);
        }
        // 出撃艦隊
        Integer dockId = Optional.ofNullable(log.getBattle())
                .map(IFormation::getDockId)
                .orElse(1);
        // 艦隊スナップショットを作る
        BattleLog.snapshot(log, dockId);
        if (AppConfig.get().isApplyBattle()) {
            // 艦隊を更新
            PhaseState p = new PhaseState(log);
            p.apply(log.getBattle());
            ShipCollection.get()
                    .getShipMap()
                    .putAll(p.getAfterFriend().stream()
                            .filter(Objects::nonNull)
                            .collect(Collectors.toMap(Ship::getId, v -> v)));
        }
    }
}
