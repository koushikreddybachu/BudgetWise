package com.koushik.expansetracker.service;

import com.koushik.expansetracker.dto.CreateScreenRequest;
import com.koushik.expansetracker.entity.Screen;
import com.koushik.expansetracker.repository.ScreenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ScreenService {

    @Autowired
    private ScreenRepository screenRepository;

    public Screen createScreen(CreateScreenRequest request) {

        Screen screen = Screen.builder()
                .screenName(request.getScreenName())
                .screenRoute(request.getScreenRoute())
                .isMenu(request.isMenu())
                .build();

        return screenRepository.save(screen);
    }
}
