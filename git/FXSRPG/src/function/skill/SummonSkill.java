package function.skill;

import java.util.Collection;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import common.SRPGCommons;
import function.controller.state.game.battle.BattleState;
import function.map.Square;
import function.unit.Unit;
import function.unit.UnitStatusType;

/**
 *　このスキルはユニットを召喚するスキルを表しているオブジェクト
 * @author 樹麗
 */
public class SummonSkill extends Skill {
	private final String summon_unit_id;
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
	 * @param summon_unit_id　召喚するユニットのID
	 * @param exp このスキルを所持することで増える必要経験値
	 * @param sound_effect_name　スキルの効果音
	 */
	public SummonSkill(@JsonProperty("id")String id, @JsonProperty("name")String name,
			@JsonProperty("skill_text")String skill_text, @JsonProperty("skill_type")SkillType skill_type,
			@JsonProperty("cost")int cost, @JsonProperty("variable_power")int variable_power,
			@JsonProperty("fixed_power")int fixed_power, @JsonProperty("range_distance")int range_distance,
			@JsonProperty("effect_range")int effect_range, @JsonProperty("target_type")TargetType target_type,
			@JsonProperty("summon_unit_id")String summon_unit_id, @JsonProperty("exp")int exp,
			@JsonProperty("sound_effect_name")String sound_effect_name) {
		super(id, name, skill_text, skill_type, cost, variable_power, fixed_power, range_distance,
				effect_range, target_type, exp, sound_effect_name);
		this.summon_unit_id = summon_unit_id;
	}
	/* (非 Javadoc)
	 * @see function.skill.Skill#action(function.controller.state.game.battle.GameBattleState, function.unit.Unit, java.util.List)
	 * 対象の場所にユニットを召喚する
	 */
	@Override
	public void action(BattleState battle, Unit unit, List<Square> target_list) {
		if(unit.isActed()){ return; }
		if(unit.getStatus(UnitStatusType.MP).getCurrentStatus() < getCost()){ return; }
		if(!this.getTargetType().IsWithinRangeTarget(unit, target_list)){ return; }
		int lv = (int) getSkillType().getPower(unit, this);
		if(lv < 0){ return; }
		Unit summon_unit = SRPGCommons.getUnit(summon_unit_id).clone();
		summon_unit.setIsFriend(unit.isFriend());
		while(summon_unit.getLv() <= lv){
			summon_unit.addCurrentlyExp(summon_unit.getNeedExp(), false);
		}
		for(Square square : target_list){
			int num = 0;
			Collection<Unit> unit_list = battle.getGameMap().getGameMapData().getSameForces(summon_unit);
			while(true){
				String id = summon_unit.getId() + num;
				if(unit_list.stream().filter(e -> e.getId().equals(id)).count() == 0){
					break;
				}
				num++;
			}
			if(square.getUnit() == null){
				battle.getGameMap().setUnit(square, (Unit)summon_unit.copy(summon_unit.getId() + num, summon_unit.getName() + num));
			}
		}
		unit.setIsActed(true);
	}
	/**
	 * @return 召喚するユニットのID
	 */
	public String getSummonUnitId(){
		return this.summon_unit_id;
	}
	/**
	 * @return サモンスキルの基本値を返す
	 */
	public static SummonSkill defaultData() {
		return new SummonSkill("", "", "", SkillType.MATERIAL, 0, 0, 0, 0, 0, TargetType.ENEMY, "", 0, "");
	}
	/* (非 Javadoc)
	 * @see function.GameData#getDefaultData()
	 */
	@Override
	public SummonSkill getDefaultData() {
		return defaultData();
	}

}
