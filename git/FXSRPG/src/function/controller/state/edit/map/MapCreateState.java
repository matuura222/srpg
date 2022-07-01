package function.controller.state.edit.map;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import common.IEditable;
import common.SRPGCommons;
import function.controller.mapchip.MapChipStatusController;
import function.controller.state.StateBase;
import function.controller.state.edit.EditTitleState;
import function.controller.state.edit.EditorLeftClickedMenu;
import function.controller.state.edit.unit.UnitLVUpController;
import function.controller.unit.UnitDetailsController;
import function.map.GameMap;
import function.map.GameMapData;
import function.map.MapChip;
import function.map.Square;
import function.unit.Unit;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.SubScene;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;

public class MapCreateState extends StateBase implements IEditable {
	@FXML
	private SubScene game_map_scene;
	@FXML
	private ListView<Unit> unit_list;
	@FXML
	private ListView<MapChip> map_chip_list;
	@FXML
	private ListView<GameMapData> game_map_list;
	@FXML
	private TabPane status_tab_pane;
	@FXML
	private AnchorPane status_pane, map_status_pane;
	@FXML
	private TextArea message;
	private UnitDetailsController unit_status = new UnitDetailsController();
	private MapChipStatusController map_chip_status = new MapChipStatusController();
	private GameMapStatusController game_map_status = new GameMapStatusController();
	private GameMap game_map = new GameMap();
	private EditorLeftClickedMenu menu = new EditorLeftClickedMenu(this);
	public MapCreateState() {
		super("../edit/map/MapCreate");
		unit_list.getItems().addAll(SRPGCommons.getUnitList());
		unit_list.getItems().add(Unit.defaultData());
		map_chip_list.getItems().addAll(SRPGCommons.getMapChipList());
		File dir = new File("./data/map/map");
		String[] file_name_list = dir.list();
		for(String file_name : file_name_list){
			if(file_name.equals(".dat")){ continue; }
			File file = new File("./data/map/map/" + file_name);
			game_map_list.getItems().add((GameMapData)SRPGCommons.getData(file, GameMapData.class));
		}
	}
	@Override
	public void startState(){
	}
	@Override
	public void drawState() {
		game_map.update();
	}
	@Override
	protected StateBase decideState() {
		if("EditTitle".equals(getStateName())){ return new EditTitleState(); }
		return this;
	}
	@Override
	public void endState(){
	}
	private void showMenu(MouseEvent e){
		menu.show((Node) e.getSource(), e.getScreenX(), e.getScreenY());
	}
	@FXML
	private void save(){
		String old_id = game_map.getGameMapData().getId();
		String new_id = game_map_status.getMapId();
		boolean can_save = true;
		String s = "";
		if(!old_id.equals(new_id) && Files.exists(Paths.get("./data/map/map/" + game_map_status.getMapId() + ".dat"))){
			s += "すでに同じIDのマップがあります\n別のIDにしてください\n";
			can_save = false;
		}
		GameMapData new_data = (GameMapData) game_map.getGameMapData().copy(new_id, game_map_status.getMapName());
		if(new_data.getEnemyList().size() <= 0){
			s += "最低1体の敵ユニットを配置してください\n";
			can_save = false;
		}
		if(new_data.getSortieAreaNum() <= 0){
			s += "最低１マスの味方エリアを配置してください\n";
			can_save = false;
		}
		if(!can_save){
			message.setText("セーブできませんでした\n" + s);
			return;
		}
		message.setText("");
		File old_file = new File("./data/map/map/" + old_id + ".dat");
		File new_file = new File("./data/map/map/" + new_id + ".dat");
		old_file.renameTo(new_file);
		SRPGCommons.saveData(new_file, new_data);
		game_map_list.getItems().set(game_map_list.getItems().indexOf(game_map.getGameMapData()), new_data);
		//game_map_list.getSelectionModel().select(new_data);

	}
	@FXML
	private void initMap(ActionEvent e){
		game_map.init((GameMapData) GameMapData.defaultData().copy(game_map.getGameMapData().getId(), game_map.getGameMapData().getName()));
		game_map_status.detailsShow(game_map.getGameMapData());
	}
	@FXML
	private void mouseEntered(MouseEvent e){
		game_map.mapMoveStart();
	}
	@FXML
	private void mouseExited(MouseEvent e){
		game_map.mapMoveStop();
	}
	@FXML
	private void mouseDragged(MouseEvent e){
		mapMove(e);
	}
	@FXML
	private void mapMove(MouseEvent e){
		double x = e.getSceneX() - game_map_scene.localToScene(game_map_scene.getBoundsInLocal()).getMinX();
		double y = e.getSceneY() - game_map_scene.localToScene(game_map_scene.getBoundsInLocal()).getMinY();
		game_map.mapMove(x, y);
	}
	@FXML
	private void selectSquare(MouseEvent e){
		if(game_map_status.getMapId().equals("")){ message.setText("マップIDが入力されていません。入力してください"); return; }
		double x = e.getSceneX() - game_map_scene.localToScene(game_map_scene.getBoundsInLocal()).getMinX();
		double y = e.getSceneY() - game_map_scene.localToScene(game_map_scene.getBoundsInLocal()).getMinY();
		Square square = game_map.getSquare(x, y);
		if(e.getButton() == MouseButton.SECONDARY){
			game_map.setIsSortieArea(square, !square.isSortieArea());
			game_map_status.update();
		}
		if(e.getButton() == MouseButton.MIDDLE){
			Unit unit = game_map.getSquare(x, y).getUnit();
			if(unit != null){
				UnitLVUpController details = new UnitLVUpController();
				details.edit(unit);
				details.setId(unit.getName());
				addSubWindowPane(details);
			}
		}
		if(e.getButton() != MouseButton.PRIMARY){ return; }
		Tab tab = status_tab_pane.getSelectionModel().getSelectedItem();
		if(tab.getText().equals("ユニット")){
			Unit unit = unit_list.getSelectionModel().getSelectedItem();
			if(unit == null){ return; }
			if(unit.getId().equals("")){
				game_map.setUnit(x, y, null);
				game_map_status.update();
				return;
			}
			int num = 0;
			String id = unit.getId();
			String name = unit.getName();
			unit.setIsFriend(square.isSortieArea());
			do{
				unit = (Unit) unit.copy(id + num, name + num);
				num++;
			}while(game_map.getGameMapData().getUnitList().contains(unit));
			Square s = game_map.getSquare(x, y);
			if(s.isSortieArea()){
				unit.setIsFriend(true);
			}else{
				unit.setIsFriend(false);
			}
			game_map.setUnit(x, y, unit);
		}else if(tab.getText().equals("マップチップ")){
			MapChip map_chip = map_chip_list.getSelectionModel().getSelectedItem();
			if(map_chip.getId().equals("")){ return; }
			game_map.setMapChip(x, y, map_chip);
		}else if(tab.getText().equals("マップ")){
			Unit unit = game_map.getUnit(x, y);
			if(unit == null){ return; }
			unit_status.detailsShow(unit);
			status_pane.getChildren().set(0, unit_status);
		}
		game_map_status.update();
		message.setText("");
	}
	@FXML
	private void selectMap(MouseEvent e){
		if(e.getButton() == MouseButton.SECONDARY){ showMenu(e); return; }
		if(e.getButton() != MouseButton.PRIMARY){ return; }
		if(game_map_list.getSelectionModel().getSelectedItem() == null){ return; }
		if(game_map_list.getSelectionModel().getSelectedItem().getId().equals("")){ return; }
		if(game_map_list.getSelectionModel().getSelectedItem().getId().equals(game_map.getGameMapData().getId())) { return; }
		if(e.getClickCount() < 2){ return; }
		edit(e);
	}
	@FXML
	private void selectUnit(MouseEvent e){
		if(e.getButton() != MouseButton.PRIMARY){ return; }
		if(unit_list.getSelectionModel().getSelectedItem() == null){ return; }
		if(unit_list.getSelectionModel().getSelectedItem().getId().equals("")){ return; }
		unit_status.detailsShow(unit_list.getSelectionModel().getSelectedItem());
		status_pane.getChildren().set(0, unit_status);
		AnchorPane.setTopAnchor(unit_status, 0.0);
		AnchorPane.setBottomAnchor(unit_status, 0.0);
		AnchorPane.setLeftAnchor(unit_status, 0.0);
		AnchorPane.setRightAnchor(unit_status, 0.0);
	}
	@FXML
	private void selectMapChip(MouseEvent e){
		if(e.getButton() != MouseButton.PRIMARY){ return; }
		if(map_chip_list.getSelectionModel().getSelectedItem() == null){ return; }
		if(map_chip_list.getSelectionModel().getSelectedItem().getId().equals("")){ return; }
		map_chip_status.detailsShow(map_chip_list.getSelectionModel().getSelectedItem());
		status_pane.getChildren().set(0, map_chip_status);
		AnchorPane.setTopAnchor(map_chip_status, 0.0);
		AnchorPane.setBottomAnchor(map_chip_status, 0.0);
		AnchorPane.setLeftAnchor(map_chip_status, 0.0);
		AnchorPane.setRightAnchor(map_chip_status, 0.0);
	}
	@Override
	public void edit(Event e) {
		game_map_scene.setCamera(game_map.getCamera());
		game_map_scene.setRoot(game_map);
		GameMapData data = game_map_list.getSelectionModel().getSelectedItem().clone();
		game_map.init(data);
		game_map_status.detailsShow(data);
		map_status_pane.getChildren().set(0, game_map_status);
	}
	@Override
	public void add(Event e) {
		edit(e);
	}
	@Override
	public void copy(Event e) {
		edit(e);
	}
	@Override
	public void remove(Event e) {
		File dir = new File("./data/map/map");
		List<String> list = game_map_list.getItems().stream().map(s -> s.getId()).collect(Collectors.toList());
		for(String s : dir.list()){
			if(!list.contains(s.subSequence(0, s.length() - 4))){
				File file = new File("./data/map/map/" + s);
				file.delete();
				break;
			}
		}
		game_map_scene.setCamera(null);
		game_map_scene.setRoot(new Region());
	}
}
