package team.moca.camo.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import team.moca.camo.common.annotation.Authenticate;
import team.moca.camo.controller.dto.PageDto;
import team.moca.camo.controller.dto.PageResponseDto;
import team.moca.camo.controller.dto.ResponseDto;
import team.moca.camo.controller.dto.request.CafeRequest;
import team.moca.camo.controller.dto.response.CafeDetailsResponse;
import team.moca.camo.controller.dto.response.CafeListResponse;
import team.moca.camo.domain.Cafe;
import team.moca.camo.domain.User;
import team.moca.camo.domain.value.Coordinates;
import team.moca.camo.repository.CafeRepository;
import team.moca.camo.service.CafeService;

import java.util.List;

@Slf4j
@RequestMapping("/api/cafes")
@RestController
public class CafeController {

    private final CafeService cafeService;

    @Autowired
    private CafeRepository cafeRepository;

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

    @PostMapping("/new")
    public void RegisterCafe(@RequestBody CafeRequest cafeRequest) {
        cafeService.createCafe(cafeRequest);
    }
//
//    @PutMapping("/update/{id}")
//    public void updateCafe(@PathVariable("id") String cafeId, @RequestBody CafeRequest cafeRequest, User owner) {
//        cafeService.updateCafe(cafeId, cafeRequest, owner);
//    }
//
//    @DeleteMapping("/delete/{id}")
//    public void deleteCafe(@PathVariable("id") String cafeId, User owner) {
//        cafeService.deleteCafe(cafeId, owner);
//    }
}
