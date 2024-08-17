package logbook.internal;

import lombok.Getter;

import java.util.stream.Stream;

/**
 * 海域
 */
@Getter
public enum SeaArea {

    識別札1("本国艦隊", 1, 4),
    識別札2("ForceH", 2, 6),
    識別札3("ForceM先遣隊", 3, 8),
    識別札4("ForceM", 4, 10),
    識別札5("渾作戦部隊", 5, 12),
    識別札6("渾作戦増援", 6, 14),
    識別札7("ラバウル支援", 7, 16),
    識別札8("MO攻略部隊", 8, 18),
    識別札9("MO機動部隊", 9, 20),
    識別札10("識別札10", 10, 5),
    識別札11("識別札11", 11, 7);

    /**
     * 名前
     * -- GETTER --
     *  名前を取得します。
     *
     */
    private final String name;

    /**
     * 海域(イベント海域のお札)
     * -- GETTER --
     *  海域(イベント海域のお札)を取得します。
     *
     */
    private final int area;

    private final int imageIndex;

    SeaArea(String name, int area, int imageIndex) {
        this.name = name;
        this.area = area;
        this.imageIndex = imageIndex;
    }

    @Override
    public String toString() {
        return this.name;
    }

    /**
     * イベント海域を取得します
     *
     * @param area お札
     * @return 海域
     */
    public static SeaArea fromArea(int area) {
        return Stream.of(SeaArea.values()).filter(s -> s.getArea() == area).findAny().orElse(null);
    }
}
