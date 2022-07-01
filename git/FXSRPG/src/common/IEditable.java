package common;

import javafx.event.Event;

/**
 * このインターフェースはエディトを行うことができることを意味します
 * @author 樹麗
 *
 */
public interface IEditable {
	void edit(Event e);
	void add(Event e);
	void copy(Event e);
	void remove(Event e);
}
