package function;

import java.util.Collections;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * GameDataクラスをまとめるためのクラス
 * @author 樹麗
 * @param <T>　どのGameDataクラスを扱っているか
 */
public class GameDataContainer<T extends GameData> {
	private Map<String, T> data;
	/**
	 * @param data 初期値
	 */
	@JsonCreator
	public GameDataContainer(@JsonProperty("data")Map<String, T> data){
		this.data = data;
	}
	/**
	 * dataに登録する
	 * @param key キー
	 * @param value 値
	 */
	public void putValue(String key, T value) {
		data.put(key, value);
	}
	/**
	 * 渡された値を全てdataに登録する
	 * @param data データ
	 */
	public void putAll(Map<String, T> data) {
		this.data.putAll(data);
	}
	/**
	 * @param id キー
	 * @return idに対応する値を返す
	 */
	public T getValue(String id){
		return data.get(id);
	}
	/**
	 * @return dataを不変Mapにして返す
	 */
	public Map<String, T> getData(){
		return Collections.unmodifiableMap(data);
	}
}
