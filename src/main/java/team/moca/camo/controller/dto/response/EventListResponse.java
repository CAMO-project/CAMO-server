package team.moca.camo.controller.dto.response;

import lombok.Builder;
import lombok.Getter;
import team.moca.camo.domain.Cafe;
import team.moca.camo.domain.Event;

import java.time.LocalDateTime;

@Getter
public class EventListResponse {

    private final String eventId;
    private final String eventTitle;
    private final String imageUrl;
    private final String eventUrl;
    private final LocalDateTime eventStart;
    private final LocalDateTime eventEnd;
    private final String cafeId;

    @Builder
    protected EventListResponse(String eventId, String eventTitle, String imageUrl, String eventUrl,
                                LocalDateTime eventStart, LocalDateTime eventEnd, String cafeId) {
        this.eventId = eventId;
        this.eventTitle = eventTitle;
        this.imageUrl = imageUrl;
        this.eventUrl = eventUrl;
        this.eventStart = eventStart;
        this.eventEnd = eventEnd;
        this.cafeId = cafeId;
    }

    public static EventListResponse of(Event event, Cafe cafe) {
        return EventListResponse.builder()
                .eventId(event.getId())
                .eventTitle(event.getTitle())
                .imageUrl(event.getImageUrl())
                .eventUrl(event.getEventUrl())
                .eventStart(event.getEventStart())
                .eventEnd(event.getEventEnd())
                .cafeId(cafe.getId())
                .build();
    }
}
