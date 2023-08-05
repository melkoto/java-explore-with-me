package ru.practicum.main.comment.service.pub;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.main.comment.dto.ShortCommentResponseDto;
import ru.practicum.main.comment.mapper.CommentMapper;
import ru.practicum.main.comment.repository.PublicCommentRepository;
import ru.practicum.main.error.NotFoundException;
import ru.practicum.main.event.repository.PublicEventRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PublicCommentServiceImpl implements PublicCommentService {
    private final PublicCommentRepository commentRepository;
    private final PublicEventRepository eventRepository;

    public PublicCommentServiceImpl(PublicCommentRepository commentRepository, PublicEventRepository eventRepository) {
        this.commentRepository = commentRepository;
        this.eventRepository = eventRepository;

    }

    @Override
    public List<ShortCommentResponseDto> getEventComments(Long eventId, Integer from, Integer size) {
        if (!eventRepository.existsById(eventId)) {
            throw new NotFoundException("Event with id " + eventId + " not found.");
        }

        log.info("Found {} comments for event with id {}", commentRepository.countByEventId(eventId), eventId);

        return commentRepository.findByEventId(eventId, PageRequest.of(from, size))
                .stream()
                .map(CommentMapper::mapToShortCommentResponseDto)
                .collect(Collectors.toList());
    }
}
