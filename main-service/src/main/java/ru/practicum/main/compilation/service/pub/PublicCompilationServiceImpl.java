package ru.practicum.main.compilation.service.pub;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.main.compilation.dto.CompilationResponseDto;
import ru.practicum.main.compilation.mapper.CompilationMapper;
import ru.practicum.main.compilation.model.Compilation;
import ru.practicum.main.compilation.repository.CompilationRepository;
import ru.practicum.main.error.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.main.compilation.mapper.CompilationMapper.toCompilationResponseDto;

@Service
public class PublicCompilationServiceImpl implements PublicCompilationService {
    private final CompilationRepository compilationRepository;

    public PublicCompilationServiceImpl(CompilationRepository compilationRepository) {
        this.compilationRepository = compilationRepository;
    }


    @Override
    public List<CompilationResponseDto> getCompilations(int offset, int limit, Boolean isPinned) {
        Pageable pageRequest = Pageable.ofSize(limit).withPage(offset);

        List<Compilation> compilations = isPinned ? compilationRepository.findAllByPinnedIsTrue(pageRequest).getContent()
                : compilationRepository.findAll(pageRequest).getContent();

        return compilations.stream()
                .map(CompilationMapper::toCompilationResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public CompilationResponseDto getCompilation(long id) {
        Compilation compilation = compilationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("No compilation found with id " + id));

        return toCompilationResponseDto(compilation);
    }
}
