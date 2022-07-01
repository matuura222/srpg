package function.battle;

import java.util.List;

import function.battle.range.AttackRange;
import function.battle.range.MoveRange;
import function.controller.state.game.battle.BattleState;
import function.controller.state.game.battle.sub.SelectSkill;
import function.map.GameMapData;
import function.unit.Unit;
import function.unit.UnitStatusType;

/**
 * この列挙型はゲームの行動を制御しているオブジェクトです。
 * @author 樹麗
 *
 */
public enum BattleCommand {
	/**
	 * このコマンドは選択されているユニットを何もせず行動済みにします。
	 * @author 樹麗
	 *
	 */
	WAIT{
		@Override
		public void action(Unit unit, BattleState battle, GameMapData map_data){
			battle.getGameMap().clearTextGraphics();
			unit.setIsActed(true);
			NEXT_UNIT.action(unit, battle, map_data);
			battle.getGameMap().mapChaning();
		}
	},
	/**
	 * このコマンドは選択されているユニットを移動させるコマンドです。
	 * @author 樹麗
	 *
	 */
	MOVE{
		@Override
		public void action(Unit unit, BattleState battle, GameMapData map_data){
			battle.getGameMap().clearTextGraphics();
			if(unit.isActed() || unit.isMoved()){ return; }
			MoveRange mr = new MoveRange(map_data);
			mr.startSearch(map_data.getUnitLocate(unit), unit);
			mr.drawRange(battle.getGameMap().getRangeGraphics());
			battle.setRange(mr);
			battle.setActionCommand(ActionCommand.MOVE);
		}
	},
	/**
	 * このコマンドは選択されているユニットで攻撃させるコマンドです。
	 * @author 樹麗
	 *
	 */
	ATTACK{

		@Override
		public void action(Unit unit, BattleState battle, GameMapData map_data){
			battle.getGameMap().clearTextGraphics();
			if(unit.isActed()){ return; }
			AttackRange ar = new AttackRange(map_data);
			ar.startSearch(map_data.getUnitLocate(unit), unit);
			ar.drawRange(battle.getGameMap().getRangeGraphics());
			battle.setRange(ar);
			battle.setActionCommand(ActionCommand.ATTACK);
		}
	},
	/**
	 * このコマンドは選択されているユニットのスキルを使用するコマンドです。
	 * @author 樹麗
	 *
	 */
	SKILL{
		@Override
		public void action(Unit unit, BattleState battle, GameMapData map_data){
			if(unit.isActed()){ return; }
			battle.getGameMap().clearTextGraphics();
			battle.addSubWindowPane(new SelectSkill(battle, unit));
		}
	},
	/**
	 * このコマンドは選択されているユニットを次のユニットに変更するコマンドです。
	 * @author 樹麗
	 *
	 */
	NEXT_UNIT{
		@Override
		public void action(Unit unit, BattleState battle, GameMapData map_data){
			if(!unit.isActed() && unit.isMoved()){ return; }
			if(!unit.isFriend()){ return; }
			List<Unit> unit_list = (List<Unit>) map_data.getSameForces(unit);
			if(map_data.isAllActed(unit_list)){ return; }
			if(map_data.isAllDied(unit_list)){ return; }
			int unit_num = unit_list.indexOf(unit);
			while(true){
				unit_num++;
				if(unit_list.size() <= unit_num){
					if(map_data.isAllActed(unit_list)){ return; }
					unit_num = 0;
				}
				if(unit_list.get(unit_num).isActed()){ continue; }
				if(unit_list.get(unit_num).getStatus(UnitStatusType.HP).getCurrentStatus() <= 0){
					unit_list.get(unit_num).setIsActed(true);
					continue;
				}
				break;
			}
			battle.removeSubWindow();
			map_data.setSource(map_data.getUnitLocate(unit_list.get(unit_num)));
			battle.setSource(map_data.getUnitLocate(unit_list.get(unit_num)));
			battle.setSelectUnit(unit_list.get(unit_num));
			battle.getGameMap().getRangeGraphics().clearRect(0, 0, map_data.getMapSizeX() * 40, map_data.getMapSizeY() * 40);
			battle.getGameMap().mapChaning();
			battle.setActionCommand(ActionCommand.NONE);
			battle.getGameMap().adjustMap(map_data.getSource());
		}
	},
	/**
	 * このコマンドはユニットの能力を確認するコマンドです。
	 * @author 樹麗
	 *
	 */
	STATUS{
		@Override
		public void action(Unit unit, BattleState battle, GameMapData map_data){
			battle.getGameMap().clearTextGraphics();
			battle.setActionCommand(ActionCommand.STATUS);
		}
	},
	/**
	 * このコマンドは今までの行動をキャンセルするコマンドです。
	 * @author 樹麗
	 */
	CANCEL{
		@Override
		public void action(Unit unit, BattleState battle, GameMapData map_data){
			battle.getGameMap().clearTextGraphics();
			if(unit.isActed()){ return; }
			battle.getGameMap().moveUnit(map_data.getSource(), battle.getSource());
//			map_data.getSource().setUnit(null);
//			battle.getSource().setUnit(unit);
			map_data.setSource(battle.getSource());
			unit.setIsMoved(false);
			battle.setActionCommand(ActionCommand.NONE);
			battle.getGameMap().getRangeGraphics().clearRect(0, 0, map_data.getMapSizeX() * 40, map_data.getMapSizeY() * 40);
			battle.getGameMap().mapChaning();
		}
	},
	/**
	 * このコマンドは自身のターンを終わらせるコマンドです。
	 * @author 樹麗
	 *
	 */
	TURN_END{
		@Override
		public void action(Unit unit, BattleState battle, GameMapData map_data){
			battle.removeSubWindow();
			battle.getGameMap().clearTextGraphics();
			for(Unit u : map_data.getSameForces(unit)){
				u.setIsActed(true);
			}
			battle.turnEnd();
		}
	};
	/**
	 * @param unit　行動するユニット
	 * @param battle バトルステート
	 * @param map_data マップデータ
	 */
	public abstract void action(Unit unit, BattleState battle,  GameMapData map_data);
}
