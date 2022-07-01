package function.controller.state.edit.race;

import function.controller.state.edit.EditorControllerBase;
import function.unit.Race;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Slider;
import javafx.scene.text.Text;

public class RaceStatusEditController extends EditorControllerBase<Race> {

	public RaceStatusEditController() {
		super("race/RaceStatusEdit");
	}

	@FXML
	private void valueChange(Event e){
	    Slider s = (Slider)e.getSource();
		((Text)getAllNode().get(s.getId() + "_value")).setText(String.valueOf(Math.floor(s.getValue() * 10) / 10));
	}
	@Override
	public void edit(Race race){
		((Slider)getAllNode().get("hp")).setValue(race.getHpUp());
		((Slider)getAllNode().get("mp")).setValue(race.getMpUp());
		((Slider)getAllNode().get("attack")).setValue(race.getAttackUp());
		((Slider)getAllNode().get("defense")).setValue(race.getDefenseUp());
		((Slider)getAllNode().get("magic_attack")).setValue(race.getMagicAttackUp());
		((Slider)getAllNode().get("magic_defense")).setValue(race.getMagicDefenseUp());
		((Slider)getAllNode().get("exp_rate")).setValue(race.getExpRate());
		((Text)getAllNode().get("hp_value")).setText(String.valueOf(race.getHpUp()));
		((Text)getAllNode().get("mp_value")).setText(String.valueOf(race.getMpUp()));
		((Text)getAllNode().get("attack_value")).setText(String.valueOf(race.getAttackUp()));
		((Text)getAllNode().get("defense_value")).setText(String.valueOf(race.getDefenseUp()));
		((Text)getAllNode().get("magic_attack_value")).setText(String.valueOf(race.getMagicAttackUp()));
		((Text)getAllNode().get("magic_defense_value")).setText(String.valueOf(race.getMagicDefenseUp()));
		((Text)getAllNode().get("exp_rate_value")).setText(String.valueOf(race.getExpRate()));
	}
	public double getHp(){ return ((Slider)getAllNode().get("hp")).getValue(); }
	public double getMp(){ return ((Slider)getAllNode().get("mp")).getValue(); }
	public double getAttack(){ return ((Slider)getAllNode().get("attack")).getValue(); }
	public double getDefense(){ return ((Slider)getAllNode().get("defense")).getValue(); }
	public double getMagicAttack(){ return ((Slider)getAllNode().get("magic_attack")).getValue(); }
	public double getMagicDefense(){ return ((Slider)getAllNode().get("magic_defense")).getValue(); }
	public double getExpRate(){ return ((Slider)getAllNode().get("exp_rate")).getValue(); }
}
