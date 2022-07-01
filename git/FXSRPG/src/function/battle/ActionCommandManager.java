package function.battle;

import java.lang.reflect.InvocationTargetException;

import common.SRPGCommons;
import function.controller.state.game.battle.BattleState;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

/**
 * このクラスはアクションコマンドを制御するオブジェクトです。
 * @author 樹麗
 *
 */
public class ActionCommandManager {
	private static ActionCommand action_command = ActionCommand.NONE;
	private static Service<Void> service;
	private static Task<Void> task;
	static {
		service = new Service<Void>(){
			@Override
			protected Task<Void> createTask() {
				Task<Void> t = new Task<Void>(){
					@Override
					protected Void call() throws Exception{
						try {
							SRPGCommons.runAndWait(task);
						} catch (InvocationTargetException e) {
							// TODO 自動生成された catch ブロック
							e.printStackTrace();
						} catch (InterruptedException e) {
							// TODO 自動生成された catch ブロック
							e.printStackTrace();
						}
						return null;
					}
				};
				return t;
			}
		};
	}
	private ActionCommandManager(){}
	/**
	 * @param action_command　実行するアクションコマンド
	 * 渡されたアクションコマンド別スレッドで実行する。
	 * アクションコマンドが実行中の場合実行できない。
	 */
	/**
	 * @param action_command　実行するアクションコマンド
	 * @param battle　バトルステート
	 */
	public static void action(ActionCommand action_command, BattleState battle){
		ActionCommandManager.action_command = action_command;
		task = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				ActionCommandManager.action_command.action(battle, battle.getGameMap().getGameMapData().getSource(),
						battle.getSelectSquare());
				notify();
				return null;
			}
		};
		service.reset();
		service.start();


	}
}
