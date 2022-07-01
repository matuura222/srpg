package function.controller.skill;

import function.controller.ControllerBase;
import function.skill.Skill;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;

/**
 * スキルのステータスを表示するためのクラス
 * @author 樹麗
 *
 */
public class SkillStatusController extends ControllerBase {
	public SkillStatusController(){
		super("skill/SkillStatus");
	}
	/**
	 * 渡されたスキルのステータスを表示する
	 * @param skill　対象スキル
	 */
	public void show(Skill skill){
		((Text)getAllNode().get("skill_name")).setText(skill.getName());
		((Text)getAllNode().get("skill_type")).setText(skill.getSkillType().getName());
		((Text)getAllNode().get("skill_cost")).setText(String.valueOf(skill.getCost()));
		((Text)getAllNode().get("skill_power")).setText(
				String.valueOf(skill.getVariablePower()) + "% + " + String.valueOf(skill.getFixedPower())
			);
		((Text)getAllNode().get("skill_range_distance")).setText(String.valueOf(skill.getRangeDistance()));
		((Text)getAllNode().get("skill_effect_range")).setText(String.valueOf(skill.getEffectRange()));
		((Text)getAllNode().get("skill_target")).setText(skill.getTargetType().getName());
		((TextArea)getAllNode().get("skill_text")).setText(skill.getSkillText());
	}
}
