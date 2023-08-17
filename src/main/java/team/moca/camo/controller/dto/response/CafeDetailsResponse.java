package team.moca.camo.controller.dto.response;

import lombok.Builder;
import lombok.Getter;
import team.moca.camo.domain.Cafe;
import team.moca.camo.domain.Image;
import team.moca.camo.domain.Tag;
import team.moca.camo.domain.embedded.Address;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class CafeDetailsResponse {

    private final String cafeId;
    private final String cafeName;
    private final String address;
    private final String contact;
    private final String cafeIntroduction;
    private final String reward;
    private final int userStamps;
    private final int requiredStamps;
    private final List<String> tags;
    private final List<String> images;
    private final boolean isFavorite;
    private final boolean isOwner;

    @Builder
    protected CafeDetailsResponse(
            String cafeId, String cafeName, String address, String contact, String cafeIntroduction,
            String reward, int userStamps, int requiredStamps, List<String> tags, List<String> images, boolean isFavorite, boolean isOwner) {
        this.cafeId = cafeId;
        this.cafeName = cafeName;
        this.address = address;
        this.contact = contact;
        this.cafeIntroduction = cafeIntroduction;
        this.reward = reward;
        this.userStamps = userStamps;
        this.requiredStamps = requiredStamps;
        this.tags = tags;
        this.images = images;
        this.isFavorite = isFavorite;
        this.isOwner = isOwner;
    }

    public static CafeDetailsResponse of(Cafe cafe, int userStamps, boolean isFavorite, boolean isOwner) {
        Address address = cafe.getAddress();
        return CafeDetailsResponse.builder()
                .cafeId(cafe.getId())
                .cafeName(cafe.getName())
                .address(String.join(" ", address.getRoadAddress(), address.getAddressDetail()))
                .contact(cafe.getContact())
                .cafeIntroduction(cafe.getIntroduction())
                .reward(cafe.getReward())
                .userStamps(userStamps)
                .requiredStamps(cafe.getRequiredStamps())
                .tags(cafe.getTags().stream()
                        .map(Tag::getTagName)
                        .collect(Collectors.toList()))
                .images(cafe.getImages().stream()
                        .map(Image::getUrl)
                        .collect(Collectors.toList()))
                .isFavorite(isFavorite)
                .isOwner(isOwner)
                .build();
    }
}
