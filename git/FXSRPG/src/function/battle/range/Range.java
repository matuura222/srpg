package function.battle.range;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import function.map.GameMapData;
import function.map.Square;
import function.unit.Unit;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * このクラスはアクションの行動範囲は決めるオブジェクトです。
 * @author 樹麗
 *
 */
public abstract class Range {
	private int total_cost, range_cost;
	private Map<String, RangeValue> list;
	private GameMapData data;
	private Unit unit;
	/**
	 * @param data　ゲームのマップデータ
	 */
	public Range(GameMapData data) {
		this.data = data;
		list = new HashMap<String, RangeValue>();
	}
	/**
	 * @param square 範囲を決める最初の場所
	 * @param unit 行動するユニット
	 */
	public final void startSearch(Square square, Unit unit) {
		this.unit = unit;
		init();
		RangeValue value = new RangeValue(null, data.getSquare(square.getX(), square.getY()), total_cost);
		list.put(square.getId(), value);
		while(value.getCost() > 0){
			search(value, value.getTo().getX(), value.getTo().getY() - 1, value.getCost());
			search(value, value.getTo().getX(), value.getTo().getY() + 1, value.getCost());
			search(value, value.getTo().getX() - 1, value.getTo().getY(), value.getCost());
			search(value, value.getTo().getX() + 1, value.getTo().getY(), value.getCost());
			value.close();
			value = list.values().stream()
					.filter(vv -> !vv.isClosed())
					.max((s1, s2) -> s1.getCost() - s2.getCost()).get();
		}
	}
	/**
	 * @param value この座標から探索を行う
	 * @param x ゲームマップの座標X
	 * @param y ゲームマップの座標Y
	 * @param c 現在残っているコスト
	 * それぞれの探索を行う
	 */
	protected abstract void search(RangeValue value, int x, int y, int c);
	/**
	 * コストの初期化を行う
	 */
	protected abstract void init();
	/**
	 * @param graphics 描画するGraphicsContext
	 * 渡されたGraphicsContextに計算された範囲を描画する
	 */
	public void drawRange(GraphicsContext graphics){
		graphics.clearRect(0, 0, data.getMapSizeX() * 40, data.getMapSizeY() * 40);
		for(RangeValue value : list.values()){
			if(value.getCost() < range_cost){
				graphics.setFill(Color.color(1, 0, 0, 0.3));
			}else if(value.getCost() >= 0){
				graphics.setFill(Color.color(0, 0, 1, 0.3));
			}
			graphics.fillRect(value.getTo().getX() * 40, value.getTo().getY() * 40, 40, 40);
		}
	}
	protected int getTotalCost() {
		return this.total_cost;
	}
	protected void setTotalCost(int total_cost){
		this.total_cost = total_cost;
	}
	protected int getRangeCost(){
		return this.range_cost;
	}
	protected void setRangeCost(int range_cost){
		this.range_cost = range_cost;
	}
	protected Unit getUnit(){
		return this.unit;
	}
	public Map<String, RangeValue> getMap() {
		return Collections.unmodifiableMap(list);
	}
	protected void putRangeValue(String key, RangeValue value) {
		list.put(key, value);
	}
	protected GameMapData getGameMapData() {
		return this.data;
	}
	/**
	 * 範囲を決めるために必要な要素
	 * @author 樹麗
	 *
	 */
	public class RangeValue{
		private Square to;
		private Square from;
		private int cost;
		private boolean is_closed = false;
		/**
		 * @param from どこから探索をしたのか
		 * @param to 探索を行った場所
		 * @param cost ここを探索した時に残っていたコスト
		 */
		protected RangeValue(Square from, Square to, int cost){
			this.from = from;
			this.to = to;
			this.cost = cost;
		}
		public int getCost(){
			return cost;
		}
		protected Square getFrom(){
			return from;
		}
		public Square getTo(){
			return to;
		}
		public boolean isClosed(){
			return this.is_closed;
		}
		private void close(){
			is_closed = true;
		}
		public String toString(){
			return to.toString() + " " + cost;
		}
	}
}
