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
import java.time.LocalDateTime;

@Getter
@Table(name = "Event")
@Entity
public class Event extends BaseEntity {

    @Column(name = "event_title", nullable = false, length = 30)
    private String title;

    @Column(name = "event_contents", nullable = false, length = 500)
    private String contents;

    @Column(name = "image_url", length = 500)
    private String imageUrl;

    @Column(name = "event_url", length = 500)
    private String eventUrl;

    @Column(name = "event_start", nullable = false)
    private LocalDateTime eventStart;

    @Column(name = "event_end", nullable = false)
    private LocalDateTime eventEnd;

    @JoinColumn(name = "cafe_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Cafe cafe;

    protected Event() {
        super(Domain.EVENT);
    }

    @Builder
    protected Event(String title, String contents, String imageUrl, String eventUrl,
                    LocalDateTime eventStart, LocalDateTime eventEnd) {
        this();
        this.title = title;
        this.contents = contents;
        this.imageUrl = imageUrl;
        this.eventUrl = eventUrl;
        this.eventStart = eventStart;
        this.eventEnd = eventEnd;
    }
}
