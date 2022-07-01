package function;

import function.unit.Unit;

/**
 *　DamageクラスはUnitクラスに与えるダメージを表しているオブジェクトです。
 * @author 樹麗
 */
public class Damage {
	private int damage;
	private DamageType type;
	/**
	 * @param damage　ダメージ量
	 * @param type ダメージタイプ
	 */
	public Damage(int damage, DamageType type) {
		this.damage = damage;
		this.type = type;
	}
	/**
	 * @param unit ダメージを与える対象
	 * @return　ダメージタイプに応じてunitからダメージを引いた値を返す
	 */
	public int getDamage(Unit unit) {
		return type.calc(unit, damage);
	}
}
