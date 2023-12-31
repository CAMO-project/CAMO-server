package team.moca.camo.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import team.moca.camo.common.EntityGraphNames;
import team.moca.camo.domain.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByEmail(String email);

    Optional<User> findByPhone(String phone);

    Optional<User> findByNickname(String nickname);

    Optional<User> findByKakaoId(String kakaoId);

    @EntityGraph(value = EntityGraphNames.USER_FAVORITE_CAFES)
    Optional<User> findWithFavoriteCafesById(String id);

    @EntityGraph(value = EntityGraphNames.USER_LIKE_MENUS)
    Optional<User> findWithLikeMenusById(String id);
}
