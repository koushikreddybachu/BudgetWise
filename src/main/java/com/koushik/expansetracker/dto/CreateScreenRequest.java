package com.koushik.expansetracker.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateScreenRequest {
    private String screenName;
    private String screenRoute;
    private boolean isMenu;
}
