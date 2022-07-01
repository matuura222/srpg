package function.controller.state.edit.skill;

import function.bgm.SoundEffect;
import function.controller.state.edit.EditorControllerBase;
import function.skill.Skill;
import function.skill.TargetType;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class SkillStatusEditController extends EditorControllerBase<Skill> {
	@FXML
	private ChoiceBox<TargetType> target_type;
	public SkillStatusEditController(){
		super("skill/SkillStatusEdit");
		target_type.getItems().addAll(TargetType.values());
	}
	@FXML
    private void valueChange(Event e) {
    	Slider s = (Slider)e.getSource();
		((Text)getAllNode().get(s.getId() + "_value")).setText(String.valueOf(Math.round(s.getValue())));
    }
	@FXML
	private void bgmPlay(ActionEvent e){
		SoundEffect.play(((TextField)getAllNode().get("sound_effect_name")).getText());
	}
	@Override
	public void edit(Skill skill){
		((Slider)getAllNode().get("cost")).setValue(skill.getCost());
		((Slider)getAllNode().get("exp")).setValue(skill.getExp());
		((Slider)getAllNode().get("variable_power")).setValue(skill.getVariablePower());
		((Slider)getAllNode().get("fixed_power")).setValue(skill.getFixedPower());
		((Slider)getAllNode().get("range_distance")).setValue(skill.getRangeDistance());
		((Slider)getAllNode().get("effect_range")).setValue(skill.getEffectRange());
		target_type.getSelectionModel().select(skill.getTargetType());
		((Text)getAllNode().get("cost_value")).setText(String.valueOf(skill.getCost()));
		((Text)getAllNode().get("exp_value")).setText(String.valueOf(skill.getExp()));
		((Text)getAllNode().get("variable_power_value")).setText(String.valueOf(skill.getVariablePower()));
		((Text)getAllNode().get("fixed_power_value")).setText(String.valueOf(skill.getFixedPower()));
		((Text)getAllNode().get("range_distance_value")).setText(String.valueOf(skill.getRangeDistance()));
		((Text)getAllNode().get("effect_range_value")).setText(String.valueOf(skill.getEffectRange()));
		((TextField)getAllNode().get("sound_effect_name")).setText(skill.getSoundEffectName());
		((TextArea)getAllNode().get("skill_text")).setText(skill.getSkillText());
	}

	public int getCost(){ return (int)((Slider)getAllNode().get("cost")).getValue(); }
	public int getExp(){ return (int)((Slider)getAllNode().get("exp")).getValue(); }
	public int getVariablePower(){ return (int)((Slider)getAllNode().get("variable_power")).getValue(); }
	public int getFixedPower(){ return (int)((Slider)getAllNode().get("fixed_power")).getValue(); }
	public int getRangeDistance(){ return (int)((Slider)getAllNode().get("range_distance")).getValue(); }
	public int getEffectRange(){ return (int)((Slider)getAllNode().get("effect_range")).getValue(); }
	public TargetType getTargetTaype(){ return target_type.getSelectionModel().getSelectedItem(); }
	public void setTargetTypes(TargetType... target_type_list){
		if(target_type_list.length == 0){ return; }
		target_type.getItems().clear();
		target_type.getItems().addAll(target_type_list);
		target_type.getSelectionModel().select(0);
	}
	public String getSkillText(){ return ((TextArea)getAllNode().get("skill_text")).getText(); }
	public String getSoundEffectName() { return ((TextField)getAllNode().get("sound_effect_name")).getText(); }
}
