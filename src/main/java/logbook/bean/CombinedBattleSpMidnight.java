package logbook.bean;

import java.io.Serializable;
import java.util.List;

import javax.json.JsonObject;

import logbook.bean.BattleTypes.ICombinedBattle;
import logbook.bean.BattleTypes.IFormation;
import logbook.bean.BattleTypes.IMidnightBattle;
import logbook.bean.BattleTypes.INSupport;
import logbook.internal.JsonHelper;
import lombok.Data;

/**
 * 夜戦(連合艦隊)
 */
@Data
public class CombinedBattleSpMidnight implements ICombinedBattle, IMidnightBattle, IFormation, INSupport, Serializable {

    private static final long serialVersionUID = -364877629377359534L;

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
     * api_formation
     */
    private List<Integer> formation;

    /**
     * api_friendly_info
     */
    private BattleTypes.FriendlyInfo friendlyInfo;

    /**
     * api_friendly_battle
     */
    private BattleTypes.FriendlyBattle friendlyBattle;

    /**
     * api_touch_plane
     */
    private List<Integer> touchPlane;

    /**
     * api_flare_pos
     */
    private List<Integer> flarePos;

    /**
     * api_hougeki
     */
    private BattleTypes.MidnightHougeki hougeki;

    /**
     * api_n_support_flag
     */
    private Integer nSupportFlag;

    /**
     * api_n_support_info
     */
    private BattleTypes.SupportInfo nSupportInfo;

    /**
     * api_smoke_type
     */
    private Integer smokeType;

    /**
     * JsonObjectから{@link CombinedBattleSpMidnight}を構築します
     *
     * @param json JsonObject
     * @return {@link CombinedBattleSpMidnight}
     */
    public static CombinedBattleSpMidnight toBattle(JsonObject json) {
        CombinedBattleSpMidnight bean = new CombinedBattleSpMidnight();
        JsonHelper.bind(json)
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
            .set("api_eSlot", bean::setESlot, JsonHelper.toList(JsonHelper::toIntegerList))
            .set("api_fParam", bean::setFParam, JsonHelper.toList(JsonHelper::toIntegerList))
            .set("api_eParam", bean::setEParam, JsonHelper.toList(JsonHelper::toIntegerList))
            .set("api_fParam_combined", bean::setFParamCombined, JsonHelper.toList(JsonHelper::toIntegerList))
            .setIntegerList("api_formation", bean::setFormation)
            .set("api_friendly_info", bean::setFriendlyInfo, BattleTypes.FriendlyInfo::toFriendlyInfo)
            .set("api_friendly_battle", bean::setFriendlyBattle, BattleTypes.FriendlyBattle::toFriendlyBattle)
            .setIntegerList("api_touch_plane", bean::setTouchPlane)
            .setIntegerList("api_flare_pos", bean::setFlarePos)
            .set("api_hougeki", bean::setHougeki, BattleTypes.MidnightHougeki::toMidnightHougeki)
            .setInteger("api_n_support_flag", bean::setNSupportFlag)
            .set("api_n_support_info", bean::setNSupportInfo, BattleTypes.SupportInfo::toSupportInfo)
            .setInteger("api_smoke_type", bean::setSmokeType)
        ;
        return bean;
    }
}
