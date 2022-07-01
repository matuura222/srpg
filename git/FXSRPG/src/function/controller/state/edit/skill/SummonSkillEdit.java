package function.controller.state.edit.skill;

import common.SRPGCommons;
import function.controller.unit.UnitDetailsController;
import function.skill.Skill;
import function.skill.SummonSkill;
import function.skill.TargetType;
import function.unit.Unit;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

public class SummonSkillEdit extends SkillDetailsEditControllerBase{
	@FXML
	private ListView<Unit> summon_unit_list;
	@FXML
	private UnitDetailsController summon_unit_details;
	private SkillEditController skill_edit;
	public SummonSkillEdit(SkillEditController skill_edit) {
		super("skill/SummonSkillEdit");
		this.skill_edit = skill_edit;
		status.setTargetTypes(TargetType.NOTHING);
		summon_unit_list.getItems().addAll(SRPGCommons.getUnitList());
	}
	@FXML
	public void summonUnitShow(Event e){
		summon_unit_details.detailsShow(summon_unit_list.getSelectionModel().getSelectedItem());
	}
	@Override
	public void edit(Skill edit_target) {
		status.edit(edit_target);
		if(SummonSkill.class == edit_target.getClass()){
			Unit unit = SRPGCommons.getUnit(((SummonSkill)edit_target).getSummonUnitId());
			summon_unit_list.getSelectionModel().select(unit);
			summon_unit_details.detailsShow(unit);
		}
	}
	@Override
	public SummonSkill getSkill() {
		return new SummonSkill(skill_edit.getSkillId(), skill_edit.getSkillName(), status.getSkillText(),
				skill_edit.getSkillType(), status.getCost(), status.getVariablePower(), status.getFixedPower(),
				status.getRangeDistance(), status.getEffectRange(), TargetType.NOTHING,
				summon_unit_list.getSelectionModel().getSelectedItem().getId(), status.getExp(), status.getSoundEffectName());
	}
}
