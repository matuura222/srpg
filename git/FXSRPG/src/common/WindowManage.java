package common;

import javafx.stage.Stage;

/**
 * このクラスはWindowを制御するオブジェクトです。
 * @author 樹麗
 *
 */
public class WindowManage {
	private WindowManage(){}
	/**
	 * @param stage 大きさを変えるwindow
	 * @param width 変更後の幅
	 * @param height 変更後の高さ
	 * 渡されたwindowを指定された大きさに変更します。
	 */
	public static void WindowSizeChage(Stage stage, double width, double height){
		stage.setWidth(width + stage.getWidth() - stage.getScene().getWidth());
		stage.setHeight(height + stage.getHeight() - stage.getScene().getHeight());
	}
}
