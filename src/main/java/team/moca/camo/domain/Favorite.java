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
@Table(name = "Favorite",
        uniqueConstraints = @UniqueConstraint(
                name = "favorite_unique", columnNames = {"user_id", "cafe_id"}
        ))
@Entity
public class Favorite extends BaseEntity {

    @JoinColumn(name = "user_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @JoinColumn(name = "cafe_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Cafe cafe;

    protected Favorite() {
        super(Domain.FAVORITE);
    }

    public Favorite(User user, Cafe cafe) {
        this();
        this.user = user;
        this.cafe = cafe;
    }
}
