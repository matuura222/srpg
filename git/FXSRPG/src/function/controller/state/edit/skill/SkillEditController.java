package function.controller.state.edit.skill;

import common.SRPGCommons;
import function.controller.state.edit.EditorControllerBase;
import function.controller.state.edit.SRPGEditorState;
import function.skill.Skill;
import function.skill.SkillType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class SkillEditController extends EditorControllerBase<Skill> {
	@FXML
	private TextField skill_name, skill_id;
	@FXML
	private ChoiceBox<SkillType> skill_type;
	@FXML
	private AnchorPane edit_pane;
	private SkillDetailsEditControllerBase details;
	private SRPGEditorState editor;
	private Skill skill;
	public SkillEditController(SRPGEditorState editor){
		super("skill/SkillEdit");
		skill_type.getItems().addAll(SkillType.values());
		this.editor = editor;
		skill_type.setOnAction(this::changeSkillType);
	}
	@FXML
	public void save(ActionEvent e){
		Skill skill = details.getSkill();
		if((SRPGCommons.getSkillList().contains(this.skill) == false && SRPGCommons.getSkillList().contains(skill))
				|| (this.skill.equals(skill) == false && SRPGCommons.getSkillList().contains(skill))){
			editor.setMessage("識別IDが重複しています。\n別のIDにしてください");
			return;
		}
		editor.setMessage("スキル：" + skill.getName() + "をセーブしました");
		editor.skillUpdate(this.skill, skill);
		this.skill = skill;
	}
	@FXML
	private void changeSkillType(ActionEvent e){
		switch(skill_type.getValue()){
			case MATERIAL : case MAGIC :
				details = new AttackSkillEdit(this);
				details.edit(this.skill);
				edit_pane.getChildren().set(0, details);
				break;
			case SUMMON:
				details = new SummonSkillEdit(this);
				details.edit(this.skill);
				edit_pane.getChildren().set(0, details);
				break;
			case SUPPORT:
				details = new SupportSkillEdit(this);
				details.edit(this.skill);
				edit_pane.getChildren().set(0, details);
				break;
			case CONDITIONS:
				details = new ConditionsSkillEdit(this);
				details.edit(this.skill);
				edit_pane.getChildren().set(0, details);
				break;
			default: throw new IllegalArgumentException();
		}
		AnchorPane.setTopAnchor(details, 0.0);
		AnchorPane.setBottomAnchor(details, 0.0);
		AnchorPane.setLeftAnchor(details, 0.0);
		AnchorPane.setRightAnchor(details, 0.0);
	}
	@Override
	public void edit(Skill skill){
		this.skill = skill;
		skill_name.setText(skill.getName());
		skill_id.setText(skill.getId());
		skill_type.setValue(skill.getSkillType());
		details.edit(skill);
	}
	public String getSkillId(){
		return this.skill_id.getText();
	}
	public String getSkillName(){
		return this.skill_name.getText();
	}
	public SkillType getSkillType(){
		return this.skill_type.getValue();
	}
}
