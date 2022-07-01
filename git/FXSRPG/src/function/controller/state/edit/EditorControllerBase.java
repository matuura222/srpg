package function.controller.state.edit;

import function.controller.ControllerBase;

public abstract class EditorControllerBase<T> extends ControllerBase{
	protected EditorControllerBase(String name){
		super("state/edit/" + name);
	}
	public abstract void edit(T edit_target);
}
