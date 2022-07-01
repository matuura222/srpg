package function.controller.state.edit;

import common.IEditable;
import function.GameData;
import javafx.event.Event;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;

public class EditorLeftClickedMenu extends ContextMenu implements IEditable{
	private IEditable edit;
	public EditorLeftClickedMenu(IEditable edit){
		this.edit = edit;
		MenuItem[] menu_item_list = new MenuItem[4];
		menu_item_list[0] = new MenuItem("編集");
		menu_item_list[1] = new MenuItem("複製");
		menu_item_list[2] = new MenuItem("追加");
		menu_item_list[3] = new MenuItem("削除");
		menu_item_list[0].setOnAction(e -> edit(e));
		menu_item_list[1].setOnAction(e -> copy(e));
		menu_item_list[2].setOnAction(e -> add(e));
		menu_item_list[3].setOnAction(e -> remove(e));
		getItems().addAll(menu_item_list);
	}
	public void edit(Event e){
		@SuppressWarnings("unchecked")
		ListView<GameData> list = (ListView<GameData>) this.getOwnerNode();
		if(list.getSelectionModel().getSelectedItem().getId().equals("")){ return; }
		edit.edit(e);
	}
	public void copy(Event e){
		@SuppressWarnings("unchecked")
		ListView<GameData> list = (ListView<GameData>) this.getOwnerNode();
		GameData data = list.getSelectionModel().getSelectedItem();
		GameData copy_data = data.copy(data.getId() + "_copy", data.getName() + "_copy");
		int num = 1;
		while(list.getItems().contains(copy_data)){
			num++;
			copy_data = data.copy(data.getId() + "_copy" + num, data.getName() + "_copy" + num);
		}
		if(list.getItems().contains(copy_data)){ return; }
		list.getItems().add(list.getItems().indexOf(data) + 1, copy_data);
		list.getSelectionModel().select(copy_data);
		edit.copy(e);
	}
	public void add(Event e){
		@SuppressWarnings("unchecked")
		ListView<GameData> list = (ListView<GameData>) this.getOwnerNode();
		GameData data = list.getItems().get(list.getItems().size() - 1);
		int num = list.getItems().size();
		GameData add_data = data.getDefaultData().copy("データ" + num, "データ" + num);
		while(list.getItems().contains(add_data)){
			num++;
			add_data = data.getDefaultData().copy("データ" + num, "データ" + num);
		}
		list.getItems().add(list.getItems().size(), add_data);
		list.getSelectionModel().select(list.getItems().size() - 1);
		edit.add(e);
	}
	public void remove(Event e){
		@SuppressWarnings("unchecked")
		ListView<GameData> list = (ListView<GameData>) this.getOwnerNode();
		list.getItems().remove(list.getSelectionModel().getSelectedIndex());
		edit.remove(e);
	}

}
