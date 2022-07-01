package function;

/**
 *　いつ効果を作用するかを表している列挙型
 * @author 樹麗
 */
public enum ActivateType {
	/**
	 * 常に効果を発揮する
	 */
	ALWAYS("常時"),
	/**
	 * ゲーム開始時に効果を発揮する
	 */
	GAME_START("ゲーム開始"),
	/**
	 * ゲーム終了時に効果を発揮する
	 */
	GAME_END("ゲームエンド"),
	/**
	 * 攻撃時に効果を発揮する
	 */
	ATTACK("攻撃時"),
	/**
	 * 防御時に効果を発揮する
	 */
	DEFENSE("防御時"),
	/**
	 * ターン開始時に効果を発揮する
	 */
	TURN_START("ターン開始"),
	/**
	 * ターン終了時に効果を発揮する
	 */
	TURN_END("ターン終了");
	private String name;
	/**
	 * @param name　名前
	 */
	ActivateType(String name){
		this.name = name;
	}
	/**
	 * @return 名前を返す
	 */
	public String getName(){
		return this.name;
	}
	/* (非 Javadoc)
	 * @see java.lang.Enum#toString()
	 */
	public String toString() {
		return this.name;
	}
}
