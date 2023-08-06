package team.moca.camo.domain;

import lombok.Getter;
import team.moca.camo.domain.value.Domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Getter
@Table(name = "Tag")
@Entity
public class Tag extends BaseEntity {

    @Column(name = "tag_name", nullable = false, unique = true, length = 10)
    private String tagName;

    @JoinTable(name = "Cafe_Tag",
            joinColumns = @JoinColumn(name = "tag_id"),
            inverseJoinColumns = @JoinColumn(name = "cafe_id"))
    @ManyToMany
    private List<Cafe> cafes = new ArrayList<>();

    protected Tag() {
        super(Domain.TAG);
    }

    protected Tag(String tagName) {
        this();
        this.tagName = tagName;
    }
}
