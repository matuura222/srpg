package function.controller.state.edit.unit;

import java.util.Collection;

import common.SRPGCommons;
import function.controller.state.edit.EditorControllerBase;
import function.controller.state.edit.SRPGEditorState;
import function.controller.state.edit.skill.HaveSkillController;
import function.skill.Skill;
import function.unit.Job;
import function.unit.Race;
import function.unit.ThoughtType;
import function.unit.Unit;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class UnitEditController extends EditorControllerBase<Unit> {
	@FXML
    private TextField unit_name, image_name;
    @FXML
    private ImageView image;
    @FXML
    private ChoiceBox<Race> race;
    @FXML
    private ChoiceBox<Job> job;
    @FXML
    private ChoiceBox<ThoughtType> thought_type;
    @FXML
    private TextField unit_id;
    @FXML
    private UnitStatusEditController status;
    @FXML
    private HaveSkillController<Unit> skill;
    private SRPGEditorState editor;
    private Unit unit;
	public UnitEditController(SRPGEditorState editor){
		super("/unit/UnitEdit");
		job.getItems().addAll(SRPGCommons.getJobList());
		job.setValue(SRPGCommons.getJob("障害物"));
		job.setOnAction(this::changeJob);
		race.getItems().addAll(SRPGCommons.getRaceList());
		race.setValue(SRPGCommons.getRace("障害物"));
		race.setOnAction(this::changeRace);
		thought_type.getItems().addAll(ThoughtType.values());
		thought_type.setValue(ThoughtType.CHARGE);
		this.editor = editor;
	}
	@FXML
	private void updateImage(){
		image.setImage(new Image(SRPGCommons.getURL() + "/unit/img/" + image_name.getText()));
		if(image.getImage().isError()){
			image.setImage(new Image(SRPGCommons.getURL() + "/unit/img/NotImage.png"));
		}
	}
	@FXML
	private void save(ActionEvent e){
		Unit unit = new Unit(unit_id.getText(), unit_name.getText(), 1, 0, job.getSelectionModel().getSelectedItem().getName(),
				race.getSelectionModel().getSelectedItem().getName(), skill.getHaveSkillIdList(), status.getHp(), status.getMp(),
				status.getAttack(), status.getDefense(), status.getMagicAttack(), status.getMagicDefense(), status.getMove(),
				status.getAttackRange(), thought_type.getSelectionModel().getSelectedItem(), image_name.getText());
		if((SRPGCommons.getUnitList().contains(this.unit) == false && SRPGCommons.getUnitList().contains(unit))
				|| (this.unit.equals(unit) == false && SRPGCommons.getUnitList().contains(unit))){
			editor.setMessage("識別IDが重複しています。\n別のIDにしてください");
			return;
		}
		editor.setMessage("ユニット：" + unit.getName() + "をセーブしました");
		editor.unitUpdate(this.unit, unit);
		this.unit = unit;
	}
	private void changeJob(ActionEvent e){
		if(job.getValue() == null){ return; }
		status.changeJob(job.getValue());
	}
	private void changeRace(ActionEvent e){
		if(race.getValue() == null){ return; }
		status.changeRace(race.getValue());
	}
	public void updateJob(Collection<Job> job_list){
		job.getItems().clear();
		job.getItems().addAll(job_list);
		status.updateJob();
	}
	public void updateRace(Collection<Race> race_list){
		race.getItems().clear();
		race.getItems().addAll(race_list);
		status.updateRace();
	}
	public void updateSkill(Collection<Skill> skill_list){

	}
	@Override
	public void edit(Unit unit){
		this.unit = unit;
		unit_name.setText(unit.getName());
		image_name.setText(unit.getImageName());
		unit_id.setText(unit.getId());
		updateImage();
		job.setValue(unit.getJob());
		race.setValue(unit.getRace());
		thought_type.setValue(unit.getThoughtType());
		status.edit(unit);
		skill.edit(unit);
	}
}
