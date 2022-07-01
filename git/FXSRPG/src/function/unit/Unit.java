package function.unit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import common.IgetImageable;
import common.IgetSkillIdListdable;
import common.IgetStateListable;
import common.SRPGCommons;
import function.ActivateType;
import function.Damage;
import function.GameData;
import function.battle.range.EffectRange;
import function.bgm.SoundEffect;
import function.controller.state.game.battle.BattleState;
import function.map.Square;
import function.skill.ConditionSkill;
import function.skill.Skill;
import javafx.scene.image.Image;
/**
 * このクラスはユニットを表しているオブジェクトです。
 * @author 樹麗
 *
 */
@JsonIgnoreProperties({"race", "job", "state_list", "image", "id_to_skill", "need_exp", "give_exp", "acted", "friend", "moved"})
public class Unit extends GameData implements IgetImageable, IgetSkillIdListdable, IgetStateListable{
	private String image_name;
	private int lv;
	private int need_exp;//ＬＶアップに必要な経験値
	private int give_exp;//倒した時に手に入る経験値
	private int currently_exp;//現在の経験値量
	private ThoughtType thought_type;
	private boolean is_friend, is_acted, is_moved;
	private Image image;
	private Race race;
	private Job job;
	private Map<String, Skill> id_to_skill;
	private Map<String, UnitState> state_list;
	private List<String> remove_state_id_list;
	private Map<UnitStatusType, UnitStatus> status;
	/**
	 * @param id　ユニットID
	 * @param unit_name　ユニット名
	 * @param lv ユニットのLV
	 * @param currently_exp 所有経験値
	 * @param job_id ユニットの職業
	 * @param race_id ユニットの種族
	 * @param skill_id_list ユニットが持っているスキル
	 * @param hp ユニットの初期HP
	 * @param mp ユニットの初期MP
	 * @param attack ユニットの初期攻撃力
	 * @param defense ユニットの初期防御力
	 * @param magic_attack ユニットの初期魔力
	 * @param magic_defense ユニットの初期魔防
	 * @param move ユニットの初期移動力
	 * @param attack_range ユニットのの初期攻撃範囲
	 * @param thought_type ユニットの初期行動パターン
	 * @param image_name ユニットの画像
	 */
	@JsonCreator
	public Unit(@JsonProperty("id")String id, @JsonProperty("name")String unit_name,
			@JsonProperty("lv")int lv, @JsonProperty("currently_exp")int currently_exp,
			@JsonProperty("job_id")String job_id, @JsonProperty("race_id")String race_id,
			@JsonProperty("skill_id_list")List<String> skill_id_list, @JsonProperty("basic_max_hp")int hp,
			@JsonProperty("basic_max_mp")int mp, @JsonProperty("basic_attack")int attack,
			@JsonProperty("basic_defense")int defense, @JsonProperty("basic_magic_attack")int magic_attack,
			@JsonProperty("basic_magic_defense")int magic_defense, @JsonProperty("basic_move")int move,
			@JsonProperty("basic_attack_range")int attack_range, @JsonProperty("thought_type")ThoughtType thought_type,
			@JsonProperty("image_name")String image_name) {
		super(id, unit_name);
		this.lv = lv;
		this.job = SRPGCommons.getJob(job_id);
		this.race = SRPGCommons.getRace(race_id);
		skill_id_list.addAll(this.job.getSkillIdList());
		skill_id_list.addAll(this.race.getSkillIdList());
		this.id_to_skill = new HashMap<String, Skill>();
		for(String skill_id : skill_id_list) {
			Skill skill = SRPGCommons.getSkill(skill_id);
			this.id_to_skill.put(skill_id, skill);
		}
		this.is_acted = this.is_moved = false;
		this.thought_type = thought_type;
		this.state_list = new HashMap<String, UnitState>();
		this.remove_state_id_list = new ArrayList<String>();
		this.status = new HashMap<UnitStatusType, UnitStatus>();
		status.put(UnitStatusType.MAX_HP, new UnitStatus(UnitStatusType.MAX_HP, hp, lv, (getJob().getHpUp() + getRace().getHpUp()) / 100.0));
		status.put(UnitStatusType.MAX_MP, new UnitStatus(UnitStatusType.MAX_MP, mp, lv, (getJob().getMpUp() + getRace().getMpUp()) / 100.0));
		status.put(UnitStatusType.HP, new UnitStatus(UnitStatusType.HP, status.get(UnitStatusType.MAX_HP).getDefaultStatus(), lv, 0));
		status.put(UnitStatusType.MP, new UnitStatus(UnitStatusType.MP, status.get(UnitStatusType.MAX_MP).getDefaultStatus(), lv, 0));
		status.put(UnitStatusType.ATTACK, new UnitStatus(UnitStatusType.ATTACK, attack, lv, (getJob().getAttackUp() + getRace().getAttackUp()) / 100.0));
		status.put(UnitStatusType.DEFENSE, new UnitStatus(UnitStatusType.DEFENSE, defense, lv, (getJob().getDefenseUp() + getRace().getDefenseUp()) / 100.0));
		status.put(UnitStatusType.MAGIC_ATTACK, new UnitStatus(UnitStatusType.MAGIC_ATTACK, magic_attack, lv, (getJob().getMagicAttackUp() + getRace().getMagicAttackUp()) / 100.0));
		status.put(UnitStatusType.MAGIC_DEFENSE, new UnitStatus(UnitStatusType.MAGIC_DEFENSE, magic_defense, lv, (getJob().getMagicDefenseUp() + getRace().getMagicDefenseUp()) / 100.0));
		status.put(UnitStatusType.ATTACK_RANGE, new UnitStatus(UnitStatusType.ATTACK_RANGE, attack_range, lv, 0.0));
		status.put(UnitStatusType.MOVE, new UnitStatus(UnitStatusType.MOVE, move, lv, 0.0));
		calcExp();
		this.currently_exp = currently_exp;
		updateImage(image_name);
	}
	/**
	 * ユニットの現在の状態
	 * @param battle　バトルステート
	 * @param activate 現在の状況
	 */
	public void currentState(BattleState battle, ActivateType activate){
		if(getStatus(UnitStatusType.HP).getCurrentStatus() <= 0) { return; }
		passiveSkillActivate(battle, activate);
		for(UnitState state : state_list.values()){
			if(state.getActivateType() != ActivateType.ALWAYS && state.getActivateType() != activate) {
				if(state.getIsActivated()) {
					state.inactive(this);
				}
				continue;
			}
			state.activate(this, activate);
			state.activate(this, ActivateType.ALWAYS);
		}
		for(String key : remove_state_id_list){
			UnitState state = state_list.get(key);
			if(state.getStatusType() != UnitStatusType.HP && state.getStatusType() != UnitStatusType.MP) {
				if(!state.getIsActivated()) { continue; }
				int status = state.getVariableAmount() * getStatus(state.getStatusType()).getDefaultStatus() + state.getFixedAmount();
				getStatus(state.getStatusType()).addCurrentStatus(-status);
			}
			state_list.remove(key);
		}
		remove_state_id_list.clear();
		if(this.status.get(UnitStatusType.MAX_HP).getCurrentStatus() < this.status.get(UnitStatusType.HP).getCurrentStatus()){
			this.status.get(UnitStatusType.HP).addCurrentStatus(
					-(this.status.get(UnitStatusType.HP).getCurrentStatus() - this.status.get(UnitStatusType.MAX_HP).getCurrentStatus())
					);
		}
		if(this.status.get(UnitStatusType.MAX_MP).getCurrentStatus() < this.status.get(UnitStatusType.MP).getCurrentStatus()){
			this.status.get(UnitStatusType.MP).addCurrentStatus(
					-(this.status.get(UnitStatusType.MP).getCurrentStatus() - this.status.get(UnitStatusType.MAX_MP).getCurrentStatus())
					);
		}
	}
	/**
	 * activateに対応したスキルを発動させる
	 * @param battle バトルステート
	 * @param activate　現在の状況
	 */
	public void passiveSkillActivate(BattleState battle, ActivateType activate) {
		for(Skill skill : id_to_skill.values()) {
			if(skill.getClass() != ConditionSkill.class) { continue; }
			if(activate != ((ConditionSkill) skill).getActivateType() &&
					((ConditionSkill) skill).getActivateType() != ActivateType.ALWAYS) { continue; }
			Square square = battle.getGameMap().getGameMapData().getUnitLocate(this);
			EffectRange range = new EffectRange(battle.getGameMap().getGameMapData(), skill);
			range.startSearch(square, this);
			List<Square> square_list = range.getMap().values().stream()
				.map(s -> s.getTo())
				.collect(Collectors.toList());
			skill.action(battle, this, square_list);
		}
	}
	/**
	 * @return　ユニットのLVを返す
	 */
	public int getLv(){ return this.lv; }
	/**
	 * @return ユニットの職業IDを返す
	 */
	public String getJobId(){ return job.getId(); }
	/**
	 * @return ユニットの職業を返す
	 */
	public Job getJob(){ return job; }
	/**
	 * @return　ユニットの種族IDを返す
	 */
	public String getRaceId(){ return race.getId(); }
	/**
	 * @return ユニットの職業を返す
	 */
	public Race getRace(){ return race; }
	/* (非 Javadoc)
	 * @see common.IgetStateListable#getStateList()
	 */
	@Override
	public List<UnitState> getStateList(){ return this.state_list.values().stream().collect(Collectors.toList()); }
	/* (非 Javadoc)
	 * @see common.IgetSkillIdListdable#getSkillIdList()
	 */
	@Override
	public List<String> getSkillIdList(){ return id_to_skill.keySet().stream().collect(Collectors.toList()); }
	/**
	 * @return　ユニットの所有しているスキルを返す
	 */
	public Map<String, Skill> getIdToSkill(){ return id_to_skill; }
	/**
	 * @return hpの基本値を返す
	 */
	public int getBasicMaxHp(){ return status.get(UnitStatusType.MAX_HP).getBasicStatus(); }
	/**
	 * @return mpの基本値を返す
	 */
	public int getBasicMaxMp(){ return status.get(UnitStatusType.MAX_MP).getBasicStatus(); }
	/**
	 * @return 攻撃力の基本値を返す
	 */
	public int getBasicAttack(){ return status.get(UnitStatusType.ATTACK).getBasicStatus(); }
	/**
	 * @return　防御力の基本値を返す
	 */
	public int getBasicDefense(){ return status.get(UnitStatusType.DEFENSE).getBasicStatus(); }
	/**
	 * @return　魔力の基本値を返す
	 */
	public int getBasicMagicAttack(){ return status.get(UnitStatusType.MAGIC_ATTACK).getBasicStatus(); }
	/**
	 * @return　魔防の基本値を返す
	 */
	public int getBasicMagicDefense(){ return status.get(UnitStatusType.MAGIC_DEFENSE).getBasicStatus(); }
	/**
	 * @return　移動力の基本値を返す
	 */
	public int getBasicMove(){ return status.get(UnitStatusType.MOVE).getBasicStatus(); }
	/**
	 * @return　攻撃範囲の基本値を返す
	 */
	public int getBasicAttackRange(){ return status.get(UnitStatusType.ATTACK_RANGE).getBasicStatus(); }
	/**
	 * @param type　ステータスタイプ
	 * @return　渡されたステータスタイプに対応するステータスを返す
	 */
	public UnitStatus getStatus(UnitStatusType type){ return this.status.get(type); }
	/**
	 * @return　ユニットの行動パターンを返す
	 */
	public ThoughtType getThoughtType(){ return thought_type; }
	/**
	 * 渡された行動パターンに変更する
	 * @param thought_type 行動パターン
	 */
	void setThoughtType(ThoughtType thought_type){ this.thought_type = thought_type; }
	/**
	 * @return 味方かどうかを返す
	 */
	public boolean isFriend() { return this.is_friend; }
	/**
	 * @param is_friend 味方かどうか
	 */
	public void setIsFriend(boolean is_friend){ this.is_friend = is_friend;  }
	/**
	 * @return 行動済みかどうか
	 */
	public boolean isActed() { return this.is_acted; }
	/**
	 * @param is_acted 行動済みかどうか
	 */
	public void setIsActed(boolean is_acted) { this.is_acted = is_acted; }
	/**
	 * @return 移動済みかどうか
	 */
	public boolean isMoved(){ return this.is_moved; }
	/**
	 * @param is_moved 移動済みかどうか
	 */
	public void setIsMoved(boolean is_moved){ this.is_moved = is_moved; }
	/**
	 * ユニットに新しい状態を追加する
	 * すでにある状態の場合上書きする
	 * @param state 状態
	 */
	public void putState(UnitState state) {
		UnitState s = state_list.get(state.getId());
		if(s != null) {
			state.setIsActivated(s.getIsActivated());
		}
		state_list.put(state.getId(), state);
		state.activate(this, ActivateType.ALWAYS);
	}
	/**
	 *　渡されたidに対応するidを削除する
	 * @param id id
	 */
	void addRemoveState(String id){
		if(!remove_state_id_list.contains(id)) {
			remove_state_id_list.add(id);
		}
	}
	/* (非 Javadoc)
	 * @see common.IgetImageable#getImage()
	 */
	@Override
	public Image getImage() { return this.image; }
	/**
	 * @return 画像名を返す
	 */
	public String getImageName() { return image_name; }
	/**
	 * @param image_name　更新先の画像名
	 * 指定した画像が存在した場合その画像に更新する。
	 * 無かった場合、NotImageの画像になる
	 */
	public void updateImage(String image_name){
		this.image_name = image_name;
		Image image = new Image(SRPGCommons.getURL() + "/unit/img/" + image_name);
		if(image.isError()){
			this.image = new Image(SRPGCommons.getURL() + "/unit/img/NotImage.png");
			return;
		}
		this.image = image;
	}
	/**
	 * @return 必要経験値を返す
	 */
	public int getNeedExp() { return this.need_exp; }
	/**
	 * @return 倒された時に渡す経験値を返す
	 */
	public int getGiveExp(){ return give_exp; }
	/**
	 * @return 現在所有している経験値を返す
	 */
	public int getCurrentlyExp() { return this.currently_exp; }
	/**
	 * @param currently_exp　取得した経験値
	 * @param is_bgm_on　LVUP時音を出すか
	 */
	public void addCurrentlyExp(int currently_exp, boolean is_bgm_on) {
		this.currently_exp += currently_exp;
		lvUp(is_bgm_on);
	}
	/**
	 * @param cost　消費するMP
	 * 指定された分MPを消費する。
	 */
	public void mpCost(int cost){
		this.status.get(UnitStatusType.MP).addCurrentStatus(-cost);
		if(this.status.get(UnitStatusType.MP).getCurrentStatus() > this.status.get(UnitStatusType.MAX_MP).getCurrentStatus()){
			this.status.get(UnitStatusType.MP).addCurrentStatus(this.status.get(UnitStatusType.MAX_MP).getCurrentStatus() - this.status.get(UnitStatusType.MP).getCurrentStatus());
		}
	}
	/**
	 * @param damage 与えられたダメージ
	 * 受けたダメージに応じてHPを変動させる。
	 */
	public void damage(Damage damage){
		int d = damage.getDamage(this);
		this.status.get(UnitStatusType.HP).addCurrentStatus(-d);
		if(this.status.get(UnitStatusType.HP).getCurrentStatus() > this.status.get(UnitStatusType.MAX_HP).getCurrentStatus()){
			this.status.get(UnitStatusType.HP).addCurrentStatus(this.status.get(UnitStatusType.MAX_HP).getCurrentStatus() - this.status.get(UnitStatusType.HP).getCurrentStatus());
		}
	}
	/**
	 * @param is_bgm_on　LVUP時音を出すか
	 * 必要経験値に足りていた場合所有経験値を減少させステータスを変動させる
	 */
	private void lvUp(boolean is_bgm_on) {
		if(getNeedExp() > getCurrentlyExp()){ return; }
		if(is_bgm_on){
			SoundEffect.play("lv_up.mp3");
		}
		lv++;
		int exp = getNeedExp();
		int max_hp = this.status.get(UnitStatusType.MAX_HP).getDefaultStatus();
		int max_mp = this.status.get(UnitStatusType.MAX_MP).getDefaultStatus();
		for(UnitStatusType type : UnitStatusType.values()){
			status.get(type).lvChange(lv);
		}
		this.status.get(UnitStatusType.HP).addCurrentStatus(this.status.get(UnitStatusType.MAX_HP).getCurrentStatus() - max_hp);
		this.status.get(UnitStatusType.MP).addCurrentStatus(this.status.get(UnitStatusType.MAX_MP).getCurrentStatus() - max_mp);
		calcExp();
		addCurrentlyExp(-exp, is_bgm_on);
	}
	/**
	 * レベルをダウンさせ、その分ステータスを変動させる
	 */
	public void lvDown(){
		if(lv <= 1){ return; }
		lv--;
		int max_hp = this.status.get(UnitStatusType.MAX_HP).getDefaultStatus();
		int max_mp = this.status.get(UnitStatusType.MAX_MP).getDefaultStatus();
		for(UnitStatusType type : UnitStatusType.values()){
			status.get(type).lvChange(lv);
		}
		this.status.get(UnitStatusType.HP).addCurrentStatus(this.status.get(UnitStatusType.MAX_HP).getCurrentStatus() - max_hp);
		this.status.get(UnitStatusType.MP).addCurrentStatus(this.status.get(UnitStatusType.MAX_MP).getCurrentStatus() - max_mp);
		calcExp();
	}
	/**
	 * LVUPに必要な経験値を計算する
	 */
	private void calcExp() {
		double exp = 0;
		exp += this.status.get(UnitStatusType.MAX_HP).getCurrentStatus()  / 10;
		exp += this.status.get(UnitStatusType.MAX_MP).getCurrentStatus() / 10;
		exp += this.status.get(UnitStatusType.ATTACK).getCurrentStatus() / 3;
		exp += this.status.get(UnitStatusType.DEFENSE).getCurrentStatus() / 3;
		exp += this.status.get(UnitStatusType.MAGIC_ATTACK).getCurrentStatus() / 3;
		exp += this.status.get(UnitStatusType.MAGIC_DEFENSE).getCurrentStatus() / 3;
		exp += this.status.get(UnitStatusType.MOVE).getCurrentStatus();
		exp += this.status.get(UnitStatusType.ATTACK_RANGE).getCurrentStatus() * 2;
		for(String key : getSkillIdList()){
			exp += SRPGCommons.getSkill(key).getExp();
		}
		this.give_exp = (int) exp;
		if(give_exp <= 0) {
			give_exp = 1;
		}
		double exp_rate = 1;
		exp_rate += (getRace().getExpRate() / 100) + (getJob().getExpRate() / 100);
		for(int i = 1 ; i < lv ; i++){
			exp *= exp_rate;
		}
		if(exp <= 0){
			exp = 1;
		}
		this.need_exp = (int) exp;
	}
	/* (非 Javadoc)
	 * @see function.GameData#clone()
	 */
	@Override
	public Unit clone(){
		Unit unit = null;
		unit = (Unit)super.clone();
		unit.status = new HashMap<UnitStatusType, UnitStatus>();
		for(UnitStatus status : this.status.values()) {
			unit.status.put(status.getUnitStatusType(), status.clone());
		}
		unit.state_list = new HashMap<String, UnitState>();
		for(UnitState state : this.state_list.values()) {
			unit.state_list.put(state.getId(), state.clone());
		}
		unit.remove_state_id_list = new ArrayList<String>();
		for(String id : this.remove_state_id_list) {
			unit.remove_state_id_list.add(id);
		}
		return unit;
	}
	/* (非 Javadoc)
	 * @see function.GameData#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + (is_friend ? 1231 : 1237);
		return result;
	}
	/* (非 Javadoc)
	 * @see function.GameData#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Unit other = (Unit) obj;
		if (is_friend != other.is_friend)
			return false;
		return true;
	}
	/* (非 Javadoc)
	 * @see function.GameData#toString()
	 */
	@Override
	public String toString(){ return getName(); }
	/* (非 Javadoc)
	 * @see function.GameData#getDefaultData()
	 */
	@Override
	public Unit getDefaultData(){
		return defaultData();
	}
	/**
	 * @return ユニットの基本値を返す
	 */
	public static Unit defaultData(){
		return new Unit("",  "", 1, 0, "障害物", "障害物", new ArrayList<String>(), 1, 0, 0, 0, 0, 0, 0, 0, ThoughtType.CHARGE, "");
	}

}