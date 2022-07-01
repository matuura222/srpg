package function.controller.state.game.battle.sub;

import function.battle.ActionCommand;
import function.battle.range.SkillRange;
import function.controller.state.game.battle.BattleState;
import function.controller.unit.UnitSkill;
import function.unit.Unit;
import javafx.scene.control.Button;

/**
 * ユニットのスキルを使用するためのクラス
 * @author 樹麗
 *
 */
public class SelectSkill extends UnitSkill {
	private Button button;
	/**
	 * @param battle バトルステート
	 * @param unit 使用するユニット
	 */
	public SelectSkill(BattleState battle, Unit unit){
		detailsShow(unit);
		setPrefHeight(getPrefHeight() + 25);
		button = new Button("選択");
		button.setPrefWidth(80);
		button.setLayoutX(this.getPrefWidth() - 80);
		button.setLayoutY(this.getPrefHeight() - 20);
		button.setOnAction(e -> {
			if(unit.isActed()){ return; }
			if(getSelectSkill() == null){ return; }
			SkillRange skill_range = new SkillRange(battle.getGameMap().getGameMapData(), getSelectSkill());
			skill_range.startSearch(battle.getGameMap().getGameMapData().getUnitLocate(unit), unit);
			skill_range.drawRange(battle.getGameMap().getRangeGraphics());
			battle.setSelectSkill(getSelectSkill());
			battle.setRange(skill_range);
			battle.setActionCommand(ActionCommand.SKILL);
		});
		getChildren().add(button);
		battle.addRemoveSubWindowName(getId());
	}
}
