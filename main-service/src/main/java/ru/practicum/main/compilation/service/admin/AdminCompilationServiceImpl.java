package ru.practicum.main.compilation.service.admin;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.main.compilation.dto.CompilationResponseDto;
import ru.practicum.main.compilation.dto.CreateCompilationDto;
import ru.practicum.main.compilation.dto.UpdateCompilationDto;
import ru.practicum.main.compilation.model.Compilation;
import ru.practicum.main.compilation.repository.CompilationRepository;
import ru.practicum.main.error.NotFoundException;
import ru.practicum.main.event.model.Event;
import ru.practicum.main.event.repository.AdminEventRepository;

import java.util.Set;

import static ru.practicum.main.compilation.mapper.CompilationMapper.toCompilation;
import static ru.practicum.main.compilation.mapper.CompilationMapper.toCompilationResponseDto;
import static ru.practicum.main.event.eventEnums.State.PUBLISHED;

@Slf4j
@Service
public class AdminCompilationServiceImpl implements AdminCompilationService {
    private final AdminEventRepository eventRepository;
    private final CompilationRepository compilationRepository;

    public AdminCompilationServiceImpl(AdminEventRepository eventRepository, CompilationRepository compilationRepository) {
        this.eventRepository = eventRepository;
        this.compilationRepository = compilationRepository;
    }

    @Override
    public CompilationResponseDto createCompilation(CreateCompilationDto newCompilationDto) {
        Set<Event> events = eventRepository.findAllByIdIn(newCompilationDto.getEvents());

        Compilation newCompilation = toCompilation(newCompilationDto, events);

        Compilation savedCompilation = compilationRepository.save(newCompilation);

        return toCompilationResponseDto(savedCompilation);
    }

    @Override
    public CompilationResponseDto updateCompilation(Long compilationId, UpdateCompilationDto updateCompilationRequest) {
        Compilation compilation = findCompilationById(compilationId);
        update(compilation, updateCompilationRequest);

        log.info("Updating compilation = {} with id = {}", compilation, compilationId);

        compilationRepository.save(compilation);
        CompilationResponseDto compilationResponseDto = toCompilationResponseDto(compilation);

        log.info("Compilation updated successfully. New compilation = {}", compilationResponseDto);

        return compilationResponseDto;
    }

    @Override
    public CompilationResponseDto deleteCompilation(long id) {
        Compilation compilation = findCompilationById(id);

        compilationRepository.deleteById(id);
        log.info("Compilation: {} with id = {} deleted successfully", compilation, id);

        return toCompilationResponseDto(compilation);
    }

    private Compilation findCompilationById(Long compilationId) {
        return compilationRepository.findById(compilationId).orElseThrow(() ->
                new NotFoundException("Compilation with id = " + compilationId + " not found."));
    }

    private void update(Compilation compilation, UpdateCompilationDto request) {
        updateEventsIfPresent(compilation, request.getEvents());
        updatePinnedIfPresent(compilation, request.getPinned());
        updateTitleIfPresent(compilation, request.getTitle());
    }

    private void updateEventsIfPresent(Compilation compilation, Set<Long> eventIds) {
        if (eventIds != null) {
            Set<Event> events = eventRepository.findByIdInAndState(eventIds, PUBLISHED);
            compilation.setEvents(events);
        }
    }

    private void updatePinnedIfPresent(Compilation compilation, Boolean pinned) {
        if (pinned != null) {
            compilation.setPinned(pinned);
        }
    }

    private void updateTitleIfPresent(Compilation compilation, String title) {
        if (title != null && !title.isBlank()) {
            compilation.setTitle(title);
        }
    }
}
