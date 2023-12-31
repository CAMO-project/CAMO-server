package team.moca.camo.domain;

import lombok.Getter;
import team.moca.camo.domain.value.Domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Getter
@Table(name = "Coupon")
@Entity
public class Coupon extends BaseEntity {

    @Column(name = "stamps", nullable = false)
    private int stamps;

    @JoinColumn(name = "user_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @JoinColumn(name = "cafe_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Cafe cafe;

    protected Coupon() {
        super(Domain.COUPON);
    }

    protected Coupon(int stamps) {
        this();
        this.stamps = stamps;
    }
}
