package function;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver;
/**
 * ゲームで扱うデータの基底クラス
 * @author 樹麗
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
		include = JsonTypeInfo.As.PROPERTY,
		property = "@type")
@JsonTypeIdResolver(GameDataTypeIdResolver.class)
public abstract class GameData implements Cloneable {
	private String id;
	private String name;
	/**
	 * @param id データＩＤ
	 * @param name　データ名
	 */
	protected GameData(String id, String name){
		this.id = id;
		this.name = name;
	}
	/**
	 * @return　データidを返す
	 */
	public String getId(){ return this.id; }
	/**
	 * @return データ名を返す
	 */
	public String getName(){ return this.name; }
	/**
	 * @param name　渡された値にデータ名を変更する
	 */
	public void setName(String name){ this.name = name; }
	/**
	 * @return　data データの初期値を返す
	 */
	@JsonIgnore
	public abstract GameData getDefaultData();

	/**
	 * @param id コピー後のＩＤ
	 * @param name　コピー後の名前
	 * @return コピーしたデータを渡されたＩＤと名前に変更し返す
	 */
	public GameData copy(String id, String name){
		GameData data = clone();
		data.id = id;
		data.name = name;
		return data;
	}
	/* (非 Javadoc)
	 * @see java.lang.Object#clone()
	 */
	@Override
	public GameData clone(){
		GameData data = null;
		try {
			data = (GameData)super.clone();
		} catch (CloneNotSupportedException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		return data;
	}
	/* (非 Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString(){ return this.name; }
	/* (非 Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
	/* (非 Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GameData other = (GameData) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
