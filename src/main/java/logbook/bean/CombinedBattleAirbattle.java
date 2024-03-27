package logbook.bean;

import java.io.Serializable;
import java.util.List;

import javax.json.JsonObject;

import logbook.bean.BattleTypes.IAirBaseAttack;
import logbook.bean.BattleTypes.IAirBattle;
import logbook.bean.BattleTypes.ICombinedBattle;
import logbook.bean.BattleTypes.IFormation;
import logbook.bean.BattleTypes.ISortieBattle;
import logbook.bean.BattleTypes.ISupport;
import logbook.internal.JsonHelper;
import lombok.Data;

/**
 * 航空戦(連合艦隊)
 */
@Data
public class CombinedBattleAirBattle
    implements ICombinedBattle, ISortieBattle, IFormation, IAirBattle, ISupport, IAirBaseAttack, Serializable {

    private static final long serialVersionUID = 5422374810830868549L;

    /**
     * api_air_base_injection
     */
    private BattleTypes.AirBaseAttack airBaseInjection;

    /**
     * api_air_base_attack
     */
    private List<BattleTypes.AirBaseAttack> airBaseAttack;

    /**
     * api_dock_id/api_deck_id
     */
    private Integer dockId;

    /**
     * api_ship_ke
     */
    private List<Integer> shipKe;

    /**
     * api_ship_lv
     */
    private List<Integer> shipLv;

    /**
     * api_f_nowhps
     */
    private List<Integer> fNowHps;

    /**
     * api_f_maxhps
     */
    private List<Integer> fMaxHps;

    /**
     * api_e_nowhps
     */
    private List<Integer> eNowHps;

    /**
     * api_e_maxhps
     */
    private List<Integer> eMaxHps;

    /**
     * api_f_nowhps_combined
     */
    private List<Integer> fNowHpsCombined;

    /**
     * api_f_maxhps_combined
     */
    private List<Integer> fMaxHpsCombined;

    /**
     * api_midnight_flag
     */
    private Boolean midnightFlag;

    /**
     * api_eSlot
     */
    private List<List<Integer>> eSlot;

    /**
     * api_fParam
     */
    private List<List<Integer>> fParam;

    /**
     * api_eParam
     */
    private List<List<Integer>> eParam;

    /**
     * api_fParam_combined
     */
    private List<List<Integer>> fParamCombined;

    /**
     * api_search
     */
    private List<Integer> search;

    /**
     * api_formation
     */
    private List<Integer> formation;

    /**
     * api_stage_flag
     */
    private List<Integer> stageFlag;

    /**
     * api_injection_kouku
     */
    private BattleTypes.Kouku injectionKouku;

    /**
     * api_kouku
     */
    private BattleTypes.Kouku kouku;

    /**
     * api_support_flag
     */
    private Integer supportFlag;

    /**
     * api_support_info
     */
    private BattleTypes.SupportInfo supportInfo;

    /**
     * api_stage_flag2
     */
    private List<Integer> stageFlag2;

    /**
     * api_kouku2
     */
    private BattleTypes.Kouku kouku2;

    /**
     * api_smoke_type
     */
    private Integer smokeType;

    /**
     * JsonObjectから{@link CombinedBattleAirBattle}を構築します
     *
     * @param json JsonObject
     * @return {@link CombinedBattleAirBattle}
     */
    public static CombinedBattleAirBattle toBattle(JsonObject json) {
        CombinedBattleAirBattle bean = new CombinedBattleAirBattle();
        JsonHelper.bind(json)
            .set("api_air_base_injection", bean::setAirBaseInjection,
                BattleTypes.AirBaseAttack::toAirBaseAttack)
            .set("api_air_base_attack", bean::setAirBaseAttack,
                JsonHelper.toList(BattleTypes.AirBaseAttack::toAirBaseAttack))
            .setInteger("api_dock_id", bean::setDockId)
            .setInteger("api_deck_id", bean::setDockId)
            .setIntegerList("api_ship_ke", bean::setShipKe)
            .setIntegerList("api_ship_lv", bean::setShipLv)
            .setIntegerList("api_f_nowhps", bean::setFNowHps)
            .setIntegerList("api_f_maxhps", bean::setFMaxHps)
            .setIntegerList("api_e_nowhps", bean::setENowHps)
            .setIntegerList("api_e_maxhps", bean::setEMaxHps)
            .setIntegerList("api_f_nowhps_combined", bean::setFNowHpsCombined)
            .setIntegerList("api_f_maxhps_combined", bean::setFMaxHpsCombined)
            .setBoolean("api_midnight_flag", bean::setMidnightFlag)
            .set("api_eSlot", bean::setESlot, JsonHelper.toList(JsonHelper::toIntegerList))
            .set("api_fParam", bean::setFParam, JsonHelper.toList(JsonHelper::toIntegerList))
            .set("api_eParam", bean::setEParam, JsonHelper.toList(JsonHelper::toIntegerList))
            .set("api_fParam_combined", bean::setFParamCombined, JsonHelper.toList(JsonHelper::toIntegerList))
            .setIntegerList("api_search", bean::setSearch)
            .setIntegerList("api_formation", bean::setFormation)
            .setIntegerList("api_stage_flag", bean::setStageFlag)
            .set("api_injection_kouku", bean::setInjectionKouku, BattleTypes.Kouku::toKouku)
            .set("api_kouku", bean::setKouku, BattleTypes.Kouku::toKouku)
            .setInteger("api_support_flag", bean::setSupportFlag)
            .set("api_support_info", bean::setSupportInfo, BattleTypes.SupportInfo::toSupportInfo)
            .setIntegerList("api_stage_flag2", bean::setStageFlag2)
            .set("api_kouku2", bean::setKouku2, BattleTypes.Kouku::toKouku)
            .setInteger("api_smoke_type", bean::setSmokeType)
        ;
        return bean;
    }
}
