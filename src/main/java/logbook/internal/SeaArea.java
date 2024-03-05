package logbook.internal;

import lombok.Getter;

import java.util.stream.Stream;

/**
 * 海域
 */
@Getter
public enum SeaArea {

    識別札1("第二艦隊", 1, 4),
    識別札2("南沙哨戒部隊", 2, 10),
    識別札3("輸送護衛船団", 3, 12),
    識別札4("第一水雷戦隊", 4, 14),
    識別札5("第五艦隊主力", 5, 16),
    識別札6("第一艦隊", 6, 18),
    /*==========*/
    識別札7("欧州遠征先遣隊", 7, 20),
    識別札8("イギリス連絡部隊", 8, 22),
    識別札9("英海軍主力部隊", 9, 24),
    識別札10("英D-Day上陸部隊", 10, 5),
    識別札11("米D-Day上陸部隊", 11, 7);

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
