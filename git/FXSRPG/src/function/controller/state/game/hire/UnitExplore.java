package function.controller.state.game.hire;

import java.io.File;

import common.SRPGCommons;
import function.GameDataContainer;
import function.controller.ControllerBase;
import function.controller.unit.UnitDetailsController;
import function.unit.Unit;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

/**
 * ユニットの探索結果を表示するクラス
 * @author 樹麗
 *
 */
public class UnitExplore extends ControllerBase{
	@FXML
	private UnitDetailsController unit_details;
	@FXML
	private TextField unit_name, unit_id, image_name;
	@FXML
	private Text cost, message;
	private Unit unit;
	private UnitHireState hire;
	private UnitExplore() {
		super("state/game/hire/UnitExplore");
		setId("探索結果");
	}
	public UnitExplore(UnitHireState hire){
		this();
		this.hire = hire;
	}
	/**
	 * 渡されたユニットの表債を表示し、雇用した時にかかる金額を算出する
	 * @param target 対象のユニット
	 */
	public void show(Unit target) {
		unit_details.detailsShow(target);
		unit_name.setText(target.getName());
		int cost = 0;
		cost += target.getBasicMaxHp() * 5;
		cost += target.getBasicMaxMp() * 5;
		cost += target.getBasicAttack() * 20;
		cost += target.getBasicDefense() * 20;
		cost += target.getBasicMagicAttack() * 20;
		cost += target.getBasicMagicDefense() * 20;
		cost += Math.pow(target.getBasicMove(), 2) * 100;
		cost += Math.pow(target.getBasicAttackRange(), 3) * 100;
		cost += target.getJob().getExpRate() * 100;
		cost += target.getRace().getExpRate() * 100;
		for(String id : target.getSkillIdList()){
			cost += SRPGCommons.getSkill(id).getExp() * 100;
		}
		this.cost.setText(String.valueOf(cost));
		this.unit = target;
		unit_id.setText(target.getId());
		image_name.setText(target.getImageName());
	}
	/**
	 * 名前を変更する
	 */
	@FXML
	private void rename(){
		unit.setName(unit_name.getText());
		unit_details.detailsShow(unit);
	}
	/**
	 * 画像を変更する
	 */
	@FXML
	private void changeImage(){
		unit.updateImage(image_name.getText());
		unit_details.detailsShow(unit);
	}
	/**
	 * ユニットを雇用する
	 * 雇用できなかった場合理由を表示する
	 */
	@FXML
	private void unitHire(){
		File file = new File("./data/tmp/my_unit.dat");
		@SuppressWarnings("unchecked")
		GameDataContainer<Unit> container = (GameDataContainer<Unit>) SRPGCommons.getData(file, GameDataContainer.class);
		Unit unit = (Unit) this.unit.copy(unit_id.getText(), this.unit.getName());
		if(container.getData().containsKey(unit.getId())){
			message.setText("同じIDのユニットがいます\nIDを変えてください");
			return;
		}
		if(hire.expenditureMoney(Integer.parseInt(cost.getText()))){
			container.putValue(unit.getId(), unit);
			SRPGCommons.saveData(file, container);
			SRPGCommons.saveData(new File("./data/save/auto/my_unit.dat"), container);
			message.setText("雇用しました");
			hire.closeSubWindow("探索結果");
		}else{
			message.setText("雇用費用が足りません");
		}
	}
}
