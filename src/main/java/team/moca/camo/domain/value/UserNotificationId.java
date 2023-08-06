package team.moca.camo.domain.value;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class UserNotificationId implements Serializable {

    private String userId;
    private String notificationId;

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        UserNotificationId that = (UserNotificationId) object;

        if (!Objects.equals(userId, that.userId)) return false;
        return Objects.equals(notificationId, that.notificationId);
    }

    @Override
    public int hashCode() {
        int result = userId != null ? userId.hashCode() : 0;
        result = 31 * result + (notificationId != null ? notificationId.hashCode() : 0);
        return result;
    }
}
