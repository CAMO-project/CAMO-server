package team.moca.camo.api;

import lombok.Getter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import team.moca.camo.exception.BusinessException;
import team.moca.camo.exception.error.AuthenticationError;

import java.net.URI;
import java.nio.charset.StandardCharsets;

@Service
public class KakaoApiService {

    private static final String KAKAO_API_HOST = "https://kapi.kakao.com";
    private static final String TOKEN_INFORMATION_INQUIRY_API_ENDPOINT = "/v2/user/me";
    private static final String TOKEN_PREFIX = "Bearer ";

    private final RestTemplate restTemplate;

    public KakaoApiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getKakaoAccountId(String token) {
        RequestEntity<Void> request = generateRequestEntity(token);
        ResponseEntity<KakaoTokenResponse> response = restTemplate.exchange(request, KakaoTokenResponse.class);

        HttpStatus statusCode = response.getStatusCode();
        if (!statusCode.is2xxSuccessful()) {
            throw new BusinessException(AuthenticationError.USER_AUTHENTICATION_FAIL);
        }

        KakaoTokenResponse kakaoTokenResponse = response.getBody();
        if (kakaoTokenResponse == null) {
            throw new BusinessException(AuthenticationError.USER_AUTHENTICATION_FAIL);
        }
        return kakaoTokenResponse.getId();
    }

    private RequestEntity<Void> generateRequestEntity(String token) {
        URI uri = UriComponentsBuilder.fromUriString(KAKAO_API_HOST + TOKEN_INFORMATION_INQUIRY_API_ENDPOINT)
                .build()
                .encode(StandardCharsets.UTF_8)
                .toUri();
        HttpHeaders headers = generateKakaoTokenApiHeader(token);
        return new RequestEntity<>(headers, HttpMethod.GET, uri);
    }

    private HttpHeaders generateKakaoTokenApiHeader(String token) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_TYPE,
                MediaType.APPLICATION_FORM_URLENCODED_VALUE + ";charset=" + StandardCharsets.UTF_8);
        httpHeaders.add(HttpHeaders.AUTHORIZATION, TOKEN_PREFIX + token);
        return httpHeaders;
    }
}

@Getter
class KakaoTokenResponse {

    private String id;
}
