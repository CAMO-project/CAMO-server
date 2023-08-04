package team.moca.camo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.moca.camo.DTO.CafeDTO;
import team.moca.camo.domain.Cafe;
import team.moca.camo.repository.CafeRepository;

import java.util.List;

@Service
public class CafeService {
    @Autowired
    private CafeRepository cafeRepository;

    public void registerCafe(CafeDTO cafeDTO) {
        cafeRepository.save(new Cafe(
                cafeDTO.getCafeName(),
                cafeDTO.getCafeContact(),
                cafeDTO.getCafeIntroduction(),
                cafeDTO.getCafeReward(),
                cafeDTO.getCafeRequiredStamps(),
                cafeDTO.getCafeCity(),
                cafeDTO.getCafeTown(),
                cafeDTO.getCafeAddressDetail(),
                cafeDTO.getCafeCreatedAt(),
                cafeDTO.getCafeUpdatedAt())
        );
    }

    public Cafe searchId(Integer id) {
        return cafeRepository.findById(id).get();
    }

    public List<Cafe> searchCafeList() {
        return cafeRepository.findAll();
    }
}