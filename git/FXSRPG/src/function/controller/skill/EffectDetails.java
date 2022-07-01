package function.controller.skill;

import java.util.List;

import common.IgetStateListable;
import function.controller.ControllerBase;
import function.unit.UnitState;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

/**
 * 効果詳細を見るためのクラス
 * @author 樹麗
 *
 */
public class EffectDetails extends ControllerBase{
	@FXML
	private ListView<UnitState> state_list;
	public EffectDetails(){
		super("skill/EffectDetails");
	}
	/**
	 * 選択されているステートを表示する
	 * @param e マウスイベント
	 */
	@FXML
	private void stateShow(MouseEvent e) {
		UnitState state = state_list.getSelectionModel().getSelectedItem();
		if(state == null) { return; }
		((Text)getAllNode().get("state_name")).setText(state.getName());
		((Text)getAllNode().get("activate_type")).setText(state.getActivateType().getName());
		((Text)getAllNode().get("status_type")).setText(state.getStatusType().getName());
		((Text)getAllNode().get("effect")).setText(state.getVariableAmount() + "% +" + state.getFixedAmount());
		((Text)getAllNode().get("effect_time")).setText(String.valueOf(state.getEffectTime()));
	}
	/**
	 * 渡されたlistの詳細を見る
	 * @param list 対象
	 */
	public void detailsShow(IgetStateListable list) {
		if(!(list instanceof IgetStateListable)) { return; }
		List<UnitState> state_list = ((IgetStateListable) list).getStateList();
		this.state_list.getItems().addAll(state_list);
	}
}
