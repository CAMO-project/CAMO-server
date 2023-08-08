package team.moca.camo.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import team.moca.camo.api.dto.KakaoAddressResponse;
import team.moca.camo.api.dto.KakaoCoordinatesResponse;
import team.moca.camo.api.dto.KakaoLocalResponseDto;
import team.moca.camo.domain.value.Coordinates;
import team.moca.camo.exception.BusinessException;
import team.moca.camo.exception.error.ExternalApiError;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
@Service
public class KakaoLocalApiService {

    private static final String KAKAO_LOCAL_API_HOST = "https://dapi.kakao.com";
    private static final String COORDINATES_TO_ADDRESS_ENDPOINT = "/v2/local/geo/coord2regioncode";
    private static final String TOKEN_PREFIX = "KakaoAK ";
    private static final String ADDRESS_TO_COORDINATES_ENDPOINT = "/v2/local/search/address";

    private final KakaoProperties kakaoProperties;
    private final RestTemplate restTemplate;

    public KakaoLocalApiService(KakaoProperties kakaoProperties, RestTemplate restTemplate) {
        this.kakaoProperties = kakaoProperties;
        this.restTemplate = restTemplate;
    }

    public KakaoAddressResponse coordinatesToAddress(final Coordinates coordinates) {
        RequestEntity<Void> request = generateRequestEntity(coordinates);
        ResponseEntity<KakaoLocalResponseDto<KakaoAddressResponse>> response;
        try {
            response = restTemplate.exchange(request, new ParameterizedTypeReference<>() {
            });
        } catch (HttpClientErrorException.Unauthorized e) {
            throw new BusinessException(ExternalApiError.KAKAO_API_REQUEST_FAIL);
        }

        KakaoLocalResponseDto<KakaoAddressResponse> responseBody = response.getBody();
        assert responseBody != null;
        List<KakaoAddressResponse> addressList = responseBody.getDocuments();

        for (KakaoAddressResponse address : addressList) {
            if (address.getRegionType().equals("B")) {
                return address;
            }
        }

        throw new BusinessException(ExternalApiError.EMPTY_RESULT);
    }

    public Coordinates addressToCoordinates(final String address) {
        RequestEntity<Void> request = generateRequestEntity(address);
        ResponseEntity<KakaoLocalResponseDto<KakaoCoordinatesResponse>> response;
        try {
            response = restTemplate.exchange(request, new ParameterizedTypeReference<>() {
            });
        } catch (HttpClientErrorException.Unauthorized e) {
            throw new BusinessException(ExternalApiError.KAKAO_API_REQUEST_FAIL);
        }

        KakaoLocalResponseDto<KakaoCoordinatesResponse> responseBody = response.getBody();
        assert responseBody != null;
        KakaoCoordinatesResponse kakaoCoordinatesResponse = responseBody.getDocuments().get(0);
        return kakaoCoordinatesResponse.getCoordinates();
    }

    private RequestEntity<Void> generateRequestEntity(final Coordinates coordinates) {
        HttpHeaders headers = generateAuthorizationHeaders();
        URI uri = UriComponentsBuilder.fromUriString(KAKAO_LOCAL_API_HOST
                        .concat(KakaoLocalApiService.COORDINATES_TO_ADDRESS_ENDPOINT))
                .queryParam("x", coordinates.getLongitude())
                .queryParam("y", coordinates.getLatitude())
                .build()
                .encode(StandardCharsets.UTF_8)
                .toUri();
        return new RequestEntity<>(headers, HttpMethod.GET, uri);
    }

    private RequestEntity<Void> generateRequestEntity(final String address) {
        HttpHeaders headers = generateAuthorizationHeaders();
        URI uri = UriComponentsBuilder.fromUriString(KAKAO_LOCAL_API_HOST
                        .concat(KakaoLocalApiService.ADDRESS_TO_COORDINATES_ENDPOINT))
                .queryParam("query", address)
                .build()
                .encode(StandardCharsets.UTF_8)
                .toUri();
        return new RequestEntity<>(headers, HttpMethod.GET, uri);
    }

    private HttpHeaders generateAuthorizationHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, TOKEN_PREFIX.concat(kakaoProperties.getApiKey()));
        return headers;
    }
}
