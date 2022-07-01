package common;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * このクラスは親を持っているwindowクラスです。
 * @author 樹麗
 *
 */
public class SubWindow extends Stage{
	private Pane pane;
	private boolean is_needing = false;
	/**
	 * @param owner 親Window
	 * @param pane 描画するパネル
	 * @param name windowのタイトル
	 */
	public SubWindow(Stage owner, Pane pane, String name){
		initOwner(owner);
		setScene(new Scene(pane));
		this.setResizable(false);
		this.setTitle(name);
		setX(owner.getX());
		show();
	}
	/**
	 * パネルを更新する
	 */
	public void update(){
		pane.getScene().getRoot();
	}
	/**
	 * @return　必要なWindowかどうかを返す
	 */
	public boolean isNeeding(){
		return this.is_needing;
	}
	/**
	 * @param is_needing 必要なWindowかどうか
	 */
	public void setIsNeeding(boolean is_needing){
		this.is_needing = is_needing;
	}

}
