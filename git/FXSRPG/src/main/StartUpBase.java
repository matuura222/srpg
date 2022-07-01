package main;

import java.io.File;

import common.WindowManage;
import function.controller.state.StateBase;
import javafx.animation.AnimationTimer;
import javafx.animation.FadeTransition;
import javafx.animation.SequentialTransition;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * 起動構成の基底クラス
 * @author 樹麗
 */
public abstract class StartUpBase extends Application {
	/**
	 * 親ステージ
	 */
	protected static Stage stage;
	/**
	 * 現在のステート
	 */
	protected static StateBase current;
	private static boolean is_changing_state = false;
	/**
	 * @param base 最初のステート
	 */
	protected StartUpBase(StateBase base){
		if(current != null){ return; }
		current = base;
		current.startState();
	}
	/* (非 Javadoc)
	 * @see javafx.application.Application#start(javafx.stage.Stage)
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		stage = primaryStage;
		Scene scene = new Scene(current);
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.setTitle("SRPG");
		primaryStage.showingProperty().addListener(e -> {//closeが呼びされたとき
			File dir = new File("./data/tmp");
			if(dir.exists()){
				for(File file : dir.listFiles()) {
					file.delete();
				}
				dir.delete();
			}
		});
		AnimationTimer timer = new AnimationTimer(){
			@Override
			public void handle(long now){
				if(is_changing_state){ return; }
				StateBase current = StartUpBase.current.doState();
				if(current != StartUpBase.current){
					changeState(current);
				}
			}
		};
		timer.start();
		primaryStage.show();
	}
	private static void changeState(StateBase state){
		if(is_changing_state){ return; }
		is_changing_state = true;
		current.endState();
		for(String key : current.geetSubWindowName()){
			current.addRemoveSubWindowName(key);
		}
		current.removeSubWindow();
		WindowManage.WindowSizeChage(stage, state.getPrefWidth(), state.getPrefHeight());
		Rectangle rect = new Rectangle(0, 0, stage.getWidth(), stage.getHeight());
		rect.setFill(Color.BLACK);
		rect.setOpacity(0);
		//current.getChildren().set(0, rect);
		current.getChildren().add(rect);
		FadeTransition fade_in = new FadeTransition(Duration.millis(400), rect);
		fade_in.setFromValue(0);
		fade_in.setToValue(1);
		fade_in.setOnFinished(e -> {
			current.getChildren().remove(rect);
			state.getChildren().add(rect);
			stage.getScene().setRoot(state);
			state.startState();
			current = state;
		});

		FadeTransition fade_out = new FadeTransition(Duration.millis(400), rect);
		fade_out.setFromValue(1);
		fade_out.setToValue(0);
		fade_out.setOnFinished(e -> {
			state.getChildren().remove(rect);
			is_changing_state = false;
		});
		SequentialTransition animation = new SequentialTransition(fade_in, fade_out);
		animation.play();
	}
	/**
	 * 最初のステージを返す
	 * @return　stage　親ステージ
	 */
	public static Stage getStage(){ return stage; }
}
