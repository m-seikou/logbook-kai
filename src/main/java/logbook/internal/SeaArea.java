package logbook.internal;

import lombok.Getter;

import java.util.stream.Stream;

/**
 * 海域
 */
@Getter
public enum SeaArea {

    識別札1("第一艦隊", 1, 4),
    識別札2("第二艦隊", 2, 10),
    識別札3("第五艦隊", 3, 12),
    識別札4("機動部隊", 4, 14),
    識別札5("第六艦隊", 5, 16),
    識別札7("第三艦隊先遣隊", 6, 18),
    識別札6("前衛艦隊", 7, 20),
    識別札8("第三艦隊", 8, 22),
    識別札9("ウェーキ島輸送部隊", 9, 24),
    識別札10("新編竜巻部隊", 10, 5),
    識別札11("Z決戦艦隊", 11, 7);

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
