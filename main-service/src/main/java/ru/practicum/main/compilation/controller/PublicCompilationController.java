package ru.practicum.main.compilation.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.main.compilation.dto.CompilationResponseDto;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping("/compilations")
@Slf4j
@Validated
public class PublicCompilationController {

    @GetMapping
    public ResponseEntity<List<CompilationResponseDto>> getCompilations(@RequestParam(value = "pinned") Boolean pinned,
                                                                        @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                                        @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("Get compilations with: \npinned={}, \nfrom={}, \nsize={}", pinned, from, size);
        return ResponseEntity.ok().body(null);
    }

    @GetMapping("/{compId}")
    public ResponseEntity<CompilationResponseDto> getCompilation(@Positive @RequestParam(name = "compId") Long compId) {
        log.info("getCompilation: compId={}", compId);
        return ResponseEntity.ok().body(null);
    }
}
