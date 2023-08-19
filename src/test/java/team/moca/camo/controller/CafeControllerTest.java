package team.moca.camo.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHeaders;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import team.moca.camo.TestInstanceFactory;
import team.moca.camo.config.ObjectMapperConfig;
import team.moca.camo.controller.dto.PageDto;
import team.moca.camo.controller.dto.response.CafeListResponse;
import team.moca.camo.domain.Cafe;
import team.moca.camo.domain.User;
import team.moca.camo.domain.value.Coordinates;
import team.moca.camo.security.jwt.JwtUtils;
import team.moca.camo.service.CafeService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@DisplayName("카페 컨트롤러 테스트")
@WithMockUser
@Import(value = ObjectMapperConfig.class)
@WebMvcTest(controllers = CafeController.class)
class CafeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CafeService cafeService;
    @MockBean
    private JwtUtils jwtUtils;

    @DisplayName("인증된 사용자의 주변 카페 목록 조회 요청에 성공한다.")
    @Test
    void nearbyCafeListSuccess() throws Exception {
        // given
        User testUser = TestInstanceFactory.getTestUserInstance();
        Cafe testCafe = TestInstanceFactory.getTestCafeInstance();
        double latitude = 37.12345678;
        double longitude = 127.12345678;

        // when
        when(jwtUtils.extractAccountIdFromToken(anyString())).thenReturn(testUser.getId());
        when(cafeService.getNearbyCafeList(any(Coordinates.class), any(String.class), any(PageDto.class)))
                .thenReturn(List.of(CafeListResponse.of(testCafe, true)));
        ResultActions resultActions =
                mockMvc.perform(get("/api/cafes/nearby")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer token")
                        .param("page", "0")
                        .param("latitude", String.valueOf(latitude))
                        .param("longitude", String.valueOf(longitude)));

        // then
        resultActions.andExpectAll(
                        status().isOk(),
                        jsonPath("$.data.body.[0].cafe_name").value(testCafe.getName()),
                        jsonPath("$.data.body.[0].address")
                                .value(String.join(" ", testCafe.getAddress().getCity(), testCafe.getAddress().getTown())),
                        jsonPath("$.data.body.[0].favorite").value(true)
                )
                .andDo(print());
    }

    @DisplayName("인증되지 않은 사용자의 주변 카페 목록 조회 요청에 성공한다.")
    @Test
    void nearbyCafeListGuestUserSuccess() throws Exception {
        // given
        Cafe testCafe = TestInstanceFactory.getTestCafeInstance();
        double latitude = 37.12345678;
        double longitude = 127.12345678;

        // when
        when(jwtUtils.extractAccountIdFromToken(anyString())).thenReturn(null);
        when(cafeService.getNearbyCafeList(any(Coordinates.class), eq(null), any(PageDto.class)))
                .thenReturn(List.of(CafeListResponse.of(testCafe, false)));
        ResultActions resultActions =
                mockMvc.perform(get("/api/cafes/nearby")
                        .param("page", "0")
                        .param("latitude", String.valueOf(latitude))
                        .param("longitude", String.valueOf(longitude)));

        // then
        resultActions.andExpectAll(
                        status().isOk(),
                        jsonPath("$.data.body.[0].cafe_name").value(testCafe.getName()),
                        jsonPath("$.data.body.[0].address")
                                .value(String.join(" ", testCafe.getAddress().getCity(), testCafe.getAddress().getTown())),
                        jsonPath("$.data.body.[0].favorite").value(false)
                )
                .andDo(print());
    }

    @DisplayName("위도 또는 경도가 누락된 경우 요청에 실패한다.")
    @Test
    void nearbyCafeListFailWhenMissingCoordinates() throws Exception {
        // given
        User testUser = TestInstanceFactory.getTestUserInstance();
        Cafe testCafe = TestInstanceFactory.getTestCafeInstance();
        double latitude = 37.12345678;
        double longitude = 127.12345678;

        // when
        when(jwtUtils.extractAccountIdFromToken(anyString())).thenReturn(testUser.getId());
        when(cafeService.getNearbyCafeList(any(Coordinates.class), eq(testUser.getId()), any(PageDto.class)))
                .thenReturn(List.of(CafeListResponse.of(testCafe, true)));
        ResultActions missingLatitudeResultActions =
                mockMvc.perform(get("/api/cafes/nearby")
                        .param("page", "0")
                        .param("longitude", String.valueOf(longitude)));
        ResultActions missingLongitudeResultActions =
                mockMvc.perform(get("/api/cafes/nearby")
                        .param("page", "0")
                        .param("latitude", String.valueOf(latitude)));

        // then
        missingLatitudeResultActions.andExpectAll(
                        status().isBadRequest()
                )
                .andDo(print());
        missingLongitudeResultActions.andExpectAll(
                        status().isBadRequest()
                )
                .andDo(print());
    }
}