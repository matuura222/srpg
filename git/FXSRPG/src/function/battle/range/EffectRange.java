package function.battle.range;

import function.map.GameMapData;
import function.map.Square;
import function.skill.Skill;

/**
 * このクラスは効果範囲を決めるオブジェクトです。
 * @author 樹麗
 *
 */
public class EffectRange extends Range {
	private Skill skill;
	/**
	 * @param data　ゲームマップのデータ
	 * @param skill 使用するスキル
	 */
	public EffectRange(GameMapData data, Skill skill) {
		super(data);
		this.skill = skill;
	}
	/* (非 Javadoc)
	 * @see function.battle.range.Range#search(function.battle.range.Range.RangeValue, int, int, int)
	 */
	@Override
	protected void search(RangeValue value, int x, int y, int range_cost){
		if (x < 0 || getGameMapData().getMapSizeX() <= x){ return; }
		if (y < 0 || getGameMapData().getMapSizeY() <= y){ return; }
		int remnant_range_cost = range_cost - 1;
		Square target_square = getGameMapData().getSquare(x, y).clone();
		if (remnant_range_cost < 0){ return; }
		putRangeValue(target_square.getId(), new RangeValue(value.getTo(), target_square, remnant_range_cost));
	}
	/* (非 Javadoc)
	 * @see function.battle.range.Range#init()
	 */
	@Override
	protected void init() {
		setTotalCost(skill.getEffectRange());
		setRangeCost(skill.getEffectRange() + 1);
	}
}
