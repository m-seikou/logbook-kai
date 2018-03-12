package logbook.internal.gui;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.controlsfx.control.ToggleSwitch;
import org.controlsfx.control.textfield.TextFields;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Labeled;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;
import logbook.bean.DeckPort;
import logbook.bean.DeckPortCollection;
import logbook.bean.Ship;
import logbook.bean.ShipCollection;
import logbook.bean.ShipLabelCollection;
import logbook.bean.ShipMst;
import logbook.bean.SlotItem;
import logbook.bean.SlotItemCollection;
import logbook.internal.Items;
import logbook.internal.LoggerHolder;
import logbook.internal.Operator;
import logbook.internal.ShipFilter;
import logbook.internal.Ships;
import lombok.val;

/**
 * 所有艦娘一覧のテーブル
 *
 */
public class ShipTablePane extends VBox {

    /** テキスト */
    @FXML
    private ToggleSwitch textFilter;

    /** テキスト */
    @FXML
    private TextField textValue;

    /** 艦種 */
    @FXML
    private ToggleSwitch typeFilter;

    /** 海防艦 */
    @FXML
    private CheckBox escort;

    /** 駆逐艦 */
    @FXML
    private CheckBox destroyer;

    /** 軽巡洋艦 */
    @FXML
    private CheckBox lightCruiser;

    /** 重雷装巡洋艦 */
    @FXML
    private CheckBox torpedoCruiser;

    /** 重巡洋艦 */
    @FXML
    private CheckBox heavyCruiser;

    /** 航空巡洋艦 */
    @FXML
    private CheckBox flyingDeckCruiser;

    /** 水上機母艦 */
    @FXML
    private CheckBox seaplaneTender;

    /** 軽空母 */
    @FXML
    private CheckBox escortCarrier;

    /** 正規空母 */
    @FXML
    private CheckBox carrier;

    /** 装甲空母 */
    @FXML
    private CheckBox armoredcarrier;

    /** 戦艦 */
    @FXML
    private CheckBox battleship;

    /** 航空戦艦 */
    @FXML
    private CheckBox flyingDeckBattleship;

    /** 潜水艦 */
    @FXML
    private CheckBox submarine;

    /** 潜水空母 */
    @FXML
    private CheckBox carrierSubmarine;

    /** 揚陸艦 */
    @FXML
    private CheckBox landingship;

    /** 工作艦 */
    @FXML
    private CheckBox repairship;

    /** 潜水母艦 */
    @FXML
    private CheckBox submarineTender;

    /** 練習巡洋艦 */
    @FXML
    private CheckBox trainingShip;

    /** 補給艦 */
    @FXML
    private CheckBox supply;

    /** すべて */
    @FXML
    private CheckBox allTypes;

    /** コンディション */
    @FXML
    private ToggleSwitch condFilter;

    /** コンディション */
    @FXML
    private TextField conditionValue;

    /** コンディション条件 */
    @FXML
    private ChoiceBox<Operator> conditionType;

    /** レベル */
    @FXML
    private ToggleSwitch levelFilter;

    /** レベル */
    @FXML
    private TextField levelValue;

    /** レベル条件 */
    @FXML
    private ChoiceBox<Operator> levelType;

    /** ラベル */
    @FXML
    private ToggleSwitch labelFilter;

    /** ラベル条件 */
    @FXML
    private ChoiceBox<String> labelValue;

    /** 補強増設 */
    @FXML
    private ToggleSwitch slotExFilter;

    /** 補強増設 */
    @FXML
    private CheckBox slotExValue;

    /** 遠征 */
    @FXML
    private ToggleSwitch missionFilter;

    /** 遠征 */
    @FXML
    private CheckBox missionValue;

    /** テーブル */
    @FXML
    private TableView<ShipItem> table;

    /** 行番号 */
    @FXML
    private TableColumn<ShipItem, Integer> row;

    /** ID */
    @FXML
    private TableColumn<ShipItem, Integer> id;

    /** 艦娘 */
    @FXML
    private TableColumn<ShipItem, Ship> ship;

    /** 艦種 */
    @FXML
    private TableColumn<ShipItem, String> type;

    /** Lv */
    @FXML
    private TableColumn<ShipItem, Integer> lv;

    /** 経験値 */
    @FXML
    private TableColumn<ShipItem, Integer> exp;

    /** Next */
    @FXML
    private TableColumn<ShipItem, Integer> next;

    /** cond */
    @FXML
    private TableColumn<ShipItem, Integer> cond;

    /** ラベル */
    @FXML
    private TableColumn<ShipItem, Set<String>> label;

    /** 制空 */
    @FXML
    private TableColumn<ShipItem, Integer> seiku;

    /** 砲戦火力 */
    @FXML
    private TableColumn<ShipItem, Integer> hPower;

    /** 雷戦火力 */
    @FXML
    private TableColumn<ShipItem, Integer> rPower;

    /** 夜戦火力 */
    @FXML
    private TableColumn<ShipItem, Integer> yPower;

    /** 対潜火力 */
    @FXML
    private TableColumn<ShipItem, Integer> tPower;

    /** 火力(素) */
    @FXML
    private TableColumn<ShipItem, Integer> karyoku;

    /** 雷装(素) */
    @FXML
    private TableColumn<ShipItem, Integer> raisou;

    /** 対空(素) */
    @FXML
    private TableColumn<ShipItem, Integer> taiku;

    /** 装甲(素) */
    @FXML
    private TableColumn<ShipItem, Integer> soukou;

    /** 回避(素) */
    @FXML
    private TableColumn<ShipItem, Integer> kaihi;

    /** 対潜(素) */
    @FXML
    private TableColumn<ShipItem, Integer> tais;

    /** 索敵(素) */
    @FXML
    private TableColumn<ShipItem, Integer> sakuteki;

    /** 運(素) */
    @FXML
    private TableColumn<ShipItem, Integer> lucky;

    /** 装備1 */
    @FXML
    private TableColumn<ShipItem, Integer> slot1;

    /** 装備2 */
    @FXML
    private TableColumn<ShipItem, Integer> slot2;

    /** 装備3 */
    @FXML
    private TableColumn<ShipItem, Integer> slot3;

    /** 装備4 */
    @FXML
    private TableColumn<ShipItem, Integer> slot4;

    /** 装備5 */
    @FXML
    private TableColumn<ShipItem, Integer> slot5;

    /** 補強 */
    @FXML
    private TableColumn<ShipItem, Integer> slotEx;

    /** 艦娘達 */
    private final Supplier<List<Ship>> shipSupplier;

    /** 艦娘達 */
    private final ObservableList<ShipItem> shipItems = FXCollections.observableArrayList();

    /** フィルター */
    private final FilteredList<ShipItem> filteredShipItems = new FilteredList<>(this.shipItems);

    /** 画面更新が有効 */
    private boolean enable;

    /** フィルターの更新停止 */
    private boolean disableFilterUpdate;

    /** 艦娘一覧のハッシュ・コード */
    private int shipsHashCode;

    /** 艦隊名 */
    private String fleetName;

    /**
     * 所有艦娘一覧のテーブルのコンストラクタ
     *
     * @param port 艦隊
     */
    public ShipTablePane(DeckPort port) {
        this(() -> {
            Map<Integer, Ship> shipMap = ShipCollection.get()
                    .getShipMap();
            DeckPort newPort = DeckPortCollection.get()
                    .getDeckPortMap()
                    .get(port.getId());
            if (newPort != null) {
                return newPort.getShip().stream()
                        .map(shipMap::get)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());
            }
            return Collections.emptyList();
        }, port.getName());
    }

    /**
     * 所有艦娘一覧のテーブルのコンストラクタ
     *
     * @param shipSupplier 艦娘達
     */
    public ShipTablePane(Supplier<List<Ship>> shipSupplier, String fleetName) {
        this.shipSupplier = shipSupplier;
        this.fleetName = fleetName;
        try {
            FXMLLoader loader = InternalFXMLLoader.load("logbook/gui/ship_table.fxml");
            loader.setRoot(this);
            loader.setController(this);
            loader.load();
        } catch (IOException e) {
            LoggerHolder.get().error("FXMLのロードに失敗しました", e);
        }
    }

    @FXML
    void initialize() {
        try {
            TableTool.setVisible(this.table, this.getClass().toString() + "#" + "table");

            // フィルター 初期値
            this.conditionType.setItems(FXCollections.observableArrayList(Operator.values()));
            this.conditionType.getSelectionModel().select(Operator.GE);
            this.levelType.setItems(FXCollections.observableArrayList(Operator.values()));
            this.levelType.getSelectionModel().select(Operator.GE);

            // フィルターのバインド
            this.textFilter.selectedProperty().addListener((ob, ov, nv) -> {
                this.textValue.setDisable(!nv);
            });
            this.typeFilter.selectedProperty().addListener((ob, ov, nv) -> {
                this.escort.setDisable(!nv);
                this.destroyer.setDisable(!nv);
                this.lightCruiser.setDisable(!nv);
                this.torpedoCruiser.setDisable(!nv);
                this.heavyCruiser.setDisable(!nv);
                this.flyingDeckCruiser.setDisable(!nv);
                this.seaplaneTender.setDisable(!nv);
                this.escortCarrier.setDisable(!nv);
                this.carrier.setDisable(!nv);
                this.armoredcarrier.setDisable(!nv);
                this.battleship.setDisable(!nv);
                this.flyingDeckBattleship.setDisable(!nv);
                this.submarine.setDisable(!nv);
                this.carrierSubmarine.setDisable(!nv);
                this.landingship.setDisable(!nv);
                this.repairship.setDisable(!nv);
                this.submarineTender.setDisable(!nv);
                this.trainingShip.setDisable(!nv);
                this.supply.setDisable(!nv);
                this.allTypes.setDisable(!nv);
            });
            this.condFilter.selectedProperty().addListener((ob, ov, nv) -> {
                this.conditionValue.setDisable(!nv);
                this.conditionType.setDisable(!nv);
            });
            this.levelFilter.selectedProperty().addListener((ob, ov, nv) -> {
                this.levelValue.setDisable(!nv);
                this.levelType.setDisable(!nv);
            });
            this.labelFilter.selectedProperty().addListener((ob, ov, nv) -> {
                this.labelValue.setDisable(!nv);
            });
            this.slotExFilter.selectedProperty().addListener((ob, ov, nv) -> {
                this.slotExValue.setDisable(!nv);
            });
            this.missionFilter.selectedProperty().addListener((ob, ov, nv) -> {
                this.missionValue.setDisable(!nv);
            });

            this.textFilter.selectedProperty().addListener(this::filterAction);
            this.textValue.textProperty().addListener(this::filterAction);
            this.typeFilter.selectedProperty().addListener(this::filterAction);
            this.escort.selectedProperty().addListener(this::filterAction);
            this.destroyer.selectedProperty().addListener(this::filterAction);
            this.lightCruiser.selectedProperty().addListener(this::filterAction);
            this.torpedoCruiser.selectedProperty().addListener(this::filterAction);
            this.heavyCruiser.selectedProperty().addListener(this::filterAction);
            this.flyingDeckCruiser.selectedProperty().addListener(this::filterAction);
            this.seaplaneTender.selectedProperty().addListener(this::filterAction);
            this.escortCarrier.selectedProperty().addListener(this::filterAction);
            this.carrier.selectedProperty().addListener(this::filterAction);
            this.armoredcarrier.selectedProperty().addListener(this::filterAction);
            this.battleship.selectedProperty().addListener(this::filterAction);
            this.flyingDeckBattleship.selectedProperty().addListener(this::filterAction);
            this.submarine.selectedProperty().addListener(this::filterAction);
            this.carrierSubmarine.selectedProperty().addListener(this::filterAction);
            this.landingship.selectedProperty().addListener(this::filterAction);
            this.repairship.selectedProperty().addListener(this::filterAction);
            this.submarineTender.selectedProperty().addListener(this::filterAction);
            this.trainingShip.selectedProperty().addListener(this::filterAction);
            this.supply.selectedProperty().addListener(this::filterAction);
            this.condFilter.selectedProperty().addListener(this::filterAction);
            this.conditionValue.textProperty().addListener(this::filterAction);
            this.conditionType.getSelectionModel().selectedItemProperty().addListener(this::filterAction);
            this.levelFilter.selectedProperty().addListener(this::filterAction);
            this.levelValue.textProperty().addListener(this::filterAction);
            this.levelType.getSelectionModel().selectedItemProperty().addListener(this::filterAction);
            this.labelFilter.selectedProperty().addListener(this::filterAction);
            this.labelValue.getSelectionModel().selectedItemProperty().addListener(this::filterAction);
            this.slotExFilter.selectedProperty().addListener(this::filterAction);
            this.slotExValue.selectedProperty().addListener(this::filterAction);
            this.missionFilter.selectedProperty().addListener(this::filterAction);
            this.missionValue.selectedProperty().addListener(this::filterAction);

            this.conditionValue.setTextFormatter(new TextFormatter<>(new IntegerStringConverter()));
            TextFields.bindAutoCompletion(this.conditionValue, new SuggestSupport("53", "50", "49", "33", "30", "20"));
            this.conditionValue.setText("53");

            this.levelValue.setTextFormatter(new TextFormatter<>(new IntegerStringConverter()));
            TextFields.bindAutoCompletion(this.levelValue,
                    new SuggestSupport("100", "99", "90", "80", "75", "70", "65", "60", "55", "50", "40", "30", "20"));
            this.levelValue.setText("98");

            // カラムとオブジェクトのバインド
            this.row.setCellFactory(e -> {
                TableCell<ShipItem, Integer> cell = new TableCell<ShipItem, Integer>() {
                    @Override
                    protected void updateItem(Integer item, boolean empty) {
                        super.updateItem(item, empty);
                        if (!empty) {
                            TableRow<?> currentRow = this.getTableRow();
                            this.setText(Integer.toString(currentRow.getIndex() + 1));
                        } else {
                            this.setText(null);
                        }
                    }
                };
                return cell;
            });
            this.id.setCellValueFactory(new PropertyValueFactory<>("id"));
            this.ship.setCellValueFactory(new PropertyValueFactory<>("ship"));
            this.ship.setCellFactory(p -> new ShipImageCell());
            this.type.setCellValueFactory(new PropertyValueFactory<>("type"));
            this.lv.setCellValueFactory(new PropertyValueFactory<>("lv"));
            this.exp.setCellValueFactory(new PropertyValueFactory<>("exp"));
            this.next.setCellValueFactory(new PropertyValueFactory<>("next"));
            this.cond.setCellFactory(p -> new CondCell());
            this.cond.setCellValueFactory(new PropertyValueFactory<>("cond"));
            this.label.setCellValueFactory(new PropertyValueFactory<>("label"));
            this.label.setCellFactory(p -> new LabelCell());
            this.seiku.setCellValueFactory(new PropertyValueFactory<>("seiku"));
            this.hPower.setCellValueFactory(new PropertyValueFactory<>("hPower"));
            this.rPower.setCellValueFactory(new PropertyValueFactory<>("rPower"));
            this.yPower.setCellValueFactory(new PropertyValueFactory<>("yPower"));
            this.tPower.setCellValueFactory(new PropertyValueFactory<>("tPower"));
            this.karyoku.setCellValueFactory(new PropertyValueFactory<>("karyoku"));
            this.raisou.setCellValueFactory(new PropertyValueFactory<>("raisou"));
            this.taiku.setCellValueFactory(new PropertyValueFactory<>("taiku"));
            this.soukou.setCellValueFactory(new PropertyValueFactory<>("soukou"));
            this.kaihi.setCellValueFactory(new PropertyValueFactory<>("kaihi"));
            this.tais.setCellValueFactory(new PropertyValueFactory<>("tais"));
            this.sakuteki.setCellValueFactory(new PropertyValueFactory<>("sakuteki"));
            this.lucky.setCellValueFactory(new PropertyValueFactory<>("lucky"));
            this.slot1.setCellValueFactory(new PropertyValueFactory<>("slot1"));
            this.slot1.setCellFactory(p -> new ItemImageCell());
            this.slot2.setCellValueFactory(new PropertyValueFactory<>("slot2"));
            this.slot2.setCellFactory(p -> new ItemImageCell());
            this.slot3.setCellValueFactory(new PropertyValueFactory<>("slot3"));
            this.slot3.setCellFactory(p -> new ItemImageCell());
            this.slot4.setCellValueFactory(new PropertyValueFactory<>("slot4"));
            this.slot4.setCellFactory(p -> new ItemImageCell());
            this.slot5.setCellValueFactory(new PropertyValueFactory<>("slot5"));
            this.slot5.setCellFactory(p -> new ItemImageCell());
            this.slotEx.setCellValueFactory(new PropertyValueFactory<>("slotEx"));
            this.slotEx.setCellFactory(p -> new ItemImageCell());

            this.table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            SortedList<ShipItem> sortedList = new SortedList<>(this.filteredShipItems);
            this.table.setItems(sortedList);
            sortedList.comparatorProperty().bind(this.table.comparatorProperty());
            this.table.setOnKeyPressed(TableTool::defaultOnKeyPressedHandler);
            this.table.setStyle("-fx-cell-size: 16px;");
        } catch (Exception e) {
            LoggerHolder.get().error("FXMLの初期化に失敗しました", e);
        }
    }

    /**
     * このノードが画面が表示される(タブが選択される)場合に呼び出される
     */
    public void enable() {
        this.enable = true;
    }

    /**
     * このノードが画面が表示されなくなる(タブが選択されなくなる)場合に呼び出される
     */
    public void disable() {
        this.enable = false;
    }

    /**
     * 画面を更新する
     *
     */
    public void update() {
        try {
            if (!this.enable) {
                return;
            }
            List<Ship> ships = this.shipSupplier.get();

            if (this.shipsHashCode != ships.hashCode()) {
                // ハッシュ・コードが変わっている場合画面の更新
                this.shipsHashCode = ships.hashCode();

                this.shipItems.clear();
                this.shipItems.addAll(ships.stream()
                        .map(ShipItem::toShipItem)
                        .collect(Collectors.toList()));

                this.updateLabel();
            }
        } catch (Exception e) {
            LoggerHolder.get().error("画面の更新に失敗しました", e);
        }
    }

    /**
     * ラベルの更新
     */
    private void updateLabel() {
        Set<String> labels = new TreeSet<>();
        this.shipItems.forEach(ship -> {
            labels.addAll(ship.getLabel());
        });
        Set<String> beforeLabels = new HashSet<>(this.labelValue.getItems());
        this.labelValue.getItems().removeIf(l -> !labels.contains(l));
        for (String label : labels) {
            if (!beforeLabels.contains(label)) {
                this.labelValue.getItems().add(label);
            }
        }
    }

    /**
     * 艦種:すべて
     */
    @FXML
    void allTypeAction() {
        this.disableFilterUpdate = true;
        boolean selected = this.allTypes.isSelected();
        this.escort.setSelected(selected);
        this.destroyer.setSelected(selected);
        this.lightCruiser.setSelected(selected);
        this.torpedoCruiser.setSelected(selected);
        this.heavyCruiser.setSelected(selected);
        this.flyingDeckCruiser.setSelected(selected);
        this.seaplaneTender.setSelected(selected);
        this.escortCarrier.setSelected(selected);
        this.carrier.setSelected(selected);
        this.armoredcarrier.setSelected(selected);
        this.battleship.setSelected(selected);
        this.flyingDeckBattleship.setSelected(selected);
        this.submarine.setSelected(selected);
        this.carrierSubmarine.setSelected(selected);
        this.landingship.setSelected(selected);
        this.repairship.setSelected(selected);
        this.submarineTender.setSelected(selected);
        this.trainingShip.setSelected(selected);
        this.disableFilterUpdate = false;
        this.supply.setSelected(selected);
    }

    /**
     * クリップボードにコピー
     */
    @FXML
    void copy() {
        TableTool.selectionCopy(this.table);
    }

    /**
     * すべてを選択
     */
    @FXML
    void selectAll() {
        TableTool.selectAll(this.table);
    }

    /**
     * CSVファイルとして保存
     */
    @FXML
    void store() {
        try {
            TableTool.store(this.table, "所有艦娘一覧(" + this.fleetName + ")", this.getScene().getWindow());
        } catch (IOException e) {
            LoggerHolder.get().error("CSVファイルとして保存に失敗しました", e);
        }
    }

    @FXML
    void addLabel() {
        if (this.table.getSelectionModel()
                .getSelectedItems()
                .isEmpty()) {
            Tools.Conrtols.alert(AlertType.INFORMATION,
                    "艦娘が選ばれていません",
                    "ラベルを追加する艦娘を選択してください",
                    this.getScene().getWindow());
            return;
        }
        String shipNames = this.table.getSelectionModel()
                .getSelectedItems()
                .stream()
                .map(ship -> Ships.shipMst(ship.getShip()).map(ShipMst::getName).orElse(""))
                .collect(Collectors.joining(","));
        if (shipNames.length() > 50) {
            shipNames = shipNames.substring(0, 50) + "...";
        }

        TextInputDialog dialog = new TextInputDialog("");
        dialog.initOwner(this.getScene().getWindow());
        dialog.setTitle("艦娘にラベルを追加");
        dialog.setHeaderText(shipNames + "にラベルを追加します");

        TextFields.bindAutoCompletion(dialog.getEditor(), new SuggestSupport(this.labelValue.getItems()));

        val result = dialog.showAndWait();
        if (result.isPresent()) {
            String label = result.get();
            if (!label.isEmpty()) {
                val labelMap = ShipLabelCollection.get()
                        .getLabels();
                val selections = this.table.getSelectionModel()
                        .getSelectedItems();
                for (ShipItem ship : selections) {
                    ship.labelProperty().get()
                            .add(label);
                    val labels = labelMap.computeIfAbsent(ship.getId(), k -> new LinkedHashSet<>());
                    labels.add(label);
                }
                this.updateLabel();
                this.table.refresh();
            }
        }
    }

    @FXML
    void removeLabel() {
        if (this.table.getSelectionModel()
                .getSelectedItems()
                .isEmpty()) {
            Tools.Conrtols.alert(AlertType.INFORMATION,
                    "艦娘が選ばれていません",
                    "ラベルを除去する艦娘を選択してください",
                    this.getScene().getWindow());
            return;
        }
        String shipNames = this.table.getSelectionModel()
                .getSelectedItems()
                .stream()
                .map(ship -> Ships.shipMst(ship.getShip()).map(ShipMst::getName).orElse(""))
                .collect(Collectors.joining(","));
        if (shipNames.length() > 50) {
            shipNames = shipNames.substring(0, 50) + "...";
        }

        Set<String> labels = new TreeSet<>();
        val selections = this.table.getSelectionModel()
                .getSelectedItems();
        for (ShipItem ship : selections) {
            labels.addAll(ship.getLabel());
        }

        ChoiceDialog<String> dialog = new ChoiceDialog<>();
        dialog.initOwner(this.getScene().getWindow());
        dialog.getItems().addAll(labels);
        dialog.setTitle("艦娘からラベルを除去");
        dialog.setHeaderText(shipNames + "からラベルを除去");

        val result = dialog.showAndWait();
        if (result.isPresent()) {
            String label = result.get();
            if (!label.isEmpty()) {
                val labelMap = ShipLabelCollection.get()
                        .getLabels();
                for (ShipItem ship : selections) {
                    ship.labelProperty().get()
                            .remove(label);
                    labelMap.computeIfPresent(ship.getId(), (k, v) -> {
                        v.remove(label);
                        if (v.isEmpty())
                            return null;
                        return v;
                    });
                }
                this.updateLabel();
                this.table.refresh();
            }
        }
    }

    /**
     * テーブル列の表示・非表示の設定
     */
    @FXML
    void columnVisible() {
        try {
            TableTool.showVisibleSetting(this.table, this.getClass().toString() + "#" + "table",
                    (Stage) this.getScene().getWindow());
        } catch (Exception e) {
            LoggerHolder.get().error("FXMLの初期化に失敗しました", e);
        }
    }

    /**
     * フィルターを設定する
     */
    private void filterAction(ObservableValue<?> observable, Object oldValue, Object newValue) {
        if (!this.disableFilterUpdate) {
            Predicate<ShipItem> filter = this.createFilter();
            this.filteredShipItems.setPredicate(filter);
        }
    }

    /**
     * 艦娘フィルターを作成する
     * @return 艦娘フィルター
     */
    private Predicate<ShipItem> createFilter() {
        Predicate<ShipItem> filter = null;

        if (this.textFilter.isSelected()) {
            filter = ShipFilter.TextFilter.builder()
                    .text(this.textValue.getText())
                    .build();
        }
        if (this.typeFilter.isSelected()) {
            Set<String> types = Arrays.asList(
                    this.escort,
                    this.destroyer,
                    this.lightCruiser,
                    this.torpedoCruiser,
                    this.heavyCruiser,
                    this.flyingDeckCruiser,
                    this.seaplaneTender,
                    this.escortCarrier,
                    this.carrier,
                    this.armoredcarrier,
                    this.battleship,
                    this.flyingDeckBattleship,
                    this.submarine,
                    this.carrierSubmarine,
                    this.landingship,
                    this.repairship,
                    this.submarineTender,
                    this.trainingShip,
                    this.supply)
                    .stream()
                    .filter(CheckBox::isSelected)
                    .map(CheckBox::getText)
                    .collect(Collectors.toSet());

            filter = this.filterAnd(filter, ShipFilter.TypeFilter.builder()
                    .types(types)
                    .build());
        }
        if (this.condFilter.isSelected()) {
            int condition = Integer.parseInt(this.conditionValue.getText().isEmpty() ? "0"
                    : this.conditionValue.getText());
            filter = this.filterAnd(filter, ShipFilter.CondFilter.builder()
                    .conditionValue(condition)
                    .conditionType(this.conditionType.getValue())
                    .build());
        }
        if (this.levelFilter.isSelected()) {
            int level = Integer.parseInt(this.levelValue.getText().isEmpty() ? "0"
                    : this.levelValue.getText());

            filter = this.filterAnd(filter, ShipFilter.LevelFilter.builder()
                    .levelValue(level)
                    .levelType(this.levelType.getValue())
                    .build());
        }
        if (this.labelFilter.isSelected()) {
            filter = this.filterAnd(filter, ShipFilter.LabelFilter.builder()
                    .labelValue(this.labelValue.getValue() == null ? "" : this.labelValue.getValue())
                    .build());
        }
        if (this.slotExFilter.isSelected()) {
            filter = this.filterAnd(filter, ShipFilter.SlotExFilter.builder()
                    .slotEx(this.slotExValue.isSelected())
                    .build());
        }
        if (this.missionFilter.isSelected()) {
            filter = this.filterAnd(filter, ShipFilter.MissionFilter.builder()
                    .mission(this.missionValue.isSelected())
                    .build());
        }
        return filter;
    }

    private Predicate<ShipItem> filterAnd(Predicate<ShipItem> base, Predicate<ShipItem> add) {
        if (base != null) {
            return base.and(add);
        }
        return add;
    }

    /**
     * 艦娘画像のセル
     *
     */
    private static class ShipImageCell extends TableCell<ShipItem, Ship> {
        @Override
        protected void updateItem(Ship ship, boolean empty) {
            super.updateItem(ship, empty);

            if (!empty) {
                this.setGraphic(Tools.Conrtols.zoomImage(new ImageView(Ships.shipWithItemImage(ship))));
                this.setText(Ships.shipMst(ship)
                        .map(ShipMst::getName)
                        .orElse(""));
            } else {
                this.setGraphic(null);
                this.setText(null);
            }
        }
    }

    /**
     * 装備画像のセル
     *
     */
    private static class ItemImageCell extends TableCell<ShipItem, Integer> {

        private Map<Integer, SlotItem> itemMap = SlotItemCollection.get()
                .getSlotitemMap();

        @Override
        protected void updateItem(Integer itemId, boolean empty) {
            super.updateItem(itemId, empty);

            if (!empty) {
                if (itemId == 0) {
                    this.getStyleClass().add("none");
                } else {
                    this.getStyleClass().removeAll("none");
                }
                SlotItem item = this.itemMap.get(itemId);
                if (item != null) {
                    this.setGraphic(Tools.Conrtols.zoomImage(new ImageView(Items.itemImage(item))));
                    this.setText(Items.name(item));
                } else {
                    this.setGraphic(null);
                    this.setText(null);
                }
            } else {
                this.setGraphic(null);
                this.setText(null);
                this.getStyleClass().removeAll("none");
            }
        }
    }

    /**
     * コンディションのセル
     *
     */
    private static class CondCell extends TableCell<ShipItem, Integer> {
        @Override
        protected void updateItem(Integer cond, boolean empty) {
            super.updateItem(cond, empty);

            if (!empty) {
                ObservableList<String> styleClass = this.getStyleClass();
                styleClass.removeAll("deepgreen", "green", "orange", "red");
                if (cond >= Ships.DARK_GREEN && cond < Ships.GREEN) {
                    styleClass.add("deepgreen");
                } else if (cond >= Ships.GREEN) {
                    styleClass.add("green");
                } else if (cond <= Ships.ORANGE && cond > Ships.RED) {
                    styleClass.add("orange");
                } else if (cond <= Ships.RED) {
                    styleClass.add("red");
                }
                this.setText(cond.toString());
            } else {
                this.setText(null);
            }
        }
    }

    /**
     * ラベルのセル
     *
     */
    class LabelCell extends TableCell<ShipItem, Set<String>> {
        @Override
        protected void updateItem(Set<String> labels, boolean empty) {
            super.updateItem(labels, empty);

            if (!empty) {
                FlowPane pane = new FlowPane();
                for (String label : labels) {
                    Button button = new Button(label);
                    button.setStyle("-fx-color: " + this.colorCode(label.hashCode()));
                    button.setOnAction(this::handle);
                    pane.getChildren().add(button);
                }
                this.setGraphic(pane);
                this.setText(null);
            } else {
                this.setGraphic(null);
                this.setText(null);
            }
        }

        private void handle(ActionEvent e) {
            Object src = e.getSource();
            if (src instanceof Labeled) {
                Labeled label = (Labeled) src;
                ShipTablePane.this.labelValue.getSelectionModel().select(label.getText());
                ShipTablePane.this.labelFilter.setSelected(true);
            }
        }

        /**
         * シード値seedから#rrggbb形式のカラーコードを返します<br>
         * 同じシード値であれば必ず同じカラーコードを返します
         *
         * @param seed シード値
         * @return カラーコード
         */
        private String colorCode(int seed) {
            long n = 123456789L;
            n ^= seed * 2685821657736338717L;
            for (int i = 0; i < 3; i++) {
                n ^= n >>> 13;
                n ^= n << 17;
                n ^= n >>> 15;
            }
            String hex = Long.toString(n & 0xFFFFFF, 16);
            return "#" + ("000000" + hex).substring(hex.length());
        }
    }
}
