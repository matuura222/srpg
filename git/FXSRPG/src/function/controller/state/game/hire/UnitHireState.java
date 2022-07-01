package function.controller.state.game.hire;

import java.io.File;

import common.SRPGCommons;
import function.GameDataContainer;
import function.controller.state.StateBase;
import function.controller.state.edit.skill.HaveSkillController;
import function.unit.Job;
import function.unit.Race;
import function.unit.ThoughtType;
import function.unit.Unit;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

/**
 * ユニットの雇用を決めるステート
 * @author 樹麗
 *
 */
public class UnitHireState extends StateBase {
	 @FXML
	 private Text money, hire_cost, search_cost, message;
	 @FXML
	 private ChoiceBox<Job> job_box;
	 @FXML
	 private ChoiceBox<Race> race_box;
	 @FXML
	 private HaveSkillController<Unit> have_skill;
	 public UnitHireState(){
		super("hire/UnitHire");
		job_box.getItems().addAll(SRPGCommons.getJobList());
		job_box.getItems().remove(SRPGCommons.getJob(""));
		job_box.getSelectionModel().select(0);
		race_box.getItems().addAll(SRPGCommons.getRaceList());
		race_box.getItems().remove(SRPGCommons.getRace(""));
		race_box.getSelectionModel().select(0);
		search_cost.setText(String.valueOf(getSearchCost()));
		hire_cost.setText(String.valueOf(getHireCost()));
		job_box.setOnAction(e -> {
			search_cost.setText(String.valueOf(getSearchCost()));
			hire_cost.setText(String.valueOf(getHireCost()));
		});
		race_box.setOnAction(e -> {
			search_cost.setText(String.valueOf(getSearchCost()));
			hire_cost.setText(String.valueOf(getHireCost()));
		});
		have_skill.setOnSkillShow(e ->{
			search_cost.setText(String.valueOf(getSearchCost()));
			hire_cost.setText(String.valueOf(getHireCost()));
		});
		File file = new File("./data/tmp/resource.dat");
		this.money.setText(String.valueOf(SRPGCommons.getData(file, Integer.class)));

	}
	 /* (非 Javadoc)
	 * @see function.controller.state.StateBase#startState()
	 */
	@Override
	public void startState() {
		// TODO 自動生成されたメソッド・スタブ

	}
	/* (非 Javadoc)
	 * @see function.controller.state.StateBase#drawState()
	 */
	@Override
	public void drawState() {

	}
	/* (非 Javadoc)
	 * @see function.controller.state.StateBase#decideState()
	 */
	@Override
	protected StateBase decideState() {
		return this;
	}
	/* (非 Javadoc)
	 * @see function.controller.state.StateBase#endState()
	 */
	@Override
	public void endState(){

	}
	/**
	 * 現在所有しているユニットを見る
	 */
	@FXML
	private void myUnitShow() {
		MyUnitShow my_unit = new MyUnitShow(this);
		addSubWindowPane(my_unit);
	}
	/**
	 * 指定された条件にあったユニットを生成し表示する
	 */
	@FXML
	private void explore() {
		int m = Integer.parseInt(money.getText());
		int s_cost = Integer.parseInt(search_cost.getText());
		if(m < s_cost){ message.setText("探索費用がありません"); return; }
		File file = new File("./data/tmp/my_unit.dat");
		@SuppressWarnings("unchecked")
		GameDataContainer<Unit> container = (GameDataContainer<Unit>) SRPGCommons.getData(file, GameDataContainer.class);
		int num = container.getData().values().size();
		Unit unit = new Unit("ユニット" + num,  "ユニット" + num, 1, 0,job_box.getSelectionModel().getSelectedItem().getId(),
				race_box.getSelectionModel().getSelectedItem().getId(), have_skill.getHaveSkillIdList(),
				(int) (Math.random() * ((int)((Slider)getAllNode().get("max_hp")).getValue() - (int)((Slider)getAllNode().get("min_hp")).getValue()) + (int)((Slider)getAllNode().get("min_hp")).getValue()),
				(int) (Math.random() * ((int)((Slider)getAllNode().get("max_mp")).getValue() - (int)((Slider)getAllNode().get("min_mp")).getValue()) + (int)((Slider)getAllNode().get("min_mp")).getValue()),
				(int) (Math.random() * ((int)((Slider)getAllNode().get("max_attack")).getValue() - (int)((Slider)getAllNode().get("min_attack")).getValue()) + (int)((Slider)getAllNode().get("min_attack")).getValue()),
				(int) (Math.random() * ((int)((Slider)getAllNode().get("max_defense")).getValue() - (int)((Slider)getAllNode().get("min_defense")).getValue()) + (int)((Slider)getAllNode().get("min_defense")).getValue()),
				(int) (Math.random() * ((int)((Slider)getAllNode().get("max_magic_attack")).getValue() - (int)((Slider)getAllNode().get("min_magic_attack")).getValue()) + (int)((Slider)getAllNode().get("min_magic_attack")).getValue()),
				(int) (Math.random() * ((int)((Slider)getAllNode().get("max_magic_defense")).getValue() - (int)((Slider)getAllNode().get("min_magic_defense")).getValue()) + (int)((Slider)getAllNode().get("min_magic_defense")).getValue()),
				(int) (Math.random() * ((int)((Slider)getAllNode().get("max_move")).getValue() - (int)((Slider)getAllNode().get("min_move")).getValue()) + (int)((Slider)getAllNode().get("min_move")).getValue()),
				(int) (Math.random() * ((int)((Slider)getAllNode().get("max_attack_range")).getValue() - (int)((Slider)getAllNode().get("min_attack_range")).getValue()) + (int)((Slider)getAllNode().get("min_attack_range")).getValue()),
				ThoughtType.CHARGE, "senshi.png");

		while(container.getValue(unit.getId()) != null){
			num++;
			unit = (Unit) unit.copy("ユニット" + num, "ユニット" + num);
		}
		UnitExplore explore = new UnitExplore(this);
		explore.show(unit);
		addSubWindowPane(explore);
		money.setText(String.valueOf(m - s_cost));
		SRPGCommons.saveData(new File("./data/tmp/resource.dat"), money.getText());
	}
	/**
	 * 条件の設定
	 * @param event イベント
	 */
	@FXML
	private void valueChange(Event event) {
		valueChange((Slider)event.getSource());
	}
	/**
	 * 条件の設定
	 * @param e イベント
	 */
	@FXML
	private void inputValue(ActionEvent e){
		TextField text = (TextField) e.getSource();
		int value;
		//_valueを必ずつけているため
		Slider slider = (Slider) getAllNode().get(text.getId().substring(0, text.getId().length() - 6));
		try{
			value = Integer.parseInt(text.getText());
		}catch(NumberFormatException ex){
			//オーバーフローの場合対象の最大値に設定
			value = (int) slider.getMax();
		}
		if(slider.getMax() < value){ value = (int) slider.getMax(); }
		slider.setValue(value);
		valueChange(slider);
	}
	/**
	 * 最低値が最大値を上回らない、または最大値が最低値を下回らないように調整する
	 * @param slider
	 */
	private void valueChange(Slider slider){
		((TextField)getAllNode().get(slider.getId() + "_value")).setText(String.valueOf(Math.round(slider.getValue())));
		if(slider.getId().startsWith("min")){
			//minまたはmaxが先頭に必ずついてるため
			Slider s = ((Slider)getAllNode().get("max" + slider.getId().substring(3)));
			if(slider.getValue() > s.getValue()){
				s.setValue(Math.round(slider.getValue()));
				((TextField)getAllNode().get(s.getId() + "_value")).setText(String.valueOf(Math.round(s.getValue())));
			}
		}else{
			Slider s = ((Slider)getAllNode().get("min" + slider.getId().substring(3)));
			if(slider.getValue() < s.getValue()){
				s.setValue(Math.round(slider.getValue()));
				((TextField)getAllNode().get(s.getId() + "_value")).setText(String.valueOf(Math.round(s.getValue())));
			}
		}
		search_cost.setText(String.valueOf(getSearchCost()));
		hire_cost.setText(String.valueOf(getHireCost()));
	}
	/**
	 * 探索に必要な金額を算出して返す
	 * @return　探すのに必要な金額
	 */
	private int getSearchCost(){
		int cost = 0;
		cost +=  ((Slider)getAllNode().get("min_hp")).getValue() * ((Slider)getAllNode().get("max_hp")).getValue();
		cost += (((Slider)getAllNode().get("min_mp")).getValue() + 1) * ((Slider)getAllNode().get("max_mp")).getValue();
		cost += (((Slider)getAllNode().get("min_attack")).getValue() + 1) * ((Slider)getAllNode().get("max_attack")).getValue();
		cost += (((Slider)getAllNode().get("min_defense")).getValue() + 1) * ((Slider)getAllNode().get("max_defense")).getValue();
		cost += (((Slider)getAllNode().get("min_magic_attack")).getValue() + 1) * ((Slider)getAllNode().get("max_magic_attack")).getValue();
		cost += (((Slider)getAllNode().get("min_magic_defense")).getValue() + 1) * ((Slider)getAllNode().get("max_magic_defense")).getValue();
		cost += (((Slider)getAllNode().get("min_move")).getValue() + 1) * Math.pow(((Slider)getAllNode().get("max_move")).getValue(), 2) * 100;
		cost += (((Slider)getAllNode().get("min_attack_range")).getValue() + 1) * Math.pow(((Slider)getAllNode().get("max_attack_range")).getValue(), 3) * 100;
		cost += job_box.getSelectionModel().getSelectedItem().getExpRate() * 100;
		cost += race_box.getSelectionModel().getSelectedItem().getExpRate() * 100;
		for(String id : have_skill.getHaveSkillIdList()){
			cost += SRPGCommons.getSkill(id).getExp() * 100;
		}
		return cost;
	}
	/**
	 * 雇うのに必要な最高金額を算出して返す
	 * @return 雇うのに必要な金額
	 */
	private int getHireCost(){
		int cost = 0;
		cost += ((Slider)getAllNode().get("max_hp")).getValue() * 5;
		cost += ((Slider)getAllNode().get("max_mp")).getValue() * 5;
		cost += ((Slider)getAllNode().get("max_attack")).getValue() * 20;
		cost += ((Slider)getAllNode().get("max_defense")).getValue() * 20;
		cost += ((Slider)getAllNode().get("max_magic_attack")).getValue() * 20;
		cost += ((Slider)getAllNode().get("max_magic_defense")).getValue() * 20;
		cost += Math.pow(((Slider)getAllNode().get("max_move")).getValue(), 2) * 100;
		cost +=  Math.pow(((Slider)getAllNode().get("max_attack_range")).getValue(), 3) * 100;
		cost += job_box.getSelectionModel().getSelectedItem().getExpRate() * 100;
		cost += race_box.getSelectionModel().getSelectedItem().getExpRate() * 100;
		for(String id : have_skill.getHaveSkillIdList()){
			cost += SRPGCommons.getSkill(id).getExp() * 100;
		}
		return cost;
	}
	/**
	 * 渡された金額分所有している金を引き、引けたかを返す
	 * @param cost　金額
	 * @return　支払えたかどうか
	 */
	public boolean expenditureMoney(int cost){
		int money = Integer.parseInt(this.money.getText());
		if(money < cost){
			return false;
		}
		money -= cost;
		this.money.setText(String.valueOf(money));
		SRPGCommons.saveData(new File("./data/tmp/resource.dat"), money);
		SRPGCommons.saveData(new File("./data/save/auto/resource.dat"), money);
		return true;
	}
}
