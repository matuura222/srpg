package function.controller.state.edit.job;

import function.controller.state.edit.EditorControllerBase;
import function.unit.Job;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Slider;
import javafx.scene.text.Text;

public class JobStatusEditController extends EditorControllerBase<Job> {
	public JobStatusEditController() {
		super("job/JobStatusEdit");
	}

	@FXML
	private void valueChange(Event e){
	    Slider s = (Slider)e.getSource();
		((Text)getAllNode().get(s.getId() + "_value")).setText(String.valueOf(Math.floor(s.getValue() * 10) / 10));
	}
	@Override
	public void edit(Job job){
		((Slider)getAllNode().get("hp")).setValue(job.getHpUp());
		((Slider)getAllNode().get("mp")).setValue(job.getMpUp());
		((Slider)getAllNode().get("attack")).setValue(job.getAttackUp());
		((Slider)getAllNode().get("defense")).setValue(job.getDefenseUp());
		((Slider)getAllNode().get("magic_attack")).setValue(job.getMagicAttackUp());
		((Slider)getAllNode().get("magic_defense")).setValue(job.getMagicDefenseUp());
		((Slider)getAllNode().get("exp_rate")).setValue(job.getExpRate());
		((Text)getAllNode().get("hp_value")).setText(String.valueOf(job.getHpUp()));
		((Text)getAllNode().get("mp_value")).setText(String.valueOf(job.getMpUp()));
		((Text)getAllNode().get("attack_value")).setText(String.valueOf(job.getAttackUp()));
		((Text)getAllNode().get("defense_value")).setText(String.valueOf(job.getDefenseUp()));
		((Text)getAllNode().get("magic_attack_value")).setText(String.valueOf(job.getMagicAttackUp()));
		((Text)getAllNode().get("magic_defense_value")).setText(String.valueOf(job.getMagicDefenseUp()));
		((Text)getAllNode().get("exp_rate_value")).setText(String.valueOf(job.getExpRate()));
	}
	public double getHp(){ return ((Slider)getAllNode().get("hp")).getValue(); }
	public double getMp(){ return ((Slider)getAllNode().get("mp")).getValue(); }
	public double getAttack(){ return ((Slider)getAllNode().get("attack")).getValue(); }
	public double getDefense(){ return ((Slider)getAllNode().get("defense")).getValue(); }
	public double getMagicAttack(){ return ((Slider)getAllNode().get("magic_attack")).getValue(); }
	public double getMagicDefense(){ return ((Slider)getAllNode().get("magic_defense")).getValue(); }
	public double getExpRate(){ return ((Slider)getAllNode().get("exp_rate")).getValue(); }

}
