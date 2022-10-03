package logbook.internal;

import java.util.stream.Stream;

/**
 * 海域
 */
public enum SeaArea {
    識別札1("前路掃討部隊", 1, 4),
    識別札2("遠征偵察部隊", 2, 8),
    識別札3("遠征艦隊先遣隊", 3, 10),
    識別札4("地中海連合艦隊", 4, 12),
    識別札5("トーチ作戦英軍部隊", 5, 14),
    識別札6("トーチ作戦派遣部隊", 6, 16),
    識別札7("中央任務部隊", 7, 18),
    識別札8("東方任務部隊", 8, 20),
    識別札9("西方先遣部隊", 9, 22),
    識別札10("西方任務決戦部隊", 10, 5);

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
