package team.moca.camo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import team.moca.camo.common.annotation.Authenticate;
import team.moca.camo.controller.dto.PageDto;
import team.moca.camo.controller.dto.PageResponseDto;
import team.moca.camo.controller.dto.request.CafeRequest;
import team.moca.camo.controller.dto.response.CafeListResponse;
import team.moca.camo.domain.User;
import team.moca.camo.domain.value.Coordinates;
import team.moca.camo.service.CafeService;

import java.util.List;

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
            @RequestBody Coordinates coordinates, @RequestParam(name = "page", defaultValue = "0") int page
    ) {
        PageDto pageDto = PageDto.of(page);
        List<CafeListResponse> nearbyCafeList = cafeService.getNearbyCafeList(coordinates, authenticatedAccountId, pageDto);
        return PageResponseDto.of(nearbyCafeList, "User's nearby cafe list.", pageDto);
    }

    @GetMapping("/new")
    public void RegisterCafe(@RequestBody CafeRequest cafeRequest, User owner) {
        cafeService.createCafe(cafeRequest, owner);
    }
}
