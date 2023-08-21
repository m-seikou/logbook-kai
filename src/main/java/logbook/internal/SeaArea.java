package logbook.internal;

import java.util.stream.Stream;

/**
 * 海域
 */
public enum SeaArea {

    識別札1("第二艦隊", 1, 4),
    識別札2("南沙哨戒部隊", 2, 6),
    識別札3("輸送護衛船団", 3, 8),
    識別札4("第一水雷戦隊", 4, 10),
    識別札5("第五艦隊主力", 5, 12),
    識別札6("第一艦隊", 6, 14),
    /*==========*/
    識別札7("逆上陸部隊", 7, 16),
    識別札8("決戦連合艦隊", 8, 18),
    識別札9("unknown", 9, 22),
    識別札10("unknown", 10, 5);

    /**
     * 名前
     */
    private final String name;

    /**
     * 海域(イベント海域のお札)
     */
    private final int area;

    private final int imageIndex;

    SeaArea(String name, int area, int imageIndex) {
        this.name = name;
        this.area = area;
        this.imageIndex = imageIndex;
    }

    /**
     * 名前を取得します。
     *
     * @return 名前
     */
    public String getName() {
        return this.name;
    }

    /**
     * 海域(イベント海域のお札)を取得します。
     *
     * @return 海域(イベント海域のお札)
     */
    public int getArea() {
        return this.area;
    }

    public int getImageIndex() {
        return imageIndex;
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
