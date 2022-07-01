package function.controller.state.edit.skill;

import java.util.ArrayList;
import java.util.List;

import common.IgetSkillIdListdable;
import common.SRPGCommons;
import function.controller.skill.AttackSkillDetailsController;
import function.controller.skill.ConditionSkillDetailsController;
import function.controller.skill.SkillDetailsControllerBase;
import function.controller.skill.SummonSkillDetailsController;
import function.controller.skill.SupportSkillDetailsController;
import function.controller.state.edit.EditorControllerBase;
import function.skill.AttackSkill;
import function.skill.ConditionSkill;
import function.skill.Skill;
import function.skill.SummonSkill;
import function.skill.SupportSkill;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

public class HaveSkillController<T extends IgetSkillIdListdable> extends EditorControllerBase<T> {
	@FXML
	ListView<Skill> have_skill_list, skill_list;
	@FXML
	private AnchorPane have_skill_details_pane, skill_details_pane;
	List<String> skill_id_list = new ArrayList<String>();
	Skill have_target_skill = null;
	Skill target_skill = null;
	EventHandler<MouseEvent> event;
	public HaveSkillController(){
		super("skill/HaveSkill");
		skill_list.getItems().addAll(SRPGCommons.getSkillList());
	}
	@FXML
	void skillShow(MouseEvent e){
		if(MouseButton.PRIMARY != e.getButton()){ return; }
		if(e.getSource().equals(have_skill_list)){
			if(have_skill_list.getSelectionModel().getSelectedItem() == null){ return; }
			if(have_skill_list.getSelectionModel().getSelectedItem().equals(have_target_skill)){
				if(e.getClickCount() < 2){ return; }
				have_skill_list.getItems().remove(have_target_skill);
				skill_id_list.remove(have_target_skill.getId());
				skillShowReset();
				if(event != null){
					event.handle(e);
				}
				return;
			}
			skillShow("have_skill", have_skill_list.getSelectionModel().getSelectedItem());
			have_target_skill = have_skill_list.getSelectionModel().getSelectedItem();
		}else if(e.getSource().equals(skill_list)){
			if(skill_list.getSelectionModel().getSelectedItem() == null){ return; }
			if(skill_list.getSelectionModel().getSelectedItem().equals(target_skill)){
				if(e.getClickCount() < 2){ return; }
				if(have_skill_list.getItems().contains(target_skill)){ return; }
				have_skill_list.getItems().add(target_skill);
				skill_id_list.add(target_skill.getId());
				if(event != null){
					event.handle(e);
				}
				return;
			}
			skillShow("skill", skill_list.getSelectionModel().getSelectedItem());
			target_skill = skill_list.getSelectionModel().getSelectedItem();
		}
	}
	private void skillShowReset(){
		have_skill_details_pane.getChildren().set(0, new Pane());
		this.have_target_skill = null;
	}
	private void skillShow(String s, Skill skill){
		SkillDetailsControllerBase details = null;
		if(skill.getClass() == AttackSkill.class) {
			details = new AttackSkillDetailsController();
		}else if(skill.getClass() == SummonSkill.class) {
			details = new SummonSkillDetailsController();
		}else if(skill.getClass() == SupportSkill.class) {
			details = new SupportSkillDetailsController();
		}else if(skill.getClass() == ConditionSkill.class) {
			details = new ConditionSkillDetailsController();
		}else {
			throw new IllegalArgumentException();
		}
		details.detailsShow(skill);
		if(s.equals("have_skill")) {
			have_skill_details_pane.getChildren().set(0, details);
		}else if(s.equals("skill")) {
			skill_details_pane.getChildren().set(0, details);
		}
		AnchorPane.setTopAnchor(details, 0.0);
		AnchorPane.setBottomAnchor(details, 0.0);
		AnchorPane.setLeftAnchor(details, 0.0);
		AnchorPane.setRightAnchor(details, 0.0);

		this.target_skill = skill;
	}
	@Override
	public void edit(T edit_target) {
		have_skill_list.getItems().clear();
		for(String id : edit_target.getSkillIdList()) {
			have_skill_list.getItems().addAll(SRPGCommons.getSkill(id));
		}
	}
	public void setOnSkillShow(EventHandler<MouseEvent> event){ this.event = event; }
	public List<String> getHaveSkillIdList(){ return skill_id_list; }

}
