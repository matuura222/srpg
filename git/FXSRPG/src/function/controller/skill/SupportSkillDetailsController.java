package function.controller.skill;


import common.IgetStateListable;
import function.skill.Skill;
import function.skill.SupportSkill;
import javafx.fxml.FXML;

/**
 * サポートスキルの詳細を見るためのクラス
 * @author 樹麗
 */
public class SupportSkillDetailsController extends SkillDetailsControllerBase{
	@FXML
	private EffectDetails effect_details;
	public SupportSkillDetailsController() {
		super("skill/SupportSkillDetails");
	}
	/* (非 Javadoc)
	 * @see function.controller.skill.SkillDetailsControllerBase#show(function.skill.Skill)
	 */
	@Override
	public void detailsShow(Skill skill) {
		if(skill.getClass() != SupportSkill.class) { return; }
		getStatus().show(skill);
		effect_details.detailsShow((IgetStateListable) skill);
	}
}
