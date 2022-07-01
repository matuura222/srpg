package function.controller.state.edit.race;

import java.util.Collection;

import common.SRPGCommons;
import function.controller.state.edit.EditorControllerBase;
import function.controller.state.edit.SRPGEditorState;
import function.controller.state.edit.skill.HaveSkillController;
import function.skill.Skill;
import function.unit.Race;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class RaceEditController  extends EditorControllerBase<Race> {
	@FXML
    private TextField race_name, race_id;
	@FXML
	RaceStatusEditController status;
	@FXML
	HaveSkillController<Race> skill;
	SRPGEditorState editor;
	Race race;
	public RaceEditController(SRPGEditorState editor){
		super("race/RaceEdit");
		this.editor = editor;
	}
	@FXML
	private void save(ActionEvent e){
		Race race = new Race(race_id.getText(), race_name.getText(), status.getHp(), status.getMp(), status.getAttack(),
				status.getDefense(), status.getMagicAttack(), status.getMagicDefense(), skill.getHaveSkillIdList(), status.getExpRate());
		if((SRPGCommons.getRaceList().contains(this.race) == false && SRPGCommons.getRaceList().contains(race))
				|| (this.race.equals(race) == false && SRPGCommons.getRaceList().contains(race))){
			editor.setMessage("識別IDが重複しています。\n別のIDにしてください");
			return;
		}
		editor.setMessage("種族：" + race.getId() + "をセーブしました");
		editor.raceUpdate(this.race, race);
		this.race = race;
	}
	@Override
	public void edit(Race race){
		this.race = race;
		race_id.setText(race.getId());
		race_name.setText(race.getName());
		status.edit(race);
		skill.edit(race);
	}
	public void skillUpdate(Collection<Skill> skill_list){

	}
}
