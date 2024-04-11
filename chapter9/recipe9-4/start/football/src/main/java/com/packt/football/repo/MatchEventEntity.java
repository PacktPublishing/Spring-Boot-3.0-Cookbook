package com.packt.football.repo;

import java.time.LocalDateTime;

// import org.hibernate.annotations.JdbcTypeCode;
// import org.hibernate.type.SqlTypes;

// import io.hypersistence.utils.hibernate.type.json.JsonType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

@Table(name = "match_events")
@Entity
public class MatchEventEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "event_time")
    private LocalDateTime time;

    @Type(type = "io.hypersistence.utils.hibernate.type.json.JsonType")
    private MatchEventDetails details;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_id", nullable = false)
    private MatchEntity match;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public MatchEventDetails getDetails() {
        return details;
    }

    public void setDetails(MatchEventDetails details) {
        this.details = details;
    }

}
