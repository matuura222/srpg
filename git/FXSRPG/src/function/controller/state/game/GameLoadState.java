package function.controller.state.game;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import common.SRPGCommons;
import function.GameDataContainer;
import function.controller.state.StateBase;
import function.unit.Unit;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;

/**
 * ゲームデータをロードするステート
 * @author 樹麗
 *
 */
public class GameLoadState extends StateBase {
	@FXML
	private ListView<String> save_data_list;
	@FXML
	private ListView<Unit> unit_list;
	@FXML
	private Text money;
	@FXML
	private TextArea message;
	private StateBase state;
	protected GameLoadState(StateBase state) {
		super("GameLoad");
		File dir = new File("./data/save");
		save_data_list.getItems().addAll(dir.list());
		this.state = state;
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
		if("back".equals(getStateName())){
			this.state.setStateName("");
			return this.state;
		}else if("Preparation".equals(getStateName())){
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
	 * 選択されたデータを見る
	 */
	@FXML
	private void saveDataShow(){
		unit_list.getItems().clear();
		String dir_name = save_data_list.getSelectionModel().getSelectedItem();
		@SuppressWarnings("unchecked")
		GameDataContainer<Unit> data = (GameDataContainer<Unit>) SRPGCommons.getData(
				new File("./data/save/" + dir_name + "/my_unit.dat"), GameDataContainer.class
			);
		unit_list.getItems().addAll(data.getData().values());
		money.setText(String.valueOf(SRPGCommons.getData(
				new File("./data/save/" + dir_name + "/resource.dat"), Integer.class)));
	}
	/**
	 * 選択されたデータを読み込む
	 */
	@FXML
	private void load(){
		if(save_data_list.getSelectionModel().getSelectedItem() == null) {
			message.setText("ファイルを選択してください");
			return;
		}
		String dir_name = save_data_list.getSelectionModel().getSelectedItem();
		SRPGCommons.createTmpFile();
		String path = "./data/save/" + dir_name;
		try {
			Files.copy(Paths.get(path + "/resource.dat"), Paths.get("./data/tmp/resource.dat"), StandardCopyOption.REPLACE_EXISTING);
			Files.copy(Paths.get(path + "/my_unit.dat"), Paths.get("./data/tmp/my_unit.dat"), StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		setStateName("Preparation");
	}
}
