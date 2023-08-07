package team.moca.camo.domain;

import lombok.Builder;
import lombok.Getter;
import team.moca.camo.domain.embedded.Location;
import team.moca.camo.domain.value.Domain;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

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

    @Column(name = "rating_average", nullable = false)
    private double ratingAverage;

    @Column(name = "favorites_count", nullable = false)
    private int favoritesCount;

    @Column(name = "business_registration_number", nullable = false, length = 15)
    private String businessRegistrationNumber;

    @JoinColumn(name = "user_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private User owner;

    @OneToMany(mappedBy = "cafe")
    private List<Menu> menus = new ArrayList<>();

    @OneToMany(mappedBy = "cafe")
    private List<Coupon> coupons = new ArrayList<>();

    @OneToMany(mappedBy = "cafe")
    private List<Favorite> favorites = new ArrayList<>();

    @OneToMany(mappedBy = "cafe")
    private List<Event> events = new ArrayList<>();

    @JoinTable(name = "Cafe_Tag",
            joinColumns = @JoinColumn(name = "cafe_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    @ManyToMany
    private List<Cafe> cafes = new ArrayList<>();

    @OneToMany(mappedBy = "cafe")
    private List<Image> images = new ArrayList<>();

    protected Cafe() {
        super(Domain.CAFE);
        ratingAverage = 0;
        favoritesCount = 0;
    }

    @Builder
    protected Cafe(String name, String contact, String introduction, String reward,
                   int requiredStamps, Location location, String businessRegistrationNumber) {
        this();
        this.name = name;
        this.contact = contact;
        this.introduction = introduction;
        this.reward = reward;
        this.requiredStamps = requiredStamps;
        this.location = location;
        this.businessRegistrationNumber = businessRegistrationNumber;
    }
}
