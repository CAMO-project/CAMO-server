package team.moca.camo.controller.dto.response;

import lombok.Builder;
import lombok.Getter;
import team.moca.camo.domain.Cafe;
import team.moca.camo.domain.embedded.Address;

import java.util.List;

@Getter
public class CafeDetailsResponse {

    private final String cafeId;
    private final String cafeName;
    private final String address;
    private final String contact;
    private final String cafeIntroduction;
    private final int userStamps;
    private final int requiredStamps;
    private final List<String> tags;
    private final List<String> imagesId;
    private final boolean isFavorite;

    @Builder
    protected CafeDetailsResponse(String cafeId, String cafeName, String address, String contact, String cafeIntroduction,
                                  int userStamps, int requiredStamps, List<String> tags, List<String> imagesId, boolean isFavorite) {
        this.cafeId = cafeId;
        this.cafeName = cafeName;
        this.address = address;
        this.contact = contact;
        this.cafeIntroduction = cafeIntroduction;
        this.userStamps = userStamps;
        this.requiredStamps = requiredStamps;
        this.tags = tags;
        this.imagesId = imagesId;
        this.isFavorite = isFavorite;
    }

    public static CafeDetailsResponse of(Cafe cafe, int userStamps, List<String> tagsName, List<String> imagesId, boolean isFavorite) {
        Address address = cafe.getAddress();
        return CafeDetailsResponse.builder()
                .cafeId(cafe.getId())
                .cafeName(cafe.getName())
                .address(String.join(" ", address.getRoadAddress(), address.getAddressDetail()))
                .contact(cafe.getContact())
                .cafeIntroduction(cafe.getIntroduction())
                .userStamps(userStamps)
                .requiredStamps(cafe.getRequiredStamps())
                .tags(tagsName)
                .imagesId(imagesId)
                .isFavorite(isFavorite)
                .build();
    }
}
