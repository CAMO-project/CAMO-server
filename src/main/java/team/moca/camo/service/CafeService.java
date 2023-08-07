package team.moca.camo.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.moca.camo.api.KakaoLocalApiService;
import team.moca.camo.api.dto.KakaoAddressResponse;
import team.moca.camo.controller.dto.CafePreviewListResponse;
import team.moca.camo.domain.Cafe;
import team.moca.camo.domain.value.Coordinates;
import team.moca.camo.repository.CafeLocationRepository;
import team.moca.camo.repository.CafeRepository;

import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
public class CafeService {

    private final CafeRepository cafeRepository;
    private final CafeLocationRepository cafeLocationRepository;
    private final KakaoLocalApiService kakaoLocalApiService;

    public CafeService(CafeRepository cafeRepository, CafeLocationRepository cafeLocationRepository, KakaoLocalApiService kakaoLocalApiService) {
        this.cafeRepository = cafeRepository;
        this.cafeLocationRepository = cafeLocationRepository;
        this.kakaoLocalApiService = kakaoLocalApiService;
    }

    public List<CafePreviewListResponse> getNearbyCafePreviewList(Coordinates userCoordinates) {
        KakaoAddressResponse userAddress = kakaoLocalApiService.coordinatesToAddress(userCoordinates);
        String region2depthName = userAddress.getRegion2depthName();

        List<Cafe> nearbyCafes = cafeRepository.findByTown(region2depthName);
        List<CafePreviewListResponse> cafePreviewList = nearbyCafes.stream()
                .map(CafePreviewListResponse::of)
                .collect(Collectors.toList());
        return cafePreviewList;
    }
}
