package function.controller.state.game.battle.sub;

import java.util.List;
import java.util.stream.Collectors;

import function.battle.BattleCommand;
import function.bgm.SoundEffect;
import function.controller.ControllerBase;
import function.controller.state.game.battle.BattleState;
import function.unit.Unit;
import function.unit.UnitStatusType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

/**
 * バトルメニューを操作するクラス
 * @author 樹麗
 *
 */
public class BattleMenuController extends ControllerBase{
	@FXML
	private Text unit_name;
	@FXML
	private Text unit_num;
	@FXML
	private Text unit_Inactive_num;
	private Unit unit;
	private BattleState game;
	public BattleMenuController(BattleState game){
		super("state/game/battle/sub/BattleMenu");
		this.game = game;
	}
	/**
	 * バトルコマンドMOVEを実行する
	 * 選択されているユニットの移動範囲を表示し、選択されたスクエアに移動する
	 */
	@FXML
	private void move(){
		SoundEffect.play("select.mp3");
		game.battleCommandAction(BattleCommand.MOVE);
	}
	/**
	 * バトルコマンドATTACKを実行する
	 * 選択されているユニットの攻撃範囲を表示し、選択されたスクエアにいるユニットに攻撃する
	 */
	@FXML
	private void attack(){
		SoundEffect.play("select.mp3");
		game.battleCommandAction(BattleCommand.ATTACK);
	}
	/**
	 * バトルコマンドWAITを実行する
	 * 選択されているユニットを行動済みにする
	 */
	@FXML
	private void wait(ActionEvent e){
		SoundEffect.play("select.mp3");
		game.battleCommandAction(BattleCommand.WAIT);
	}
	/**
	 * バトルコマンドSKILLを実行する
	 * 選択されているユニットのスキルを表示し、選択されたスキルを使用する
	 */
	@FXML
	private void skill(){
		SoundEffect.play("select.mp3");
		game.battleCommandAction(BattleCommand.SKILL);
	}
	/**
	 * バトルコマンドSTATUSを実行する
	 * 選択されたスクエアにいるユニットの詳細を表示する
	 */
	@FXML
	private void statusCheck(){
		SoundEffect.play("select.mp3");
		game.battleCommandAction(BattleCommand.STATUS);
	}
	/**
	 * バトルコマンドNEXT_UNITを実行する
	 * 選択されたユニットの行動を飛ばし次のユニットの行動に移る
	 */
	@FXML
	private void nextUnit(){
		SoundEffect.play("select.mp3");
		game.battleCommandAction(BattleCommand.NEXT_UNIT);
	}
	/**
	 * バトルコマンドCANCELを実行する
	 * 選択されたユニットの行動をキャンセルする
	 */
	@FXML
	private void cancel(){
		SoundEffect.play("cancel.mp3");
		game.battleCommandAction(BattleCommand.CANCEL);
	}
	/**
	 * バトルコマンドTURN_ENDを実行する
	 * 全てのユニットの行動を終え、相手のターンに移行する
	 */
	@FXML
	private void end(){
		SoundEffect.play("select.mp3");
		game.battleCommandAction(BattleCommand.TURN_END);
	}
	/**
	 * @return 現在選ばれているユニットを返す
	 */
	public Unit getUnit(){ return this.unit; }
	/**
	 * @param unit　選ばれたユニット
	 */
	public void setUnit(Unit unit){
		this.unit = unit;
		unit_name.setText(unit.getName());
		updateUnitNum();
	}
	/**
	 * 指示できるユニット、行動できるユニット、行動済みのユニットを更新し、表示する
	 */
	public void updateUnitNum(){
		List<Unit> list = game.getGameMap().getGameMapData().getFriendList().stream()
			.filter(e -> e.getStatus(UnitStatusType.HP).getCurrentStatus() > 0)
			.collect(Collectors.toList());
		unit_num.setText(String.valueOf(list.size()));
		unit_Inactive_num.setText(String.valueOf(list.stream().filter(e -> !e.isActed()).count()));
	}
}
