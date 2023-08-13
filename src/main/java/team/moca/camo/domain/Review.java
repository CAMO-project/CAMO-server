package team.moca.camo.domain;

import lombok.Getter;
import team.moca.camo.common.EntityGraphNames;
import team.moca.camo.domain.value.Domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedEntityGraphs;
import javax.persistence.Table;

@Getter
@Table(name = "Review")
@NamedEntityGraphs(
        value = {
                @NamedEntityGraph(
                        name = EntityGraphNames.REVIEW_WRITER,
                        attributeNodes = @NamedAttributeNode(value = "writer")
                )
        }
)
@Entity
public class Review extends BaseEntity {

    @Column(name = "review_contents", nullable = false, length = 500)
    private String contents;

    @Column(name = "star_rating", updatable = false)
    private int starRating;

    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User writer;

    @JoinColumn(name = "cafe_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Cafe cafe;

    protected Review() {
        super(Domain.REVIEW);
    }

    public Review(String contents, int starRating) {
        this();
        this.contents = contents;
        this.starRating = starRating;
    }
}
