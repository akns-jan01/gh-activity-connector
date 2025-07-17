package com.akns.gh_activity_connector.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.akns.gh_activity_connector.model.RepositoryInfo;
import com.akns.gh_activity_connector.service.GitHubService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/github")
@RequiredArgsConstructor
public class GitHubController {

    private final GitHubService gitHubService;

    @GetMapping("/activity/{username}")
    public List<RepositoryInfo> getActivity(@PathVariable String username) {
        return gitHubService.fetchRepositoriesWithCommits(username);
    }
}
