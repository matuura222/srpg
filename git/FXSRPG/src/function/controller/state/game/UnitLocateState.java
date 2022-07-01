package function.controller.state.game;

import function.bgm.SoundEffect;
import function.controller.state.StateBase;
import function.controller.state.game.battle.BattleState;
import function.controller.state.game.sub.MyUnitList;
import function.controller.unit.UnitDetailsController;
import function.map.GameMap;
import function.map.GameMapData;
import function.map.Square;
import function.unit.Unit;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.SubScene;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

/**
 * ユニットの配置を決めるステートクラスです
 * @author 樹麗
 */
public class UnitLocateState extends StateBase {
	@FXML
	private SubScene game_map_scene;
	@FXML
	private MyUnitList unit_list;
	GameMap game_map = new GameMap();
	/**
	 * マップデータを読み込む
	 * @param data マップデータ
	 */
	public UnitLocateState(GameMapData data) {
		super("UnitLocate");
		game_map.init(data);
	}
	/* (非 Javadoc)
	 * @see function.controller.state.StateBase#startState()
	 */
	public void startState(){
		game_map_scene.setCamera(game_map.getCamera());
		game_map_scene.setRoot(game_map);
	}
	/* (非 Javadoc)
	 * @see function.controller.state.StateBase#drawState()
	 */
	@Override
	public void drawState() {
		game_map.update();
	}
	/* (非 Javadoc)
	 * @see function.controller.state.StateBase#decideState()
	 */
	@Override
	protected StateBase decideState() {
		if("GameBattle".equals(getStateName())){ return new BattleState(game_map.getGameMapData()); }
		if("StageSelect".equals(getStateName())){ return new StageSelectState(); }
		return this;
	}
	/* (非 Javadoc)
	 * @see function.controller.state.StateBase#endState()
	 */
	@Override
	public void endState(){
	}
	/**
	 * 味方ユニットが配置されているとゲームを開始し、現在のステートをバトルステートに変更する
	 * @param e イベント
	 */
	@FXML
	private void start(ActionEvent e){
		if(game_map.getGameMapData().getFriendList().size() <= 0){ return; }
		game_map.setIsDrawSortieArea(false);
		changeScene(e);
	}
	/**
	 * マップを初期状態に戻す
	 */
	@FXML
	private void initUnitLocate(){
		SoundEffect.play("select.mp3");
		for(Unit unit : unit_list.getUnitList()){
			game_map.removeUnit(unit);
		}
	}
	/**
	 * マウスがマップ上に入った場合マップの移動を始める
	 */
	@FXML
	private void mouseEntered(){
		game_map.mapMoveStart();
	}
	/**
	 * マウスがマップ外に出た場合マップ移動を止める
	 */
	@FXML
	private void mouseExited(){
		game_map.mapMoveStop();
	}
	/**
	 * マウスの位置によってマップを移動させる
	 * @param e マウスの座標
	 */
	@FXML
	private void mapMove(MouseEvent e){
		double x = e.getSceneX() - game_map_scene.localToScene(game_map_scene.getBoundsInLocal()).getMinX();
		double y = e.getSceneY() - game_map_scene.localToScene(game_map_scene.getBoundsInLocal()).getMinY();
		game_map.mapMove(x, y);
	}
	/**
	 * 選択されたスクエアに処理を行う
	 * @param e マウスの座標
	 */
	@FXML
	private void selectSquare(MouseEvent e){
		double x = e.getSceneX() - game_map_scene.localToScene(game_map_scene.getBoundsInLocal()).getMinX();
		double y = e.getSceneY() - game_map_scene.localToScene(game_map_scene.getBoundsInLocal()).getMinY();
		Square square = game_map.getSquare(x, y);
		if(e.getButton() == MouseButton.MIDDLE && square.getUnit() != null){ checkUnit(x, y); }
		if(e.getButton() != MouseButton.PRIMARY){ return; }
		if(!square.isSortieArea()){ return; }
		if(square.getUnit() != null && !unit_list.isExistsUnit(square.getUnit())){ return; }
		Unit unit = unit_list.getSelectUnit();
		if(unit == null){ return; }
		unit.setIsFriend(true);
		if(game_map.getGameMapData().getSameForces(unit).contains(unit)){
			game_map.removeUnit(unit);
		}
		game_map.setUnit(x, y, unit);
		game_map.update();
	}
	/**
	 * 選択されたスクエアにユニットがいた場合ユニットの詳細を開く
	 * @param x マップのＸ座標
	 * @param y　マップのＹ座標
	 */
	private void checkUnit(double x, double y){
		Square square = game_map.getSquare(x, y);
		if(square.getUnit() == null){ return; }
		UnitDetailsController unit_details = new UnitDetailsController();
		unit_details.detailsShow(square.getUnit());
		addSubWindowPane(unit_details);
	}
}
