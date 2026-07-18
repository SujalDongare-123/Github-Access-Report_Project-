package com.sujal.githubreport.controller;

import com.sujal.githubreport.dto.CollaboratorDTO;
import com.sujal.githubreport.dto.ReportDTO;
import com.sujal.githubreport.dto.RepositoryDTO;
import com.sujal.githubreport.service.GithubService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RepositoryController {

	private final GithubService githubService;

	public RepositoryController(GithubService githubService) {
		this.githubService = githubService;
	}

	@GetMapping("/api/repositories")
	public List<RepositoryDTO> getRepositories() {
		return githubService.getRepositories();
	}

	
	@GetMapping("/api/repositories/{repositoryName}/collaborators")
	public List<CollaboratorDTO> getCollaborators(@PathVariable String repositoryName) {

		return githubService.getCollaborators(repositoryName);
	}
	
	
	@GetMapping("/api/report")
	public List<ReportDTO> generateReport() {

	    return githubService.generateAccessReport();

	}
	
	

}