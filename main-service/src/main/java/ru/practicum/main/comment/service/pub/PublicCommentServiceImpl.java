package ru.practicum.main.comment.service.pub;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.main.comment.dto.ShortCommentResponseDto;
import ru.practicum.main.comment.repository.PublicCommentRepository;
import ru.practicum.main.event.repository.PublicEventRepository;

import java.util.List;

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
    public List<ShortCommentResponseDto> getEventComments(Long eventId, Integer page, Integer size) {
        return null;
    }
}
