package function.controller.state.game;

import java.io.File;

import common.SRPGCommons;
import function.controller.state.StateBase;

/**
 * タイトルを表示するステート
 * @author 樹麗
 *
 */
public class TitleState extends StateBase {
	/**
	 * タイトルを読み込む
	 */
	public TitleState() {
		super("Title");
	}

	/* (非 Javadoc)
	 * @see function.controller.state.StateBase#startState()
	 */
	@Override
	public void startState() {
		File file = new File("./data/tmp");
		file.mkdir();
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
		if("Preparation".equals(getStateName())){
			SRPGCommons.createTmpFile();
			return new PreparationState();
		}else if("Load".equals(getStateName())){
			return new GameLoadState(this);
		}
		return this;
	}
	/* (非 Javadoc)
	 * @see function.controller.state.StateBase#endState()
	 */
	@Override
	public void endState(){

	}
}
