package team.moca.camo.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import team.moca.camo.DTO.CafeDTO;
import team.moca.camo.domain.Cafe;
import team.moca.camo.service.CafeService;

import java.util.ArrayList;
import java.util.List;

@RestController
public class CafeController {

    private final CafeService cafeService;

    public CafeController(CafeService cafeService) {
        this.cafeService = cafeService;
    }

    @PostMapping("/api/cafe/new")
    public void registerCafe(@RequestBody CafeDTO cafeDTO) {
        cafeService.registerCafe(cafeDTO);
    }

    @GetMapping("api/cafe/{id}")
    public Cafe searchCafeById(@PathVariable("id") String id) {
        return cafeService.searchId(id);
    }

    @GetMapping("api/cafe/list")
    public Page<Cafe> searchCafeList(@PageableDefault (page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return cafeService.cafeList(pageable);
    }

    @GetMapping("api/cafe/list/{name}")
    public Page<Cafe> cafeSearchName(@PageableDefault (page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                                     @PathVariable("name") String searchKeyword) {

        Page<Cafe> cafeList = null;

        if (searchKeyword == null) {
            cafeList = cafeService.cafeList(pageable);
        } else {
//            cafeList = cafeService.cafeSearchList(searchKeyword);
        }

        return cafeList;
    }

    @PutMapping("/api/cafe/update/{id}")
    public void updateCafe(@PathVariable("id") String id, @RequestBody CafeDTO cafeDTO) {

        cafeService.updateCafe(id, cafeDTO);
    }

    @DeleteMapping("/api/cafe/delete/{id}")
    public void deleteCafe(@PathVariable("id") String id) {
        cafeService.deleteCafe(id);
    }
}
