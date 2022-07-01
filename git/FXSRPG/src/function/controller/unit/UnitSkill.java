package function.controller.unit;

import java.util.stream.Collectors;

import function.controller.ControllerBase;
import function.controller.skill.AttackSkillDetailsController;
import function.controller.skill.ConditionSkillDetailsController;
import function.controller.skill.SkillDetailsControllerBase;
import function.controller.skill.SummonSkillDetailsController;
import function.controller.skill.SupportSkillDetailsController;
import function.skill.AttackSkill;
import function.skill.ConditionSkill;
import function.skill.Skill;
import function.skill.SkillType;
import function.skill.SummonSkill;
import function.skill.SupportSkill;
import function.unit.Unit;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TabPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

/**
 * ユニットのスキルを表示するためのクラス
 * @author 樹麗
 *
 */
public class UnitSkill extends ControllerBase {
@FXML
	private TabPane skill_tab_pane;
	@FXML
	private ListView<Skill> material_skill_list, magic_skill_list, support_skill_list, summon_skill_list,
		conditions_skill_list;
	@FXML
	private AnchorPane skill_details_pane;
	private Skill skill;
	public UnitSkill(){
		super("unit/UnitSkill");
	}

	/**
	 * 渡されたユニットのスキルを表示する
	 * @param target　表示するユニット
	 */
	public void detailsShow(Unit target) {
		material_skill_list.getItems().clear();
		magic_skill_list.getItems().clear();
		summon_skill_list.getItems().clear();
		support_skill_list.getItems().clear();
		conditions_skill_list.getItems().clear();
		material_skill_list.getItems().addAll( target.getIdToSkill().values().stream()
				.filter(skill -> skill.getSkillType() == SkillType.MATERIAL)
				.collect(Collectors.toList()));
		magic_skill_list.getItems().addAll( target.getIdToSkill().values().stream()
				.filter(skill -> skill.getSkillType() == SkillType.MAGIC)
				.collect(Collectors.toList()));
		summon_skill_list.getItems().addAll( target.getIdToSkill().values().stream()
				.filter(skill -> skill.getSkillType() == SkillType.SUMMON)
				.collect(Collectors.toList()));
		support_skill_list.getItems().addAll( target.getIdToSkill().values().stream()
				.filter(skill -> skill.getSkillType() == SkillType.SUPPORT)
				.collect(Collectors.toList()));
		conditions_skill_list.getItems().addAll( target.getIdToSkill().values().stream()
				.filter(skill -> skill.getSkillType() == SkillType.CONDITIONS)
				.collect(Collectors.toList()));
		setId(target.getId() + "のスキル");
	}
	@FXML
	private void skillShow(MouseEvent e){
		@SuppressWarnings("unchecked")
		ListView<Skill> skill_list = (ListView<Skill>) skill_tab_pane.getSelectionModel().getSelectedItem().getContent();
		skill = skill_list.getSelectionModel().getSelectedItem();
		if(skill == null){ return; }
		SkillDetailsControllerBase details = null;
		if(skill.getClass() == AttackSkill.class) {
			details = new AttackSkillDetailsController();
		}else if(skill.getClass() == SummonSkill.class) {
			details = new SummonSkillDetailsController();
		}else if(skill.getClass() == SupportSkill.class) {
			details = new SupportSkillDetailsController();
		}else if(skill.getClass() == ConditionSkill.class) {
			details = new ConditionSkillDetailsController();
		}
		skill_details_pane.getChildren().set(0, details);
		details.detailsShow(skill);
		AnchorPane.setTopAnchor(details, 0.0);
		AnchorPane.setBottomAnchor(details, 0.0);
		AnchorPane.setLeftAnchor(details, 0.0);
		AnchorPane.setRightAnchor(details, 0.0);
	}
	/**
	 * @return　選択されているスキルを返す
	 */
	public Skill getSelectSkill(){
		return this.skill;
	}
}
