package function.controller.skill;


import common.IgetStateListable;
import function.skill.ConditionSkill;
import function.skill.Skill;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

/**
 * 条件スキルの詳細を見るためのスキル
 * @author 樹麗
 *
 */
public class ConditionSkillDetailsController extends SkillDetailsControllerBase{
	@FXML
	private EffectDetails effect_details;
	@FXML
	private Text activate_type;
	public ConditionSkillDetailsController() {
		super("skill/ConditionSkillDetails");
	}
	@Override
	public void detailsShow(Skill skill) {
		if(skill.getClass() != ConditionSkill.class) { return; }
		getStatus().show(skill);
		effect_details.detailsShow((IgetStateListable) skill);
		activate_type.setText(((ConditionSkill) skill).getActivateType().getName());
	}
}
