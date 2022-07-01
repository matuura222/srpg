package function.controller.skill;

import function.skill.AttackSkill;
import function.skill.Skill;

/**
 * 攻撃スキルの詳細を見るためのスキル
 * @author 樹麗
 *
 */
public class AttackSkillDetailsController extends SkillDetailsControllerBase{

	public AttackSkillDetailsController() {
		super("skill/AttackSkillDetails");
	}
	@Override
	public void detailsShow(Skill skill) {
		if(skill.getClass() != AttackSkill.class) { return; }
		getStatus().show(skill);
	}
}
