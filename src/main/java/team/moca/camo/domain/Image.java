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
@Table(name = "Image")
@Entity
public class Image extends BaseEntity {

    @Column(name = "image_url", nullable = false, length = 200)
    private String url;

    @JoinColumn(name = "cafe_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Cafe cafe;

    protected Image() {
        super(Domain.IMAGE);
    }

    protected Image(String url) {
        this();
        this.url = url;
    }
}
