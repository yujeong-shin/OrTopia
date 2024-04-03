package com.example.ordering_lecture.recommend.controller;

import com.example.ordering_lecture.recommend.service.RecommendationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RecommendationController {
    private final RecommendationService recommendationService;

    public RecommendationController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    @GetMapping("/export-csv")
    public String exportCSV() {
        return recommendationService.exportDataToCsv();
    }

    @GetMapping("/member/{id}/recommendations")
    public String recommendations(@PathVariable Long id){
        return recommendationService.getRecommendations(id);
    }
}
