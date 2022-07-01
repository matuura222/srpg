package function.controller.state.game.battle;

import java.util.Collection;

import function.ActivateType;
import function.battle.ActionCommand;
import function.battle.ActionCommandManager;
import function.battle.BattleCommand;
import function.battle.GameBattleTurn;
import function.battle.range.Range;
import function.controller.state.StateBase;
import function.controller.state.game.battle.sub.BattleMenuController;
import function.map.GameMap;
import function.map.GameMapData;
import function.map.Square;
import function.skill.Skill;
import function.unit.Unit;
import function.unit.UnitStatusType;
import javafx.fxml.FXML;
import javafx.scene.SubScene;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

/**
 * 戦闘をするステート
 * @author 樹麗
 *
 */
public class BattleState extends StateBase {
	@FXML
	private SubScene game_map_scene;
	private BattleMenuController battle_menu;
	private GameMap game_map = new GameMap();
	private Range range;
	private Skill skill;
	private ActionCommand action_command;
//	private ActionCommandManager action_manager;
	private Square source, select_square;
	private GameBattleTurn turn;
	private boolean is_game_end = false;
	/**
	 * @param data ゲームマップ
	 */
	public BattleState(GameMapData data) {
		super("battle/Battle");
		game_map_scene.setCamera(game_map.getCamera());
		game_map_scene.setRoot(game_map);
		game_map.init(data);
		game_map.setIsDrawSortieArea(false);
		game_map.setGameBattleState(this);
		battle_menu = new BattleMenuController(this);
		battle_menu.setId("バトルメニュー");
//		action_manager = new ActionCommandManager();
		action_command = ActionCommand.NONE;
		addSubWindowPane(battle_menu);
		turn = new GameBattleTurn(this);

	}
	/* (非 Javadoc)
	 * @see function.controller.state.StateBase#startState()
	 */
	@Override
	public void startState(){
		for(Unit unit : game_map.getGameMapData().getEnemyList()) {
			unit.currentState(this, ActivateType.GAME_START);
		}
		for(Unit unit : game_map.getGameMapData().getFriendList()) {
			unit.currentState(this, ActivateType.GAME_START);
		}
		battle_menu.setUnit(game_map.getGameMapData().getFriendList().stream().findFirst().get());
		game_map.getGameMapData().setSource(game_map.getGameMapData().getUnitLocate(battle_menu.getUnit()));
		setSource(game_map.getGameMapData().getSource());
		source = game_map.getGameMapData().getSource();
		game_map.adjustMap(source);
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
		if(is_game_end && turn.getIsFriendTurn()){ return new BattleEndState(game_map.getGameMapData()); }
		return this;
	}
	/* (非 Javadoc)
	 * @see function.controller.state.StateBase#endState()
	 */
	@Override
	public void endState(){
	}
	/**
	 * マウスがマップ内に入った場合マップ移動を開始する
	 */
	@FXML
	private void mouseEntered(){
		game_map.mapMoveStart();
	}
	/**
	 * マウスがマップ外に出た場合マップ移動を停止する
	 */
	@FXML
	private void mouseExited(){
		game_map.mapMoveStop();
	}
	/**
	 * マウスの位置に応じてマップを移動させる
	 * @param e　マウス座標
	 */
	@FXML
	private void mapMove(MouseEvent e){
		double x = e.getSceneX() - game_map_scene.localToScene(game_map_scene.getBoundsInLocal()).getMinX();
		double y = e.getSceneY() - game_map_scene.localToScene(game_map_scene.getBoundsInLocal()).getMinY();
		game_map.mapMove(x, y);
	}
	/**
	 * マウスの位置のスクエアを選択する
	 * 選ばれているアクションコマンドに応じて処理を行う
	 * @param e マウス座標
	 */
	@FXML
	private void selectSquare(MouseEvent e){
		if(!getSubWindow("バトルメニュー").isShowing()){ getSubWindow("バトルメニュー").show(); }
		if(e.getButton() == MouseButton.SECONDARY){
			this.action_command = ActionCommand.NONE;
			game_map.clearRangeGraphics();
			game_map.mapChaning();
		}
		if(e.getButton() != MouseButton.PRIMARY){ return; }
		double x = e.getSceneX() - game_map_scene.localToScene(game_map_scene.getBoundsInLocal()).getMinX();
		double y = e.getSceneY() - game_map_scene.localToScene(game_map_scene.getBoundsInLocal()).getMinY();
		this.select_square = game_map.getSquare(x, y);
		ActionCommandManager.action(action_command, this);
//		action_manager.action(this.action_command, this);
//		this.action_manager.action(this.action_command);
//		setActionCommand(ActionCommand.NONE);
	}
	/**
	 * 渡されたバトルコマンドを実行する
	 * @param battle_command バトルコマンド
	 */
	public void battleCommandAction(BattleCommand battle_command){
		if(!turn.getIsFriendTurn()){ return; }
		battle_command.action(battle_menu.getUnit(), this, game_map.getGameMapData());
	}
	/**
	 * 渡されたユニットをゲームマップから取り除く
	 * @param unit ユニット
	 */
	public void deleteUnit(Unit unit){
		Square square = game_map.getGameMapData().getUnitLocate(unit);
		Collection<Unit> unit_list;
		if(square.getUnit().isFriend()){
			unit_list = game_map.getGameMapData().getFriendList();
		}else{
			unit_list = game_map.getGameMapData().getEnemyList();
		}
		game_map.removeUnit(unit);
		is_game_end = isGameEnd(unit_list);
		game_map.mapChaning();
	}
	/**
	 * 渡されたユニットリストが全て死んでいた場合バトルを終了する
	 * @param unit_list ユニットリスト
	 * @return ゲームが終了しているかどうか
	 */
	private boolean isGameEnd(Collection<Unit> unit_list){
		for(Unit unit : unit_list){
			if(unit.getStatus(UnitStatusType.HP).getCurrentStatus() > 0){
				return false;
			}
		}
		for(Unit unit : game_map.getGameMapData().getFriendList()) {
			unit.currentState(this, ActivateType.GAME_END);
		}
		return true;
	}
	/**
	 * 行動を終える
	 */
	public void turnEnd(){
		game_map.clearRangeGraphics();
		game_map.mapChaning();
		turn.turnEnd();
	}
	/**
	 * @return ゲームマップを返す
	 */
	public GameMap getGameMap(){
		return this.game_map;
	}
	/**
	 * @return　基準となるスクエア
	 */
	public Square getSource(){
		return this.source;
	}
	/**
	 * @param source 基準となるスクエア
	 */
	public void setSource(Square source){
		this.source = source;
	}
	/**
	 * @return 選択されているスクエアを返す
	 */
	public Square getSelectSquare(){
		return this.select_square;
	}
	/**
	 * @param square 選択されたスクエア
	 */
	public void setSelectSquare(Square square){
		this.select_square = square;
	}
	/**
	 * @param range 使用するRange
	 */
	public void setRange(Range range){
		this.range = range;
	}
	/**
	 * @return 使用しているRangeを返す
	 */
	public Range getRange(){
		return this.range;
	}
	/**
	 * @param skill　選択されたスキル
	 */
	public void setSelectSkill(Skill skill){
		this.skill = skill;
	}
	/**
	 * @return 選択されているスキルを返す
	 */
	public Skill getSelectSkill(){
		return this.skill;
	}
	/**
	 * @param unit 選択されたユニット
	 */
	public void setSelectUnit(Unit unit){
		battle_menu.setUnit(unit);
	}
	/**
	 * @param action_command　選択されたアクションコマンド
	 */
	public void setActionCommand(ActionCommand action_command){
		this.action_command = action_command;
	}
	/**
	 * @return　選択されているアクションコマンド
	 */
	public ActionCommand getActionCommand(){
		return this.action_command;
	}
}
