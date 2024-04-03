package com.example.ordering_lecture.recommend.controller;

import com.example.ordering_lecture.common.OrTopiaResponse;
import com.example.ordering_lecture.recommend.service.RecommendationService;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
    public ResponseEntity<OrTopiaResponse> recommendations(@PathVariable Long id){
        List<RecommendedItem> recommendations = recommendationService.getRecommendations(id);
        OrTopiaResponse orTopiaResponse = new OrTopiaResponse("create success", recommendations);
        return new ResponseEntity<>(orTopiaResponse, HttpStatus.CREATED);
    }
}
