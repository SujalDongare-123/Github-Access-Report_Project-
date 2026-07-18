package com.sujal.githubreport.client;

import com.sujal.githubreport.dto.CollaboratorDTO;
import com.sujal.githubreport.dto.RepositoryDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
public class GithubClient {

	private final RestClient restClient;

	@Value("${github.token}")
	private String token;

	@Value("${github.organization}")
	private String organization;

	@Value("${github.api.base-url}")
	private String baseUrl;

	public GithubClient(RestClient restClient) {
		this.restClient = restClient;
	}

	
	public List<RepositoryDTO> getRepositories() {

		String url = baseUrl + "/orgs/" + organization + "/repos";

		return restClient.get().uri(url).header("Authorization", "Bearer " + token).retrieve()
				.body(new ParameterizedTypeReference<List<RepositoryDTO>>() {
				});
	}
	
	
	
	
	public List<CollaboratorDTO> getCollaborators(String repositoryName) {

	    String url = baseUrl + "/repos/" + organization + "/" + repositoryName + "/collaborators";

	    return restClient.get()
	            .uri(url)
	            .header("Authorization", "Bearer " + token)
	            .retrieve()
	            .body(new ParameterizedTypeReference<List<CollaboratorDTO>>() {});
	}
	
	
	
}