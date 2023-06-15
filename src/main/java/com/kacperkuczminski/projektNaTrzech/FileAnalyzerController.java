package com.kacperkuczminski.projektNaTrzech;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
public class FileAnalyzerController {

    private final FileAnalyzerService fileAnalyzerService;

    public FileAnalyzerController(FileAnalyzerService fileAnalyzerService) {
        this.fileAnalyzerService = fileAnalyzerService;
    }

    @PostMapping("/analyze-file")
    public ResponseEntity<FileAnalysisResultDTO> analyzeFile(@RequestParam("file") MultipartFile file) {
        FileAnalysisResultDTO result = fileAnalyzerService.analyzeFile(file);
        return ResponseEntity.ok(result);
    }
}
