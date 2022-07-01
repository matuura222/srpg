package function.controller.state.edit.mapchip;

import function.controller.state.edit.EditorControllerBase;
import function.map.MapChip;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Slider;
import javafx.scene.text.Text;

public class MapChipStatusEditController extends EditorControllerBase<MapChip>{
	public MapChipStatusEditController(){
		super("mapchip/MapChipStatusEdit");
	}
	@Override
	public void edit(MapChip map_chip){
		((Slider)getAllNode().get("move_cost")).setValue(map_chip.getMoveCost());
		((CheckBox)getAllNode().get("is_movable")).setSelected(map_chip.isMovable());;
		((Text)getAllNode().get("move_cost_value")).setText(String.valueOf(map_chip.getMoveCost()));
	}
	@FXML
	private void valueChange(Event e){
	    Slider s = (Slider)e.getSource();
		((Text)getAllNode().get(s.getId() + "_value")).setText(String.valueOf(Math.round(s.getValue())));
	}
	public int getMoveCost(){ return (int)((Slider)getAllNode().get("move_cost")).getValue(); }
	public boolean iisMovable(){ return ((CheckBox)getAllNode().get("is_movable")).isSelected(); }
}
