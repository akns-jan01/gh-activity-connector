package com.akns.gh_activity_connector.service;




import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.akns.gh_activity_connector.model.CommitInfo;
import com.akns.gh_activity_connector.model.RepositoryInfo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GitHubService {

    @Value("${github.token}")
    private String token;

    @Value("${github.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate;
    
    private static final int PAGE_SIZE = 100;
    private static final int MAX_COMMITS = 20;

    
    public List<RepositoryInfo> fetchRepositoriesWithCommits(String username) {
        List<RepositoryInfo> repositories = new ArrayList<>();
        int page = 1;

        while (true) {
            String url = UriComponentsBuilder
                    .fromUriString(apiUrl + "/users/" + username + "/repos")
                    .queryParam("per_page", PAGE_SIZE)
                    .queryParam("page", page++)
                    .toUriString();


            List<Map<String, Object>> repoResponse = fetchForList(url);
            if (repoResponse.isEmpty()) break;

            for (Map<String, Object> repo : repoResponse) {
                String repoName = (String) repo.get("name");
                String fullName = (String) repo.get("full_name");

                RepositoryInfo repoInfo = new RepositoryInfo();
                repoInfo.setName(repoName);
                repoInfo.setFullName(fullName);
                repoInfo.setCommits(fetchCommits(fullName));
                repositories.add(repoInfo);
            }
        }

        return repositories;
    }

    @SuppressWarnings("unchecked")
    private List<CommitInfo> fetchCommits(String fullRepoName) {
        String url = UriComponentsBuilder
                .fromUriString(apiUrl + "/repos/" + fullRepoName + "/commits")
                .queryParam("per_page", MAX_COMMITS)
                .queryParam("page", 1)
                .toUriString();


        List<Map<String, Object>> commitsResponse = fetchForList(url);
        List<CommitInfo> commits = new ArrayList<>();

        for (Map<String, Object> commitObj : commitsResponse) {
			Map<String, Object> commit = (Map<String, Object>) commitObj.get("commit");
            Map<String, Object> author = commit != null ? (Map<String, Object>) commit.get("author") : null;

            CommitInfo info = new CommitInfo();
            info.setMessage(commit != null ? (String) commit.get("message") : "No message");
            info.setAuthor(author != null ? (String) author.get("name") : "unknown");
            info.setTimestamp(author != null ? (String) author.get("date") : null);

            commits.add(info);
        }

        return commits;
    }
    
	private List<Map<String, Object>> fetchForList(String url) {
		HttpEntity<Void> entity = new HttpEntity<>(createHeaders());

		ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(url, HttpMethod.GET, entity,
				new ParameterizedTypeReference<List<Map<String, Object>>>() {});

		if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
			return response.getBody();
		}

		return Collections.emptyList();
	}


    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        return headers;
    }
}