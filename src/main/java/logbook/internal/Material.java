package logbook.internal;

public enum Material {
    なし(0),
    燃料(1),
    弾薬(2),
    鋼材(3),
    ボーキ(4),
    バーナー(5),
    バケツ(6),
    釘(7),
    ネジ(8);

    private final int id;

    Material(int id) {
        this.id = id;
    }
    public int getId(){
        return this.id;
    }
    public static Material get(int id){
        for(int i=0;i<Material.values().length;i++){
            if(Material.values()[i].id == id){
                return Material.values()[i];
            }
        }
        return Material.なし;
    }
}
