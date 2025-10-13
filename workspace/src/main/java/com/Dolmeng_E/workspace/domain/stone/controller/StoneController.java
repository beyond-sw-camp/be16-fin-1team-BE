package com.Dolmeng_E.workspace.domain.stone.controller;

import com.Dolmeng_E.workspace.domain.stone.service.StoneService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/stone")
@RequiredArgsConstructor
public class StoneController {
    private final StoneService stoneService;
}
