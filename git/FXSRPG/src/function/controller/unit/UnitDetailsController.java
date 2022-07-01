package function.controller.unit;

import function.controller.ControllerBase;
import function.controller.skill.EffectDetails;
import function.unit.Unit;
import javafx.fxml.FXML;

/**
 * ユニットの詳細を見るためのクラス
 * @author 樹麗
 *
 */
public class UnitDetailsController extends ControllerBase {
	@FXML
	private UnitStatus unit_status;
	@FXML
	private UnitSkill unit_skill;
	@FXML
	private EffectDetails effect;
	public UnitDetailsController(){
		super("unit/UnitDetails");
	}
	/**
	 * 渡されたユニットの状態を見る
	 * @param target 対象ユニット
	 */
	public void detailsShow(Unit target) {
		unit_status.detailsShow(target);
		unit_skill.detailsShow(target);
		effect.detailsShow(target);
		String s = target.isFriend() ? "味方 " : "敵 ";
		setId(s + " " + target.getName());
	}
}
