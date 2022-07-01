package function.controller.state.edit.mapchip;

import common.SRPGCommons;
import function.controller.state.edit.EditorControllerBase;
import function.controller.state.edit.SRPGEditorState;
import function.map.MapChip;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class MapChipEditController extends EditorControllerBase<MapChip> {
	@FXML
    private TextField map_chip_name, map_chip_id, image_name;
	@FXML
	private ImageView image;
	@FXML
	private MapChipStatusEditController status;
	SRPGEditorState editor;
	MapChip map_chip;
	public MapChipEditController(SRPGEditorState editor){
		super("mapchip/MapChipEdit");
		this.editor = editor;
	}
	@FXML
	private void updateImage(){
		image.setImage(new Image(SRPGCommons.getURL() + "/map/img/" + image_name.getText()));
		if(image.getImage().isError()){
			image.setImage(new Image(SRPGCommons.getURL() + "/map/img/NotImage.png"));
		}
	}
	@FXML
	private void save(ActionEvent e){
		MapChip map_chip = new MapChip(map_chip_id.getText(), map_chip_name.getText(),
				status.getMoveCost(), status.iisMovable(), image_name.getText());
		if((SRPGCommons.getMapChipList().contains(this.map_chip) == false && SRPGCommons.getMapChipList().contains(map_chip))
				|| (this.map_chip.equals(map_chip) == false)){
			editor.setMessage("識別IDが重複しています。\n別のIDにしてください");
			return;
		}
		editor.setMessage("マップチップ：" + map_chip.getName() + "をセーブしました");
		editor.mapChipUpdate(this.map_chip, map_chip);
		this.map_chip = map_chip;
	}
	@Override
	public void edit(MapChip map_chip){
		map_chip_name.setText(map_chip.getName());
		map_chip_id.setText(map_chip.getId());
		image_name.setText(map_chip.getImageName());
		updateImage();
		status.edit(map_chip);
		this.map_chip = map_chip;
	}
}
