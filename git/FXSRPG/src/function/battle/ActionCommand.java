package function.battle;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import function.ActivateType;
import function.Damage;
import function.DamageType;
import function.battle.range.AttackRange;
import function.battle.range.EffectRange;
import function.battle.range.Range.RangeValue;
import function.bgm.SoundEffect;
import function.controller.state.game.battle.BattleState;
import function.controller.unit.UnitDetailsController;
import function.map.Square;
import function.skill.Skill;
import function.unit.Unit;
import function.unit.UnitStatusType;

/**
 * この列挙型はゲームの行動を表しているオブジェクトです。
 * @author 樹麗
 *
 */
public enum ActionCommand {
	/**
	 *　このコマンドは何も行動をせず終わります
	 * @author 樹麗
	 */
	NONE(){
		@Override
		public void action(BattleState battle, Square source, Square destination){
			if(source.getUnit().isMoved()){ return; }
			Unit unit = destination.getUnit();
			if(unit == null || !unit.isFriend()){ return; }
			battle.getGameMap().getGameMapData().setSource(destination);
			battle.setSource(destination);
			battle.setSelectUnit(unit);
			battle.getGameMap().getRangeGraphics().clearRect(0, 0, battle.getGameMap().getGameMapData().getMapSizeX() * 40,
					battle.getGameMap().getGameMapData().getMapSizeY() * 40);
			battle.getGameMap().mapChaning();
			battle.setActionCommand(ActionCommand.NONE);
			battle.getGameMap().adjustMap(destination);
		}
	},
	/**
	 * このコマンドは選択されているユニットを行動済みにするコマンドです。
	 * @author 樹麗
	 */
	WAIT(){
		@Override
		public void action(BattleState battle, Square source, Square destination){

			source.getUnit().setIsActed(true);
		}
	},
	/**
	 * このコマンドは選択されているユニットで選択されたSquareにいるユニットに攻撃するコマンドです。
	 * @author 樹麗
	 */
	ATTACK(){
		@Override
		public void action(BattleState battle, Square source, Square destination){
			if(source.getUnit().isActed()){ return; }
			RangeValue value = battle.getRange().getMap().get(destination.getId());
			if(value == null){ battle.setActionCommand(NONE); return; }
			if(destination.getUnit() == null){ battle.setActionCommand(NONE); return; }
			if(source.equals(destination)) { battle.setActionCommand(NONE); return; }
			Unit source_unit = source.getUnit();
			Unit destination_unit = destination.getUnit();
			if(source_unit.isFriend() == destination_unit.isFriend()){ battle.setActionCommand(NONE); return; }
			battle(battle, source, destination);
			if(destination_unit.getStatus(UnitStatusType.HP).getCurrentStatus() > 0){
				AttackRange ar = new AttackRange(battle.getGameMap().getGameMapData());
				ar.startSearch(destination, destination_unit);
				RangeValue v = ar.getMap().get(source.getId());
				if(v != null && v.getCost() >= 0){
					battle(battle, destination, source);
				}
			}
			if(source_unit.getStatus(UnitStatusType.HP).getCurrentStatus() <= 0){
				BattleCommand.NEXT_UNIT.action(source_unit, battle, battle.getGameMap().getGameMapData());
			}
			source_unit.setIsActed(true);
			battle.setActionCommand(NONE);
			battle.getGameMap().clearRangeGraphics();
		}
	},
	/**
	 * このコマンドは選択されているユニットを選択されたSquareに移動するコマンドです。
	 * @author 樹麗
	 */
	MOVE(){
		@Override
		public void action(BattleState battle, Square source, Square destination){
			if(source.getUnit().isMoved() || source.getUnit().isActed()){ return; }
			RangeValue value = battle.getRange().getMap().get(destination.getId());
			if(value == null){ return; }
			if(!value.getTo().canInvade()){ return; }
			if(source.equals(destination)){ return; }
			Unit unit = source.getUnit();
			if(unit.isMoved()){ return; }
			SoundEffect.play("move.mp3");
			try {
				TimeUnit.MILLISECONDS.sleep(250);
			} catch (InterruptedException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
			battle.getGameMap().moveUnit(source, destination);
//			destination.setUnit(source.getUnit());
//			source.setUnit(null);
			unit.setIsMoved(true);
			battle.getGameMap().getGameMapData().setSource(destination);
			battle.setActionCommand(NONE);
			battle.getGameMap().clearRangeGraphics();
		}
	},
	/**
	 * このコマンドは選択されているユニットの選択されたスキルを使用するコマンドです。
	 * @author 樹麗
	 */
	SKILL(){
		@Override
		public void action(BattleState battle, Square source, Square destination){
			Square square = battle.getRange().getMap().get(destination.getId()).getTo();
			if(square == null){ battle.setActionCommand(NONE); return; }
			Skill skill = battle.getSelectSkill();
			EffectRange effect_range = new EffectRange(battle.getGameMap().getGameMapData(), skill);
			effect_range.startSearch(destination, source.getUnit());
			List<Square> list = battle.getGameMap().getGameMapData().getGameMap().stream()
				.map(s -> s.stream()
					.filter(e -> effect_range.getMap().get(e.getId()) != null)
					.collect(Collectors.toList()))
				.flatMap(s -> s.stream())
				.collect(Collectors.toList());
			skill.action(battle, source.getUnit(), list);
			battle.setActionCommand(NONE);
			battle.getGameMap().clearRangeGraphics();
			battle.removeSubWindow();
		}
	},
	/**
	 * このコマンドは選択されたSquareにいるユニットのステータスを確認するコマンドです。
	 * @author 樹麗
	 */
	STATUS(){
		@Override
		public void action(BattleState battle, Square source, Square destination){
			if(destination.getUnit() == null){ return; }
			SoundEffect.play("select.mp3");
			UnitDetailsController status = new UnitDetailsController();
			status.detailsShow(destination.getUnit());
			battle.addSubWindowPane(status);
		}
	};
	/**
	 * @param battle　コマンドが影響を及ぼすオブジェクトです。
	 * @param source　コマンドを使用するユニットがいるSquareです。
	 * @param destination コマンドを使用されるSquareでです。
	 * コマンドを実行する
	 */
	public abstract void action(BattleState battle, Square source, Square destination);
	/**
	 * @param battle 影響を及ぼされるオブジェクトです。
	 * @param source 攻撃側のユニットがいるSquareです。
	 * @param destination 防御側のユニットがいるSquareです。
	 * 選択されているSquareにいるユニット同士で戦闘を行う
	 */
	private static void battle(BattleState battle, Square source, Square destination){
		Unit attack_unit = source.getUnit();
		Unit defense_unit = destination.getUnit();
		attack_unit.currentState(battle, ActivateType.ATTACK);
		Damage damage = new Damage(attack_unit.getStatus(UnitStatusType.ATTACK).getCurrentStatus(), DamageType.MATERIAL);
		defense_unit.currentState(battle, ActivateType.DEFENSE);
		defense_unit.damage(damage);
		battle.getGameMap().drawText(destination.getX() * battle.getGameMap().getMapChipWidth() + battle.getGameMap().getMapChipWidth() / 2,
				destination.getY() * battle.getGameMap().getMapChipHeight() + battle.getGameMap().getMapChipHeight() / 2,
				String.valueOf(damage.getDamage(defense_unit)));
		SoundEffect.play("attack.mp3");
		try {
			TimeUnit.MILLISECONDS.sleep(250);
		} catch (InterruptedException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		if(defense_unit.getStatus(UnitStatusType.HP).getCurrentStatus() <= 0){
			attack_unit.addCurrentlyExp(defense_unit.getGiveExp(), true);
			battle.deleteUnit(defense_unit);
		}
		attack_unit.currentState(battle, ActivateType.ALWAYS);
		defense_unit.currentState(battle, ActivateType.ALWAYS);
	}
}
