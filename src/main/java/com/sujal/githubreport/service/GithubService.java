package com.sujal.githubreport.service;

import com.sujal.githubreport.client.GithubClient;
import com.sujal.githubreport.dto.CollaboratorDTO;
import com.sujal.githubreport.dto.ReportDTO;
import com.sujal.githubreport.dto.RepositoryDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GithubService {

    private final GithubClient githubClient;

    public GithubService(GithubClient githubClient) {
        this.githubClient = githubClient;
    }

    public List<RepositoryDTO> getRepositories() {
        return githubClient.getRepositories();
    }
    
 
    public List<CollaboratorDTO> getCollaborators(String repositoryName) {
        return githubClient.getCollaborators(repositoryName);
    }
    
    
    
    
    
    public List<ReportDTO> generateAccessReport() {

        List<RepositoryDTO> repositories = githubClient.getRepositories();

        List<ReportDTO> report = new ArrayList<>();

        for (RepositoryDTO repository : repositories) {

            List<CollaboratorDTO> collaborators =
                    githubClient.getCollaborators(repository.getName());

            for (CollaboratorDTO collaborator : collaborators) {

                String permission;

                if (collaborator.getPermissions().isAdmin()) {
                    permission = "ADMIN";
                } else if (collaborator.getPermissions().isPush()) {
                    permission = "WRITE";
                } else {
                    permission = "READ";
                }

                ReportDTO reportDTO = new ReportDTO(
                        repository.getName(),
                        collaborator.getLogin(),
                        permission
                );

                report.add(reportDTO);
            }
        }

        return report;
    }
    
    
    
    
    
}