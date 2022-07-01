package function.controller;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import common.SRPGCommons;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;

/**
 * ゲームコントローラーの基底クラスです
 * @author 樹麗
 *
 */
public abstract class ControllerBase extends AnchorPane {
	private Map<String, Node> all_node = new HashMap<String, Node>();
	/**
	 * 指定されたコントローラーを読み込む
	 * @param name　コントローラー名
	 */
	protected ControllerBase(String name){
		try {
			FXMLLoader loader = new FXMLLoader(new URL(SRPGCommons.getURL() + "/controller/" + name + ".fxml"));
			loader.setController(this);
			loader.setRoot(this);
			loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		all_node = getAllNodes();
	}
	/**
	 * @return 全てのIDのあるノードを返す
	 */
	protected Map<String, Node> getAllNode(){
		return all_node;
	}
	private Map<String, Node> getAllNodes(){
		Map<String, Node> nodes = new HashMap<String, Node>();
		addAllDescendents(this, nodes);
		return nodes;
	}
	private void addAllDescendents(Parent parent, Map<String, Node> nodes) {
		for (Node node : parent.getChildrenUnmodifiable()) {
			if(node.getId() != null){ nodes.put(node.getId(), node); }
			if (node instanceof Parent){
				addAllDescendents((Parent)node, nodes);
			}
		}
	}
}
