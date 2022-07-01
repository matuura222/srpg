package function.controller.state.game.battle;

import java.io.File;
import java.util.Map;
import java.util.stream.Collectors;

import common.SRPGCommons;
import function.GameDataContainer;
import function.controller.state.StateBase;
import function.controller.state.game.PreparationState;
import function.controller.state.game.UnitLocateState;
import function.map.GameMapData;
import function.unit.Unit;
import function.unit.UnitStatusType;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

/**
 * バトル終了時のステート
 * @author 樹麗
 *
 */
public class BattleEndState extends StateBase {
	private GameDataContainer<Unit> container;
	private GameMapData data;
	private boolean is_tried = false;;
	/**
	 * 終了時の状態に応じて処理を変更する
	 * @param data ゲームマップデータ
	 */
	@SuppressWarnings("unchecked")
	public BattleEndState(GameMapData data) {
		super("battle/BattleEnd");
		this.data = data;
		int earn_cost = data.getEnemyList().stream()
				.filter(e -> e.getStatus(UnitStatusType.HP).getCurrentStatus() <= 0)
				.mapToInt(e -> e.getGiveExp() * 10)
				.sum();
		container = (GameDataContainer<Unit>) SRPGCommons.getData(new File("./data/tmp/my_unit.dat"), GameDataContainer.class);
		Map<String, Unit> my_unit_list = data.getFriendList().stream()
				.filter(e -> container.getData().containsKey(e.getId()))
				.collect(Collectors.toMap(e -> e.getId(), e -> e));
		container.putAll(my_unit_list);
		int expense_cost = data.getFriendList().stream()
				.filter(e -> container.getData().values().contains(e))
				.mapToInt(e -> (e.getStatus(UnitStatusType.HP).getDefaultStatus() - e.getStatus(UnitStatusType.HP).getCurrentStatus()) * 5)
				.sum();
		if(isGameClear()){
			((Text)getAllNode().get("title")).setText("Game Clear");
		}else{
			((Text)getAllNode().get("title")).setText("Game Over");
		}
		((Text)getAllNode().get("earn_cost_text")).setText(String.valueOf(earn_cost));
		((Text)getAllNode().get("expense_cost_text")).setText(String.valueOf(expense_cost));
		((Text)getAllNode().get("total_cost_text")).setText(String.valueOf(earn_cost - expense_cost));
	}
	/* (非 Javadoc)
	 * @see function.controller.state.StateBase#startState()
	 */
	@Override
	public void startState(){

	}
	/* (非 Javadoc)
	 * @see function.controller.state.StateBase#drawState()
	 */
	@Override
	public void drawState() {
		// TODO 自動生成されたメソッド・スタブ

	}
	/* (非 Javadoc)
	 * @see function.controller.state.StateBase#decideState()
	 */
	@Override
	protected StateBase decideState() {
		if(is_tried){ return new UnitLocateState(this.data); }
		if("Preparation".equals(getStateName())){ return new PreparationState(); }
		return this;
	}
	/* (非 Javadoc)
	 * @see function.controller.state.StateBase#endState()
	 */
	@Override
	public void endState() {

	}
	/**
	 * もう一度同じステージに挑戦する
	 */
	@FXML
	private void tryStage(){
		this.data = (GameMapData) SRPGCommons.getData(new File("./data/map/map/" + this.data.getId() + ".dat"), GameMapData.class);
		this.is_tried = true;
	}
	/**
	 * 手に入れた物をセーブし準備に戻る
	 */
	@FXML
	private void confirm(){
		File file = new File("./data/tmp/resource.dat");
		int money = (int) SRPGCommons.getData(file, Integer.class);
		money += Integer.parseInt(((Text)getAllNode().get("total_cost_text")).getText());
		SRPGCommons.saveData(file, money);
		SRPGCommons.saveData(new File("./data/save/auto/resource.dat"), money);
		file = new File("./data/tmp/my_unit.dat");
		SRPGCommons.saveData(file, container);
		SRPGCommons.saveData(new File("./data/save/auto/my_unit.dat"), container);
		setStateName("Preparation");
	}
	private boolean isGameClear(){
		for(Unit unit : this.data.getFriendList()){
			if(unit.getStatus(UnitStatusType.HP).getCurrentStatus() > 0){
				return true;
			}
		}
		return false;
	}
}
