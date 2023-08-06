package team.moca.camo.domain;

import lombok.Getter;
import team.moca.camo.domain.embedded.UserNotificationId;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

@Getter
@Table(name = "User_Notification")
@Entity
public class UserNotification {

    @EmbeddedId
    private UserNotificationId id;

    @Column(name = "read", nullable = false)
    private boolean read;

    @MapsId(value = "userId")
    @JoinColumn(name = "user_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @MapsId(value = "notificationId")
    @JoinColumn(name = "notification_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Notification notification;
}
