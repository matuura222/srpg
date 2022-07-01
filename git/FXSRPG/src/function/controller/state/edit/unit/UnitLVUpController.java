package function.controller.state.edit.unit;

import function.controller.state.edit.EditorControllerBase;
import function.controller.unit.UnitSkill;
import function.controller.unit.UnitStatus;
import function.unit.Unit;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class UnitLVUpController extends EditorControllerBase<Unit> {
	@FXML
	private UnitStatus unit_status;
	@FXML
	private UnitSkill unit_skill;
	private Unit unit;

	public UnitLVUpController(){
		super("/unit/UnitLvUp");
	}
	@Override
	public void edit(Unit edit_target) {
		unit_status.detailsShow(edit_target);
		unit_skill.detailsShow(edit_target);
		String s = edit_target.isFriend() ? "味方 " : "敵 ";
		this.setId(s + edit_target.getName() + "のステータス");
		this.unit = edit_target;
	}
	@FXML
	private void lvUp(ActionEvent e){
		this.unit.addCurrentlyExp(this.unit.getNeedExp(), false);
		unit_status.detailsShow(this.unit);
	}
	@FXML
	private void lvDown(ActionEvent e){
		this.unit.lvDown();
		unit_status.detailsShow(this.unit);
	}
}
