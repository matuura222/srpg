package function.controller.state.edit;

import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;

public class NumberTextField extends TextField {
	public NumberTextField(){
		TextFormatter<String> text_formatter = new TextFormatter<>( s -> {
			if(s.getText().matches("[0-9]*")){
				return s;
			}
			return null;
		});
		this.setTextFormatter(text_formatter);
	}
}
