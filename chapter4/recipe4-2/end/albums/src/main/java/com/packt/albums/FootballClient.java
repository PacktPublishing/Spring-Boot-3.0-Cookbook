package com.packt.albums;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient("FootballServer")
public interface FootballClient {

    @RequestMapping(method = RequestMethod.GET, value = "/players")
    List<Player> getPlayers();

}
