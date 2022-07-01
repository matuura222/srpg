package function.skill;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import function.ActivateType;
import function.Damage;
import function.DamageType;
import function.bgm.SoundEffect;
import function.controller.state.game.battle.BattleState;
import function.map.Square;
import function.unit.Unit;
import function.unit.UnitStatusType;

/**
 *　このクラスは攻撃をするスキルを表しているオブジェクト
 * @author 樹麗
 */
public class AttackSkill extends Skill {
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
	public AttackSkill(@JsonProperty("id")String id, @JsonProperty("name")String name,
			@JsonProperty("skill_text")String skill_text, @JsonProperty("skill_type")SkillType skill_type,
			@JsonProperty("cost")int cost, @JsonProperty("variable_power")int variable_power,
			@JsonProperty("fixed_power")int fixed_power, @JsonProperty("range_distance")int range_distance,
			@JsonProperty("effect_range")int effect_range, @JsonProperty("target_type")TargetType target_type,
			@JsonProperty("exp")int exp, @JsonProperty("sound_effect_name")String sound_effect_name) {
		super(id, name, skill_text, skill_type, cost, variable_power, fixed_power, range_distance,
				effect_range, target_type, exp, sound_effect_name);
	}
	/* (非 Javadoc)
	 * @see function.skill.Skill#action(function.controller.state.game.battle.GameBattleState, function.unit.Unit, java.util.List)
	 * 対象にダメージを与える
	 */
	@Override
	public void action(BattleState battle, Unit unit, List<Square> target_list) {
		if(unit.getStatus(UnitStatusType.MP).getCurrentStatus() < getCost()){ return; }
		if(!this.getTargetType().IsWithinRangeTarget(unit, target_list)){ return; }
		unit.mpCost(getCost());
		unit.currentState(battle, ActivateType.ATTACK);
		SoundEffect.play(this.getSoundEffectName());
		for(Square target : getTargetType().getWithinRangeTarget(unit, target_list)){
			double damage = getSkillType().getPower(unit, this);
			target.getUnit().currentState(battle, ActivateType.DEFENSE);
			target.getUnit().damage(new Damage((int)(damage), DamageType.MATERIAL));
			battle.getGameMap().drawText(target.getX() * battle.getGameMap().getMapChipWidth() + battle.getGameMap().getMapChipWidth() / 2,
					target.getY() * battle.getGameMap().getMapChipWidth() + battle.getGameMap().getMapChipWidth() / 2,
					String.valueOf((int)(damage)));
			target.getUnit().currentState(battle, ActivateType.ALWAYS);
			if(target.getUnit().getStatus(UnitStatusType.HP).getCurrentStatus() <= 0){
				unit.addCurrentlyExp(target.getUnit().getGiveExp(), true);
				battle.deleteUnit(target.getUnit());
			}
		}
		unit.currentState(battle, ActivateType.ALWAYS);
		unit.setIsActed(true);
	}
	/**
	 * @return　攻撃スキルの基本値を返す
	 */
	public static AttackSkill defaultData() {
		return new AttackSkill("", "", "", SkillType.MATERIAL, 0, 0, 0, 0, 0, TargetType.ENEMY, 0, "");
	}
	/* (非 Javadoc)
	 * @see function.GameData#getDefaultData()
	 */
	@Override
	public AttackSkill getDefaultData() {
		return defaultData();
	}
}