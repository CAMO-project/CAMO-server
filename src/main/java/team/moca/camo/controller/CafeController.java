package team.moca.camo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import team.moca.camo.DTO.CafeDTO;
import team.moca.camo.service.CafeService;

@RestController
public class CafeController {

    @Autowired
    private CafeService cafeService;

    @PostMapping("/api/cafe/new")
    public void registerCafe(@RequestBody CafeDTO cafeDTO) {
        cafeService.registerCafe(cafeDTO);
    }
}
