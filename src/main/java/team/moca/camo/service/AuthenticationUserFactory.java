package team.moca.camo.service;

import org.springframework.stereotype.Service;
import team.moca.camo.common.GuestUser;
import team.moca.camo.domain.User;
import team.moca.camo.exception.BusinessException;
import team.moca.camo.exception.error.AuthenticationError;

import java.util.Optional;
import java.util.function.Function;

@Service
public class AuthenticationUserFactory {

    public User getAuthenticatedUserOrGuestUserWithFindOption(
            final String authenticatedAccountId, final Function<String, Optional<User>> findUserFunction
    ) {
        if (authenticatedAccountId == null) {
            return GuestUser.getInstance();
        } else {
            return findUserFunction.apply(authenticatedAccountId)
                    .orElseThrow(() -> new BusinessException(AuthenticationError.USER_AUTHENTICATION_FAIL));
        }
    }
}
