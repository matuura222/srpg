package function.skill;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import common.IgetStateListable;
import function.ActivateType;
import function.controller.state.game.battle.BattleState;
import function.map.Square;
import function.unit.Unit;
import function.unit.UnitState;

/**
 *　このスキルはユニットを支援するスキルを表しているオブジェクト
 * @author 樹麗
 */
public class ConditionSkill extends Skill implements IgetStateListable{
	private List<UnitState> state_list;
	private ActivateType activate_type;
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
	 * @param state_list　スキルの効果一覧
	 * @param activate_type スキルの効果発動タイミング
	 * @param exp このスキルを所持することで増える必要経験値
	 * @param sound_effect_name　スキルの効果音
	 */
	public ConditionSkill(@JsonProperty("id")String id, @JsonProperty("name")String name,
			@JsonProperty("skill_text")String skill_text, @JsonProperty("skill_type")SkillType skill_type,
			@JsonProperty("cost")int cost, @JsonProperty("variable_power")int variable_power,
			@JsonProperty("fixed_power")int fixed_power, @JsonProperty("range_distance")int range_distance,
			@JsonProperty("effect_range")int effect_range, @JsonProperty("target_type")TargetType target_type,
			@JsonProperty("state_list")List<UnitState> state_list, @JsonProperty("activate_type") ActivateType activate_type,
			@JsonProperty("exp")int exp, @JsonProperty("sound_effect_name")String sound_effect_name) {
		super(id, name, skill_text, skill_type, cost, variable_power, fixed_power, range_distance,
				effect_range, target_type, exp, sound_effect_name);
		this.state_list = state_list;
		this.activate_type = activate_type;
	}
	/* (非 Javadoc)
	 * @see function.skill.Skill#action(function.controller.state.game.battle.GameBattleState, function.unit.Unit, java.util.List)
	 */
	@Override
	public void action(BattleState battle, Unit unit, List<Square> target_list) {
		//if(unit.getMp().getCurrentStatus() < getCost()){ return; }
		if(!this.getTargetType().IsWithinRangeTarget(unit, target_list)){ return; }
		List<Square> target = getTargetType().getWithinRangeTarget(unit, target_list);
		for(Square square : target){
			for(UnitState state : state_list){
				square.getUnit().putState(state.clone());
			}
		}
		battle.getGameMap().mapChaning();
	}
	/* (非 Javadoc)
	 * @see common.IgetStateListable#getStateList()
	 */
	public List<UnitState> getStateList(){ return Collections.unmodifiableList(this.state_list); }
	/**
	 * @return　アクティベートタイプを返す
	 */
	public ActivateType getActivateType() { return this.activate_type; }
	/**
	 * @return　条件スキルの基本値を返す
	 */
	public static ConditionSkill defaultData() {
		return new ConditionSkill("", "", "", SkillType.MATERIAL, 0, 0, 0, 0, 0, TargetType.ENEMY, new ArrayList<UnitState>(), ActivateType.ALWAYS, 0, "");
	}
	/* (非 Javadoc)
	 * @see function.GameData#getDefaultData()
	 */
	@Override
	public ConditionSkill getDefaultData() {
		return defaultData();
	}
}
