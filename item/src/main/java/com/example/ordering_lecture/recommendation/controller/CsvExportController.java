package com.example.ordering_lecture.recommendation.controller;

import com.example.ordering_lecture.recommendation.service.CsvExportService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CsvExportController {
    private final CsvExportService csvExportService;

    public CsvExportController(CsvExportService csvExportService) {
        this.csvExportService = csvExportService;
    }

    @GetMapping("/export-csv")
    public String exportCSV() {
        return csvExportService.exportDataToCsv();
    }
}
