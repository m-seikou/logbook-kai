package logbook.internal;

import java.util.stream.Stream;

/**
 * 海域
 */
public enum SeaArea {

    識別札1("横須賀防備戦隊", 1, 4),
    識別札2("第二水雷戦隊", 2, 6),
    識別札3("支援連合艦隊", 3, 8),
    識別札4("連合艦隊", 4, 10),
    識別札5("空母機動部隊", 5, 12),
    識別札6("unknown", 6, 16),
    識別札7("unknown", 7, 18),
    識別札8("unknown", 8, 20),
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
