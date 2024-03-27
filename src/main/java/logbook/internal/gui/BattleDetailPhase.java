package logbook.internal.gui;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import logbook.bean.*;
import logbook.internal.LoggerHolder;
import logbook.internal.PhaseState;
import logbook.internal.Ships;

/**
 * 戦闘ログ詳細のフェーズ
 */
public class BattleDetailPhase extends TitledPane {

    /**
     * フェイズ
     */
    private final PhaseState phase;

    /**
     * 詳細
     */
    private final List<PhaseState.AttackDetail> attackDetails = new ArrayList<>();

    /**
     * 付加情報
     */
    private final List<? extends Node> nodes;

    /**
     * 付加情報
     */
    @FXML
    private VBox infomation;

    /**
     * 第1艦隊
     */
    @FXML
    private VBox afterFriend;

    /**
     * 第2艦隊
     */
    @FXML
    private VBox afterFriendCombined;

    /**
     * 敵第2艦隊
     */
    @FXML
    private VBox afterEnemyCombined;

    /**
     * 敵第1艦隊
     */
    @FXML
    private VBox afterEnemy;

    /**
     * 詳細
     */
    @FXML
    private VBox detail;

    /**
     * 友軍艦隊フラグ
     */
    private final boolean isFriendlyBattle;

    private final List<PhaseState.AttackDetail> TSBK = new ArrayList<>();
    private final List<PhaseState.AttackDetail> openingAttack = new ArrayList<>();

    /**
     * 戦闘ログ詳細のフェーズのコンストラクタ
     *
     * @param phase フェイズ
     */
    public BattleDetailPhase(PhaseState phase) {
        this(phase, null);
    }

    /**
     * 戦闘ログ詳細のフェーズのコンストラクタ
     *
     * @param phase      フェイズ
     * @param infomation 付加情報
     */
    public BattleDetailPhase(PhaseState phase, List<? extends Node> infomation) {
        this(phase, infomation, false);
    }

    /**
     * 戦闘ログ詳細のフェーズのコンストラクタ
     *
     * @param phase            フェイズ
     * @param infomation       付加情報
     * @param isFriendlyBattle 友軍艦隊フラグ
     */
    public BattleDetailPhase(PhaseState phase, List<? extends Node> infomation, boolean isFriendlyBattle) {
        this.phase = new PhaseState(phase);
        if (!phase.getAttackDetails().isEmpty()) {
            for (PhaseState.AttackDetail i : phase.getAttackDetails()) {
                if (i.getAtType() == BattleTypes.SortieAtTypeTSBK.対潜先制爆雷攻撃) {
                    this.TSBK.add(i);
                } else if (i.getAtType() == BattleTypes.SortieAtTypeRaigeki.開幕雷撃) {
                    this.openingAttack.add(i);
                } else {
                    this.attackDetails.add(i);
                }
            }
        }
        this.nodes = infomation;
        this.isFriendlyBattle = isFriendlyBattle;
        try {
            FXMLLoader loader = InternalFXMLLoader.load("logbook/gui/battle_detail_phase.fxml");
            loader.setRoot(this);
            loader.setController(this);
            loader.load();
        } catch (IOException e) {
            LoggerHolder.get().error("FXMLのロードに失敗しました", e);
        }
    }

    @FXML
    void initialize() {

        if (this.nodes != null) {
            this.infomation.getChildren().addAll(this.nodes);
        }

        PopOver<Chara> popover = new PopOver<>((node, chara) -> {
            VBox child = new VBox();
            child.getChildren().add(new FleetTabShipPopup(chara, this.phase.getItemMap()));
            List<PhaseState.AttackDetail> details = this.attackDetails.stream()
                .filter(e -> {
                    if (chara.getClass() != e.getAttacker().getClass()) {
                        return false;
                    }
                    if (chara.isShip()) {
                        return Objects.equals(chara.asShip().getId(), e.getAttacker().asShip().getId());
                    }
                    if (chara.isFriend()) {
                        return Objects.equals(chara.getShipId(), e.getAttacker().getShipId());
                    }
                    if (chara.isEnemy()) {
                        return Objects.equals(chara.asEnemy().getOrder(), e.getAttacker().asEnemy().getOrder());
                    }
                    return false;
                })
                .collect(Collectors.toList());
            if (!details.isEmpty()) {
                Label text = new Label("与ダメージ");
                text.getStyleClass().add("title");
                child.getChildren().add(text);
                child.getChildren().add(this.detailNode(details));
            }
            return new PopOverPane(Ships.toName(chara), child);
        });

        if (this.isFriendlyBattle) {
            for (Friend friend : this.phase.getAfterFriendly()) {
                if (friend != null) {
                    BattleDetailPhaseShip phaseShip = new BattleDetailPhaseShip(friend, null, null);
                    this.afterFriend.getChildren().add(phaseShip);
                    // マウスオーバーでのポップアップ
                    popover.install(phaseShip, friend);
                }
            }
        } else {
            for (Ship ship : this.phase.getAfterFriend()) {
                if (ship != null) {
                    BattleDetailPhaseShip phaseShip = new BattleDetailPhaseShip(ship,
                        this.phase.getItemMap(), this.phase.getEscape());
                    this.afterFriend.getChildren().add(phaseShip);
                    // マウスオーバーでのポップアップ
                    popover.install(phaseShip, ship);
                }
            }
            for (Ship ship : this.phase.getAfterFriendCombined()) {
                if (ship != null) {
                    BattleDetailPhaseShip phaseShip = new BattleDetailPhaseShip(ship,
                        this.phase.getItemMap(), this.phase.getEscape());
                    this.afterFriendCombined.getChildren().add(phaseShip);
                    // マウスオーバーでのポップアップ
                    popover.install(phaseShip, ship);
                }
            }
        }
        for (Enemy enemy : this.phase.getAfterEnemyCombined()) {
            if (enemy != null) {
                BattleDetailPhaseShip phaseShip = new BattleDetailPhaseShip(enemy, null, null);
                this.afterEnemyCombined.getChildren().add(phaseShip);
                // マウスオーバーでのポップアップ
                popover.install(phaseShip, enemy);
            }
        }
        for (Enemy enemy : this.phase.getAfterEnemy()) {
            if (enemy != null) {
                BattleDetailPhaseShip phaseShip = new BattleDetailPhaseShip(enemy, null, null);
                this.afterEnemy.getChildren().add(phaseShip);
                // マウスオーバーでのポップアップ
                popover.install(phaseShip, enemy);
            }
        }

        if (!this.TSBK.isEmpty()) {
            TitledPane pane = new TitledPane("対潜先制爆雷攻撃", new VBox());
            pane.setAnimated(false);
            pane.setExpanded(false);
            pane.expandedProperty().addListener((ChangeListener<Boolean>) this::initializeDetailTSBK);
            this.detail.getChildren().add(pane);
        }
        if (!this.openingAttack.isEmpty()) {
            TitledPane pane = new TitledPane("開幕雷撃", new VBox());
            pane.setAnimated(false);
            pane.setExpanded(false);
            pane.expandedProperty().addListener((ChangeListener<Boolean>) this::initializeDetailOpeningAttack);
            this.detail.getChildren().add(pane);
        }
        if (!this.attackDetails.isEmpty()) {
            TitledPane pane = new TitledPane("詳細", new VBox());
            pane.setAnimated(false);
            pane.setExpanded(false);
            pane.expandedProperty().addListener((ChangeListener<Boolean>) this::initializeDetail);
            this.detail.getChildren().add(pane);
        }
    }

    private void initializeDetail(ObservableValue<? extends Boolean> ob, Boolean o, Boolean n) {
        if (!n)
            return;
        for (Node node : this.detail.getChildren()) {
            if (!(node instanceof TitledPane)) {
                continue;
            }
            if (((TitledPane) node).getText().equals("対潜先制爆雷攻撃")) {
                continue;
            }
            if (((TitledPane) node).getText().equals("開幕雷撃")) {
                continue;
            }
            Parent content = this.detailNode(this.attackDetails);
            ((TitledPane) node).setContent(content);
        }
    }

    private void initializeDetailTSBK(ObservableValue<? extends Boolean> ob, Boolean o, Boolean n) {
        if (!n)
            return;
        for (Node node : this.detail.getChildren()) {
            if (!(node instanceof TitledPane)) {
                continue;
            }
            if (!((TitledPane) node).getText().equals("対潜先制爆雷攻撃")) {
                continue;
            }
            Parent content = this.detailNode(this.TSBK);
            ((TitledPane) node).setContent(content);
        }
    }

    private void initializeDetailOpeningAttack(ObservableValue<? extends Boolean> ob, Boolean o, Boolean n) {
        if (!n)
            return;
        for (Node node : this.detail.getChildren()) {
            if (!(node instanceof TitledPane)) {
                continue;
            }
            if (!((TitledPane) node).getText().equals("開幕雷撃")) {
                continue;
            }
            Parent content = this.detailNode(this.openingAttack);
            ((TitledPane) node).setContent(content);
        }
    }

    private Parent detailNode(List<PhaseState.AttackDetail> details) {
        VBox content = new VBox();
        for (PhaseState.AttackDetail detail : details) {
            VBox action = new VBox();
            // 砲撃戦が割とごちゃごちゃして見えるので一部に色を付けて見る
            if (detail.getAtType() instanceof BattleTypes.SortieAtTypeRaigeki) {
                // 通常の雷撃。連合艦隊だと砲撃の間に挟まるので
                action.setStyle("-fx-background-color: #d0d0ff");
            } else if (detail.getAtType().isTouch()) {
                // 各種タッチ。効果が大きいため強調の意味も込めて
                action.setStyle("-fx-background-color: #ffd0d0");
            }
            Chara attacker = detail.getAttacker();
            Chara defender = detail.getDefender();

            StringBuilder sb = new StringBuilder();
            if (attacker != null)
                sb.append(Ships.toName(attacker)).append("が");
            sb.append(Ships.toName(defender)).append("に");
            sb.append(detail.getDamage()).append("ダメージ");
            sb.append("(").append(detail.getAtType()).append(")");
            action.getChildren().add(new Label(sb.toString()));

            StringJoiner sj = new StringJoiner("/");
            for (int i = 0; i < detail.getDamages().size(); i++) {
                if (detail.getDamages().get(i) <= -1) {
                    continue;
                }
                StringBuilder sb2 = new StringBuilder();
                sb2.append((i + 1)).append("回目").append(detail.getDamages().get(i)).append("ダメージ");
                if (detail.getCritical().get(i) == 2) {
                    sb2.append("(クリティカル)");
                }
                sj.add(sb2);
            }
            action.getChildren().add(new Label(sj.toString()));

            HBox graphic = new HBox();
            graphic.getChildren().add(new BattleDetailPhaseShip(attacker,
                this.phase.getItemMap(), this.phase.getEscape()));
            graphic.getChildren().add(new BattleDetailPhaseShip(defender,
                this.phase.getItemMap(), this.phase.getEscape()));
            action.getChildren().add(graphic);
            content.getChildren().add(action);
            content.getChildren().add(new Separator());
        }
        return content;
    }
}
