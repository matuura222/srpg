package function.skill;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;

import function.GameData;
import function.controller.state.game.battle.BattleState;
import function.map.Square;
import function.unit.Unit;

/**
 * このクラスはスキルを表している基底クラスです
 * @author 樹麗
 */
public abstract class Skill extends GameData{
	private final SkillType skill_type;
	private final String skill_text, sound_effect_name;
	private final int cost, range_distance, effect_range, exp, variable_power, fixed_power;
	private final TargetType target_type;
	/**
	 * @param id　スキルID
	 * @param name スキル名
	 * @param skill_text スキルの説明
	 * @param skill_type スキルの種類
	 * @param cost 消費するMP
	 * @param variable_power スキルの威力(変動値)
	 * @param fixed_power スキルの威力(固定値)
	 * @param range_distance スキルの射程距離
	 * @param effect_range スキルの効果範囲
	 * @param target_type スキルの効果対象
	 * @param exp このスキルを所持することで増える必要経験値
	 * @param sound_effect_name　スキルの効果音
	 */
	@JsonCreator
	public Skill(String id, String name,
			String skill_text, SkillType skill_type, int cost, int variable_power, int fixed_power,
			int range_distance, int effect_range, TargetType target_type, int exp, String sound_effect_name){
		super(id, name);
		this.cost = cost;
		this.skill_type = skill_type;
		this.variable_power = variable_power;
		this.fixed_power = fixed_power;
		this.skill_text = skill_text;
		this.range_distance = range_distance;
		this.effect_range = effect_range;
		this.target_type = target_type;
		this.exp = exp;
		this.sound_effect_name = sound_effect_name;
	}
	/**
	 * @param battle ゲームバトル
	 * @param unit　スキルを発動したユニット
	 * @param target_list　ターゲットのスクエア
	 * スキルに応じて効果を発揮する
	 */
	public abstract void action(BattleState battle, Unit unit, List<Square> target_list);

	/**
	 * @return　スキルタイプを返す
	 */
	public final SkillType getSkillType() {
		return this.skill_type;
	}
	/**
	 * @return 消費MPを返す
	 */
	public final int getCost() {
		return this.cost;
	}
	/**
	 * @return 威力(変数値)を返す
	 */
	public final int getVariablePower() {
		return this.variable_power;
	}
	/**
	 * @return 威力(固定値)を返す
	 */
	public final int getFixedPower(){
		return this.fixed_power;
	}
	/**
	 * @return スキルの説明を返す
	 */
	public final String getSkillText() {
		return this.skill_text;
	}
	/**
	 * @return スキルの射程を返す
	 */
	public final int getRangeDistance() {
		return this.range_distance;
	}
	/**
	 * @return　スキルの効果範囲を返す
	 */
	public final int getEffectRange() {
		return this.effect_range;
	}
	/**
	 * @return ターゲットタイプを返す
	 */
	public final TargetType getTargetType() {
		return this.target_type;
	}
	/**
	 * @return スキル所有時に増える必要経験値
	 */
	public final int getExp(){
		return this.exp;
	}
	/**
	 * @return 使用時の効果音名
	 */
	public final String getSoundEffectName(){
		return this.sound_effect_name;
	}
	/* (非 Javadoc)
	 * @see function.GameData#clone()
	 */
	@Override
	public Skill clone(){
		Skill skill = null;
		skill = (Skill)super.clone();
		return skill;
	}
}