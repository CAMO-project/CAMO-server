package team.moca.camo.domain;

import lombok.Builder;
import lombok.Getter;
import team.moca.camo.domain.value.Domain;
import team.moca.camo.domain.value.Location;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Getter
@Table(name = "Cafe")
@Entity
public class Cafe extends BaseEntity {

    @Column(name = "cafe_name", nullable = false, length = 20)
    private String name;

    @Column(name = "cafe_contact", length = 15)
    private String contact;

    @Column(name = "cafe_introduction", nullable = false, length = 1000)
    private String introduction;

    @Column(name = "reward", length = 100)
    private String reward;

    @Column(name = "required_stamps")
    private int requiredStamps;

    @Embedded
    private Location location;

    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User owner;

    protected Cafe() {
        super(Domain.CAFE);
    }

    @Builder
    protected Cafe(String name, String contact, String introduction, String reward,
                   int requiredStamps, Location location) {
        this();
        this.name = name;
        this.contact = contact;
        this.introduction = introduction;
        this.reward = reward;
        this.requiredStamps = requiredStamps;
        this.location = location;
    }
}
