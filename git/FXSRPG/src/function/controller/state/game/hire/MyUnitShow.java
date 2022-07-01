package function.controller.state.game.hire;

import java.io.File;
import java.util.stream.Collectors;

import common.SRPGCommons;
import function.GameDataContainer;
import function.controller.ControllerBase;
import function.controller.state.game.sub.MyUnitList;
import function.unit.Unit;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

/**
 * 現在所有しているユニットの雇用を決めるクラス
 * @author 樹麗
 *
 */
public class MyUnitShow extends ControllerBase{
	@FXML
	private MyUnitList list;
	@FXML
	private TextField name;
	private UnitHireState unit_hire;
	public MyUnitShow(UnitHireState unit_hire){
		super("state/game/hire/MyUnitShow");
		this.unit_hire = unit_hire;
	}
	/**
	 * 選択されているユニットを解雇する
	 */
	@FXML
	private void dismissal(){
		if(list.getSelectUnit() == null || list.getSelectUnit().getId().equals("")){ return; }
		list.getUnitList().remove(list.getSelectUnit());
		GameDataContainer<Unit> container = new GameDataContainer<Unit>(list.getUnitList().stream()
				.filter(s -> !s.getId().equals(""))
				.collect(Collectors.toMap(s -> s.getId(), s -> s)));
		SRPGCommons.saveData(new File("./data/tmp/my_unit.dat"), container);
		SRPGCommons.saveData(new File("./data/save/auto/my_unit.dat"), container);
	}
	/**
	 * 選択されているユニットを訓練する
	 */
	@FXML
	private void training(){
		if(list.getSelectUnit() == null || list.getSelectUnit().getId().equals("")){ return; }
		Unit unit = list.getSelectUnit();
		int cost = (unit.getNeedExp() - unit.getCurrentlyExp()) * 100;
		if(!unit_hire.expenditureMoney(cost)){ return; }
		unit.addCurrentlyExp(cost / 100, false);
		list.update();
		GameDataContainer<Unit> container = new GameDataContainer<Unit>(list.getUnitList().stream()
				.filter(s -> !s.getId().equals(""))
				.collect(Collectors.toMap(s -> s.getId(), s -> s)));
		SRPGCommons.saveData(new File("./data/tmp/my_unit.dat"), container);
		SRPGCommons.saveData(new File("./data/save/auto/my_unit.dat"), container);
	}
	/**
	 * 名前を変更するためのフィールドを出す
	 */
	@FXML
	private void change(){
		name.setVisible(true);
		name.setText(list.getSelectUnit().getName());
	}
	/**
	 * 選択されているユニットの名前を入力された名前に変更する
	 */
	@FXML
	private void rename(){
		name.setVisible(false);
		Unit unit = list.getSelectUnit();
		unit.setName(name.getText());
		list.update();
		GameDataContainer<Unit> container = new GameDataContainer<Unit>(list.getUnitList().stream()
				.filter(u -> !u.getId().equals(""))
				.collect(Collectors.toMap(u -> u.getId(), u -> u)));
		SRPGCommons.saveData(new File("./data/tmp/my_unit.dat"),  container);
		SRPGCommons.saveData(new File("./data/save/auto/my_unit.dat"), container);
	}
}
