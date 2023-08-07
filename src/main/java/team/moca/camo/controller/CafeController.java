package team.moca.camo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team.moca.camo.common.annotation.Authenticate;
import team.moca.camo.controller.dto.CafeListResponse;
import team.moca.camo.controller.dto.ResponseDto;
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

    @GetMapping("")
    public ResponseDto<List<CafeListResponse>> userNearbyCafeList(
            @Authenticate String authenticatedAccountId, @RequestBody Coordinates coordinates
    ) {
        List<CafeListResponse> nearbyCafeList = cafeService.getNearbyCafeList(coordinates, authenticatedAccountId);
        return ResponseDto.of(nearbyCafeList, "User's nearby cafe list.");
    }
}
