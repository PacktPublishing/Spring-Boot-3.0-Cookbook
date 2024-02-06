package com.packt.footballpg;

import java.time.LocalDateTime;

public record MatchEvent(LocalDateTime time, MatchEventDetails details) {

}
