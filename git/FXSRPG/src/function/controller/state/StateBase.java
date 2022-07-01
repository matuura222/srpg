package function.controller.state;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import common.SubWindow;
import function.bgm.SoundEffect;
import function.controller.ControllerBase;
import javafx.application.Platform;
import javafx.css.Styleable;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import main.StartUpBase;

/**
 * ゲームステートの基底クラス
 * @author 樹麗
 *
 */
public abstract class StateBase extends ControllerBase {
//	private Map<String, Node> all_node = new HashMap<String, Node>();
	private String state_name;
	private static Map<String, SubWindow> name_to_sub_window = new HashMap<String, SubWindow>();
	private static List<Pane> sub_window_pane = new ArrayList<Pane>();
	private static List<String> remove_window_name_list = new ArrayList<String>();
	/**
	 * @param name　ステート名
	 */
	protected StateBase(String name){
		super("state/game/" + name);
	}
	/**
	 * ステートの処理行い返す
	 * @return　各ステートの条件に従いステートを返す
	 */
	public StateBase doState(){
		drawState();
		if(sub_window_pane.size() > 0){
			for(Pane pane : sub_window_pane){
				if(name_to_sub_window.containsKey(pane.getId())){
					Platform.runLater(() ->{
						name_to_sub_window.get(pane.getId()).close();
						name_to_sub_window.remove(pane.getId());
					});
				}
				Platform.runLater(() ->{
					SubWindow sub = new SubWindow(StartUpBase.getStage(), pane, pane.getId());
					name_to_sub_window.put(sub.getTitle(), sub);
				});
			}
			sub_window_pane.clear();
		}
		for(SubWindow sub : name_to_sub_window.values()){
			if(sub.isShowing()){
				//sub.update();
				continue;
			}else if(!sub.isNeeding()){
				sub.close();
			}
		}
		return decideState();
	}
	/**
	 * 最初だけ必要な処理を実行する
	 */
	public abstract void startState();
	/**
	 * ループ処理
	 */
	protected abstract void drawState();
	/**
	 * @return 条件に従いステートを返す
	 */
	protected abstract StateBase decideState();
	/**
	 * ステート終了時に実行
	 */
	public abstract void endState();
	/**
	 * ステート変更時の処理
	 * @param event イベント
	 */
	@FXML
	protected void changeScene(Event event){
		SoundEffect.play("select.mp3");
		state_name = ((Styleable)event.getSource()).getId();
	}
	/**
	 * ゲーム終了時の処理
	 */
	@FXML
	private void exit(){
		Platform.exit();
	}
	/**
	 * サブWindowを閉じる
	 */
	public void removeSubWindow(){
		for(String s : remove_window_name_list){
			name_to_sub_window.get(s).close();
			name_to_sub_window.remove(s);
		}
		remove_window_name_list.clear();
	}
	/**
	 * 渡されたパネルを表示するためのウィンドウを追加
	 * @param pane パネル
	 */
	public void addSubWindowPane(Pane pane){
		sub_window_pane.add(pane);
	}
	/**
	 * 削除するWindow名を追加する
	 * @param name Window名
	 */
	public void addRemoveSubWindowName(String name){
		remove_window_name_list.add(name);
	}
	/**
	 * @return 現在のSubWindow全ての名前を返す
	 */
	public Collection<String> geetSubWindowName(){ return name_to_sub_window.keySet(); }
	/**
	 * @param name Window名
	 * @return 指定されたWindowを返す
	 */
	public SubWindow getSubWindow(String name){
		return name_to_sub_window.get(name);
	}
	public void closeSubWindow(String name){
		name_to_sub_window.get(name).close();
	}
	/**
	 * @return 現在のステート名を返す
	 */
	protected String getStateName(){ return state_name; }
	/**
	 * ステート名を変更し、条件に従いステートを変更する
	 * @param state_name ステート名
	 */
	public void setStateName(String state_name){ this.state_name = state_name; }
}
