package team.moca.camo.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import team.moca.camo.common.annotation.Authenticate;
import team.moca.camo.controller.dto.PageDto;
import team.moca.camo.controller.dto.PageResponseDto;
import team.moca.camo.controller.dto.ResponseDto;
import team.moca.camo.controller.dto.response.CafeDetailsResponse;
import team.moca.camo.controller.dto.response.CafeListResponse;
import team.moca.camo.controller.dto.value.SortType;
import team.moca.camo.domain.value.Coordinates;
import team.moca.camo.service.CafeService;

import java.util.List;

@Slf4j
@RequestMapping("/api/cafes")
@RestController
public class CafeController {

    private final CafeService cafeService;

    public CafeController(CafeService cafeService) {
        this.cafeService = cafeService;
    }

    @GetMapping("/nearby")
    public PageResponseDto<List<CafeListResponse>> userNearbyCafeList(
            @Authenticate(required = false) String authenticatedAccountId,
            @ModelAttribute Coordinates coordinates, @RequestParam(name = "page", defaultValue = "0") int page
    ) {
        PageDto pageDto = PageDto.of(page);
        List<CafeListResponse> nearbyCafeList = cafeService.getNearbyCafeList(coordinates, authenticatedAccountId, pageDto);
        return PageResponseDto.of(nearbyCafeList, "User's nearby cafe list.", pageDto);
    }

    @GetMapping("/{cafe_id}")
    public ResponseDto<CafeDetailsResponse> cafeDetails(
            @Authenticate(required = false) String authenticatedAccountId,
            @PathVariable(name = "cafe_id") String cafeId
    ) {
        CafeDetailsResponse cafeDetailsResponse =
                cafeService.getCafeDetailsInformation(cafeId, authenticatedAccountId);
        log.info("Cafe details information of [{}]", cafeId);
        return ResponseDto.of(cafeDetailsResponse, String.format("Cafe details information of [%s]", cafeId));
    }

    @GetMapping("")
    public PageResponseDto<List<CafeListResponse>> cafeListOrderByDistance(
            @Authenticate(required = false) String authenticatedAccountId,
            @ModelAttribute Coordinates coordinates,
            @RequestParam(name = "sort", defaultValue = "RATING") SortType sortType,
            @RequestParam(name = "page", defaultValue = "0") int page
    ) {
        PageDto pageDto = PageDto.of(page);
        List<CafeListResponse> sortedCafeList =
                cafeService.getSortedCafeList(coordinates, sortType, authenticatedAccountId, pageDto);
        return PageResponseDto.of(sortedCafeList, String.format("Cafe list sorted by %s", sortType.getSortPropertyName()), pageDto);
    }
}
