package team.moca.camo.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team.moca.camo.controller.dto.ResponseDto;
import team.moca.camo.controller.dto.response.EventListResponse;
import team.moca.camo.service.EventService;

import java.util.List;

@Slf4j
@RequestMapping("/api/events")
@RestController
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("/{cafe_id}")
    public ResponseDto<List<EventListResponse>> eventListOfSpecificCafe(@PathVariable(name = "cafe_id") String cafeId) {
        List<EventListResponse> eventList = eventService.getEventListOfCafe(cafeId);
        log.info("Event list of cafe [{}]", cafeId);
        return ResponseDto.of(eventList, String.format("Event list of cafe [%s]", cafeId));
    }
}
