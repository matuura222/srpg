package function.controller.state.edit.unit;

import java.util.stream.Collectors;

import common.SRPGCommons;
import function.controller.state.edit.EditorControllerBase;
import function.skill.Skill;
import function.skill.SkillType;
import function.unit.Job;
import function.unit.Race;
import function.unit.Unit;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.text.Text;

public class UnitStatusEditController extends EditorControllerBase<Unit> {
	@FXML
	private ListView<Skill> job_active_skill, job_passive_skill, race_active_skill, race_passive_skill;
	private Job job = SRPGCommons.getJob("障害物");
	private Race race = SRPGCommons.getRace("障害物");
	public UnitStatusEditController(){
		super("/unit/UnitStatusEdit");
	}
	@FXML
    void valueChange(Event e) {
    	Slider s = (Slider)e.getSource();
		((Text)getAllNode().get(s.getId() + "_value")).setText(String.valueOf(Math.round(s.getValue())));
    }
	public void updateJob(){
		if(SRPGCommons.getJob(job.getId()) == null){ changeJob(SRPGCommons.getJob("")); return; }
		changeJob(SRPGCommons.getJob(job.getId()));
	}
	public void changeJob(Job job){
		if(job == null){ job = SRPGCommons.getJob(""); }
		this.job = job;
		job_active_skill.getItems().clear();
		job_active_skill.getItems().addAll(SRPGCommons.getSkillList().stream()
				.filter(s -> this.job.getSkillIdList().contains(s.getId()))
				.filter(s -> s.getSkillType() != SkillType.CONDITIONS)
				.collect(Collectors.toList()));
		job_passive_skill.getItems().clear();
		job_passive_skill.getItems().addAll(SRPGCommons.getSkillList().stream()
				.filter(s -> this.job.getSkillIdList().contains(s.getId()))
				.filter(s -> s.getSkillType() == SkillType.CONDITIONS)
				.collect(Collectors.toList()));
		upStatusUpdate();
	}
	public void updateRace(){
		if(SRPGCommons.getRace(race.getId()) == null){ changeJob(SRPGCommons.getJob("")); return; }
		changeRace(SRPGCommons.getRace(race.getId()));
	}
	public void changeRace(Race race){
		if(race == null){ race = SRPGCommons.getRace(""); }
		this.race = race;
		race_active_skill.getItems().clear();
		race_active_skill.getItems().addAll(SRPGCommons.getSkillList().stream()
				.filter(s -> this.race.getSkillIdList().contains(s.getId()))
				.filter(s -> s.getSkillType() != SkillType.CONDITIONS)
				.collect(Collectors.toList()));
		race_passive_skill.getItems().clear();
		race_passive_skill.getItems().addAll(SRPGCommons.getSkillList().stream()
				.filter(s -> this.race.getSkillIdList().contains(s.getId()))
				.filter(s -> s.getSkillType() == SkillType.CONDITIONS)
				.collect(Collectors.toList()));
		upStatusUpdate();
	}
	private void upStatusUpdate(){
		((Text)getAllNode().get("lv_up_hp")).setText(String.valueOf(job.getHpUp() + race.getHpUp()));
		((Text)getAllNode().get("lv_up_mp")).setText(String.valueOf(job.getMpUp() + race.getMpUp()));
		((Text)getAllNode().get("lv_up_attack")).setText(String.valueOf(job.getAttackUp() + race.getAttackUp()));
		((Text)getAllNode().get("lv_up_defense")).setText(String.valueOf(job.getDefenseUp() + race.getDefenseUp()));
		((Text)getAllNode().get("lv_up_magic_attack")).setText(String.valueOf(job.getMagicAttackUp() + race.getMagicAttackUp()));
		((Text)getAllNode().get("lv_up_magic_defense")).setText(String.valueOf(job.getMagicDefenseUp() + race.getMagicDefenseUp()));
	}
	@Override
	public void edit(Unit unit){
		((Slider)getAllNode().get("hp")).setValue(unit.getBasicMaxHp());
		((Slider)getAllNode().get("mp")).setValue(unit.getBasicMaxMp());
		((Slider)getAllNode().get("attack")).setValue(unit.getBasicAttack());
		((Slider)getAllNode().get("defense")).setValue(unit.getBasicDefense());
		((Slider)getAllNode().get("magic_attack")).setValue(unit.getBasicMagicAttack());
		((Slider)getAllNode().get("magic_defense")).setValue(unit.getBasicMagicDefense());
		((Slider)getAllNode().get("move")).setValue(unit.getBasicMove());
		((Slider)getAllNode().get("attack_range")).setValue(unit.getBasicAttackRange());
		((Text)getAllNode().get("hp_value")).setText(String.valueOf(unit.getBasicMaxHp()));
		((Text)getAllNode().get("mp_value")).setText(String.valueOf(unit.getBasicMaxMp()));
		((Text)getAllNode().get("attack_value")).setText(String.valueOf(unit.getBasicAttack()));
		((Text)getAllNode().get("defense_value")).setText(String.valueOf(unit.getBasicDefense()));
		((Text)getAllNode().get("magic_attack_value")).setText(String.valueOf(unit.getBasicMagicAttack()));
		((Text)getAllNode().get("magic_defense_value")).setText(String.valueOf(unit.getBasicMagicDefense()));
		((Text)getAllNode().get("move_value")).setText(String.valueOf(unit.getBasicMove()));
		((Text)getAllNode().get("attack_range_value")).setText(String.valueOf(unit.getBasicAttackRange()));
		changeJob(unit.getJob());
		changeRace(unit.getRace());
	}
	public int getHp(){ return (int)((Slider)getAllNode().get("hp")).getValue(); }
	public int getMp(){ return (int)((Slider)getAllNode().get("mp")).getValue(); }
	public int getAttack(){ return (int)((Slider)getAllNode().get("attack")).getValue(); }
	public int getDefense(){ return (int)((Slider)getAllNode().get("defense")).getValue(); }
	public int getMagicAttack(){ return (int)((Slider)getAllNode().get("magic_attack")).getValue(); }
	public int getMagicDefense(){ return (int)((Slider)getAllNode().get("magic_defense")).getValue(); }
	public int getMove(){ return (int)((Slider)getAllNode().get("move")).getValue(); }
	public int getAttackRange(){ return (int)((Slider)getAllNode().get("attack_range")).getValue(); }
}
