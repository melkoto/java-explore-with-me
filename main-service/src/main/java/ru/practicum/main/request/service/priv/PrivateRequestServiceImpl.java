package ru.practicum.main.request.service.priv;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.main.error.BadRequestException;
import ru.practicum.main.error.NotFoundException;
import ru.practicum.main.event.model.Event;
import ru.practicum.main.event.repository.PrivateEventRepository;
import ru.practicum.main.request.RequestMapper;
import ru.practicum.main.request.dto.ParticipationRequestDto;
import ru.practicum.main.request.model.Request;
import ru.practicum.main.request.repository.PrivateRequestRepository;
import ru.practicum.main.user.model.User;
import ru.practicum.main.user.repository.AdminUserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static ru.practicum.main.event.eventEnums.State.*;
import static ru.practicum.main.request.RequestMapper.toParticipationRequestDto;

@Service
@Slf4j
public class PrivateRequestServiceImpl implements PrivateRequestService {

    private final PrivateRequestRepository requestRepository;
    private final PrivateEventRepository eventRepository;
    private final AdminUserRepository userRepository;

    private RequestMapper requestMapper;

    public PrivateRequestServiceImpl(PrivateRequestRepository requestRepository, PrivateEventRepository eventRepository,
                                     AdminUserRepository userRepository) {
        this.requestRepository = requestRepository;
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
    }

    @Override
    public ParticipationRequestDto createRequest(Long userId, Long eventId) {
        validateId(userId, "User");
        validateId(eventId, "Event");

        Event event = findEventById(eventId);
        validateEventConstraints(event, userId);
        validateParticipantLimit(event, eventId);

        Request request = createNewRequest(userId, event);
        return toParticipationRequestDto(requestRepository.save(request));
    }

    private void validateId(Long id, String entityName) {
        Optional.ofNullable(id).orElseThrow(() -> new BadRequestException(
                "Id of " + entityName + " is null"));
    }

    private Event findEventById(Long eventId) {
        return eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException(
                "Event with id=" + eventId + " not found"));
    }

    private void validateEventConstraints(Event event, Long userId) {
        if (Objects.equals(event.getInitiator().getId(), userId) || event.getState() != PUBLISHED) {
            throw new NotFoundException("Request for event id=" + event.getId() + " was not found");
        }
    }

    private void validateParticipantLimit(Event event, Long eventId) {
        Integer participantLimit = event.getParticipantLimit();
        if (participantLimit > 0 && participantLimit <= requestRepository.countByEventIdAndStatus(eventId,
                PUBLISHED)) {
            throw new BadRequestException("For event id=" + eventId + " member limit exceeded");
        }
    }

    private Request createNewRequest(Long userId, Event event) {
        Request request = new Request();
        request.setRequester(userRepository.getReferenceById(userId));
        request.setEvent(event);
        request.setCreated(LocalDateTime.now());
        if (event.getRequestModeration()) {
            request.setStatus(PENDING);
        } else {
            request.setStatus(PUBLISHED);
        }
        return request;
    }

    @Override
    public List<ParticipationRequestDto> getRequests(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("User with id=" + userId + " was not found"));

        List<Request> requests = requestRepository.findByRequesterId(user.getId());

        return requests.stream().map(RequestMapper::toParticipationRequestDto).collect(Collectors.toList());
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("User with id=" + userId + " was not found"));
    }

    @Override
    public ParticipationRequestDto cancelRequest(Long userId, Long requestId) {
        validateId(userId, "User");
        validateId(requestId, "Request");

        Request request = findRequestByIdAndUserId(requestId, userId);
        if (request.getStatus() != CANCELED) {
            request.setStatus(CANCELED);
        }
        return toParticipationRequestDto(request);
    }

    private Request findRequestByIdAndUserId(Long requestId, Long userId) {
        return requestRepository.findByIdAndRequesterId(requestId, userId)
                .orElseThrow(() -> new NotFoundException("Request with id=" + requestId + " was not found"));
    }

    @Override
    public List<ParticipationRequestDto> getUserEventRequests(Long userId, Long eventId) {
        validateId(userId, "User");
        validateId(eventId, "Event");

        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException("Event with id=" + eventId + " was not found"));

        return requestRepository.getUserEventRequests(userId, event.getId()).stream()
                .map(RequestMapper::toParticipationRequestDto).collect(Collectors.toList());
    }

}
