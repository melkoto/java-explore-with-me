package ru.practicum.main.event.mapper;

import lombok.Data;
import ru.practicum.main.event.dto.LocationDto;
import ru.practicum.main.event.model.Location;

@Data
public class LocationMapper {

    public static Location dtoToLocation(LocationDto dto) {
        Location location = new Location();
        location.setLat(dto.getLat());
        location.setLon(dto.getLon());

        return location;
    }

    public static LocationDto locationToDto(Location location) {
        LocationDto dto = new LocationDto();
        dto.setLat(location.getLat());
        dto.setLon(location.getLon());

        return dto;
    }
}
