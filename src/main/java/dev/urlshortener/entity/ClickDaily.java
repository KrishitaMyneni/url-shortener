package dev.urlshortener.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "click_daily", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "link_id", "click_date" })
})
public class ClickDaily {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "link_id", nullable = false)
    private Link link;

    @Column(name = "click_date", nullable = false)
    private LocalDate clickDate;

    @Column(nullable = false)
    private Long clickCount = 0L;

    public ClickDaily() {
    }

    public Long getId() {
        return id;
    }

    public Link getLink() {
        return link;
    }

    public void setLink(Link link) {
        this.link = link;
    }

    public LocalDate getClickDate() {
        return clickDate;
    }

    public void setClickDate(LocalDate clickDate) {
        this.clickDate = clickDate;
    }

    public Long getClickCount() {
        return clickCount;
    }

    public void setClickCount(Long clickCount) {
        this.clickCount = clickCount;
    }
}