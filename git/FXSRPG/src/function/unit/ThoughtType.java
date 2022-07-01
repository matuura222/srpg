package function.unit;

import function.battle.ActionCommand;
import function.battle.range.AttackRange;
import function.battle.range.MoveRange;
import function.battle.range.Range;
import function.battle.range.Range.RangeValue;
import function.controller.state.game.battle.BattleState;
import function.map.GameMapData;
import function.map.Square;
import function.skill.Skill;

/**
 * ユニットの行動パターンを表している列挙型
 * @author 樹麗
 */
public enum ThoughtType {
	/**
	 *　この行動パターンは何もせず行動を終える
	 * @author 樹麗
	 */
	WAIT("待機"){
		@Override
		public void action(Unit unit, BattleState battle, GameMapData data){
//			ActionCommandManager.action(ActionCommand.WAIT, battle);
			ActionCommand.WAIT.action(battle, data.getUnitLocate(unit), data.getUnitLocate(unit));
		}

		@Override
		protected Square getTargetWithinRange(Unit unit, GameMapData data, Range range, int action_range) {
			return null;
		}

		@Override
		protected Square getOutOfRangeTarget(Unit unit, GameMapData data, Range range, int action_range) {
			return null;
		}

		@Override
		protected Square getTarget(Unit unit, GameMapData data) {
			// TODO 自動生成されたメソッド・スタブ
			return null;
		}
	},
	/**
	 *　この行動パターンは最も近いユニットに接近し攻撃する
	 * @author 樹麗
	 */
	CHARGE("突撃"){
		@Override
		public void action(Unit unit, BattleState battle, GameMapData data){
			Skill skill = null;
			for(Skill s : unit.getIdToSkill().values()){
				if(unit.getStatus(UnitStatusType.MP).getCurrentStatus() >= s.getCost()) { continue; }
				if(skill.getRangeDistance() < s.getRangeDistance()){
					skill = s;
				}
			}
			int range = unit.getStatus(UnitStatusType.ATTACK_RANGE).getCurrentStatus();
			if(skill != null){
				range = skill.getRangeDistance();
			}
			MoveRange move_range = new MoveRange(data);
			move_range.startSearch(data.getUnitLocate(unit), unit);
			battle.setRange(move_range);
			Square target_square = getTargetWithinRange(unit, data, move_range, range);
			Square move_square;
			move_range.getMap();
			if(target_square == null){
				move_square = getOutOfRangeTarget(unit, data, move_range, range);
			}else{
				Square square = move_range.getMap().get(data.getUnitLocate(unit).getId()).getTo();
				move_square = getDestination(data, move_range, square, target_square, range);
			}
			battle.setSelectSquare(data.getSquare(move_square.getX(), move_square.getY()));
//			ActionCommandManager.action(ActionCommand.MOVE, battle);
			ActionCommand.MOVE.action(battle, data.getUnitLocate(unit), data.getSquare(move_square.getX(), move_square.getY()));

			if(target_square != null){
				AttackRange attack_range = new AttackRange(data);
				attack_range.startSearch(data.getUnitLocate(unit), unit);
				battle.setRange(attack_range);
				battle.setSelectSquare(target_square);
//				ActionCommandManager.action(ActionCommand.ATTACK, battle);
				ActionCommand.ATTACK.action(battle, data.getUnitLocate(unit), target_square);
				if(unit.getStatus(UnitStatusType.HP).getCurrentStatus() <= 0){
					return;
				}
			}
//			ActionCommandManager.action(ActionCommand.WAIT, battle);
			ActionCommand.WAIT.action(battle, data.getUnitLocate(unit), data.getUnitLocate(unit));
		}
		@Override
		protected Square getTarget(Unit unit, GameMapData data) {
			int target_dist = 100;//対象との距離
			Square target_square = null;
			Square source = data.getUnitLocate(unit);
			for(Unit target_unit : data.getHostileForces(unit)){
				if(target_unit.getStatus(UnitStatusType.HP).getCurrentStatus() <= 0){ continue; }
				Square square = data.getUnitLocate(target_unit);
				int dist = data.getSquareDist(source, square);
				if(dist < target_dist){//攻撃範囲内に対象がいる場合
					target_square = square;
				}
			}
			return target_square;
		}
		@Override
		protected Square getTargetWithinRange(Unit unit, GameMapData data, Range range, int action_range){
			int target_dist = 100;//対象との距離
			Square square = null;
			Square source = data.getUnitLocate(unit);
			for(Unit target_unit : data.getHostileForces(unit)){
				if(target_unit.getStatus(UnitStatusType.HP).getCurrentStatus() <= 0){ continue; }
				RangeValue target_value = range.getMap().get(data.getUnitLocate(target_unit).getId());
				if(target_value == null){ continue; }
				int dist = data.getSquareDist(source, target_value.getTo());
				if(target_value.getCost() < action_range && dist < target_dist){//攻撃範囲内に対象がいる場合
					target_dist = dist;
					square = target_value.getTo();
				}
			}
			return square;
		}
		@Override
		protected Square getOutOfRangeTarget(Unit unit, GameMapData data, Range range, int action_range){
			Square source = data.getUnitLocate(unit);
			Square target_square = source;
			Square square = source;
			int target_dist = 100;
			for(Unit target_unit : data.getHostileForces(unit)) {
				if(target_unit.getStatus(UnitStatusType.HP).getCurrentStatus() <= 0){ continue; }
				Square t = data.getUnitLocate(target_unit);
				int dist = data.getSquareDist(source, target_square);
				if(target_dist > dist) {//一番近い対象の場所の取得
					target_dist = dist;
					target_square = t;
				}
			}
			target_dist = 100;
			//移動範囲内で対象と最も近い場所の取得
			for(RangeValue v : range.getMap().values()){
				if(!v.getTo().canInvade()){ continue; }
				if(v.getTo().getUnit() != null) { continue; }
				int dist = data.getSquareDist(v.getTo(), target_square);
				if(dist < target_dist ) {
					target_dist = dist;
					square = data.getSquare(v.getTo().getX(), v.getTo().getY());
				}
			}
			return square;
		}

	};
	private String name;
	ThoughtType(String name){
		this.name = name;
	}
	/**
	 * @param unit 行動するユニット
	 * @param data　ゲームマップデータ
	 * @return　行動対象となるスクエア
	 */
	protected abstract Square getTarget(Unit unit, GameMapData data);
	/**
	 * @param unit 行動するユニット
	 * @param data ゲームマップのデータ
	 * @param range 移動できる範囲
	 * @param action_range 行動範囲
	 * @return　行動先のスクエア
	 * 対象が範囲内にいた場合に呼ばれる
	 */
	protected abstract Square getTargetWithinRange(Unit unit, GameMapData data, Range range, int action_range);
	/**
	 * @param unit 行動するユニット
	 * @param data ゲームマップのデータ
	 * @param range 移動できる範囲
	 * @param action_range 行動範囲
	 * @return　行動先のスクエア
	 * 対象が範囲外にいた場合に呼ばれる
	 */
	protected abstract Square getOutOfRangeTarget(Unit unit, GameMapData data, Range range, int action_range);
	/**
	 * @param unit 行動するユニット
	 * @param battle 戦闘に必要な
	 * @param data ゲームマップのデータ
	 */
	public abstract void action(Unit unit, BattleState battle, GameMapData data);
	private static Square getDestination(GameMapData map, Range range, Square square, Square target, int action_range) {
		Square destination = square;
		int dist = map.getSquareDist(square, target);
		boolean is_decide = false;
		for(RangeValue value : range.getMap().values()){
			if(is_decide){ break; }
			if(!value.getTo().canInvade()){ continue; }
			int d = map.getSquareDist(target, value.getTo());
			if(action_range >= d && dist < d){
				dist = d;
				destination = value.getTo();
				if(action_range == d){
					break;
				}
			}
		}
		if(destination.getUnit() != null){
			destination = range.getMap().values().stream()
					.filter(e -> e.getTo().canInvade())
					.min((e1, e2) -> map.getSquareDist(target, e1.getTo()) - map.getSquareDist(target, e2.getTo())).get().getTo();
		}
		return destination;
	}
	public String getName(){ return this.name; }
	@Override
	public String toString(){ return this.name; }
}
