package function.controller.state.edit.map;

import function.controller.ControllerBase;
import function.map.GameMapData;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class GameMapStatusController extends ControllerBase {
	private GameMapData data;
	public GameMapStatusController() {
		super("state/edit/map/MapStatus");
	}
	public void detailsShow(GameMapData target) {
		data = target;
		((TextField)getAllNode().get("id")).setText(target.getId());
		((TextField)getAllNode().get("name")).setText(target.getName());
		((Text)getAllNode().get("sortie_area_num")).setText(String.valueOf(target.getSortieAreaNum()));
		((Text)getAllNode().get("enemy_num")).setText(String.valueOf(target.getEnemyList().size()));
		((Text)getAllNode().get("enemy_lv_num")).setText(String.valueOf(target.getEnemyList().stream()
				.mapToInt(x -> x.getLv())
				.sum()));
		((Text)getAllNode().get("friend_num")).setText(String.valueOf(target.getFriendList().size()));
		((Text)getAllNode().get("friend_lv_num")).setText(String.valueOf(target.getFriendList().stream()
				.mapToInt(x -> x.getLv())
				.sum()));
	}
	public void update(){
		((Text)getAllNode().get("sortie_area_num")).setText(String.valueOf(data.getSortieAreaNum()));
		((Text)getAllNode().get("enemy_num")).setText(String.valueOf(data.getEnemyList().size()));
		((Text)getAllNode().get("enemy_lv_num")).setText(String.valueOf(data.getEnemyList().stream()
				.mapToInt(x -> x.getLv())
				.sum()));
		((Text)getAllNode().get("friend_num")).setText(String.valueOf(data.getFriendList().size()));
		((Text)getAllNode().get("friend_lv_num")).setText(String.valueOf(data.getFriendList().stream()
				.mapToInt(x -> x.getLv())
				.sum()));
	}
	public String getMapName(){ return ((TextField)getAllNode().get("name")).getText(); }
	public String getMapId(){ return ((TextField)getAllNode().get("id")).getText();}
}
