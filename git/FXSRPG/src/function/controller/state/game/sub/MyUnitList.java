package function.controller.state.game.sub;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Collection;

import common.SRPGCommons;
import function.GameDataContainer;
import function.controller.unit.UnitDetailsController;
import function.unit.Unit;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 * 自身が所有しているユニットを見るためのクラス
 * @author 樹麗
 *
 */
public class MyUnitList extends VBox{
	@FXML
	private Text name;
	@FXML
	private ListView<Unit> list;
	@FXML
	private UnitDetailsController unit_details;

	public MyUnitList(){
		try {
			FXMLLoader loader = new FXMLLoader(new URL(SRPGCommons.getURL() + "/controller/state/game/sub/MyUnitList.fxml"));
			loader.setController(this);
			loader.setRoot(this);
			loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		@SuppressWarnings("unchecked")
		GameDataContainer<Unit> continer = (GameDataContainer<Unit>) SRPGCommons.getData(new File("./data/tmp/my_unit.dat"), GameDataContainer.class);
		list.getItems().addAll(continer.getData().values());
		list.getItems().add(Unit.defaultData());
	}
	/**
	 * 選択されたユニットの詳細を見る
	 */
	@FXML
	private void unitShow(){
		Unit unit = list.getSelectionModel().getSelectedItem();
		if(unit == null || unit.getId().equals("")){ return; }
		unit_details.detailsShow(unit);
	}
	/**
	 * @return 選択されているユニットを返す
	 */
	public Unit getSelectUnit(){
		return this.list.getSelectionModel().getSelectedItem();
	}
	/**
	 * @return 所有しているユニット全てを返す
	 */
	public Collection<Unit> getUnitList(){
		return list.getItems();
	}
	/**
	 * @param unit ユニット
	 * @return 渡されたユニットが現在所有しているか返す
	 */
	public boolean isExistsUnit(Unit unit){ return list.getItems().contains(unit); }
	/**
	 * ユニットを最新の状態にアプデ―トする
	 */
	public void update(){
		Unit unit = list.getSelectionModel().getSelectedItem();
		list.getItems().set(list.getSelectionModel().getSelectedIndex(), unit);
		unit_details.detailsShow(unit);
		list.getSelectionModel().select(unit);
	}
}
