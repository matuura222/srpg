package function.controller.state.edit.skill;

import function.controller.state.edit.EditorControllerBase;
import function.skill.Skill;
import javafx.fxml.FXML;

public abstract class SkillDetailsEditControllerBase extends EditorControllerBase<Skill>{
	@FXML
	protected SkillStatusEditController status;
	protected SkillDetailsEditControllerBase(String name) {
		super(name);
	}
	public SkillStatusEditController getStatus(){ return this.status; }
	public abstract Skill getSkill();
}
