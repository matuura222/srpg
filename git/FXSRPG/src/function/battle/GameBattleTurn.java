package function.battle;

import java.util.Optional;

import function.ActivateType;
import function.controller.state.game.battle.BattleState;
import function.map.Square;
import function.unit.Unit;
import function.unit.UnitStatusType;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

/**
 * ゲームのターンを制御しているオブジェクトです。
 * @author 樹麗
 *
 */
public class GameBattleTurn {
	private boolean is_friend_turn;
	private BattleState battle;
	private Service<Void> service;
	/**
	 * @param battle バトルステート
	 */
	public GameBattleTurn(BattleState battle){
		this.battle = battle;
		this.is_friend_turn = true;
		service = new Service<Void>(){
			@Override
			protected Task<Void> createTask() {
				Task<Void> t = new Task<Void>(){
					@Override
					protected Void call() throws Exception {
						for(Unit unit : battle.getGameMap().getGameMapData().getEnemyList()){
							unit.setIsMoved(false);
							unit.setIsActed(false);
							unit.currentState(battle, ActivateType.TURN_START);
						}
						for(Unit unit : battle.getGameMap().getGameMapData().getEnemyList()){
							if(unit.getStatus(UnitStatusType.HP).getCurrentStatus() <= 0){ continue; }
							Square square = battle.getGameMap().getGameMapData().getUnitLocate(unit);
							battle.getGameMap().getGameMapData().setSource(square);
							battle.getGameMap().adjustMap(square);
							battle.setSource(square);
							unit.getThoughtType().action(unit, battle, battle.getGameMap().getGameMapData());
							battle.getGameMap().clearTextGraphics();
						}
						friendTurn();
						return null;
					}
				};
				return t;
			}
		};
	}
	/**
	 * 味方のターンにする
	 */
	private void friendTurn(){
		for(Unit unit : battle.getGameMap().getGameMapData().getEnemyList()) {
			unit.currentState(battle, ActivateType.TURN_END);
		}
		for(Unit unit : battle.getGameMap().getGameMapData().getFriendList()){
			unit.setIsActed(false);
			unit.setIsMoved(false);
			unit.currentState(battle,ActivateType.TURN_START);
		}
		Optional<Unit> unit = battle.getGameMap().getGameMapData().getFriendList().stream()
				.filter(e -> e.getStatus(UnitStatusType.HP).getCurrentStatus() > 0)
				.findFirst();
		if(unit.isPresent()){
			battle.setSelectUnit(unit.get());
			Square square = battle.getGameMap().getGameMapData().getUnitLocate(unit.get());
			battle.getGameMap().getGameMapData().setSource(square);
			battle.setSource(square);
			battle.getGameMap().adjustMap(square);
		}
		is_friend_turn = true;
	}
	/**
	 * ターンエンドする
	 */
	public void turnEnd(){
		if(!this.is_friend_turn){ return; }
		if(service.isRunning()) { return; }
		this.is_friend_turn = false;
		for(Unit unit : battle.getGameMap().getGameMapData().getFriendList()) {
			unit.currentState(battle, ActivateType.TURN_END);
		}
		service.reset();
		service.start();
		battle.getGameMap().mapChaning();
	}
	/**
	 * @return 味方のターンかどうかを返す
	 */
	public boolean getIsFriendTurn(){
		return this.is_friend_turn;
	}
}
