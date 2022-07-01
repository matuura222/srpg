package function.battle.range;

import function.map.GameMapData;
import function.map.Square;
import function.skill.Skill;

/**
 * このクラスはスキルの射程距離を決めるオブジェクトです。
 * @author 樹麗
 *
 */
public class SkillRange extends Range{
	private Skill skill;
	/**
	 * @param data ゲームマップのデータ
	 * @param skill 使用するスキル
	 */
	public SkillRange(GameMapData data, Skill skill) {
		super(data);
		this.skill = skill;
	}

	/* (非 Javadoc)
	 * @see function.battle.range.Range#search(function.battle.range.Range.RangeValue, int, int, int)
	 */
	@Override
	protected void search(RangeValue value, int x, int y, int range_cost) {
		if (x < 0 || getGameMapData().getMapSizeX() <= x){ return; }
		if (y < 0 || getGameMapData().getMapSizeY() <= y){ return; }
		int remnant_range_cost = range_cost - 1;
		Square square = getGameMapData().getSquare(x, y).clone();
		if (remnant_range_cost < 0){ return; }
//		square.setMoveCost(remnant_range_cost);
		putRangeValue(square.getId(), new RangeValue(value.getTo(), square, remnant_range_cost));

//		search4(x, y, remnant_range_cost);
	}
	/* (非 Javadoc)
	 * @see function.battle.range.Range#init()
	 */
	@Override
	protected void init() {
		setTotalCost(skill.getRangeDistance());
		setRangeCost(skill.getRangeDistance() + 1);
	}

}
