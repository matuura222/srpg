package function.battle.range;

import function.map.GameMapData;
import function.map.Square;
import function.unit.UnitStatusType;

/**
 * このクラス攻撃範囲を決めるオブジェクトです。
 * @author 樹麗
 *
 */
public class AttackRange extends Range {
	/**
	 * @param data ゲームマップのデータ
	 */
	public AttackRange(GameMapData data){
		super(data);
	}
	/* (非 Javadoc)
	 * @see function.battle.range.Range#search(function.battle.range.Range.RangeValue, int, int, int)
	 */
	protected void search(RangeValue value, int x, int y, int range_cost) {
		if (x < 0 || getGameMapData().getMapSizeX() <= x){ return; }
		if (y < 0 || getGameMapData().getMapSizeY() <= y){ return; }
		int remnant_range_cost = range_cost - 1;
		Square square = getGameMapData().getSquare(x, y).clone();
		putRangeValue(square.getId(), new RangeValue(value.getTo(), square, remnant_range_cost));
	}
	/* (非 Javadoc)
	 * @see function.battle.range.Range#init()
	 */
	@Override
	protected void init(){
		setTotalCost(getUnit().getStatus(UnitStatusType.ATTACK_RANGE).getCurrentStatus());
		setRangeCost(getUnit().getStatus(UnitStatusType.ATTACK_RANGE).getCurrentStatus() + 1);
	}
}
