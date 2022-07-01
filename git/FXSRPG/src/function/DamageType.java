package function;

import function.unit.Unit;
import function.unit.UnitStatusType;

/**
 *　ダメージの種類を表している列挙型
 * @author 樹麗
 */
public enum DamageType {
	/**
	 * この属性は攻撃力と防御力からダメージを算出する
	 */
	MATERIAL(){
		protected int calc(Unit unit, int damage) {
			if(damage > 0) {
				damage -= unit.getStatus(UnitStatusType.DEFENSE).getCurrentStatus();
				if(damage < 0 ) { damage = 0; }
			}
			return damage;
		}
	},
	/**
	 * この属性は魔力と魔防からダメージを算出する
	 */
	MAGIC(){
		protected int calc(Unit unit, int damage) {
			if(damage > 0) {
				damage -= unit.getStatus(UnitStatusType.MAGIC_DEFENSE).getCurrentStatus();
				if(damage < 0 ) { damage = 0; }
			}
			return damage;
		}
	};
	/**
	 * @param unit ダメージを受けるユニット
	 * @param damage 与えるダメージ
	 * @return unitが受ける最終ダメージを返す
	 * 最終ダメージの算出
	 */
	protected abstract int calc(Unit unit, int damage);
}
