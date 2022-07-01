package function.controller.state.game;

import java.io.File;

import common.SRPGCommons;
import function.GameDataContainer;
import function.controller.state.StateBase;
import function.unit.Unit;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;

/**
 * 現在のデータを保存するステートです
 * @author 樹麗
 *
 */
public class GameSaveState extends StateBase {
	@FXML
	private ListView<String> save_data_list;
	@FXML
	private ListView<Unit> unit_list;
	@FXML
	private Text money;
	@FXML
	private TextArea message;
	protected GameSaveState() {
		super("GameSave");
		File dir = new File("./data/save");
		save_data_list.getItems().addAll(dir.list());
		save_data_list.getItems().add("新しいセーブデータ");
	}

	/* (非 Javadoc)
	 * @see function.controller.state.StateBase#startState()
	 */
	@Override
	public void startState() {

	}

	/* (非 Javadoc)
	 * @see function.controller.state.StateBase#drawState()
	 */
	@Override
	protected void drawState() {

	}

	/* (非 Javadoc)
	 * @see function.controller.state.StateBase#decideState()
	 */
	@Override
	protected StateBase decideState() {
		if("Preparation".equals(getStateName())){
			return new PreparationState();
		}
		return this;
	}

	/* (非 Javadoc)
	 * @see function.controller.state.StateBase#endState()
	 */
	@Override
	public void endState() {

	}
	/**
	 * 選択されたセーブデータのデータを見る
	 */
	@FXML
	private void saveDataShow(){
		if("新しいセーブデータ".equals(save_data_list.getSelectionModel().getSelectedItem())){ return; }
		unit_list.getItems().clear();
		@SuppressWarnings("unchecked")
		GameDataContainer<Unit> data = (GameDataContainer<Unit>) SRPGCommons.getData(new File("./data/save/" + save_data_list.getSelectionModel().getSelectedItem() + "/my_unit.dat"), GameDataContainer.class);
		unit_list.getItems().addAll(data.getData().values());
		money.setText(String.valueOf(SRPGCommons.getData(new File("./data/save/" + save_data_list.getSelectionModel().getSelectedItem()  + "/resource.dat"), Integer.class)));

	}
	/**
	 * 選択されている場所に現在のデータを保存する
	 */
	@FXML
	private void save(){
		if(save_data_list.getSelectionModel().getSelectedItem() == null) {
			message.setText("ファイルを選択してください");
			return;
		}
		if("auto".equals(save_data_list.getSelectionModel().getSelectedItem())){
			message.setText("autoにセーブはできません");
			return;
		}
		if("新しいセーブデータ".equals(save_data_list.getSelectionModel().getSelectedItem())){
			int i = save_data_list.getItems().size() - 1;

			File file = new File("./data/save/save" + i);
			if(file.mkdir()){
				save_data_list.getItems().add(i, "save" + i);
				save_data_list.getSelectionModel().select(i);
			}else{
				message.setText("セーブに失敗しました");
				return;
			}
		}
		File my_unit_file = new File("./data/save/" + save_data_list.getSelectionModel().getSelectedItem() + "/my_unit.dat");
		SRPGCommons.saveData(my_unit_file, SRPGCommons.getData(new File("./data/save/auto/my_unit.dat"), GameDataContainer.class));
		File resource_file = new File("./data/save/" + save_data_list.getSelectionModel().getSelectedItem() + "/resource.dat");
		SRPGCommons.saveData(resource_file, SRPGCommons.getData(new File("./data/save/auto/resource.dat"), Integer.class));
		message.setText("セーブしました");
		saveDataShow();
	}
}
