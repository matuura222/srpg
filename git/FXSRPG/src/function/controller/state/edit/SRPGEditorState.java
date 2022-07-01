package function.controller.state.edit;

import java.io.File;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import common.IEditable;
import common.SRPGCommons;
import function.GameData;
import function.GameDataContainer;
import function.controller.state.StateBase;
import function.controller.state.edit.job.JobEditController;
import function.controller.state.edit.mapchip.MapChipEditController;
import function.controller.state.edit.race.RaceEditController;
import function.controller.state.edit.skill.SkillEditController;
import function.controller.state.edit.unit.UnitEditController;
import function.map.MapChip;
import function.skill.Skill;
import function.unit.Job;
import function.unit.Race;
import function.unit.Unit;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

public class SRPGEditorState extends StateBase implements IEditable{
	@FXML
	private ListView<Unit> unit_list;
	@FXML
	private ListView<Job> job_list;
	@FXML
	private ListView<Race> race_list;
	@FXML
	private ListView<Skill> skill_list;
	@FXML
	private ListView<MapChip> map_chip_list;
	@FXML
	private AnchorPane edit_pane;
	@FXML
	private TabPane edit_tab_pane;
	@FXML
	private TextArea message_area;
	private UnitEditController unit_edit;
	private JobEditController job_edit;
	private RaceEditController race_edit;
	private SkillEditController skill_edit;
	private MapChipEditController map_chip_edit;
	private EditorLeftClickedMenu left_click_menu;
	private final static Pane NONE_PANE = new Pane();
	public SRPGEditorState() {
		super("../edit/SRPGEditor");
		Map<String, Skill> id_to_skill = SRPGCommons.getSkillList().stream()
				.collect(Collectors.toMap(s -> s.getId(), s -> s));
		skill_list.getItems().addAll(id_to_skill.values());

		Map<String, Job> id_to_job = SRPGCommons.getJobList().stream()
				.collect(Collectors.toMap(s -> s.getId(), s -> s));
		job_list.getItems().addAll(id_to_job.values());

		Map<String, Race> id_to_race = SRPGCommons.getRaceList().stream()
				.collect(Collectors.toMap(s -> s.getId(), s -> s));
		race_list.getItems().addAll(id_to_race.values());

		Map<String, Unit> id_to_unit = SRPGCommons.getUnitList().stream()
				.collect(Collectors.toMap(s -> s.getId(), s -> s));
		unit_list.getItems().addAll(id_to_unit.values());

		Map<String, MapChip> id_to_map_chip = SRPGCommons.getMapChipList().stream()
				.collect(Collectors.toMap(s -> s.getId(), s -> s));
		map_chip_list.getItems().addAll(id_to_map_chip.values());

		unit_edit = new UnitEditController(this);
		race_edit = new RaceEditController(this);
		job_edit = new JobEditController(this);
		skill_edit = new SkillEditController(this);
		map_chip_edit = new MapChipEditController(this);
		left_click_menu = new EditorLeftClickedMenu(this);
	}
	@Override
	public void startState(){

	}
	@Override
	protected void drawState() {
		// TODO 自動生成されたメソッド・スタブ

	}
	@Override
	protected StateBase decideState() {
		if("EditTitle".equals(getStateName())){ return new EditTitleState(); }
		return this;
	}
	@Override
	public void endState() {
		// TODO 自動生成されたメソッド・スタブ

	}
	private void menuShow(MouseEvent e){
		if(((ListView<?>)e.getSource()).getSelectionModel().getSelectedItem() == null){ return; }
		//menu.show((ListView<?>)e.getSource(), e.getScreenX(), e.getScreenY());
		left_click_menu.show((ListView<?>)e.getSource(), e.getScreenX(), e.getScreenY());
	}
	public void edit(Event e){
		EditorControllerBase<?> edit_pane = null;
		switch(edit_tab_pane.getSelectionModel().getSelectedItem().getText()){
			case "ユニット" :
				edit_pane = unit_edit;
				unit_edit.edit(unit_list.getSelectionModel().getSelectedItem());
				this.edit_pane.getChildren().set(0, unit_edit);
				break;
			case "職業" :
				edit_pane = job_edit;
				job_edit.edit(job_list.getSelectionModel().getSelectedItem());
				this.edit_pane.getChildren().set(0, job_edit);
				break;
			case "種族" :
				edit_pane = race_edit;
				race_edit.edit(race_list.getSelectionModel().getSelectedItem());
				this.edit_pane.getChildren().set(0, race_edit);
				break;
			case "スキル" :
				edit_pane = skill_edit;
				skill_edit.edit(skill_list.getSelectionModel().getSelectedItem());
				this.edit_pane.getChildren().set(0, skill_edit);
				break;
			case "マップチップ" :
				edit_pane = map_chip_edit;
				map_chip_edit.edit(map_chip_list.getSelectionModel().getSelectedItem());
				this.edit_pane.getChildren().set(0, map_chip_edit);
				break;
		}
		AnchorPane.setTopAnchor(edit_pane, 10.0);
		AnchorPane.setBottomAnchor(edit_pane, 0.0);
		AnchorPane.setLeftAnchor(edit_pane, 0.0);
		AnchorPane.setRightAnchor(edit_pane, 0.0);
		@SuppressWarnings("unchecked")
		ListView<GameData> edit = (ListView<GameData>) edit_tab_pane.getSelectionModel().getSelectedItem().getContent();
		if(edit.getSelectionModel().getSelectedItem().getId().equals("")){ return; }
	}
	public void copy(Event e){
		edit(e);
	}
	public void add(Event e){
		edit(e);
	}
	public void remove(Event e){
		edit_pane.getChildren().set(0, NONE_PANE);
	}
	public void unitUpdate(Unit old_unit, Unit new_unit){
		unit_list.getSelectionModel().select(old_unit);
		int i = unit_list.getSelectionModel().getSelectedIndex();
		unit_list.getItems().set(i, new_unit);
		unit_list.getSelectionModel().select(i);
		save(unit_list.getItems().stream().collect(Collectors.toMap(s -> s.getId(), s -> s)), "./data/unit/unit.dat");
	}
	public void raceUpdate(Race old_race, Race new_race){
		race_list.getSelectionModel().select(old_race);
		int i = race_list.getSelectionModel().getSelectedIndex();
		race_list.getItems().set(i, new_race);
		race_list.getSelectionModel().select(i);
		unit_edit.updateRace(race_list.getItems());
		save(race_list.getItems().stream().collect(Collectors.toMap(s -> s.getId(), s -> s)), "./data/unit/race.dat");
	}
	public void jobUpdate(Job old_job, Job new_job){
		job_list.getSelectionModel().select(old_job);
		int i = job_list.getSelectionModel().getSelectedIndex();
		job_list.getItems().set(i, new_job);
		job_list.getSelectionModel().select(i);
		unit_edit.updateJob(job_list.getItems());
		save(job_list.getItems().stream().collect(Collectors.toMap(s -> s.getId(), s -> s)), "./data/unit/job.dat");
	}
	public void skillUpdate(Skill old_skill, Skill new_skill){
		skill_list.getSelectionModel().select(old_skill);
		int i = skill_list.getSelectionModel().getSelectedIndex();
		skill_list.getItems().set(i, new_skill);
		skill_list.getSelectionModel().select(i);
		unit_edit.updateSkill(skill_list.getItems());
		save(skill_list.getItems().stream().collect(Collectors.toMap(s -> s.getId(),  s -> s)), "./data/skill/skill.dat");
	}
	public void mapChipUpdate(MapChip old_map_chip, MapChip new_map_chip){
		map_chip_list.getSelectionModel().select(old_map_chip);
		int i = map_chip_list.getSelectionModel().getSelectedIndex();
		map_chip_list.getItems().set(i, new_map_chip);
		map_chip_list.getSelectionModel().select(i);
		save(map_chip_list.getItems().stream().collect(Collectors.toMap(s -> s.getId(),  s -> s)), "./data/map/mapchip.dat");
	}
	@FXML
	private void gameNodeEdit(MouseEvent e){
		if(MouseButton.SECONDARY == e.getButton()){ menuShow(e); }
		if(MouseButton.PRIMARY != e.getButton()){ return; }
		if(e.getClickCount() < 2){ return; }
		edit(e);
	}
	@FXML
	private void save(ActionEvent e){
		save(unit_list.getItems().stream().collect(Collectors.toMap(s -> s.getId(), s -> s)), "./data/unit/unit.dat");
		save(race_list.getItems().stream().collect(Collectors.toMap(s -> s.getId(), s -> s)), "./data/unit/race.dat");
		save(job_list.getItems().stream().collect(Collectors.toMap(s -> s.getId(), s -> s)), "./data/unit/job.dat");
		save(skill_list.getItems().stream().collect(Collectors.toMap(s -> s.getId(),  s -> s)), "./data/skill/skill.dat");
		save(map_chip_list.getItems().stream().collect(Collectors.toMap(s -> s.getId(),  s -> s)), "./data/map/mapchip.dat");
		message_area.setText("全てのデータをセーブしました");
}
	@FXML
	private void quit(ActionEvent e){
		Platform.exit();
	}
	private void save(Map<String, GameData> save_map, String save_place){
		Map<String, GameData> m = new TreeMap<>(new Comparator<String>(){
			@Override
			public int compare(String k1, String k2){
				return k2.compareTo(k1);
			}
		});
		m.putAll(save_map);
		GameDataContainer<GameData> continer = new GameDataContainer<GameData>(m);
		File file = new File(save_place);
		SRPGCommons.saveData(file, continer);
	}
	public void setMessage(String message) {
		message_area.setText(message);
	}
}
