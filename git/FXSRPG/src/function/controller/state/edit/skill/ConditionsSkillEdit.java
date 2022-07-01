package function.controller.state.edit.skill;

import function.ActivateType;
import function.skill.ConditionSkill;
import function.skill.Skill;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Slider;
import javafx.scene.text.Text;

public class ConditionsSkillEdit extends SkillDetailsEditControllerBase{

	@FXML
	private SkillEffectEdit effect;
	@FXML
	private ChoiceBox<ActivateType> skill_activate_type;
	private SkillEditController skill_edit;
	public ConditionsSkillEdit(SkillEditController skill_edit) {
		super("skill/ConditionsSkillEdit");
		this.skill_edit = skill_edit;
		skill_activate_type.getItems().addAll(ActivateType.values());
	}
	@FXML
	private void valueChange(Event e){
		Slider s = (Slider)e.getSource();
		((Text)getAllNode().get(s.getId() + "_value")).setText(String.valueOf(Math.round(s.getValue())));
    }
	@Override
	public void edit(Skill edit_target) {
		status.edit(edit_target);
		if(ConditionSkill.class == edit_target.getClass()){
			effect.edit(edit_target);
			skill_activate_type.getSelectionModel().select(((ConditionSkill) edit_target).getActivateType());
		}
	}
	@Override
	public ConditionSkill getSkill() {
		return new ConditionSkill(skill_edit.getSkillId(), skill_edit.getSkillName(), status.getSkillText(),
				skill_edit.getSkillType(), status.getCost(), status.getVariablePower(), status.getFixedPower(),
				status.getRangeDistance(), status.getEffectRange(), status.getTargetTaype(), effect.getStateList(),
				skill_activate_type.getValue(), status.getExp(), status.getSoundEffectName());
	}
}
