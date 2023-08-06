package team.moca.camo.domain;

import lombok.Builder;
import lombok.Getter;
import team.moca.camo.domain.value.Domain;
import team.moca.camo.domain.value.LogType;
import team.moca.camo.domain.value.Platform;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Getter
@Table(name = "User_Log")
@Entity
public class UserLog extends BaseEntity {

    @Enumerated(value = EnumType.STRING)
    @Column(name = "log_type", nullable = false, length = 15)
    private LogType logType;

    @Column(name = "log_description", nullable = false, length = 500)
    private String description;

    @Column(name = "page", nullable = false, length = 20)
    private String page;

    @Column(name = "endpoint", nullable = false, length = 50)
    private String endpoint;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "platform", length = 15)
    private Platform platform;

    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    protected UserLog() {
        super(Domain.LOG);
    }

    @Builder
    protected UserLog(LogType logType, String description, String page, String endpoint, Platform platform) {
        this();
        this.logType = logType;
        this.description = description;
        this.page = page;
        this.endpoint = endpoint;
        this.platform = platform;
    }
}
