package function.unit;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import common.IgetSkillIdListdable;
import function.GameData;

/**
 * ユニットの種族を表しているクラス
 * @author 樹麗
 */
public class Race extends GameData implements IgetSkillIdListdable {
	private List<String> skill_id_list;
	private double hp_up, mp_up, attack_up, magic_attack_up, defense_up, magic_defense_up, exp_rate;
	/**
	 * @param id 種族ＩＤ
	 * @param name　種族名
	 * @param hp_up LVUp時のHP上昇率
	 * @param mp_up LVUp時のMP上昇率
	 * @param attack_up LVUp時の攻撃力上昇率
	 * @param magic_attack_up LVUp時の魔力上昇率
	 * @param defense_up LVUp時の防御力上昇率
	 * @param magic_defense_up LVUp時の魔防上昇率
	 * @param skill_id_list 持っているスキル
	 * @param exp_rate LVUp時の必要経験値上昇率
	 */
	@JsonCreator
	public Race(@JsonProperty("id") String id, @JsonProperty("name") String name, @JsonProperty("hp_up") double hp_up,
			@JsonProperty("mp_up")double mp_up, @JsonProperty("attack_up") double attack_up, @JsonProperty("magic_attack_up") double magic_attack_up,
			@JsonProperty("defense_up")double defense_up, @JsonProperty("magic_defense_up") double magic_defense_up,
			@JsonProperty("skill_id_list")List<String> skill_id_list, @JsonProperty("exp_rate") double exp_rate){
		super(id, name);
		this.hp_up = hp_up;
		this.mp_up = mp_up;
		this.attack_up = attack_up;
		this.magic_attack_up = magic_attack_up;
		this.defense_up = defense_up;
		this.magic_defense_up = magic_defense_up;
		this.skill_id_list = skill_id_list;
		this.exp_rate = exp_rate;
	}
	/**
	 * @return ユニットのLVUP時のHP上昇率
	 */
	public double getHpUp(){
		return this.hp_up;
	}
	/**
	 * @return ユニットのLVUP時のMP上昇率
	 */
	public double getMpUp() {
		return this.mp_up;
	}
	/**
	 * @return ユニットのLVUP時の攻撃力上昇率
	 */
	public double getAttackUp(){
		return this.attack_up;
	}
	/**
	 * @return ユニットのLVUP時の魔力上昇率
	 */
	public double getMagicAttackUp() {
		return this.magic_attack_up;
	}
	/**
	 * @return ユニットのLVUP時の防御力上昇率
	 */
	public double getDefenseUp(){
		return this.defense_up;
	}
	/**
	 * @return ユニットのLVUP時の魔防上昇率
	 */
	public double getMagicDefenseUp() {
		return this.magic_defense_up;
	}
	/**
	 * @return ユニットのLVUP時の必要経験値上昇率
	 */
	public double getExpRate(){
		return this.exp_rate;
	}
	/* (非 Javadoc)
	 * @see common.IgetSkillIdListdable#getSkillIdList()
	 */
	@Override
	public List<String> getSkillIdList(){
		return this.skill_id_list;
	}
	/* (非 Javadoc)
	 * @see function.GameData#clone()
	 */
	@Override
	public Race clone(){
		Race race = null;
		race = (Race)super.clone();
		return race;
	}
	/* (非 Javadoc)
	 * @see function.GameData#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Race other = (Race) obj;
		if (getId() == null) {
			if (other.getId() != null)
				return false;
		} else if (!getId().equals(other.getId()))
			return false;
		return true;
	}
	/* (非 Javadoc)
	 * @see function.GameData#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
		return result;
	}

	/* (非 Javadoc)
	 * @see function.GameData#getDefaultData()
	 */
	@Override
	public Race getDefaultData() {
		return defaultData();
	}
	/**
	 * @return　Raceの基本値を返す
	 */
	public static Race defaultData() {
		return new Race("", "", 0, 0, 0, 0, 0, 0, new ArrayList<String>(), 0);
	}
}
