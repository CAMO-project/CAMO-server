package team.moca.camo.api;

import lombok.Getter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import team.moca.camo.exception.BusinessException;
import team.moca.camo.exception.error.AuthenticationError;

@Service
public class KakaoApiService {

    private static final String KAKAO_API_HOST = "https://kapi.kakao.com";
    private static final String TOKEN_INFORMATION_INQUIRY_API_ENDPOINT = "/v2/user/me";

    private final RestTemplate restTemplate;

    public KakaoApiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getKakaoAccountId(String token) {
        String requestUrl = KAKAO_API_HOST + TOKEN_INFORMATION_INQUIRY_API_ENDPOINT;
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);

        ResponseEntity<KakaoTokenResponse> response =
                restTemplate.getForEntity(requestUrl, KakaoTokenResponse.class, httpHeaders);
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
}

@Getter
class KakaoTokenResponse {

    private String id;
}
