package function.controller.state.game;

import java.io.File;

import common.SRPGCommons;
import function.controller.state.StateBase;
import function.map.GameMapData;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;

/**
 * ゲームステージを選択するステート
 * @author 樹麗
 *
 */
public class StageSelectState extends StateBase {
	@FXML
	private ChoiceBox<String> stage_list;
	/**
	 * ゲームステージを読み込む
	 */
	public StageSelectState(){
		super("StageSelect");
		File dir = new File("./data/map/map");
		//Arrays.sort(file_name_list, Collections.reverseOrder());
		stage_list.getItems().addAll(dir.list());
		stage_list.getSelectionModel().select(0);
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
		if("UnitLocate".equals(getStateName())){
			return new UnitLocateState(
					(GameMapData) SRPGCommons.getData(
							new File("./data/map/map/" + stage_list.getSelectionModel().getSelectedItem()),
					GameMapData.class));
			}
		if("Preparation".equals(getStateName())){
			return new PreparationState();
		}
		return this;
	}
	/* (非 Javadoc)
	 * @see function.controller.state.StateBase#endState()
	 */
	@Override
	public void endState(){
	}
	/**
	 * UnitLocateStateに変更する
	 * @param e イベント
	 */
	@FXML
	public void start(ActionEvent e){
		changeScene(e);
	}
}
