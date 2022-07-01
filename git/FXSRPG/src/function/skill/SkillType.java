package function.skill;

import function.unit.Unit;
import function.unit.UnitStatusType;

/**
 * この列挙型はスキルの種類を表しているオブジェクトです
 * @author 樹麗
 *
 */
public enum SkillType {
	/**
	 *　物理タイプ
	 *　効果はスキルを使用するユニットの攻撃力に依存しているスキルです
	 * @author 樹麗
	 */
	MATERIAL("物理") {
		@Override
		protected int getPower(Unit unit, Skill skill) {
			return (int) (unit.getStatus(UnitStatusType.ATTACK).getCurrentStatus() * (skill.getVariablePower() / 100.0)) + skill.getFixedPower();
		}
	},
	/**
	 *　魔法タイプ
	 *　効果はスキルを使用するユニットの魔力に依存しているスキルです
	 * @author 樹麗
	 */
	MAGIC("魔法") {
		@Override
		protected int getPower(Unit unit, Skill skill) {
			return (int) (unit.getStatus(UnitStatusType.MAGIC_ATTACK).getCurrentStatus() * (skill.getVariablePower() / 100.0)) + skill.getFixedPower();
		}
	},
	/**
	 *　召喚タイプ
	 *　効果はスキルを使用するユニットのLVに依存しているスキルです
	 * @author 樹麗
	 */
	SUMMON("召喚") {
		@Override
		protected int getPower(Unit unit, Skill skill) {
			return (int) (unit.getLv() * (skill.getVariablePower() / 100.0) + skill.getFixedPower());
		}
	},
	/**
	 *　支援タイプ
	 *　効果はかける相手のステータスに依存しているスキルです
	 * @author 樹麗
	 */
	SUPPORT("支援"){
		@Override
		protected int getPower(Unit unit, Skill skill) {
			return 0;
		}
	},
	/**
	 *　条件タイプ
	 *　条件を満たすと効果を発揮するスキルです
	 * @author 樹麗。
	 */
	CONDITIONS("条件") {
		@Override
		protected int getPower(Unit unit, Skill skill) {
			return 0;
		}

	};
	private String name;
	/**
	 * @param name 名前
	 */
	SkillType(String name){
		this.name = name;
	}
	/**
	 * @return　名前を返す
	 */
	public String getName() {
		return this.name;
	}
	/* (非 Javadoc)
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString(){
		return this.name;
	}
	/**
	 * @param unit　スキルを使用したユニット
	 * @param skill 使用するスキル
	 * @return 最終的な威力
	 */
	protected abstract int getPower(Unit unit, Skill skill);
//	protected abstract double getDisturbingPoewr(Unit unit);
}
