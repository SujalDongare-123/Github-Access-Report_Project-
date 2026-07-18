package com.sujal.githubreport.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportDTO {
	
	    private String repositoryName;
	    private String collaborator;
	    private String permission;

}
