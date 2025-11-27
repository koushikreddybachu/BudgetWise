package com.koushik.expansetracker.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoleScreenPermissionRequest {
    private Long roleId;
    private Long screenId;
    private boolean canView;
    private boolean canEdit;
    private boolean canDelete;
}
