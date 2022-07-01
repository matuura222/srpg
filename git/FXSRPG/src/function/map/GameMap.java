package function.map;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import common.SRPGCommons;
import function.battle.ActionCommand;
import function.battle.range.EffectRange;
import function.controller.state.game.battle.BattleState;
import function.unit.Unit;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.PerspectiveCamera;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * このクラスはゲームマップを描画するオブジェクトです
 * @author 樹麗
 *
 */
public class GameMap extends AnchorPane {
	private BattleState battle;
	private double camera_x, camera_y, mouse_x, mouse_y;
	private int chip_width, chip_height;
	private GameMapData game_map_data;
	private boolean is_left_moving, is_right_moving, is_top_moving, is_bottom_moving;
	private boolean is_draw_sortie_area = true;
	private boolean is_map_moving = true;
	@FXML
	PerspectiveCamera camera;
	@FXML
	Canvas map_canvas, unit_place_canvas, mouse_place_canvas, range_canvas, text_canvas;
	private boolean is_map_changing = false;
	private final static Color FRIEND_AREA_COLOR = new Color(0, 0, 0.5, 0.5);
	private final static Color MOUSE_PLACE_COLOR = new Color(0, 0, 0, 0.3);
	private final static Color UNIT_ACTED_COLOR = new Color(0, 0, 0, 0.5);
	/**
	 * ゲームマップを作成する
	 */
	public GameMap(){ this(""); }
	/**
	 * 指定されたマップを読み込む
	 * @param name　マップ名
	 */
	public GameMap(String name){
		try {
			FXMLLoader loader = new FXMLLoader(new URL(SRPGCommons.getURL() + "/map/GameMap.fxml"));
			loader.setController(this);
			loader.setRoot(this);
			loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		chip_width = chip_height = 40;
		GameMapData data;
		if(name.equals("")){
			data = GameMapData.defaultData();
		}else{
			data = (GameMapData) SRPGCommons.getData(new File("./data/map/map/" + name), GameMapData.class);
		}
		init(data);
	}
	/**
	 * マップを更新する
	 */
	public void update(){
		if(is_map_moving){
			if(is_left_moving && camera_x > -25){ camera_x -= 2; }
			if(is_right_moving && camera_x < map_canvas.getWidth() - getWidth() + 25){ camera_x += 2; }
			if(is_top_moving && camera_y > -25){ camera_y -= 2; }
			if(is_bottom_moving && camera_y < map_canvas.getHeight() - getHeight() + 25){ camera_y += 2; }
		}
		camera.setTranslateX(camera_x);
		camera.setTranslateY(camera_y);
		drawMousePlace();
		updateMap();
	}

	/**
	 * 渡されたマップデータで初期化する
	 * @param game_map_data　ゲームマップデータ
	 */
	public void init(GameMapData game_map_data){
		is_map_changing = true;
		GraphicsContext map_graphics = map_canvas.getGraphicsContext2D();
		GraphicsContext unit_graphics = unit_place_canvas.getGraphicsContext2D();
		for(int x = 0 ; x < map_canvas.getWidth() / chip_width ; x++){
			for(int y = 0 ; y < map_canvas.getHeight() / chip_height ; y++){
				Square square = game_map_data.getSquare(x, y);
				map_graphics.drawImage(square.getMapChip().getImage(), x * chip_width, y * chip_height, chip_width, chip_height);
				if(square.isSortieArea()){
					map_graphics.setFill(FRIEND_AREA_COLOR);
					map_graphics.fillRect(x * chip_width, y * chip_height, chip_width, chip_height);
				}if(square.getUnit() == null ){ continue; }
				unit_graphics.drawImage(square.getUnit().getImage(), x * chip_width, y * chip_width, chip_width, chip_height);
				if(square.isSortieArea()){ unit_graphics.setFill(Color.BLUE); square.getUnit().setIsFriend(true); }
				else{ unit_graphics.setFill(Color.RED); }
				unit_graphics.fillRect(x * chip_width, y * chip_height, 10, 10);
			}
		}
		this.game_map_data = game_map_data;
		GraphicsContext text_graphics = text_canvas.getGraphicsContext2D();
		text_graphics.setFont(Font.font(20));
	}
	/**
	 * マウスの場所を表示する
	 */
	private void drawMousePlace(){
		GraphicsContext mouse_place_graphics = mouse_place_canvas.getGraphicsContext2D();
		mouse_place_graphics.clearRect(0, 0, mouse_place_canvas.getWidth(), mouse_place_canvas.getHeight());
		int x, y;
		x = (int) Math.floor(mouse_x + camera.getTranslateX()) / chip_width;
		y = (int) Math.floor(mouse_y + camera.getTranslateY()) / chip_height;
		ActionCommand action_command;
		if(battle == null){
			action_command = ActionCommand.NONE;
		}else{
			action_command = battle.getActionCommand();
		}

		switch(action_command){
			case ATTACK:if(battle.getRange().getMap().get(x + " " + y) == null){ return; }
				mouse_place_graphics.setFill(MOUSE_PLACE_COLOR);
				mouse_place_graphics.fillRect(x * chip_width, y * chip_height, chip_width, chip_height);
			break;
			case MOVE:if(battle.getRange().getMap().get(x + " " + y) == null){ return; }
				mouse_place_graphics.setFill(MOUSE_PLACE_COLOR);
				mouse_place_graphics.fillRect(x * chip_width, y * chip_height, chip_width, chip_height);
				break;
			case SKILL:if(battle.getRange().getMap().get(x + " " + y) == null){ return; }
				EffectRange effect_range = new EffectRange(game_map_data, battle.getSelectSkill());
				effect_range.startSearch(game_map_data.getSquare(x, y), game_map_data.getSource().getUnit());
				effect_range.drawRange(mouse_place_graphics);
				break;
			default:
				mouse_place_graphics.setFill(MOUSE_PLACE_COLOR);
				mouse_place_graphics.fillRect(x * chip_width, y * chip_height, chip_width, chip_height);
				break;
		}
	}
	/**
	 * マップの描画を更新する
	 */
	private void updateMap(){
		if(!is_map_changing){ return; }
		GraphicsContext map_graphics = map_canvas.getGraphicsContext2D();
		GraphicsContext unit_graphics = unit_place_canvas.getGraphicsContext2D();
		map_graphics.clearRect(0, 0, getGameMapData().getMapSizeX() * chip_width, getGameMapData().getMapSizeY() * chip_height);
		unit_graphics.clearRect(0, 0, getGameMapData().getMapSizeX() * chip_width, getGameMapData().getMapSizeY() * chip_height);

		for(int x = 0 ; x < map_canvas.getWidth() / chip_width ; x++){
			for(int y = 0 ; y < map_canvas.getHeight() / chip_height ; y++){
				Square square = game_map_data.getSquare(x, y);
				map_graphics.drawImage(square.getMapChip().getImage(), x * chip_width, y * chip_width,
						chip_width, chip_height);
				if(is_draw_sortie_area && square.isSortieArea()){
					map_graphics.setFill(FRIEND_AREA_COLOR);
					map_graphics.fillRect(x * chip_width, y * chip_height, chip_width, chip_height);
				}
				if(square.getUnit() == null){ continue; }
				unit_graphics.drawImage(square.getUnit().getImage(), x * chip_width, y * chip_width, chip_width, chip_height);
				if(square.getUnit().isActed()){
					unit_graphics.setFill(UNIT_ACTED_COLOR);
					unit_graphics.fillRect(x * chip_width, y * chip_height, chip_width, chip_height);
				}
				if(square.getUnit().isFriend()){ unit_graphics.setFill(Color.BLUE); }
				else{ unit_graphics.setFill(Color.RED); }
				unit_graphics.fillRect(x * chip_width, y * chip_height, 10, 10);
			}
		}
		Square source = game_map_data.getSource();
		if(source != null){
			unit_graphics.setFill(Color.color(0, 0, 0.5, 0.3));
			unit_graphics.fillRect(source.getX() * chip_width, source.getY() * chip_height, chip_width, chip_height);
		}
		is_map_changing = false;
	}
	/**
	 * マウスクリック時に呼び出されるイベント
	 * @param e マウスイベント
	 */
	public void mouseClicked(MouseEvent e){

	}
	/**
	 * マップ移動時に呼び出される
	 */
	public void mapMoveStart(){
		is_map_moving = true;
	}
	/**
	 * マップ移動が終わったときに呼び出される
	 */
	public void mapMoveStop(){
		is_map_moving = false;
	}
	/**
	 * マップが動くかどうかの処理
	 * @param x マウスのX座標
	 * @param y マウスのY座標
	 */
	public void mapMove(double x, double y){
		is_left_moving = is_right_moving = is_top_moving = is_bottom_moving = false;
		mouse_x = x;
		mouse_y = y;
		if(x < 25){ is_left_moving = true; }
		if(x > getWidth() - 25){ is_right_moving = true; }
		if(y < 25){ is_top_moving = true; }
		if(y > getHeight() - 25){ is_bottom_moving = true; }
	}
	/**
	 * マップ変更時呼び出される
	 */
	public void mapChaning(){ this.is_map_changing = true;}

	/**
	 * 渡されたsquareクラスを中心に表示する
	 * @param square　ゲームマップの一部
	 */
	public void adjustMap(Square square){
		double mx = square.getX() * chip_width - getWidth() / 2;
		double my = square.getY() * chip_height - getHeight() / 2;
		if(mx < -25){ mx = -25; }
		if(my < -25){ my = -25; }
		if(mx > map_canvas.getWidth() - getWidth() + 25){ mx = map_canvas.getWidth() - getWidth() + 25; }
		if(my > map_canvas.getHeight() - getHeight() + 25){ my = map_canvas.getHeight() - getHeight() + 25; }
		camera_x = mx;
		camera_y = my;
	}

	/**
	 * 初期出撃Squareを設定
	 * @param square 選択されたSquare
	 * @param is_sortie_area 変更後の初期出撃Square
	 */
	public void setIsSortieArea(Square square, boolean is_sortie_area){
		if(is_sortie_area == square.isSortieArea()){ return; }
		if(is_sortie_area){
			game_map_data.sortieAreaNumUp(square);
		}else{
			game_map_data.sortieAreaNumDown(square);
		}
		square.setIsSortieArea(is_sortie_area);
		is_map_changing = true;
	}
	/**
	 * 選択されたSquareを返す
	 * @param x マウスのX座標
	 * @param y マウスのY座標
	 * @return 選択されたSquare
	 */
	public Square getSquare(double x, double y){
		return game_map_data.getSquare((int)Math.floor(x + camera.getTranslateX()) / chip_width,
				(int)Math.floor(y + camera.getTranslateY())/ chip_height);
	}
	/**
	 * 選択された場所のSquareにunitを配置する
	 * @param x マウスのY座標
	 * @param y マウスのX座標
	 * @param unit 配置するUnitクラス
	 */
	public void setUnit(double x, double y, Unit unit){
		setUnit(getSquare(x, y), unit);
	}
	/**
	 * 選択されたSquareにunitを配置する
	 * @param square　選択されたSquare
	 * @param unit 配置するUnit
	 */
	public void setUnit(Square square, Unit unit){
		if(unit == null || unit.getId().equals("")){
			if(square.getUnit() == null){ return; }
			game_map_data.getSameForces(square.getUnit()).remove(square.getUnit());
			game_map_data.getUnitList().remove(square.getUnit());
			unit = null;
		}else{
			if(square.getUnit() != null){
				game_map_data.getSameForces(square.getUnit()).remove(square.getUnit());
				game_map_data.getUnitList().remove(square.getUnit());
			}
			game_map_data.getSameForces(unit).add(unit);
			game_map_data.getUnitList().add(unit);
		}
		square.setUnit(unit);
		is_map_changing = true;
	}
	/**
	 * fromにいるUnitをtoに再配置する
	 * @param from　移動前のSquare
	 * @param to 移動後のSquare
	 */
	public void moveUnit(Square from, Square to){
		if(from.getUnit() == null){ return; }
		if(from.equals(to)){ return; }
		to.setUnit(from.getUnit());
		from.setUnit(null);
	}
	/**
	 * 渡されたUnitクラスをマップ上から消す
	 * @param unit 消すユニット
	 */
	public void removeUnit(Unit unit){
		Square square = game_map_data.getUnitLocate(unit);
		if(square != null){
			square.setUnit(null);
		//	game_map_data.getSameForces(unit).remove(unit);
		}
		this.is_map_changing = true;
	}
	/**
	 * 選択された場所にあるSquareにmap_chipを設定する
	 * @param x マウスのX座標
	 * @param y マウスのY座標
	 * @param map_chip 配置するMapChip
	 */
	public void setMapChip(double x, double y, MapChip map_chip){
		if(map_chip == null){ return; }
		int map_x = (int)Math.floor((x + camera.getTranslateX()) / chip_width);
		int map_y = (int)Math.floor((y + camera.getTranslateY()) / chip_height);
		if(map_x < 0 || map_x >= game_map_data.getMapSizeX() || map_y < 0 || map_y >= game_map_data.getMapSizeY()){ return; }
		game_map_data.getSquare(map_x, map_y).setMapChip(map_chip);
		is_map_changing = true;
	}
	/**
	 * @param x マウスのX座標
	 * @param y マウスのY座標
	 * @return 選択された場所にあるSquareに配置されているUnit
	 */
	public Unit getUnit(double x, double y){
		return game_map_data.getSquare((int)Math.floor(x + camera.getTranslateX()) / chip_width,
				(int)Math.floor(y + camera.getTranslateY())/ chip_height).getUnit();
	}
	/**
	 * @param x マウスのX座標
	 * @param y マウスのY座標
	 * @return 選択された場所にあるSquareに設定されているMapChip
	 */
	public MapChip getMapChip(double x, double y){
		return game_map_data.getSquare((int)Math.floor(x + camera.getTranslateX()) / chip_width,
				(int)Math.floor(y + camera.getTranslateY())/ chip_height).getMapChip();
	}
	/**
	 * @return 行動範囲表示用GraphicsContextを取得
	 */
	public GraphicsContext getRangeGraphics(){
		return this.range_canvas.getGraphicsContext2D();
	}
	/**
	 * @return ゲームマップ表示用GraphicsContextを取得
	 */
	public GraphicsContext getGameMapGraphics(){
		return this.map_canvas.getGraphicsContext2D();
	}
	/**
	 * 渡された文字列を指定された場所に表示する
	 * @param x ゲームマップ上のX座標
	 * @param y ゲームマップ上のY座標
	 * @param text 表示する文字列
	 */
	public void drawText(double x, double y, String text){
		GraphicsContext graphics = this.text_canvas.getGraphicsContext2D();
		graphics.fillText(text, x, y);
		this.is_map_changing = true;
	}
	/**
	 * 行動範囲表示用GraphicsContextを初期化する
	 */
	public void clearRangeGraphics(){
		GraphicsContext graphics = this.range_canvas.getGraphicsContext2D();
		graphics.clearRect(0, 0, getGameMapData().getMapSizeX() * 40, getGameMapData().getMapSizeY() * 40);
		this.is_map_changing = true;
	}

	/**
	 * テキスト表示用GraphicsContextを初期化する
	 */
	public void clearTextGraphics(){
		GraphicsContext graphics = this.text_canvas.getGraphicsContext2D();
		graphics.clearRect(0, 0, chip_width * getGameMapData().getMapSizeX(),  chip_height * getGameMapData().getMapSizeY());
		this.is_map_changing = true;
	}
	/**
	 * @return　マップチップの幅を返す
	 */
	public int getMapChipWidth(){ return this.chip_width; }
	/**
	 * @return マップチップの高さを返す
	 */
	public int getMapChipHeight(){ return this.chip_height; }
	/**
	 * @return ゲームマップデータを返す
	 */
	public GameMapData getGameMapData(){ return this.game_map_data; }
	/**
	 * @param is_map_changing　マップが変更されているか
	 */
	public void setIsMapChanging(boolean is_map_changing){ this.is_map_changing = is_map_changing; }
	/**
	 * @param is_draw_sortie_area　味方のエリアを描画するか
	 */
	public void setIsDrawSortieArea(boolean is_draw_sortie_area){ this.is_draw_sortie_area = is_draw_sortie_area; }
	/**
	 * @return カメラを返す
	 */
	public PerspectiveCamera getCamera(){ return this.camera; }
	/**
	 * @return マップ名を返す
	 */
	public String getMapName(){ return this.game_map_data.getName(); }
	/**
	 * マウスがWindow内に入っているか
	 */
	public void mouseExited() { this.is_map_moving = false; }
	/**
	 * @param battle バトルステート
	 */
	public void setGameBattleState(BattleState battle){
		this.battle = battle;
	}
}
