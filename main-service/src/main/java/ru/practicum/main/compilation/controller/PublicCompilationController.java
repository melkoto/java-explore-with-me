package ru.practicum.main.compilation.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.compilation.dto.CompilationResponseDto;
import ru.practicum.main.compilation.service.pub.PublicCompilationService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping("/compilations")
public class PublicCompilationController {
    private final PublicCompilationService publicCompilationService;

    public PublicCompilationController(PublicCompilationService publicCompilationService) {
        this.publicCompilationService = publicCompilationService;
    }

    @GetMapping
    public ResponseEntity<List<CompilationResponseDto>> getCompilations(
            @RequestParam(value = "pinned") Boolean pinned,
            @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
            @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {

        log.info("Get compilations with: \npinned={}, \nfrom={}, \nsize={}", pinned, from, size);

        return ResponseEntity.status(200).body(publicCompilationService.getCompilations(from, size, pinned));
    }

    @GetMapping("/{compId}")
    public ResponseEntity<CompilationResponseDto> getCompilation(@PathVariable(name = "compId") Long compId) {

        log.info("getCompilation: compId={}", compId);

        return ResponseEntity.status(200).body(publicCompilationService.getCompilation(compId));
    }
}
