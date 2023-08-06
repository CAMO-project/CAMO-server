package team.moca.camo.domain;

import lombok.Getter;
import team.moca.camo.domain.value.Domain;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Getter
@Table(name = "Like",
        uniqueConstraints = @UniqueConstraint(
                name = "favorite_unique", columnNames = {"user_id", "menu_id"}
        ))
@Entity
public class Like extends BaseEntity {

    @JoinColumn(name = "user_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @JoinColumn(name = "menu_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Menu menu;

    protected Like() {
        super(Domain.LIKE);
    }

    public Like(User user, Menu menu) {
        this();
        this.user = user;
        this.menu = menu;
    }
}
