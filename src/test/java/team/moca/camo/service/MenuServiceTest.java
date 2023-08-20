package team.moca.camo.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import team.moca.camo.TestInstanceFactory;
import team.moca.camo.controller.dto.PageDto;
import team.moca.camo.controller.dto.response.MenuListResponse;
import team.moca.camo.domain.Cafe;
import team.moca.camo.domain.Menu;
import team.moca.camo.domain.User;
import team.moca.camo.repository.CafeRepository;
import team.moca.camo.repository.MenuRepository;
import team.moca.camo.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@Slf4j
@DisplayName("메뉴 서비스 테스트")
@ExtendWith(value = MockitoExtension.class)
class MenuServiceTest {

    @InjectMocks
    private MenuService menuService;

    @Mock
    private MenuRepository menuRepository;
    @Mock
    private CafeRepository cafeRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private AuthenticationUserFactory authenticationUserFactory;

    @DisplayName("카페 ID를 통해 해당 카페의 대표 메뉴를 조회할 수 있다.")
    @Test
    void getSignatureMenusOfCafeSuccess() throws Exception {
        // given
        User testUser = TestInstanceFactory.getTestUser();
        Cafe testCafe = TestInstanceFactory.getTestCafe();

        // when
        when(authenticationUserFactory.getAuthenticatedUserOrGuestUserWithFindOption(eq(testUser.getId()), any(Function.class)))
                .thenReturn(testUser);
        when(cafeRepository.findById(testCafe.getId())).thenReturn(Optional.of(testCafe));
        Menu testMenu = Menu.builder().name("test menu 1").price(10_000).build();
        when(menuRepository.findByCafeAndIsSignature(eq(testCafe), any(Boolean.class), any()))
                .thenReturn(new PageImpl<>(List.of(testMenu, Menu.builder().name("test menu 2").price(5_000).build())));
        List<MenuListResponse> signatureMenus =
                menuService.getSignatureMenuListOfCafe(testCafe.getId(), testUser.getId());

        // then
        assertThat(signatureMenus).isNotNull();
        assertThat(signatureMenus.size()).isEqualTo(2);
        assertThat(signatureMenus.get(0).getMenuName()).isEqualTo(testMenu.getName());
    }

    @DisplayName("카페 ID를 통해 해당 카페의 일반 메뉴 목록을 조회할 수 있다.")
    @Test
    void getBasicMenusOfCafeSuccess() throws Exception {
        // given
        User testUser = TestInstanceFactory.getTestUser();
        Cafe testCafe = TestInstanceFactory.getTestCafe();

        // when
        when(authenticationUserFactory.getAuthenticatedUserOrGuestUserWithFindOption(eq(testUser.getId()), any(Function.class)))
                .thenReturn(testUser);
        when(cafeRepository.findById(testCafe.getId())).thenReturn(Optional.of(testCafe));
        Menu testMenu = Menu.builder().name("test menu 1").price(10_000).build();
        when(menuRepository.findByCafeAndIsSignature(eq(testCafe), any(Boolean.class), any()))
                .thenReturn(new PageImpl<>(List.of(testMenu, Menu.builder().name("test menu 2").price(5_000).build())));
        PageDto pageDto = PageDto.of(0);
        List<MenuListResponse> basicMenus =
                menuService.getBasicMenuListOfCafe(testCafe.getId(), testUser.getId(), pageDto);

        // then
        assertThat(basicMenus).isNotNull();
        assertThat(basicMenus.size()).isEqualTo(2);
        assertThat(basicMenus.get(0).getMenuName()).isEqualTo(testMenu.getName());
        assertThat(pageDto.getTotalPages()).isEqualTo(1);
    }
}