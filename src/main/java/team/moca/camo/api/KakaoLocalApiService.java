package team.moca.camo.api;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import team.moca.camo.api.dto.KakaoAddressResponse;
import team.moca.camo.api.dto.KakaoLocalResponseDto;
import team.moca.camo.domain.value.Coordinates;
import team.moca.camo.exception.BusinessException;
import team.moca.camo.exception.error.ExternalApiError;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class KakaoLocalApiService {

    private static final String KAKAO_LOCAL_API_HOST = "https://dapi.kakao.com";
    private static final String COORDINATES_TO_ADDRESS_ENDPOINT = "/v2/local/geo/coord2regioncode";
    private static final String TOKEN_PREFIX = "KakaoAK ";

    private final RestTemplate restTemplate;

    public KakaoLocalApiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public KakaoAddressResponse coordinatesToAddress(Coordinates coordinates) {
        RequestEntity<Void> request = generateRequestEntity(COORDINATES_TO_ADDRESS_ENDPOINT, coordinates);
        ResponseEntity<KakaoLocalResponseDto<KakaoAddressResponse>> response;
        try {
            response = restTemplate.exchange(request, new ParameterizedTypeReference<>() {
            });
        } catch (HttpClientErrorException.Unauthorized e) {
            throw new BusinessException(ExternalApiError.KAKAO_API_REQUEST_FAIL);
        }

        KakaoLocalResponseDto<KakaoAddressResponse> responseBody = response.getBody();
        List<KakaoAddressResponse> addressList = responseBody.getDocuments();

        for (KakaoAddressResponse address : addressList) {
            if (address.getRegionType().equals("B")) {
                return address;
            }
        }

        return null;
    }

    private RequestEntity<Void> generateRequestEntity(String endpoint, Coordinates coordinates) {
        URI uri = UriComponentsBuilder.fromUriString(KAKAO_LOCAL_API_HOST + endpoint)
                .queryParam("x", coordinates.getLongitude())
                .queryParam("y", coordinates.getLatitude())
                .build()
                .encode(StandardCharsets.UTF_8)
                .toUri();
        return new RequestEntity<>(HttpMethod.GET, uri);
    }
}
