package ru.practicum.main.request.mapper;

import ru.practicum.main.event.model.Event;
import ru.practicum.main.request.dto.EventRequestStatusUpdateResponseDto;
import ru.practicum.main.request.dto.ParticipationRequestDto;
import ru.practicum.main.request.enums.Status;
import ru.practicum.main.request.model.Request;
import ru.practicum.main.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.main.request.enums.Status.*;

public class RequestMapper {

    public static Request toRequest(User user, Event event, Status status) {
        Request request = new Request();

        request.setCreated(LocalDateTime.now());
        request.setEvent(event);
        request.setRequester(user);
        request.setStatus(status);

        return request;
    }

    public static Request toRequest(User user, Event event) {
        Request request = new Request();
        request.setRequester(user);
        request.setEvent(event);
        request.setCreated(LocalDateTime.now());
        if (event.getRequestModeration()) {
            request.setStatus(PENDING);
        } else {
            request.setStatus(CONFIRMED);
        }
        return request;
    }

    public static ParticipationRequestDto toParticipationRequestDto(Request request) {
        ParticipationRequestDto dto = new ParticipationRequestDto();
        dto.setId(request.getId());
        dto.setCreated(request.getCreated());
        dto.setEvent(request.getEvent().getId());
        dto.setRequester(request.getRequester().getId());
        dto.setStatus(request.getStatus());

        return dto;
    }

    public static EventRequestStatusUpdateResponseDto toEventRequestStatusUpdateResult(List<ParticipationRequestDto> confirmedRequests,
                                                                                       List<ParticipationRequestDto> rejectedRequests) {
        EventRequestStatusUpdateResponseDto result = new EventRequestStatusUpdateResponseDto();
        result.setConfirmedRequests(confirmedRequests);
        result.setRejectedRequests(rejectedRequests);

        return result;
    }
}
