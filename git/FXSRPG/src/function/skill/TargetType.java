package function.skill;

import java.util.List;
import java.util.stream.Collectors;

import function.map.Square;
import function.unit.Unit;

/**
 *　効果対象を表しているオブジェクトです
 * @author 樹麗
 */
public enum TargetType {
	/**
	 *　効果範囲全てのスクエアが対象
	 * @author 樹麗
	 */
	ALL("範囲全て"){
		@Override
		public boolean IsWithinRangeTarget(Unit unit, List<Square> target_list){
			for(Square square : target_list){
				if(square.getUnit() != null){
					return true;
				}
			}
			return false;
		}
		@Override
		public List<Square> getWithinRangeTarget(Unit unit, List<Square> target_list) {
			return target_list.stream().filter(s -> s.getUnit() != null).collect(Collectors.toList());
		}

	},
	/**
	 *　効果範囲にユニットがいないスクエアが対象
	 * @author 樹麗
	 */
	NOTHING("何もいない"){
		@Override
		public boolean IsWithinRangeTarget(Unit unit, List<Square> target_list){
			for(Square square : target_list){
				if(square.getUnit() == null){
					return true;
				}
			}
			return false;
		}
		@Override
		public List<Square> getWithinRangeTarget(Unit unit, List<Square> target_list) {
			return target_list.stream().filter(s -> s.getUnit() != null).collect(Collectors.toList());
		}
	},
	/**
	 *　効果範囲のスクエアにユニットいるユニットが味方のみ対象
	 * @author 樹麗
	 */
	FRIEND("味方のみ"){
		@Override
		public boolean IsWithinRangeTarget(Unit unit, List<Square> target_list){
			for(Square square : target_list){
				if(square.getUnit() != null && square.getUnit().isFriend() == unit.isFriend()){
					return true;
				}
			}
			return false;
		}
		@Override
		public List<Square> getWithinRangeTarget(Unit unit, List<Square> target_list) {
			return target_list.stream()
					.filter(s -> s.getUnit() != null && unit.isFriend() == s.getUnit().isFriend())
					.collect(Collectors.toList());
		}

	},
	/**
	 *　効果範囲のスクエアにいるユニットが敵のみ対象
	 * @author 樹麗
	 */
	ENEMY("敵のみ"){

		@Override
		public boolean IsWithinRangeTarget(Unit unit, List<Square> target_list){
			for(Square square : target_list){
				if(square.getUnit() != null && square.getUnit().isFriend() != unit.isFriend()){
					return true;
				}
			}
			return false;
		}
		@Override
		public List<Square> getWithinRangeTarget(Unit unit, List<Square> target_list) {
			return target_list.stream()
					.filter(s -> s.getUnit() != null && unit.isFriend() != s.getUnit().isFriend())
					.collect(Collectors.toList());
		}

	};
	private String name;
	/**
	 * @param name 名前
	 */
	TargetType(String name){
		this.name = name;
	}
	/**
	 * @return 名前を返す
	 */
	public String getName() {
		return name;
	}
	/* (非 Javadoc)
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString(){
		return this.name;
	}
	/**
	 * @param unit 使用したユニット
	 * @param target_list 効果範囲
	 * @return 効果範囲に効果対象がいるかどうか
	 */
	public abstract boolean IsWithinRangeTarget(Unit unit, List<Square> target_list);
	/**
	 * @param unit　使用したユニット
	 * @param target_list 効果範囲
	 * @return 効果範囲にいる効果対象
	 */
	public abstract List<Square> getWithinRangeTarget(Unit unit, List<Square> target_list);
}
