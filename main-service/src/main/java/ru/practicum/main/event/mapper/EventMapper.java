package ru.practicum.main.event.mapper;

import lombok.Data;
import ru.practicum.main.error.BadRequestException;
import ru.practicum.main.event.dto.CreateEventDto;
import ru.practicum.main.event.dto.FullEventResponseDto;
import ru.practicum.main.event.dto.ShortEventResponseDto;
import ru.practicum.main.event.dto.UpdateEventDto;
import ru.practicum.main.event.eventEnums.State;
import ru.practicum.main.event.model.Event;
import ru.practicum.main.event.model.Location;
import ru.practicum.main.user.model.User;

import java.time.LocalDateTime;

import static ru.practicum.main.category.mapper.CategoryMapper.mapCategoryResponseDtoToCategory;
import static ru.practicum.main.category.mapper.CategoryMapper.mapCategoryToCategoryResponseDto;
import static ru.practicum.main.event.mapper.LocationMapper.locationToDto;
import static ru.practicum.main.user.mapper.UserMapper.userToUserResponseDto;

@Data
public class EventMapper {
    public static <T extends UpdateEventDto> Event updateDtoToEvent(T updateDto, Event event,
                                                                    Location location) {
        if (updateDto.getAnnotation() != null) {
            event.setAnnotation(updateDto.getAnnotation());
        }

        if (updateDto.getEventDate() != null) {
            if (updateDto.getEventDate().isBefore(event.getEventDate())) {
                throw new BadRequestException("Event date cannot be changed to earlier date");
            }
            event.setEventDate(updateDto.getEventDate());
        }

        if (updateDto.getPaid() != null) {
            event.setPaid(updateDto.getPaid());
        }

        if (updateDto.getTitle() != null) {
            event.setTitle(updateDto.getTitle());
        }

        if (updateDto.getParticipantLimit() != null) {
            event.setParticipantLimit(updateDto.getParticipantLimit());
        }

        if (updateDto.getRequestModeration() != null) {
            event.setRequestModeration(updateDto.getRequestModeration());
        }

        if (updateDto.getDescription() != null) {
            event.setDescription(updateDto.getDescription());
        }

        event.setLocation(location);

        return event;
    }

    public static FullEventResponseDto toEventDto(Event event) {
        FullEventResponseDto eventDto = new FullEventResponseDto();
        eventDto.setId(event.getId());
        eventDto.setTitle(event.getTitle());
        eventDto.setAnnotation(event.getAnnotation());
        eventDto.setCategory(mapCategoryToCategoryResponseDto(event.getCategory()));
        eventDto.setPaid(event.getPaid());
        eventDto.setEventDate(event.getEventDate());
        eventDto.setInitiator(userToUserResponseDto(event.getInitiator()));
        eventDto.setDescription(event.getDescription());
        eventDto.setParticipantLimit(event.getParticipantLimit());
        eventDto.setState(event.getState());
        eventDto.setCreatedOn(LocalDateTime.now());
        eventDto.setLocation(locationToDto(event.getLocation()));
        eventDto.setRequestModeration(event.getRequestModeration());
        eventDto.setViews(0L);
        return eventDto;
    }

    public static FullEventResponseDto toEventFullDto(Event event, Long views, Integer confirmedRequests) {
        FullEventResponseDto eventFullDto = new FullEventResponseDto();
        eventFullDto.setAnnotation(event.getAnnotation());
        eventFullDto.setCategory(mapCategoryToCategoryResponseDto(event.getCategory()));
        eventFullDto.setConfirmedRequests(confirmedRequests);
        eventFullDto.setCreatedOn(event.getCreatedOn());
        eventFullDto.setDescription(event.getDescription());
        eventFullDto.setEventDate(event.getEventDate());
        eventFullDto.setId(event.getId());
        eventFullDto.setInitiator(userToUserResponseDto(event.getInitiator()));
        eventFullDto.setLocation(locationToDto(event.getLocation()));
        eventFullDto.setPaid(event.getPaid());
        eventFullDto.setParticipantLimit(event.getParticipantLimit());
        eventFullDto.setPublishedOn(event.getPublishedOn());
        eventFullDto.setState(event.getState());
        eventFullDto.setTitle(event.getTitle());
        eventFullDto.setViews(views);
        eventFullDto.setRequestModeration(event.getRequestModeration());
        return eventFullDto;
    }

    public static Event createDtoToEvent(CreateEventDto newEvent, Location location, User user) {
        if (newEvent == null) {
            return null;
        }
        Event event = new Event();
        event.setAnnotation(newEvent.getAnnotation());
        event.setCategory(mapCategoryResponseDtoToCategory(newEvent.getCategory()));
        event.setDescription(newEvent.getDescription());
        event.setEventDate(newEvent.getEventDate());
        event.setLocation(location);
        event.setRequestModeration(newEvent.getRequestModeration());
        event.setTitle(newEvent.getTitle());
        event.setInitiator(user);
        event.setState(State.PENDING);
        event.setCreatedOn(LocalDateTime.now());

        if (newEvent.getParticipantLimit() == null) {
            event.setParticipantLimit(0);
        } else {
            event.setParticipantLimit(newEvent.getParticipantLimit());
        }

        if (newEvent.getPaid() == null) {
            event.setPaid(false);
        } else {
            event.setPaid(newEvent.getPaid());
        }

        return event;
    }

    public static ShortEventResponseDto toEventShortDto(Event event, Integer confirmedRequests, Long views) {
        ShortEventResponseDto shortEventResponseDto = toEventShortDto(event);
        shortEventResponseDto.setConfirmedRequests(confirmedRequests);
        shortEventResponseDto.setViews(views);
        return shortEventResponseDto;
    }

    public static ShortEventResponseDto toEventShortDto(Event event) {
        ShortEventResponseDto shortEventResponseDto = new ShortEventResponseDto();
        shortEventResponseDto.setId(event.getId());
        shortEventResponseDto.setTitle(event.getTitle());
        shortEventResponseDto.setAnnotation(event.getAnnotation());
        shortEventResponseDto.setCategory(mapCategoryToCategoryResponseDto(event.getCategory()));
        shortEventResponseDto.setPaid(event.getPaid());
        shortEventResponseDto.setEventDate(event.getEventDate());
        shortEventResponseDto.setInitiator(userToUserResponseDto(event.getInitiator()));
        return shortEventResponseDto;
    }
}
