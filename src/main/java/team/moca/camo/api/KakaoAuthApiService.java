package team.moca.camo.api;

import lombok.Getter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import team.moca.camo.exception.BusinessException;
import team.moca.camo.exception.error.AuthenticationError;

import java.net.URI;
import java.nio.charset.StandardCharsets;

@Service
public class KakaoAuthApiService {

    private static final String KAKAO_AUTH_API_HOST = "https://kapi.kakao.com";
    private static final String TOKEN_INFORMATION_INQUIRY_API_ENDPOINT = "/v2/user/me";
    private static final String TOKEN_PREFIX = "Bearer ";

    private final RestTemplate restTemplate;

    public KakaoAuthApiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getKakaoAccountId(String token) {
        RequestEntity<Void> request = generateRequestEntity(token);
        ResponseEntity<KakaoTokenResponse> response;
        try {
            response = restTemplate.exchange(request, KakaoTokenResponse.class);
        } catch (HttpClientErrorException.Unauthorized e) {
            throw new BusinessException(AuthenticationError.USER_AUTHENTICATION_FAIL);
        }

        KakaoTokenResponse kakaoTokenResponse = response.getBody();
        if (kakaoTokenResponse == null) {
            throw new BusinessException(AuthenticationError.USER_AUTHENTICATION_FAIL);
        }
        return kakaoTokenResponse.getKakaoId();
    }

    private RequestEntity<Void> generateRequestEntity(String token) {
        URI uri = UriComponentsBuilder.fromUriString(KAKAO_AUTH_API_HOST + TOKEN_INFORMATION_INQUIRY_API_ENDPOINT)
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

    private String kakaoId;
}
