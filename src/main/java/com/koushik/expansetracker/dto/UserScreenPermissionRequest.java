package com.koushik.expansetracker.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserScreenPermissionRequest {
    private Long userId;
    private Long screenId;
    private boolean canView;
    private boolean canEdit;
    private boolean canDelete;
}
