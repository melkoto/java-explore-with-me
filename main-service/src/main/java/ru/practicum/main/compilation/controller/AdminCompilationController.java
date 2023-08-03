package ru.practicum.main.compilation.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.compilation.dto.CompilationResponseDto;
import ru.practicum.main.compilation.dto.CreateCompilationDto;
import ru.practicum.main.compilation.dto.UpdateCompilationDto;
import ru.practicum.main.compilation.service.admin.AdminCompilationService;

import javax.validation.Valid;

@Slf4j
@Validated
@RestController
@RequestMapping("/admin/compilations")
public class AdminCompilationController {
    private final AdminCompilationService adminCompilationService;

    public AdminCompilationController(AdminCompilationService adminCompilationService) {
        this.adminCompilationService = adminCompilationService;
    }

    @PostMapping
    public ResponseEntity<CompilationResponseDto> createCompilation(@Valid @RequestBody CreateCompilationDto dto) {
        log.info("createCompilation: {}", dto);
        return ResponseEntity.status(201).body(adminCompilationService.createCompilation(dto));
    }

    @DeleteMapping("/{compId}")
    public ResponseEntity<CompilationResponseDto> deleteCompilation(@PathVariable("compId") Long compId) {
        log.info("deleteCompilation: compId={}", compId);

        return ResponseEntity.status(204).body(adminCompilationService.deleteCompilation(compId));
    }

    @PatchMapping("/{compId}")
    public ResponseEntity<CompilationResponseDto> updateCompilation(@PathVariable("compId") Long compId,
                                                                    @Valid @RequestBody UpdateCompilationDto dto) {
        log.info("updateCompilation: compId={}, {}", compId, dto);
        return ResponseEntity.status(200).body(adminCompilationService.updateCompilation(compId, dto));
    }
}
