package com.sujal.githubreport.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CollaboratorDTO {

    private String login;

    private PermissionDTO permissions;

}