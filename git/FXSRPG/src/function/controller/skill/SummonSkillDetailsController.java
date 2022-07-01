package function.controller.skill;

import common.SRPGCommons;
import function.controller.unit.UnitDetailsController;
import function.skill.Skill;
import function.skill.SummonSkill;
import javafx.fxml.FXML;

/**
 * 召喚スキルの詳細を見るためのクラス
 * @author 樹麗
 *
 */
public class SummonSkillDetailsController extends SkillDetailsControllerBase{
	@FXML
	private UnitDetailsController unit_details;
	public SummonSkillDetailsController() {
		super("skill/SummonSkillDetails");
	}
	/* (非 Javadoc)
	 * @see function.controller.skill.SkillDetailsControllerBase#show(function.skill.Skill)
	 */
	@Override
	public void detailsShow(Skill skill) {
		if(skill.getClass() != SummonSkill.class) { return; }
		getStatus().show(skill);
		unit_details.detailsShow(SRPGCommons.getUnit(((SummonSkill)skill).getSummonUnitId()));
	}
}
