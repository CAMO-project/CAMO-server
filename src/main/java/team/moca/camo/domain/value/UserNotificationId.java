package team.moca.camo.domain.value;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class UserNotificationId implements Serializable {

    private String userId;
    private String notificationId;
}
