package function.controller.unit;

import function.controller.ControllerBase;
import function.unit.Unit;
import function.unit.UnitStatusType;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

/**
 * ユニットステータスを表示するためのクラス
 * @author 樹麗
 *
 */
public class UnitStatus extends ControllerBase{

	public UnitStatus() {
		super("unit/UnitStatus");
		// TODO 自動生成されたコンストラクター・スタブ
	}

	/**
	 * 渡されたユニットのステータスを表示する
	 * @param target　表示するユニット
	 */
	public void detailsShow(Unit target) {
		((ImageView)getAllNode().get("image")).setImage(target.getImage());
		((Text)getAllNode().get("name")).setText(target.getName());
		((Text)getAllNode().get("lv")).setText(String.valueOf(target.getLv()));
		((Text)getAllNode().get("exp")).setText(String.valueOf(target.getCurrentlyExp()) + " / " + String.valueOf(target.getNeedExp()));
		((Text)getAllNode().get("race")).setText(target.getRace().getName());
		((Text)getAllNode().get("job")).setText(target.getJob().getName());
		((Text)getAllNode().get("hp")).setText(String.valueOf(target.getStatus(UnitStatusType.HP).getCurrentStatus()) +
				" / " + String.valueOf(target.getStatus(UnitStatusType.MAX_HP).getCurrentStatus()));
		((Text)getAllNode().get("mp")).setText(String.valueOf(target.getStatus(UnitStatusType.MP).getCurrentStatus()) +
				" / " + String.valueOf(target.getStatus(UnitStatusType.MAX_MP).getCurrentStatus()));
		((Text)getAllNode().get("attack")).setText(String.valueOf(target.getStatus(UnitStatusType.ATTACK).getCurrentStatus()));
		((Text)getAllNode().get("magic_attack")).setText(String.valueOf(target.getStatus(UnitStatusType.MAGIC_ATTACK).getCurrentStatus()));
		((Text)getAllNode().get("defense")).setText(String.valueOf(target.getStatus(UnitStatusType.DEFENSE).getCurrentStatus()));
		((Text)getAllNode().get("magic_defense")).setText(String.valueOf(target.getStatus(UnitStatusType.MAGIC_DEFENSE).getCurrentStatus()));
		((Text)getAllNode().get("move")).setText(String.valueOf(target.getStatus(UnitStatusType.MOVE).getCurrentStatus()));
		((Text)getAllNode().get("attack_range")).setText(String.valueOf(target.getStatus(UnitStatusType.ATTACK_RANGE).getCurrentStatus()));
	}
}
