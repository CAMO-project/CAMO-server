package team.moca.camo.domain;

import lombok.Builder;
import lombok.Getter;
import team.moca.camo.domain.value.Domain;
import team.moca.camo.domain.value.NotificationType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Getter
@Table(name = "Notification")
@Entity
public class Notification extends BaseEntity {

    @Column(name = "notification_contents", nullable = false, length = 100)
    private String contents;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "notification_type", nullable = false, length = 15)
    private NotificationType notificationType;

    @OneToMany(mappedBy = "notification")
    private List<UserNotification> users = new ArrayList<>();

    protected Notification() {
        super(Domain.NOTIFICATION);
    }

    @Builder
    protected Notification(String contents, NotificationType notificationType) {
        this();
        this.contents = contents;
        this.notificationType = notificationType;
    }
}
