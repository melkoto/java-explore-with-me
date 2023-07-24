package ru.practicum.main.event.service.priv;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.client.StatsClient;
import ru.practicum.main.error.NotFoundException;
import ru.practicum.main.event.dto.ShortEventResponseDto;
import ru.practicum.main.event.model.Event;
import ru.practicum.main.event.repository.EventRepository;
import ru.practicum.main.user.model.User;
import ru.practicum.main.user.repository.AdminUserRepository;

import java.util.*;

@Service
public class PrivateEventServiceImpl implements PrivateEventService {
    private final AdminUserRepository userRepository;
    private final EventRepository eventRepository;
    private final StatsClient statsClient;

    public PrivateEventServiceImpl(AdminUserRepository userRepository, EventRepository eventRepository, StatsClient statsClient) {
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
        this.statsClient = statsClient;
    }

    @Override
    public List<ShortEventResponseDto> getEvents(Long userId, Integer from, Integer size) {
        User user = userRepository
                .findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id " + userId + " not found"));

        Set<Event> events =
                new HashSet<>(eventRepository.findByInitiatorId(userId, PageRequest.of(from / size, size)));

        return null;
    }
}
