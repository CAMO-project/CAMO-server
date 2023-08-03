package team.moca.camo.domain;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Table(name = "cafe")
@Entity
public class Cafe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer cafeId;

    @Column(name = "cafe_name")
    private String cafeName;

    @Column(name = "cafe_contact")
    private String cafeContact;

    @Column(name = "cafe_introduction")
    private String cafeIntroduction;

    @Column(name = "cafe_reward")
    private String cafeReward;

    @Column(name = "cafe_required_stamps")
    private String cafeRequiredStamps;

    @Column(name = "cafe_city")
    private String cafeCity;

    @Column(name = "cafe_town")
    private String cafeTown;

    @Column(name = "cafe_address_detail")
    private String cafeAddressDetail;

    @DateTimeFormat
    @Column(name = "cafe_created_at")
    private String cafeCreatedAt;

    @DateTimeFormat
    @Column(name = "cafe_updated_at")
    private String cafeUpdatedAt;

    public Cafe() {

    }

    public Cafe(String cafeName,
                String cafeContact,
                String cafeIntroduction,
                String cafeReward,
                String cafeRequiredStamps,
                String cafeCity,
                String cafeTown,
                String cafeAddressDetail,
                String cafeCreatedAt,
                String cafeUpdatedAt) {
        this.cafeName = cafeName;
        this.cafeContact = cafeContact;
        this.cafeIntroduction = cafeIntroduction;
        this.cafeReward = cafeReward;
        this.cafeRequiredStamps = cafeRequiredStamps;
        this.cafeCity = cafeCity;
        this.cafeTown = cafeTown;
        this.cafeAddressDetail = cafeAddressDetail;
        this.cafeCreatedAt = cafeCreatedAt;
        this.cafeUpdatedAt = cafeUpdatedAt;
    }
}
