package function.controller.skill;

import function.controller.ControllerBase;
import function.skill.Skill;
import javafx.fxml.FXML;

/**
 * スキルの詳細を見るための基底クラス
 * @author 樹麗
 *
 */
public abstract class SkillDetailsControllerBase extends ControllerBase {
	@FXML
	private SkillStatusController status;
	protected SkillDetailsControllerBase(String name) {
		super(name);
	}
	/**
	 * 渡されたスキルの詳細を見る
	 * @param skill　対象スキル
	 */
	public abstract void detailsShow(Skill skill);
	/**
	 * @return スキルステータスを返す
	 */
	protected SkillStatusController getStatus() { return this.status; }
}
