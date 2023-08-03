package ru.practicum.main.utils;

import org.springframework.stereotype.Service;
import ru.practicum.client.StatsClient;
import ru.practicum.main.event.dto.ShortEventResponseDto;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventUtils {

    private final StatsClient statClient;

    public EventUtils(StatsClient statClient) {
        this.statClient = statClient;
    }


    public <T extends ShortEventResponseDto> List<T> fillViews(List<T> events) {
        List<String> listOfUris = events.stream()
                .map(T::getId)
                .map(Object::toString)
                .map(s -> "/events/" + s)
                .collect(Collectors.toList());

        Object bodyWithViews = statClient.getAllStats(listOfUris).getBody();

        return events.stream()
                .peek(event -> {
                    if (bodyWithViews instanceof LinkedHashMap && event.getId() != null) {
                        Object views = ((LinkedHashMap<?, ?>) bodyWithViews).get(event.getId().toString());
                        if (views != null) {
                            event.setViews(Long.parseLong(views.toString()));
                        } else {
                            event.setViews(0L);
                        }
                    }
                })
                .collect(Collectors.toList());
    }

    public <T extends ShortEventResponseDto> T setViews(T event) {
        Object bodyWithViews = statClient.getAllStats(List.of("/events/" + event.getId())).getBody();
        Object views;
        if (bodyWithViews instanceof LinkedHashMap && event.getId() != null) {
            views = ((LinkedHashMap<?, ?>) bodyWithViews).get(event.getId().toString());
            if (views != null) {
                event.setViews(Long.parseLong(views.toString()));
                return event;
            }
        }
        event.setViews(0L);
        return event;
    }
}
