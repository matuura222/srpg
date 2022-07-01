package function.controller.state.edit.skill;

import function.skill.AttackSkill;
import function.skill.Skill;

public class AttackSkillEdit extends SkillDetailsEditControllerBase{
	private SkillEditController skill_edit;
	public AttackSkillEdit(SkillEditController skill_edit) {
		super("skill/AttackSkillEdit");
		this.skill_edit = skill_edit;
	}

	@Override
	public void edit(Skill edit_target) {
		status.edit(edit_target);
	}

	@Override
	public AttackSkill getSkill() {
		return new AttackSkill(skill_edit.getSkillId(), skill_edit.getSkillName(), status.getSkillText(), skill_edit.getSkillType(),
				status.getCost(), status.getVariablePower(), status.getFixedPower(), status.getRangeDistance(),
				status.getEffectRange(), status.getTargetTaype(), status.getExp(), status.getSoundEffectName());
	}
}
