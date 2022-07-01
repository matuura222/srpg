package function.controller.state.edit.job;

import java.util.Collection;

import common.SRPGCommons;
import function.controller.state.edit.EditorControllerBase;
import function.controller.state.edit.SRPGEditorState;
import function.controller.state.edit.skill.HaveSkillController;
import function.skill.Skill;
import function.unit.Job;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class JobEditController extends EditorControllerBase<Job> {
	@FXML
    private TextField job_name, job_id;
	@FXML
	JobStatusEditController status;
	@FXML
	HaveSkillController<Job> skill;
	SRPGEditorState editor;
	Job job;
	public JobEditController(SRPGEditorState editor){
		super("job/JobEdit");
		this.editor = editor;
	}
	@FXML
	private void save(ActionEvent e){
		Job job = new Job(job_id.getText(), job_name.getText(), status.getHp(), status.getMp(), status.getAttack(),
				status.getDefense(), status.getMagicAttack(), status.getMagicDefense(), skill.getHaveSkillIdList(), status.getExpRate());
		if((SRPGCommons.getJobList().contains(this.job) == false && SRPGCommons.getJobList().contains(job))
				|| (this.job.equals(job) == false && SRPGCommons.getJobList().contains(job))){
			editor.setMessage("識別IDが重複しています。\n別のIDにしてください");
			return;
		}
		editor.setMessage("職業：" + job.getName() + "をセーブしました");
		editor.jobUpdate(this.job, job);
		this.job = job;
	}
	@Override
	public void edit(Job job){
		this.job = job;
		job_id.setText(job.getId());
		job_name.setText(job.getName());
		status.edit(job);
		skill.edit(job);
	}
	public void skillUpdate(Collection<Skill> skill_list){

	}
}
