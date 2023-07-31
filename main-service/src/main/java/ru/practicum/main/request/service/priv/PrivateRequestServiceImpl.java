package ru.practicum.main.request.service.priv;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.main.error.BadRequestException;
import ru.practicum.main.error.ConflictException;
import ru.practicum.main.error.NotFoundException;
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

import static ru.practicum.main.event.eventEnums.State.CANCELED;
import static ru.practicum.main.event.eventEnums.State.PUBLISHED;
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


        Request request = toRequest(user, event);

        log.info("createRequest request = {}", request);
        log.info("createRequest event = {}", event);
        log.info("createRequest user = {}", user);

        if (!event.getRequestModeration()) {
            request.setStatus(PUBLISHED);
        }

        validateEventConstraints(event, userId);
        validateParticipantLimit(event, eventId);
        validateRepeatRequest(user, event);

        return toParticipationRequestDto(requestRepository.save(request));
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

        log.info("event.getInitiator().getId() = {}", event.getInitiator().getId());
        log.info("userId = {}", userId);
        log.info("event.getState() = {}", event.getState());

        if (Objects.equals(event.getInitiator().getId(), userId) || event.getState() != PUBLISHED) {
            throw new BadRequestException("User with id=" + userId + " can't participate in event with id="
                    + event.getId() + " because he is initiator or event is not published. " +
                    "Event state = " + event.getState() + ". " +
                    "User id = " + userId + ". " +
                    "Event initiator id = " + event.getInitiator().getId());
        }
    }

    private void validateRepeatRequest(User user, Event event) {
        boolean exists = requestRepository.existsByRequesterAndEvent(user, event);

        if (exists) {
            throw new ConflictException("User with id=" + user.getId() +
                    " has already requested participation in event with id=" + event.getId());
        }
    }


    private void validateParticipantLimit(Event event, Long eventId) {
        Integer participantLimit = event.getParticipantLimit();

        log.info("participantLimit = {}", participantLimit);
        log.info("requestRepository.countByEventIdAndStatus(eventId, PUBLISHED) = {}",
                requestRepository.countByEventIdAndStatus(eventId, PUBLISHED));

        if (participantLimit > 0 && participantLimit <= requestRepository.countByEventIdAndStatus(eventId, PUBLISHED)) {
            throw new BadRequestException("Event with id=" + eventId + " has reached participant limit. " +
                    "Participant limit = " + participantLimit + ". " +
                    "Current participants count = " + requestRepository.countByEventIdAndStatus(eventId, PUBLISHED) + ".");
        }
    }
}
