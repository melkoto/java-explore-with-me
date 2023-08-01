package ru.practicum.main.compilation.service.admin;

import ru.practicum.main.compilation.dto.CompilationResponseDto;
import ru.practicum.main.compilation.dto.CreateCompilationDto;
import ru.practicum.main.compilation.dto.UpdateCompilationDto;

public interface AdminCompilationService {
    public CompilationResponseDto createCompilation(CreateCompilationDto newCompilationDto);

    public CompilationResponseDto updateCompilation(Long compilationId, UpdateCompilationDto updateCompilationRequest);

    public void deleteCompilation(long id);
}
