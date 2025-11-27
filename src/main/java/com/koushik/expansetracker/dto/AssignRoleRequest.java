package com.koushik.expansetracker.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssignRoleRequest {
    private Long userId;
    private Long roleId;
}
