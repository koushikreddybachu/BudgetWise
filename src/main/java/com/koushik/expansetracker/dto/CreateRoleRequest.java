package com.koushik.expansetracker.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateRoleRequest {
    private String roleName;
    private String roleDescription;
}
