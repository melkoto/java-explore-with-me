package ru.practicum.main.request.service.priv;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.main.error.ConflictException;
import ru.practicum.main.error.NotFoundException;
import ru.practicum.main.event.eventEnums.State;
import ru.practicum.main.event.model.Event;
import ru.practicum.main.event.repository.PrivateEventRepository;
import ru.practicum.main.request.dto.ParticipationRequestDto;
import ru.practicum.main.request.mapper.RequestMapper;
import ru.practicum.main.request.model.Request;
import ru.practicum.main.request.repository.PrivateRequestRepository;
import ru.practicum.main.user.model.User;
import ru.practicum.main.user.repository.AdminUserRepository;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static ru.practicum.main.event.eventEnums.State.PUBLISHED;
import static ru.practicum.main.request.enums.Status.*;
import static ru.practicum.main.request.mapper.RequestMapper.toParticipationRequestDto;
import static ru.practicum.main.request.mapper.RequestMapper.toRequest;

@Service
@Slf4j
public class PrivateRequestServiceImpl implements PrivateRequestService {

    private final PrivateRequestRepository requestRepository;
    private final PrivateEventRepository eventRepository;
    private final AdminUserRepository userRepository;

    private RequestMapper requestMapper;

    public PrivateRequestServiceImpl(PrivateRequestRepository requestRepository, PrivateEventRepository eventRepository, AdminUserRepository userRepository) {
        this.requestRepository = requestRepository;
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
    }

    @Override
    public ParticipationRequestDto createRequest(Long userId, Long eventId) {
        Event event = eventRepository
                .findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event with id=" + eventId + " was not found"));

        User user = userRepository
                .findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id=" + userId + " was not found"));


        Request request = requestRepository.findByRequesterIdAndEventId(userId, eventId);

        Integer counted = requestRepository.countByEventId(eventId);

        if (event.getParticipantLimit() != null && event.getParticipantLimit() != 0 &&
                event.getParticipantLimit() <= counted) {
            throw new ConflictException("Event with id=" + eventId + " has reached the limit of participants. " +
                    "Participant limit is " + event.getParticipantLimit() + " and there are " + counted + " participants");
        }

        validateEventConstraints(event, userId);
        validateRepeatRequest(user, event);

        Request updatedRequest = (request == null) ? new Request() : request;
        Request result = toRequest(updatedRequest, user, event);

        if (event.getRequestModeration()) {
            result.setStatus(PENDING);
        } else {
            result.setStatus(CONFIRMED);
        }

        return toParticipationRequestDto(requestRepository.save(result));
    }

    @Override
    public List<ParticipationRequestDto> getRequests(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User with id=" + userId + " was not found"));

        List<Request> requests = requestRepository.findByRequesterId(user.getId());

        return requests.stream().map(RequestMapper::toParticipationRequestDto).collect(Collectors.toList());
    }

    @Override
    public ParticipationRequestDto cancelRequest(Long userId, Long requestId) {
        Request request = requestRepository
                .findByIdAndRequesterId(requestId, userId)
                .orElseThrow(() -> new NotFoundException("Request with id=" + requestId + " was not found"));

        if (request.getStatus() != CANCELED) {
            request.setStatus(CANCELED);
        }
        return toParticipationRequestDto(request);
    }

    @Override
    public List<ParticipationRequestDto> getUserEventRequests(Long userId, Long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Event with id=" + eventId + " was not found"));

        return requestRepository.getUserEventRequests(userId, event.getId()).stream().map(RequestMapper::toParticipationRequestDto).collect(Collectors.toList());
    }

    private void validateEventConstraints(Event event, Long userId) {

        Long initiatorId = event.getInitiator().getId();
        State state = event.getState();

        log.info("InitiatorId: {}", initiatorId);
        log.info("UserId: {}", userId);
        log.info("Event State: {}", state);

        if (Objects.equals(initiatorId, userId) || state != PUBLISHED) {
            throw new ConflictException(String.format("User with id=%d can't participate in event with id=%d "
                            + "because he is the initiator or event is not published. "
                            + "Event state = %s. User id = %d. Event initiator id = %d",
                    userId, event.getId(), state, userId, initiatorId));
        }
    }

    private void validateRepeatRequest(User user, Event event) {
        boolean exists = requestRepository.existsByRequesterAndEvent(user, event);

        if (exists) {
            throw new ConflictException("User with id=" + user.getId() +
                    " has already requested participation in event with id=" + event.getId());
        }
    }
}
