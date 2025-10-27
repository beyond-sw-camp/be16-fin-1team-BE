package com.Dolmeng_E.search.domain.search.controller;

import com.Dolmeng_E.search.domain.search.service.UnifiedSearchService;
import com.example.modulecommon.dto.CommonSuccessDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UnifiedSearchController {

    private final UnifiedSearchService unifiedSearchService;

    @GetMapping("/search")
    public ResponseEntity<?> searchDocument(@RequestParam String keyword) {
        return new ResponseEntity<>(CommonSuccessDto.builder()
                .result(unifiedSearchService.search(keyword, "ed682ca4-1be0-4409-949a-3cd70e524e4c"))
                .statusCode(HttpStatus.OK.value())
                .statusMessage("검색 성공")
                .build(), HttpStatus.OK);
    }

    @GetMapping("/suggest")
    public ResponseEntity<?> suggestDocument(@RequestParam String keyword) {
        return new ResponseEntity<>(CommonSuccessDto.builder()
                .result(unifiedSearchService.suggest(keyword, "ed682ca4-1be0-4409-949a-3cd70e524e4c"))
                .statusCode(HttpStatus.OK.value())
                .statusMessage("검색어 제안")
                .build(), HttpStatus.OK);
    }
}
