package ru.practicum.main.compilation.mapper;

import ru.practicum.main.compilation.dto.CompilationResponseDto;
import ru.practicum.main.compilation.dto.CreateCompilationDto;
import ru.practicum.main.compilation.model.Compilation;
import ru.practicum.main.event.model.Event;

import java.util.Set;

public class CompilationMapper {
    public static CompilationResponseDto toCompilationResponseDto(Compilation compilation) {
        if (compilation == null) {
            return null;
        }

        return new CompilationResponseDto(
                compilation.getEvents(),
                compilation.getId(),
                compilation.getPinned(),
                compilation.getTitle());
    }

    public static Compilation toCompilation(CreateCompilationDto dto, Set<Event> events) {
        Compilation compilation = new Compilation();
        compilation.setPinned(dto.getPinned());
        compilation.setTitle(dto.getTitle());
        compilation.setEvents(events);
        return compilation;
    }

}
