package function.controller.state.edit.skill;

import function.skill.Skill;
import function.skill.SupportSkill;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Slider;
import javafx.scene.text.Text;

public class SupportSkillEdit extends SkillDetailsEditControllerBase{

	@FXML
	private SkillEffectEdit effect;
	private SkillEditController skill_edit;
	public SupportSkillEdit(SkillEditController skill_edit) {
		super("skill/SupportSkillEdit");
		this.skill_edit = skill_edit;
	}
	@FXML
	private void valueChange(Event e){
		Slider s = (Slider)e.getSource();
		((Text)getAllNode().get(s.getId() + "_value")).setText(String.valueOf(Math.round(s.getValue())));
    }
	@Override
	public void edit(Skill edit_target) {
		status.edit(edit_target);
		if(SupportSkill.class == edit_target.getClass()){
			effect.edit(edit_target);
		}
	}
	@Override
	public SupportSkill getSkill() {
		return new SupportSkill(skill_edit.getSkillId(), skill_edit.getSkillName(), status.getSkillText(),
				skill_edit.getSkillType(), status.getCost(), status.getVariablePower(), status.getFixedPower(),
				status.getRangeDistance(), status.getEffectRange(), status.getTargetTaype(), effect.getStateList(),
				status.getExp(), status.getSoundEffectName());
	}
}
