package function.unit;

/**
 * ユニットのステータスを表しているオブジェクト
 * @author 樹麗
 */
public class UnitStatus implements Cloneable {
	private final int basic_status;
	private final UnitStatusType status_type;
	private int default_status, current_status, lv;
	private double status_growth_rate;
	/**
	 * @param status_type どのステータスなのか
	 * @param basic_status ステータスの初期値
	 * @param lv このオブジェクトを所有しているユニットの現在のLV
	 * @param status_growth_rate LVUP時の成長率
	 */
	public UnitStatus(UnitStatusType status_type, int basic_status, int lv, double status_growth_rate){
		this.basic_status = basic_status;
		this.status_type = status_type;
		this.default_status = this.basic_status;
		this.lv = lv;
		this.status_growth_rate = status_growth_rate;
		int i = 1;
		while(i++ < lv){
			this.default_status += Math.ceil(this.default_status * status_growth_rate);
		}
		this.current_status = this.default_status;
	}
	/**
	 * @return　ステータスタイプを返す
	 */
	public UnitStatusType getUnitStatusType(){ return this.status_type; }
	/**
	 * 0以下の場合0を返す
	 * @return　現在のステータスの値を返す
	 */
	public int getCurrentStatus(){
		if(this.current_status < 0) {
			return 0;
		}
		return this.current_status;
	}
	/**
	 * @return　ステータスの標準値を返す
	 */
	public int getDefaultStatus(){ return this.default_status; }
	/**
	 * @return　ステータスの基本値を返す
	 */
	int getBasicStatus(){ return this.basic_status; }
	/**
	 *　現在のステータスに渡された値を足す
	 * @param add_status 現在のステータスに足す値
	 */
	void addCurrentStatus(int add_status){ this.current_status += add_status; }
	/**
	 * 標準値を返す
	 * @param default_status
	 */
	void setDefaultStatus(int default_status){ this.default_status = default_status; }
	/**
	 * lvに応じてステータスが変動する
	 * 変動する値は基本値に依存する
	 * @param lv　このオブジェクトを所有しているユニットのレベル
	 */
	void lvChange(int lv){
		int i;
		int default_status = this.default_status;
		if(lv > this.lv){
			i = this.lv;
			while(i++ < lv){
				this.default_status += Math.ceil(this.default_status * status_growth_rate);
			}
		}else{
			i = 1;
			this.default_status = this.basic_status;
			while(i++ < lv){
				this.default_status += Math.ceil(this.default_status * status_growth_rate);
			}
		}

		this.current_status += this.default_status - default_status;
		this.lv = lv;
	}
	@Override
	public UnitStatus clone() {
		UnitStatus status = null;
		try {
			status = (UnitStatus) super.clone();
		} catch (CloneNotSupportedException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		return status;
	}
}
