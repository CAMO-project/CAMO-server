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
        Cafe cafe = Cafe.builder()
                .name(cafeDTO.getName())
                .city(cafeDTO.getCity())
                .town(cafeDTO.getTown())
                .addressDetail(cafeDTO.getAddressDetail())
                .contact(cafeDTO.getContact())
                .introduction(cafeDTO.getIntroduction())
                .build();

        cafeRepository.save(cafe);

    }

    public Cafe searchId(Integer id) {
        return cafeRepository.findById(id).get();
    }

    public List<Cafe> searchCafeList() {
        return cafeRepository.findAll();
    }


//    public void updateCafe(Integer id, CafeDTO cafeDTO) {
//        Cafe cafe = cafeRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다. id= " + id));
//
//        cafeRepository.save(cafe);
//    }

//    public void deleteCafe(Integer id) {
//        Cafe cafe = cafeRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카페 id = " + id));
//
//        cafeRepository.deleteById(cafe.getCafeId());
//    }
}
