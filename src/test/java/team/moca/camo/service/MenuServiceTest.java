package team.moca.camo.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import team.moca.camo.TestUtils;
import team.moca.camo.controller.dto.response.MenuListResponse;
import team.moca.camo.domain.Cafe;
import team.moca.camo.domain.Menu;
import team.moca.camo.domain.User;
import team.moca.camo.repository.CafeRepository;
import team.moca.camo.repository.MenuRepository;
import team.moca.camo.repository.UserRepository;

import java.util.List;
import java.util.Optional;

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

    @DisplayName("카페 ID를 통해 해당 카페의 대표 메뉴를 조회할 수 있다.")
    @Test
    void signatureMenusOfCafeSuccess() throws Exception {
        // given
        User testUser = TestUtils.getTestUserInstance();
        Cafe testCafe = TestUtils.getTestCafeInstance();

        // when
        when(userRepository.findWithLikeMenusById(testUser.getId())).thenReturn(Optional.of(testUser));
        when(cafeRepository.findById(testCafe.getId())).thenReturn(Optional.of(testCafe));
        Menu testMenu = Menu.builder().name("test menu 1").price(10_000).build();
        when(menuRepository.findByCafeAndIsSignature(eq(testCafe), any(Boolean.class)))
                .thenReturn(List.of(testMenu, Menu.builder().name("test menu 2").price(5_000).build()));
        List<MenuListResponse> signatureMenus = menuService.getSignatureMenuListOfCafe(testCafe.getId(), testUser.getId());

        // then
        assertThat(signatureMenus).isNotNull();
        assertThat(signatureMenus.size()).isEqualTo(2);
        assertThat(signatureMenus.get(0).getMenuName()).isEqualTo(testMenu.getName());
    }
}