package team.moca.camo.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.moca.camo.controller.dto.response.EventListResponse;
import team.moca.camo.domain.Cafe;
import team.moca.camo.domain.Event;
import team.moca.camo.exception.BusinessException;
import team.moca.camo.exception.error.ClientRequestError;
import team.moca.camo.repository.CafeRepository;
import team.moca.camo.repository.EventRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
public class EventService {

    private final EventRepository eventRepository;
    private final CafeRepository cafeRepository;

    public EventService(EventRepository eventRepository, CafeRepository cafeRepository) {
        this.eventRepository = eventRepository;
        this.cafeRepository = cafeRepository;
    }

    public List<EventListResponse> getEventListOfCafe(final String cafeId) {
        Cafe cafe = cafeRepository.findById(cafeId)
                .orElseThrow(() -> new BusinessException(ClientRequestError.NON_EXISTENT_CAFE));
        List<Event> events = eventRepository.findByCafe(cafe);
        return events.stream()
                .filter(this::isOngoingEvent)
                .map(event -> EventListResponse.of(event, cafe))
                .collect(Collectors.toList());
    }

    private boolean isOngoingEvent(final Event event) {
        return event.getEventStart().isBefore(LocalDateTime.now()) &&
                event.getEventEnd().isAfter(LocalDateTime.now());
    }
}
