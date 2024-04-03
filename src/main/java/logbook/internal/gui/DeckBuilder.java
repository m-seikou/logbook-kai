package logbook.internal.gui;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javafx.scene.control.TableView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import logbook.bean.*;
import logbook.bean.Mapinfo.AirBase;
import logbook.bean.Mapinfo.PlaneInfo;
import lombok.Data;

/**
 * デッキビルダー形式の出力
 */
public class DeckBuilder {

    /**
     * 表示されている艦をクリップボードにコピーする。
     *
     * @param table テーブル
     */
    public static void displayCopy(TableView<ShipItem> table) {
        copyToClipboard(table.getItems()
                .stream()
                .map(ShipItem::getShip)
                .collect(Collectors.toList()), null);
    }

    /**
     * 選択された艦のみをクリップボードにコピーする。
     *
     * @param table テーブル
     */
    public static void selectionCopy(TableView<ShipItem> table) {
        copyToClipboard(table.getSelectionModel()
                .getSelectedItems()
                .stream()
                .map(ShipItem::getShip)
                .collect(Collectors.toList()), null);
    }

    /**
     * 選択された基地航空隊の機体リストをクリップボードにコピーする。
     * 艦隊部分は現在の艦隊が入る。
     *
     * @param table テーブル
     */
    public static void airbaseSelectionCopy(TableView<AirBaseItem> table) {
        Airbase ab = new Airbase();
        List<Airbase> all = new ArrayList<>();
        all.add(ab);
        for (AirBaseItem item : table.getSelectionModel().getSelectedItems()) {
            if (ab.getList().size() == 4) {
                ab = new Airbase();
                all.add(ab);
            }
            ab.getList().add(item);
        }
        copyToClipboard(ShipCollection.get().getShipMap().values(), all);
    }

    /**
     * 選択された基地航空隊をクリップボードにコピーする。
     * 艦隊部分は現在の艦隊が入る。
     *
     * @param airbases 基地航空隊
     */
    public static void airbaseSelectionCopy(List<AirBase> airbases) {
        List<Airbase> list = IntStream.range(1, 4)
            .mapToObj(id -> airbases.stream().filter(ab -> ab.getRid() == id).findAny())
            .map(ab -> ab.map(Airbase::new).orElse(new Airbase()))
            .collect(Collectors.toList());
        copyToClipboard(ShipCollection.get().getShipMap().values(), list);
    }

    public static void battleDataCopy(BattleLog battleLog) {
        Map<String, Object> data = new TreeMap<>();
        data.put("version",4);
        data.put("hqlv",battleLog.getResult().getMemberLv());
        /*
         * {1:[ship...],...}
         */
        battleLog.getDeckMap().forEach((Integer f, List<Ship> ships) -> {
            Map<String, Object> fleetMap = fleetToTree(ships);
            //battleLog.getDeckMap() からは艦隊名は取れないので 第n艦隊 の固定文字列とする
            fleetMap.put("name", "第" + f + "艦隊");
            // 陣形は battleLog.getDeckMap() ではなく battleLog.getBattle().getFormation() にある
            fleetMap.put("t", battleLog.getBattle().getFormation().get(0));
            data.put("f" + f, fleetMap);
        });

        if (!battleLog.getAirBase().isEmpty()) {
            for (Mapinfo.AirBase airBase : battleLog.getAirBase()) {
                String baseId = "a" + airBase.getRid();
                Map<String, Object> airBaseData = new TreeMap<>();
                airBaseData.put("name", airBase.getName());
                airBaseData.put("mode", airBase.getActionKind());
                Map<String, Object> items = new TreeMap<>();
                for (int i = 0; i < airBase.getPlaneInfo().size(); i++) {
                    Integer slotId = airBase.getPlaneInfo().get(i).getSlotid();
                    if (battleLog.getItemMap().containsKey(slotId)) continue;
                    Map<String, Object> plane = new TreeMap<>();
                    SlotItem slotItem = battleLog.getItemMap().get(slotId);
                    plane.put("id", slotItem.getId());
                    plane.put("rf", slotItem.getLevel());
                    plane.put("mas", slotItem.getAlv());
                    items.put("i" + (i + 1), plane);
                }
                airBaseData.put("items", items);
                data.put(baseId, airBaseData);
            }
        } else {
            for (BattleTypes.AirBaseAttack airBaseAttack : battleLog.getBattle().asIAirBaseAttack().getAirBaseAttack()) {
                String baseId = "a" + airBaseAttack.getBaseId();
                if (data.containsKey(baseId)) continue;
                data.put(baseId, airBaseAttacksToTree(airBaseAttack));
            }
        }

        ObjectMapper mapper = new ObjectMapper();
        try {
            ClipboardContent content = new ClipboardContent();
            content.putString(mapper.writeValueAsString(data));
            Clipboard.getSystemClipboard().setContent(content);
        } catch (JsonProcessingException e) {
            // ignore
        }
    }

    private static Map<String, Object> fleetToTree(List<Ship> ships) {
        Map<String, Object> fleet = new TreeMap<>();
        for (int s = 0; s < ships.size(); s++) {
            fleet.put("s" + (s + 1), new Kanmusu(ships.get(s)));
        }
        return fleet;
    }

    private static Map<String, Object> airBaseAttacksToTree(BattleTypes.AirBaseAttack airBaseAttacks) {
        Map<String, Object> attack = new TreeMap<>();
        attack.put("name", "第" + airBaseAttacks.getBaseId() + "基地航空隊");
        attack.put("mode", 1);// ログに現れるのは「出撃」のみ
        Map<String, Object> items = new TreeMap<>();
        for (int i = 0; i < airBaseAttacks.getSquadronPlane().size(); i++) {
            Map<String, Object> plane = new TreeMap<>();
            plane.put("id", airBaseAttacks.getSquadronPlane().get(i).getMstId());
            plane.put("rf", 0);// ログからは改修値は取れない模様
            plane.put("mas", 0);// ログからは熟練度?は取れない模様
            items.put("i" + (i + 1), plane);
        }
        attack.put("items",items);
        return attack;
    }

    private static void copyToClipboard(Collection<Ship> ships, List<Airbase> airbase) {
        Set<Integer> targets = ships.stream().map(Ship::getId).collect(Collectors.toSet());
        Map<Integer, Ship> shipsMap = ShipCollection.get().getShipMap();
        Map<String, Object> data = new TreeMap<>();
        data.put("version", 4);
        Optional.ofNullable(Basic.get().getLevel()).ifPresent(lv -> data.put("hqlv", lv));
        DeckPortCollection.get().getDeckPortMap().values().stream().forEach(port -> {
            Map<String, DeckBuilder.Kanmusu> fleet = new TreeMap<>();
            Optional.ofNullable(port.getShip()).ifPresent(list -> {
                for (int i = 0; i < list.size(); i++) {
                    final int index = i+1;
                    Optional.ofNullable(list.get(i))
                        .filter(targets::contains)
                        .map(shipsMap::get)
                        .map(DeckBuilder.Kanmusu::new)
                        .ifPresent(kanmusu -> fleet.put("s" + index, kanmusu));
                }
            });
            data.put("f" + port.getId(), fleet);
        });
        Optional.ofNullable(airbase).ifPresent(list -> {
            for (int a = 0; a < airbase.size(); a++) {
                Airbase ab = airbase.get(a);
                if (ab.getList() != null && ab.getList().size() > 0) {
                    data.put("a" + (a+1), ab);
                    List<AirBaseItem> planes = ab.getList();
                    for (int i = 0; i < planes.size(); i++) {
                        Item item = new Item(planes.get(i));
                        ab.getItems().put("i" + (i+1), item);
                    }
                }
            }
        });
        ObjectMapper mapper = new ObjectMapper();
        try {
            ClipboardContent content = new ClipboardContent();
            content.putString(mapper.writeValueAsString(data));
            Clipboard.getSystemClipboard().setContent(content);
        } catch (JsonProcessingException e) {
            // ignore
        }
    }

    @Data
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private static class Item {
        private final int id;
        private final Integer rf;
        private final Integer mas;

        Item(SlotItem item) {
            this.id = item.getSlotitemId();
            this.rf = item.getLevel();
            this.mas = item.getAlv();
        }

        Item(AirBaseItem item) {
            this.id = item.getId();
            this.rf = item.getLevel();
            this.mas = item.getAlv();
        }
    }

    @Data
    private static class Kanmusu {
        private final int id;
        private final int lv;
        private final int luck;
        private Map<String, DeckBuilder.Item> items;

        Kanmusu(Ship ship) {
            this.id = ship.getShipId();
            this.lv = ship.getLv();
            this.luck = ship.getLucky().get(0);
            this.items = new TreeMap<>();
            Map<Integer, SlotItem> slotitemMap = SlotItemCollection.get().getSlotitemMap();
            Optional.ofNullable(ship.getSlot())
                .ifPresent(slot -> {
                    for (int i = 0; i < slot.size(); i++) {
                        final int index = i+1;
                        Optional.ofNullable(slot.get(i))
                            .map(slotitemMap::get)
                            .map(DeckBuilder.Item::new)
                            .ifPresent(item -> this.items.put("i"+(index), item));
                    }
                });
            Optional.ofNullable(ship.getSlotEx())
                .map(slotitemMap::get)
                .map(DeckBuilder.Item::new)
                .ifPresent(item -> this.items.put("ix", item));
        }
    }

    @Data
    private static class Airbase {
        private int mode = 1;     // default
        private final Map<String, DeckBuilder.Item> items = new TreeMap<>();
        @JsonIgnore
        private List<AirBaseItem> list;

        Airbase() {
            this.list = new ArrayList<>();
        }

        Airbase(AirBase ab) {
            this.mode = ab.getActionKind();
            if (ab != null && ab.getPlaneInfo() != null) {
                Map<Integer, SlotItem> map = SlotItemCollection.get().getSlotitemMap();
                this.list = ab.getPlaneInfo().stream().map(PlaneInfo::getSlotid).map(map::get).map(AirBaseItem::toAirBaseItem).collect(Collectors.toList());
            }
        }
    }
}