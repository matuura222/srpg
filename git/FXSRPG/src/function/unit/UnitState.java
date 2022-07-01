package function.unit;

import com.fasterxml.jackson.annotation.JsonProperty;

import function.ActivateType;


/**
 * このクラスはユニットの状態を表しているオブジェクト
 * @author 樹麗
 *
 */
public class UnitState implements Cloneable{
	private final String id;
	private String name;
	private final UnitStatusType status_type;
	private final ActivateType activate_type;
	private int variable_amount, fixed_amount, effect_time;
	private boolean is_activated;
	/**
	 * @param id　状態ID
	 * @param name　状態名
	 * @param status_type どのステータスに影響を及ぼすか
	 * @param activate_type いつ効果を発揮するか
	 * @param variable_amount 効果の大きさ（変動値）
	 * @param fixed_amount　効果の大きさ（固定値）
	 * @param effect_time　効果時間
	 */
	public UnitState(@JsonProperty("id")String id, @JsonProperty("name")String name,
			@JsonProperty("status_type")UnitStatusType status_type, @JsonProperty("activate_type")ActivateType activate_type,
			@JsonProperty("variable_amount")int variable_amount, @JsonProperty("fixed_amount")int fixed_amount,
			@JsonProperty("effect_time")int effect_time){
		this.id = id;
		this.name = name;
		this.status_type = status_type;
		this.activate_type = activate_type;
		this.variable_amount = variable_amount;
		this.fixed_amount = fixed_amount;
		this.effect_time = effect_time;
		is_activated = false;
	}
	/**
	 * 対象に状態に応じた影響を及ぼす
	 * @param unit 影響を及ぼす対象
	 * @param activate_type 効果を発揮するのが何時か
	 */
	void activate(Unit unit, ActivateType activate_type){
		if(activate_type == ActivateType.TURN_END) {
			effect_time--;
			if(effect_time <= 0){
				unit.addRemoveState(this.id);
				return;
			}
		}
		if(this.activate_type != activate_type) { return; }
		if(is_activated) { return; }
		unit.getStatus(status_type).addCurrentStatus((int) (unit.getStatus(status_type).getDefaultStatus() * variable_amount + fixed_amount));
		is_activated = true;
	}
	/**
	 * 渡されたunitに及ぼした影響を取り除く
	 * 影響を及ぼしたステータスがHPかMPの場合取り除かない
	 * @param unit　影響を及ぼすユニット
	 */
	void inactive(Unit unit) {
		if(status_type != UnitStatusType.HP && status_type != UnitStatusType.MP) {
			int status = getVariableAmount() * unit.getStatus(getStatusType()).getDefaultStatus() + getFixedAmount();
			unit.getStatus(getStatusType()).addCurrentStatus(-status);
		}
		is_activated = false;

	}
	/**
	 * @return IDを返す
	 */
	public String getId(){ return this.id; }
	/**
	 * @return 名前を返す
	 */
	public String getName(){ return this.name; }
	/**
	 * @return　現在効果を発揮しているかどうか
	 */
	public boolean getIsActivated() { return this.is_activated; }
	/**
	 * @param is_activated 効果を発揮しているかどうか
	 */
	void setIsActivated(boolean is_activated) { this.is_activated = is_activated; }
	/**
	 * @return　ステータスタイプを返す
	 */
	public UnitStatusType getStatusType(){
		return this.status_type;
	}
	/**
	 * @return　アクティベートタイプを返す
	 */
	public ActivateType getActivateType(){
		return this.activate_type;
	}
	/**
	 * @return　効果の大きさ（変動値）を返す
	 */
	public int getVariableAmount(){
		return this.variable_amount;
	}
	/**
	 * @return　効果の大きさ（固定値）を返す
	 */
	public int getFixedAmount(){
		return this.fixed_amount;
	}
	/**
	 * @return　効果時間を返す
	 */
	public int getEffectTime(){
		return this.effect_time;
	}
	/* (非 Javadoc)
	 * @see java.lang.Object#clone()
	 */
	@Override
	public UnitState clone(){
		UnitState state = null;
		try {
			state = (UnitState) super.clone();
		} catch (CloneNotSupportedException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		return state;
	}
	/* (非 Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
	/* (非 Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UnitState other = (UnitState) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	/* (非 Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString(){
		return this.name;
	}
}
