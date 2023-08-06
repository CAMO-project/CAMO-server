package team.moca.camo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.moca.camo.DTO.CafeDTO;
import team.moca.camo.domain.Cafe;
import team.moca.camo.repository.CafeRepository;

import java.util.List;

@Service
public class CafeService {
    private final CafeRepository cafeRepository;

    public CafeService(CafeRepository cafeRepository) {
        this.cafeRepository = cafeRepository;
    }

    public void registerCafe(CafeDTO cafeDTO) {
        Cafe cafe = Cafe.builder()
                .name(cafeDTO.getName())
                .city(cafeDTO.getCity())
                .town(cafeDTO.getTown())
                .addressDetail(cafeDTO.getAddressDetail())
                .contact(cafeDTO.getContact())
                .build();

        cafeRepository.save(cafe);

        //user가 일반회원이면 사장회원으로 전환

    }

    public Cafe searchId(String id) {
        return cafeRepository.findById(id).get();
    }

    public List<Cafe> cafeSearchList(String searchKeyword) {
        return cafeRepository.findByNameContaining(searchKeyword);
    }

    public List<Cafe> cafeList() {
        return cafeRepository.findAll();
    }

    //포함된 카페명으로 검색 기능 추가 예정


    public void updateCafe(String id, CafeDTO cafeDTO) {
        Cafe cafe = cafeRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다. id= " + id));

        //본인의 카페인지 확인후 수정할 수 있도록 구현

        cafe.updateCafe(cafeDTO.getCity(), cafeDTO.getTown(), cafeDTO.getAddressDetail(), cafeDTO.getRequiredStamps(), cafeDTO.getReward(), cafeDTO.getIntroduction());

        cafeRepository.save(cafe);
    }

    public void deleteCafe(String id) {
        Cafe cafe = cafeRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카페 id = " + id));
        //본인의 카페인지 확인후 삭제 할 수 있도록 구현
        cafeRepository.deleteById(cafe.getId());

        //카페를 삭제했을때 운영중인 카페가 0개면 사장회원 -> 일반회원으로 전환
    }
}
