package team.moca.camo.domain;

import lombok.Builder;
import lombok.Getter;
import team.moca.camo.domain.value.Domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Getter
@Table(name = "Menu")
@Entity
public class Menu extends BaseEntity {

    @Column(name = "menu_name")
    private String name;

    @Column(name = "menu_price")
    private int price;

    @Column(name = "menu_image_url")
    private String imageUrl;

    @JoinColumn(name = "cafe_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Cafe cafe;

    protected Menu() {
        super(Domain.MENU);
    }

    @Builder
    protected Menu(Domain domain, String name, int price, String imageUrl) {
        this();
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }
}
