package function.map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import common.SRPGCommons;
import function.GameData;
import function.unit.Unit;
/**
 * ゲームマップデータの１マスを表しているオブジェクトです
 * @author 樹麗
 */
@JsonIgnoreProperties({"id" ,"unit_id", "name", "map_chip", "move_cost", "selected"})
public class Square extends GameData{
	private int x, y;
	private boolean is_sortie_area;
	private boolean is_selected;
	private boolean can_invade;
	private MapChip map_chip;
	private Unit unit;
	/**
	 * @param x マップのX座標
	 * @param y マップのY座標
	 * @param map_chip_id MapChipのID
	 * @param unit 配置されているユニット
	 * @param is_sortie_area 初期出撃場所か
	 */
	@JsonCreator
	public Square(@JsonProperty("x")int x, @JsonProperty("y")int y, @JsonProperty("map_chip_id")String map_chip_id,
			@JsonProperty("unit")Unit unit, @JsonProperty("sortie_area")boolean is_sortie_area) {
		super(String.valueOf(x) + " " + String.valueOf(y), String.valueOf(x) + " " + String.valueOf(y));
		this.x = x;
		this.y = y;
		this.map_chip = SRPGCommons.getMapChip(map_chip_id);
		this.unit = unit;
		this.is_sortie_area = is_sortie_area;
	}
	/**
	 * @return　座標Xを返す
	 */
	public int getX() { return this.x; }
	/**
	 * @return 座標Yを返す
	 */
	public int getY() { return this.y; }
	/**
	 * @return 移動コストを返す
	 */
	public int getMoveCost() { return this.getMapChip().getMoveCost(); }
	/**
	 * @return　味方のスクエアかどうか
	 */
	public boolean isSortieArea(){ return this.is_sortie_area; }
	/**
	 * is_sortie_areaを渡された変数に変更する
	 * @param is_sortie_area 味方のエリアか
	 */
	public void setIsSortieArea(boolean is_sortie_area){ this.is_sortie_area = is_sortie_area; }
	/**
	 * @return 選択されているかどうか
	 */
	public boolean isSelected(){ return this.is_selected; }
	/**
	 * 渡された変数にis_selectedを変更する
	 * @param is_selected 選択されているか
	 */
	public void setIsSelected(boolean is_selected){ this.is_selected = is_selected; }
	/**
	 * @return 移動できるどうか
	 */
	public boolean canInvade(){ return this.can_invade; }
	/**
	 * 渡された変数にcan_invadeを変更する
	 * @param can_invade 移動できるかどうか
	 */
	public void setCanInvade(boolean can_invade){ this.can_invade = can_invade; }
	/**
	 * @return スクエアのマップチップIDを返す
	 */
	public String getMapChipId(){ return this.map_chip.getId(); }
	/**
	 * @return スクエアのマップチップを返す
	 */
	public MapChip getMapChip() { return this.map_chip; }
	/**
	 * 渡された変数にマップチップを変更する
	 * @param map_chip マップチップ
	 */
	void setMapChip(MapChip map_chip) { this.map_chip = map_chip; }
	/**
	 * @return スクエアにいるユニットを返す
	 */
	public Unit getUnit() {
		return this.unit;
	}
	/**
	 * @param unit　渡された変数にユニットを変更する
	 */
	void setUnit(Unit unit) {
		this.unit = unit;
	}
	/* (非 Javadoc)
	 * @see function.GameData#clone()
	 */
	@Override
	public Square clone(){
		Square square = null;
		square = (Square)super.clone();
		return square;
	}
	/* (非 Javadoc)
	 * @see function.GameData#getDefaultData()
	 */
	@Override
	public GameData getDefaultData() {
		return new Square(0, 0, "草原", null, false);
	}
}
