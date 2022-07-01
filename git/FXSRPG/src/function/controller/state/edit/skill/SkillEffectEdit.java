package function.controller.state.edit.skill;

import java.util.List;

import common.IgetStateListable;
import function.ActivateType;
import function.controller.state.edit.EditorControllerBase;
import function.skill.Skill;
import function.unit.UnitState;
import function.unit.UnitStatusType;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

public class SkillEffectEdit extends EditorControllerBase<Skill>{
	@FXML
	private ChoiceBox<UnitStatusType> status_type;
	@FXML
	private ChoiceBox<ActivateType> activate_type;
	@FXML
	private ListView<UnitState> state_list;
	@FXML
	private Text message;
	private String skill_id;
	public SkillEffectEdit() {
		super("skill/SkillEffectEdit");
		status_type.getItems().addAll(UnitStatusType.values());
		status_type.getSelectionModel().select(0);
		activate_type.getItems().addAll(ActivateType.values());
		activate_type.getSelectionModel().select(0);
	}
	@FXML
	private void valueChange(Event e){
		Slider s = (Slider)e.getSource();
		((Text)getAllNode().get(s.getId() + "_value")).setText(String.valueOf(Math.round(s.getValue())));
    }
	@FXML
	private void stateShow(MouseEvent e){
		if(e.getClickCount() < 2){ return; }
		UnitState state = state_list.getSelectionModel().getSelectedItem();
		if(state == null){ return; }
		((TextField)getAllNode().get("unit_state_id")).setText(state.getId().replace(skill_id + " ", ""));
		((TextField)getAllNode().get("unit_state_name")).setText(state.getName());
		status_type.setValue(state.getStatusType());
		activate_type.setValue(state.getActivateType());
		((Slider)getAllNode().get("variable_amount")).setValue(state.getVariableAmount());
		((Slider)getAllNode().get("fixed_amount")).setValue(state.getFixedAmount());
		((Slider)getAllNode().get("effect_time")).setValue(state.getEffectTime());
		((Text)getAllNode().get("variable_amount_value")).setText(String.valueOf(state.getVariableAmount()));
		((Text)getAllNode().get("fixed_amount_value")).setText(String.valueOf(state.getFixedAmount()));
		((Text)getAllNode().get("effect_time_value")).setText(String.valueOf(state.getEffectTime()));

	}
	@FXML
	private void addUnitState(ActionEvent e){
		UnitState state = new UnitState(
				skill_id + " " + "効果" + (state_list.getItems().size() + 1),
				"効果" + (state_list.getItems().size() + 1),
				UnitStatusType.HP, ActivateType.ALWAYS, 0, 0, 1);
		state_list.getItems().add(state);
		message.setText("");
	}
	@FXML
	private void changeUnitState(ActionEvent e){
		UnitState state = new UnitState(
				skill_id + " " + ((TextField)getAllNode().get("unit_state_id")).getText(),
				((TextField)getAllNode().get("unit_state_name")).getText(),
				status_type.getValue(), activate_type.getValue(),
				(int) ((Slider)getAllNode().get("variable_amount")).getValue(),
				(int) ((Slider)getAllNode().get("fixed_amount")).getValue(),
				(int) ((Slider)getAllNode().get("effect_time")).getValue());
		if(state_list.getItems().contains(state)){
			message.setText("同じIDが登録されています。違うIDにしてください");
			System.out.println("test");
			return;
		}
		state_list.getItems().set(state_list.getSelectionModel().getSelectedIndex(), state);
		state_list.getSelectionModel().select(state);
		message.setText("");
	}
	@FXML
	private void removeUnitState(ActionEvent e){
		state_list.getItems().remove(state_list.getSelectionModel().getSelectedIndex());
	}
	@Override
	public void edit(Skill edit_target) {
		this.skill_id = edit_target.getId();
		IgetStateListable skill = (IgetStateListable) edit_target;
		state_list.getItems().clear();
		state_list.getItems().addAll(skill.getStateList());
		state_list.getSelectionModel().select(0);
	}
	List<UnitState> getStateList(){ return this.state_list.getItems(); }
}
