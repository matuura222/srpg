package function.battle.range;

import function.map.GameMapData;
import function.map.Square;
import function.unit.UnitStatusType;

/**
 * このクラスは移動範囲を決めるオブジェクトです。
 * @author 樹麗
 *
 */
public class MoveRange extends Range {
	/**
	 * @param data ゲームマップのデータ
	 */
	public MoveRange(GameMapData data) {
		super(data);
	}
	/* (非 Javadoc)
	 * @see function.battle.range.Range#search(function.battle.range.Range.RangeValue, int, int, int)
	 */
	@Override
	protected void search(RangeValue value, int x, int y, int range_cost) {
		if (x < 0 || getGameMapData().getMapSizeX() <= x){ return; }
		if (y < 0 || getGameMapData().getMapSizeY() <= y){ return; }
		if (getMap().get(x + " " + y) != null){ return; }
		if (range_cost <= 0){ return; }
		Square square = getGameMapData().getSquare(x, y).clone();
		square.setCanInvade(true);
		int remnant_range_cost;
		if(range_cost <= getRangeCost()){
			remnant_range_cost = range_cost - 1;
			square.setCanInvade(false);
		}else{
			remnant_range_cost = range_cost - square.getMoveCost();
			if(remnant_range_cost < getRangeCost()){
				remnant_range_cost = getRangeCost() - 1;
				square.setCanInvade(false);
			}
		}
		if(square.getUnit() != null){
			square.setCanInvade(false);
			if(square.getUnit().isFriend() != getUnit().isFriend()){
				if(range_cost > getRangeCost()){
					remnant_range_cost = getRangeCost() - 1;
				}
			}
		}
		putRangeValue(square.getId(), new RangeValue(value.getTo(), square, remnant_range_cost));
	}
	/* (非 Javadoc)
	 * @see function.battle.range.Range#init()
	 */
	/* (非 Javadoc)
	 * @see function.battle.range.Range#init()
	 */
	@Override
	protected void init(){
		setTotalCost(getUnit().getStatus(UnitStatusType.MOVE).getCurrentStatus()
				+ getUnit().getStatus(UnitStatusType.ATTACK_RANGE).getCurrentStatus());
		setRangeCost(getUnit().getStatus(UnitStatusType.ATTACK_RANGE).getCurrentStatus());
	}
}
