package main;

import function.controller.state.edit.EditTitleState;
import javafx.application.Application;

/**
 *　SRPGエディタを起動するためのクラス
 * @author 樹麗
 */
public class SRPGEditorStartUp extends StartUpBase {
	public SRPGEditorStartUp() {
		super(new EditTitleState());
	}
	public static void main(String[] args){
		Application.launch(args);
	}

}
