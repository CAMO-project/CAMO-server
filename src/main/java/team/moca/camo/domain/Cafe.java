package team.moca.camo.domain;

import lombok.Builder;
import lombok.Getter;
import team.moca.camo.domain.embedded.Address;
import team.moca.camo.domain.value.Domain;
import team.moca.camo.exception.BusinessException;
import team.moca.camo.exception.error.ClientRequestError;

import javax.persistence.CascadeType;
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

    @Column(name = "cafe_name", nullable = false, length = 50)
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
    private Address address;

    @Column(name = "rating_average", nullable = false)
    private double ratingAverage;

    @Column(name = "favorites_count", nullable = false)
    private int favoritesCount;

    @Column(name = "business_registration_number", nullable = false, length = 15)
    private String businessRegistrationNumber;

    @Column(name = "thumbnail", length = 500)
    private String thumbnail;

    @JoinColumn(name = "user_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private User owner;

    @OneToMany(mappedBy = "cafe", cascade = CascadeType.REMOVE)
    private List<Menu> menus = new ArrayList<>();

    @OneToMany(mappedBy = "cafe", cascade = CascadeType.REMOVE)
    private List<Coupon> coupons = new ArrayList<>();

    @OneToMany(mappedBy = "cafe", cascade = CascadeType.REMOVE)
    private List<Favorite> favorites = new ArrayList<>();

    @OneToMany(mappedBy = "cafe", cascade = CascadeType.REMOVE)
    private List<Event> events = new ArrayList<>();

    @JoinTable(name = "Cafe_Tag",
            joinColumns = @JoinColumn(name = "cafe_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    @ManyToMany
    private List<Tag> tags = new ArrayList<>();

    @OneToMany(mappedBy = "cafe", cascade = CascadeType.REMOVE)
    private List<Image> images = new ArrayList<>();

    @OneToMany(mappedBy = "cafe", cascade = CascadeType.REMOVE)
    private List<Review> reviews = new ArrayList<>();

    protected Cafe() {
        super(Domain.CAFE);
        requiredStamps = 0;
        ratingAverage = 0;
        favoritesCount = 0;
    }

    @Builder
    protected Cafe(String name, String contact, String introduction, String reward,
                   int requiredStamps, Address address, String businessRegistrationNumber) {
        this();
        this.name = name;
        this.contact = contact;
        this.introduction = introduction;
        this.reward = reward;
        this.requiredStamps = requiredStamps;
        this.address = address;
        this.businessRegistrationNumber = businessRegistrationNumber;
    }

    public void registerBy(User owner) {
        this.owner = owner;
        owner.getCafes().add(this);
    }

    public void addTag(Tag tag) {
        if (this.tags.size() >= 3) {
            throw new BusinessException(ClientRequestError.TAGS_MAXIMUM_NUMBER_EXCEEDS);
        }
        if (this.tags.contains(tag)) {
            throw new BusinessException(ClientRequestError.DUPLICATE_TAGS);
        }

        this.tags.add(tag);
        tag.getCafes().add(this);
    }
}
