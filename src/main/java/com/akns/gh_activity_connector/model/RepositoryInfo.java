package com.akns.gh_activity_connector.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RepositoryInfo {
	
    private String name;
    private String fullName;
    private List<CommitInfo> commits;

}
