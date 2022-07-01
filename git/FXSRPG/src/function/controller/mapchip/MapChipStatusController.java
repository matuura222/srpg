package function.controller.mapchip;

import function.controller.ControllerBase;
import function.map.MapChip;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

/**
 * マップチップのステータスを表示するためのクラス
 * @author 樹麗
 *
 */
public class MapChipStatusController extends ControllerBase {
	public MapChipStatusController(){
		super("mapchip/MapChipStatus");
	}
	/**
	 * 渡されたマップチップを表示する
	 * @param target 対象マップチップ
	 */
	public void detailsShow(MapChip target) {
		((ImageView)getAllNode().get("image")).setImage(target.getImage());
		((Text)getAllNode().get("name")).setText(target.getName());
		((Text)getAllNode().get("move_cost")).setText(String.valueOf(target.getMoveCost()));
		((Text)getAllNode().get("is_movable")).setText(String.valueOf(target.isMovable()));
	}
}
