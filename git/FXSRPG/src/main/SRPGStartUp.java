package main;


import function.controller.state.game.TitleState;
import javafx.application.Application;

/**
 *　SRPGを起動するためのクラス
 * @author 樹麗
 */
public class SRPGStartUp extends StartUpBase {
	public SRPGStartUp() {
		super(new TitleState());
	}
	public static void main(String[] args){
		Application.launch(args);
	}
}

