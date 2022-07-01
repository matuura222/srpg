package function.unit;

/**
 * この列挙型はユニットのステータスの種類を表しているオブジェクト
 * @author 樹麗
 */
public enum UnitStatusType {
	/**
	 *　ステータスの現在のHPを表している
	 * @author 樹麗
	 */
	HP("HP"){
	},
	/**
	 *　ステータスの現在のMPを表している
	 * @author 樹麗
	 */
	MP("MP"){
	},
	/**
	 *　ステータスの最大HPを表している
	 * @author 樹麗
	 */
	MAX_HP("最大HP") {
	},
	/**
	 *　ステータスの最大MPを表している
	 * @author 樹麗
	 */
	MAX_MP("最大MP") {
	},
	/**
	 *　ステータスの攻撃力を表している
	 * @author 樹麗
	 */
	ATTACK("攻撃力") {
	},
	/**
	 *　ステータスの防御力を表している
	 * @author 樹麗
	 */
	DEFENSE("防御力") {
	},
	/**
	 *　ステータスの魔力を表している
	 * @author 樹麗
	 */
	MAGIC_ATTACK("魔力") {
	},
	/**
	 *　ステータスの魔防を表している
	 * @author 樹麗
	 */
	MAGIC_DEFENSE("魔防") {
	},
	/**
	 *　ステータスの移動力を表している
	 * @author 樹麗
	 */
	MOVE("移動力") {
	},
	/**
	 *　ステータスの攻撃範囲を表している
	 * @author 樹麗
	 */
	ATTACK_RANGE("攻撃範囲") {
	};
	private String name;
	/**
	 * @param name 名前
	 */
	UnitStatusType(String name){
		this.name = name;
	}
	/**
	 * @return 名前を返す
	 */
	public String getName(){
		return name;
	}
}
