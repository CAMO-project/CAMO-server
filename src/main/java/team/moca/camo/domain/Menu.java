package team.moca.camo.domain;

import lombok.Builder;
import lombok.Getter;
import team.moca.camo.domain.value.Domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Getter
@Table(name = "Menu")
@Entity
public class Menu extends BaseEntity {

    @Column(name = "menu_name", nullable = false, length = 20)
    private String name;

    @Column(name = "menu_price", nullable = false)
    private int price;

    @Column(name = "image_url", length = 200)
    private String imageUrl;

    @Column(name = "favorites_count", nullable = false)
    private int favoritesCount;

    @JoinColumn(name = "cafe_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Cafe cafe;

    @OneToMany(mappedBy = "menu")
    private List<Like> likes = new ArrayList<>();

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
