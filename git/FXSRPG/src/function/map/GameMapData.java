package function.map;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import function.GameData;
import function.unit.Unit;
import function.unit.UnitStatusType;
/**
 * ゲームマップのデータ
 * @author 樹麗
 */
@JsonIgnoreProperties({"enemy_list, friend_list, sortie_area_num", "map_size_x", "map_size_y", "source"})
@JsonTypeName("GameMapData")
public class GameMapData extends GameData{
	private List<List<Square>> map_data;
	private List<Unit> enemy_list;
	private List<Unit> friend_list;
	private List<Unit> unit_list;
	private int sortie_area_num;
	private Square source;
	/**
	 * @param map_id マップID
	 * @param map_name　マップ名
	 * @param map_data マップデータ
	 */
	@JsonCreator
	public GameMapData(@JsonProperty("map_id")String map_id, @JsonProperty("map_name")String map_name,
			@JsonProperty("game_map")List<List<Square>> map_data){
		super(map_id, map_name);
		this.map_data = map_data;
		friend_list = new ArrayList<Unit>();
		enemy_list = new ArrayList<Unit>();
		unit_list = new ArrayList<Unit>();
		for(List<Square> square_line : map_data){
			for (Square square : square_line) {
				if(square.isSortieArea()){ sortie_area_num++; }
				if(square.getUnit() != null){
					if(square.isSortieArea()){
						unit_list.add(square.getUnit());
						friend_list.add(square.getUnit());
					}else{
						enemy_list.add(square.getUnit());
						unit_list.add(square.getUnit());
					}
				}
			}
		}
		friend_list.sort((e1, e2) -> { return e1.getName().compareTo(e2.getName()); });
		enemy_list.sort((e1, e2) -> { return e1.getName().compareTo(e2.getName()); });
	}
	/**
	 * @return　味方のスクエアの数を返す
	 */
	public int getSortieAreaNum(){ return this.sortie_area_num; }
	/**
	 * @param square　対象スクエア
	 * 味方のスクエアを増やす
	 * 対象スクエアにユニットがいた場合味方のユニットにする
	 */
	void sortieAreaNumUp(Square square){
		this.sortie_area_num++;
		if(square.getUnit() != null){
			square.getUnit().setIsFriend(true);
			getEnemyList().remove(square.getUnit());
			getFriendList().add(square.getUnit());
		}
	}
	/**
	 * @param square　対象スクエア
	 * 味方のスクエアを減らす
	 * 対象スクエアにユニットがいた場合敵のユニットにする
	 */
	void sortieAreaNumDown(Square square){
		this.sortie_area_num--;
		if(square.getUnit() != null){
			square.getUnit().setIsFriend(false);
			getFriendList().remove(square.getUnit());
			getEnemyList().add(square.getUnit());
		}
	}
	/**
	 * @return 選択されているスクエアを返す
	 */
	public Square getSource(){ return this.source; }
	/**
	 * @param source 選択されたスクエア
	 */
	public void setSource(Square source){ this.source = source; }
	/**
	 * @param x マップの座標X
	 * @param y マップの座標Y
	 * @return 指定された座標のSquare
	 */
	public Square getSquare(int x, int y){
		if(x >= map_data.size()){ x = 24; }
		if(x < 0){ x = 0; }
		if(y >= map_data.size()){ y = 24; }
		if(y < 0){ y = 0; }
		return map_data.get(x).get(y);
	}
	/**
	 * @return　マップの幅
	 */
	public int getMapSizeX(){ return map_data.size(); }
	/**
	 * @return マップの高さ
	 */
	public int getMapSizeY(){ return map_data.get(0).size(); }
	/**
	 * @param s1 Square1
	 * @param s2 Square2
	 * @return s1とs2のマップ間の距離
	 */
	public int getSquareDist(Square s1, Square s2){
		return Math.abs(s1.getX() - s2.getX()) + Math.abs(s1.getY() - s2.getY());
	}
	/**
	 * @param unit　ユニット
	 * @return 渡されたユニットがいるSquare
	 */
	public Square getUnitLocate(Unit unit){
		return map_data.stream()
					.map(e -> e.stream()
						.filter(s -> unit.equals(s.getUnit()))
						.collect(Collectors.toList()))
					.flatMap(e -> e.stream())
					.findFirst().orElseGet(() -> null);
	}
	/**
	 * @param unit_list ユニットリスト
	 * @return　 渡されたCollectionのユニットがすべて行動済みの場合true
	 */
	public boolean isAllActed(Collection<Unit> unit_list) {
		for(Unit unit : unit_list){
			if(!unit.isActed()){
				return false;
			}
		}
		return true;
	}
	/**
	 * @param unit_list ユニットリスト
	 * @return 渡されたCollectionのユニットが全て死亡済みの場合
	 */
	public boolean isAllDied(Collection<Unit> unit_list){
		for(Unit unit : unit_list){
			if(unit.getStatus(UnitStatusType.HP).getCurrentStatus() > 0){
				return false;
			}
		}
		return true;
	}
	/**
	 * @return　ゲームマップを返す
	 */
	public Collection<List<Square>> getGameMap(){ return this.map_data; }
	/**
	 * @return
	 */
	/**
	 * @return 敵ユニットのリストを返す
	 */
	@JsonIgnore
	public Collection<Unit> getEnemyList(){ return enemy_list; }
	/**
	 * @return 味方ユニットのリストを返す
	 */
	@JsonIgnore
	public Collection<Unit> getFriendList(){ return friend_list; }
	/**
	 * @return ゲームマップ全てのユニットを返す
	 */
	@JsonIgnore
	public Collection<Unit> getUnitList() { return unit_list; }
	/**
	 * @param unit ユニット
	 * @return 渡されたのユニットと敵対しているユニットリストを返す
	 */
	public Collection<Unit> getHostileForces(Unit unit){
		if(unit.isFriend()){
			return enemy_list;
		}
		return friend_list;
	}
	/**
	 * @param unit　ユニット
	 * @return 渡されたユニットと同じ所属のユニットリストを返す
	 */
	public Collection<Unit> getSameForces(Unit unit){
		if(unit.isFriend()){
			return friend_list;
		}
		return enemy_list;
	}
	/* (非 Javadoc)
	 * @see function.GameData#clone()
	 */
	@Override
	public GameMapData clone(){
		GameMapData data = null;
		data = (GameMapData)super.clone();
		List<List<Square>> game_map = new ArrayList<List<Square>>();
		for(List<Square> line : this.map_data ){
			List<Square> square_line = new ArrayList<Square>();
			for(Square square : line){
				square_line.add(square.clone());
			}
			game_map.add(square_line);
		}
		data.map_data = game_map;
		return data;
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
		GameMapData other = (GameMapData) obj;
		if (getId() == null) {
			if (other.getId() != null)
				return false;
		} else if (!getId().equals(other.getId()))
			return false;
		return true;
	}
	/**
	 * @return　ゲームデータの基本値を返す
	 */
	public static GameMapData defaultData() {
		GameMapData data;
		List<List<Square>> game_map = new ArrayList<List<Square>>();
		for(int x = 0 ; x < 25 ; x++){
			List<Square> list = new ArrayList<Square>();
			for(int y = 0 ; y < 25 ; y++){
				Square square = new Square(x, y, "草原", null, false);
				list.add(square);
			}
			game_map.add(list);
		}
		data = new GameMapData("", "", game_map);
		return data;
	}
	/* (非 Javadoc)
	 * @see function.GameData#getDefaultData()
	 */
	public GameMapData getDefaultData(){
		return defaultData();
	}
}
