package ru.practicum.main.compilation.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.compilation.dto.CompilationResponseDto;
import ru.practicum.main.compilation.dto.CreateCompilationDto;
import ru.practicum.main.compilation.dto.UpdateCompilationDto;

import javax.validation.Valid;

@RestController
@RequestMapping("/admin/compilations")
@Slf4j
@Validated
public class AdminCompilationController {

    @PostMapping
    public ResponseEntity<CompilationResponseDto> createCompilation(@Valid @RequestBody CreateCompilationDto dto) {
        log.info("createCompilation: {}", dto);
        return ResponseEntity.status(200).body(null);
    }

    @DeleteMapping("/{compId}")
    public ResponseEntity<Void> deleteCompilation(@PathVariable("compId") Long compId) {
        log.info("deleteCompilation: compId={}", compId);
        return ResponseEntity.status(204).build();
    }

    @PatchMapping("/{compId}")
    public ResponseEntity<CompilationResponseDto> updateCompilation(@PathVariable("compId") Long compId,
                                                                    @Valid @RequestBody UpdateCompilationDto dto) {
        log.info("updateCompilation: compId={}, {}", compId, dto);
        return ResponseEntity.status(200).body(null);
    }
}
