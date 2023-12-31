package ru.practicum.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.dto.RequestDto;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class StatsClient extends BaseClient {

    @Autowired
    public StatsClient(@Value("${stats-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                        .build()
        );
    }

    public ResponseEntity<Object> saveHit(String app, String uri, String ip, LocalDateTime time) {
        log.info("Saving hit for app: {}, uri: {}, ip: {}, time: {}", app, uri, ip, time);
        return post("/hit", new RequestDto(app, uri, ip, getFormattedTime(time)));
    }

    public ResponseEntity<Object> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        log.info("Getting stats for start: {}, end: {}, uris: {}, unique: {}", start, end, uris, unique);

        validateTime(start, end);
        String endpoint = buildStatsEndpoint(uris, unique);
        Map<String, Object> parameters = buildParameterMap(encode(start), encode(end));

        return get(endpoint, parameters);
    }

    public ResponseEntity<Object> getAllStats(List<String> uris) {
        String urisString = "";
        if (!(uris == null) && !uris.isEmpty()) {
            urisString = "&uris=" + String.join("&uris=", uris);
        }

        return get("/stats/hits?" + urisString, null);
    }

    private void validateTime(LocalDateTime start, LocalDateTime end) {
        log.info("Validating time for start: {}, end: {}", start, end);

        if (start == null || end == null || start.isAfter(end)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Invalid time: start: " + start + ", end: " + end);
        }
    }

    private String buildStatsEndpoint(List<String> uris, Boolean unique) {
        log.info("Building stats endpoint for uris: {}, unique: {}", uris, unique);

        String endpoint = "/stats?start={start}&end={end}";
        String urisParam = Optional.ofNullable(uris)
                .filter(u -> !u.isEmpty())
                .map(u -> u.stream()
                        .map(uri -> "&uris=" + uri)
                        .collect(Collectors.joining()))
                .orElse("");
        String uniqueParam = Optional.ofNullable(unique).map(u -> "&unique=" + u).orElse("");
        return endpoint + urisParam + uniqueParam;
    }

    private Map<String, Object> buildParameterMap(String start, String end) {
        return Map.of("start", start, "end", end);
    }

    private String encode(LocalDateTime time) {
        return URLEncoder.encode(getFormattedTime(time), StandardCharsets.UTF_8);
    }

    private String getFormattedTime(LocalDateTime start) {
        return start.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

}
