package ru.practicum.main.compilation.service.pub;

import ru.practicum.main.compilation.dto.CompilationResponseDto;

import java.util.List;

public interface PublicCompilationService {
    public List<CompilationResponseDto> getCompilations(int offset, int limit, boolean isPinned);

    public CompilationResponseDto getCompilation(long id);
}
