package io.dimensio.ppmtool.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import io.dimensio.ppmtool.domain.Backlog;
import io.dimensio.ppmtool.domain.Project;
import io.dimensio.ppmtool.domain.ProjectTask;
import io.dimensio.ppmtool.exceptions.ProjectIdException;
import io.dimensio.ppmtool.exceptions.ProjectNotFoundException;
import io.dimensio.ppmtool.repositories.BacklogRepository;
import io.dimensio.ppmtool.repositories.ProjectRepository;
import io.dimensio.ppmtool.repositories.ProjectTaskRepository;
import net.bytebuddy.asm.Advice.Thrown;

@Service
public class ProjectTaskService {
	
	@Autowired
	private BacklogRepository backlogRepository;
	
	@Autowired
	private ProjectTaskRepository projectTaskRepository;
	
	@Autowired
	private ProjectRepository projectRepository;
	
	
	public ProjectTask addProjectTask(String projectIdentifier,ProjectTask projectTask) {
			
		try {
			//all PTs to be added to a specific project,project != null,BL exists
			Backlog backlog = backlogRepository.findByProjectIdentifier(projectIdentifier);
			
		
			//set the BL to PT
			projectTask.setBacklog(backlog);
			//we want our project sequence to be like this: IDPRO-1  IDPRO-2
			Integer BacklogSequence = backlog.getPTSequence();
			//update the BL sequence
			BacklogSequence++;
			
			backlog.setPTSequence(BacklogSequence);
			
			//add sequence to project task
			projectTask.setProjectSequence(projectIdentifier+"-"+BacklogSequence);
			projectTask.setProjectIdentifier(projectIdentifier);
			
			//intial priority when priority is null
			//intial status when status is null
			if(projectTask.getStatus()=="" || projectTask.getStatus()==null) {
				projectTask.setStatus("TO_DO");
			}
			
			if(projectTask.getPriority()==0 || projectTask.getPriority()==null) { //in the future we need projectTask.getPriority()==0 to handle the form
				projectTask.setPriority(3);//low priority
			}
			//projectTask.getPriority()==0 || 
			
			return projectTaskRepository.save(projectTask);
		} catch (Exception e) {
			throw new ProjectNotFoundException("Project not Found");
		}
		
	}

	public Iterable<ProjectTask> findBacklogById(String id){
	
		Project project = projectRepository.findByProjectIdentifier(id);
		
		if(project==null) {
			throw new ProjectNotFoundException("Project with ID: '"+id+"' does not exists");
		}
		
		return projectTaskRepository.findByProjectIdentifierOrderByPriority(id);
	}
	
	
	public ProjectTask findPTByProjectSequence(String backlog_id ,String pt_id) {
		
		//make sure we are searching on the right backlog
		
		Backlog backlog = backlogRepository.findByProjectIdentifier(backlog_id);
		if(backlog==null) {
			throw new ProjectNotFoundException("Project with ID: '"+backlog_id+"' does not exists");
		}
		
		//make sure that our task exists
		ProjectTask projectTask = projectTaskRepository.findByProjectSequence(pt_id);
		if(projectTask==null) {
			throw new ProjectNotFoundException("Project Task '"+pt_id+"' not found");
		}
		
		
		//make sure that the backlog/project id in the path corresponds to the right project
		if(!projectTask.getProjectIdentifier().equals(backlog_id)) {
			throw new ProjectNotFoundException("Project Task '"+pt_id+"' does not exist in project:'"+backlog_id);
		}
		
		return projectTask;
	}
	
	public ProjectTask updateByProjectSequence(ProjectTask updatedTask,String backlog_id,String pt_id) {
		
		ProjectTask projectTask = findPTByProjectSequence(backlog_id, pt_id);
		
		projectTask = updatedTask;
		
		return projectTaskRepository.save(projectTask);
		
	}
	
	public void deletePTByProjectSequence(String backlog_id,String pt_id) {
		
		ProjectTask projectTask = findPTByProjectSequence(backlog_id, pt_id);
		
		projectTaskRepository.delete(projectTask);
	}
	
	//Update project task
	//find existing projectTask
	//replace it with updated task
	//save update
	
	
	
}
