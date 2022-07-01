package function.controller.state.edit;

import function.controller.state.StateBase;
import function.controller.state.edit.map.MapCreateState;

public class EditTitleState extends StateBase {
	public EditTitleState() {
		super("../edit/EditTitle");
	}
	@Override
	public void startState() {
		// TODO 自動生成されたメソッド・スタブ
	}

	@Override
	public void drawState() {
		// TODO 自動生成されたメソッド・スタブ

	}
	@Override
	protected StateBase decideState() {
		if("SRPGEditor".equals(getStateName())){ return new SRPGEditorState(); }
		if("MapCreate".equals(getStateName())){ return new MapCreateState(); }
		return this;
	}
	@Override
	public void endState(){

	}
}
