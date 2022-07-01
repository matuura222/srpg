package function.map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import common.IgetImageable;
import common.SRPGCommons;
import function.GameData;
import javafx.scene.image.Image;
/**
 * SquareクラスのMapChipオブジェクトです
 * @author 樹麗
 *
 */
@JsonIgnoreProperties({"image"})

public class MapChip extends GameData implements IgetImageable{
	private int move_cost;
	private boolean is_movable;
	private String img_name;
	private Image img;
	/**
	 * @param id マップチップID
	 * @param chip_name マップチップ名
	 * @param move_cost 消費する移動コスト
	 * @param is_movable 移動できるか
	 * @param img_name マップチップの画像名
	 */
	@JsonCreator
	public MapChip(@JsonProperty("id")String id, @JsonProperty("chip_name")String chip_name,
				@JsonProperty("move_cost")int move_cost, @JsonProperty("movable")boolean is_movable,
				@JsonProperty("image_name")String img_name) {
		super(id, chip_name);
		this.move_cost = move_cost;
		this.is_movable = is_movable;
		this.img_name = img_name;
		this.img = new Image(SRPGCommons.getURL() + "/map/img/" + img_name);
	}
	/**
	 * @return 消費移動力を返す
	 */
	public int getMoveCost() { return this.move_cost; }
	/**
	 * @return 移動できるかを返す
	 */
	public boolean isMovable() { return this.is_movable; }
	/**
	 * @return 画像名を返す
	 */
	public String getImageName(){ return this.img_name; }
	/* (非 Javadoc)
	 * @see common.IgetImageable#getImage()
	 */
	@Override
	public Image getImage() { return this.img; }
	/* (非 Javadoc)
	 * @see function.GameData#clone()
	 */
	@Override
	public MapChip clone(){
		MapChip map_chip = null;
		map_chip = (MapChip)super.clone();
		return map_chip;
	}
	/* (非 Javadoc)
	 * @see function.GameData#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
		return result;
	}
	/* (非 Javadoc)
	 * @see function.GameData#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MapChip other = (MapChip) obj;
		if (getId() == null) {
			if (other.getId() != null)
				return false;
		} else if (!getId().equals(other.getId()))
			return false;
		return true;
	}
	/**
	 * @return mapchipをの基本値を返す
	 */
	public static MapChip defaultData() {
		return new MapChip("", "", 0, false, "");
	}
	/* (非 Javadoc)
	 * @see function.GameData#getDefaultData()
	 */
	public MapChip getDefaultData(){
		return defaultData();
	}
}
