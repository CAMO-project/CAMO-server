package team.moca.camo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import team.moca.camo.DTO.CafeDTO;
import team.moca.camo.domain.Cafe;
import team.moca.camo.service.CafeService;

import java.util.List;

@RestController
public class CafeController {

    @Autowired
    private CafeService cafeService;

    @PostMapping("/api/cafe/new")
    public void registerCafe(@RequestBody CafeDTO cafeDTO) {
        cafeService.registerCafe(cafeDTO);
    }

    @GetMapping("api/cafe/{id}")
    public Cafe searchCafeById(@PathVariable("id") Integer id) {
        return cafeService.searchId(id);
    }

    @GetMapping("api/cafe/list")
    public List<Cafe> searchCafeList() {
        return cafeService.searchCafeList();
    }
}