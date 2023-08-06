package team.moca.camo.domain;

import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;

@Getter
@Table(name = "cafe")
@Entity
public class Cafe extends BaseEntity {

    @Column(name = "name")
    private String name;

    @Column(name = "contact")
    private String contact;

    @Column(name = "introduction")
    private String introduction;

    @Column(name = "reward")
    private String reward;

    @Column(name = "required_stamps")
    private String requiredStamps;

    @Column(name = "city")
    private String city;

    @Column(name = "town")
    private String town;

    @Column(name = "address_detail")
    private String addressDetail;

    protected Cafe() {
        super(Domain.Cafe);
    }

    @Builder
    protected Cafe(String name, String city, String town, String addressDetail, String contact, String introduction) {
        this();
        this.name = name;
        this.city = city;
        this.town = town;
        this.addressDetail = addressDetail;
        this.contact = contact;
        this.introduction = introduction;
    }
}
