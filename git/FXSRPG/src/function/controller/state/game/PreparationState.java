package function.controller.state.game;

import function.controller.state.StateBase;
import function.controller.state.game.hire.UnitHireState;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;

/**
 * ゲームのメイン画面を表示するステート
 * @author 樹麗
 *
 */
public class PreparationState extends StateBase {
	@FXML
	private AnchorPane pane;
	public PreparationState() {
		super("Preparation");
	}

	/* (非 Javadoc)
	 * @see function.controller.state.StateBase#startState()
	 */
	@Override
	public void startState() {

	}

	/* (非 Javadoc)
	 * @see function.controller.state.StateBase#drawState()
	 */
	@Override
	protected void drawState() {


	}
	/* (非 Javadoc)
	 * @see function.controller.state.StateBase#decideState()
	 */
	@Override
	protected StateBase decideState() {
		if("StageSelect".equals(getStateName())){
			return new StageSelectState();
		}else if("GameSave".equals(getStateName())){
			return new GameSaveState();
		}else if("GameLoad".equals(getStateName())){
			return new GameLoadState(this);
		}else if("Title".equals(getStateName())){
			return new TitleState();
		}
		return this;
	}
	/* (非 Javadoc)
	 * @see function.controller.state.StateBase#endState()
	 */
	@Override
	public void endState() {

	}
	/**
	 * 雇用画面を開く
	 */
	@FXML
	private void unitHire(){
		pane.getChildren().set(0, new UnitHireState());
	}

}
