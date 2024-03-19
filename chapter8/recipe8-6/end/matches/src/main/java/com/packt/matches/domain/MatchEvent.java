package com.packt.matches.domain;

import java.time.LocalDateTime;

public record MatchEvent(Long id, Long matchId, LocalDateTime eventTime, int type, String description, Long player1,
        Long player2) {

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private Long matchId;
        private LocalDateTime eventTime;
        private int type;
        private String description;
        private Long player1;
        private Long player2;

        public Builder withId(Long id) {
            this.id = id;
            return this;
        }

        public Builder withMatchId(Long matchId) {
            this.matchId = matchId;
            return this;
        }

        public Builder withEventTime(LocalDateTime eventTime) {
            this.eventTime = eventTime;
            return this;
        }

        public Builder withType(int type) {
            this.type = type;
            return this;
        }

        public Builder withDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder withPlayer1(Long player1) {
            this.player1 = player1;
            return this;
        }

        public Builder withPlayer2(Long player2) {
            this.player2 = player2;
            return this;
        }

        public MatchEvent build() {
            return new MatchEvent(id, matchId, eventTime, type, description, player1, player2);
        }
    }
}
