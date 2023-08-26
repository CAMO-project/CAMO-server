package team.moca.camo.controller.dto.response;

import lombok.Builder;
import lombok.Getter;
import team.moca.camo.domain.Cafe;

@Getter
public class CafeListResponse {

    private final String cafeId;
    private final String cafeName;
    private final String address;
    private final double ratingAverage;
    private final int favoritesCount;
    private final boolean isFavorite;
    private final String thumbnail;

    @Builder
    protected CafeListResponse(String cafeId, String cafeName, String address, double ratingAverage,
                               int favoritesCount, boolean isFavorite, String thumbnail) {
        this.cafeId = cafeId;
        this.cafeName = cafeName;
        this.address = address;
        this.ratingAverage = ratingAverage;
        this.favoritesCount = favoritesCount;
        this.isFavorite = isFavorite;
        this.thumbnail = thumbnail;
    }

    public static CafeListResponse of(Cafe cafe, boolean isFavorite) {
        return CafeListResponse.builder()
                .cafeId(cafe.getId())
                .cafeName(cafe.getName())
                .address(String.join(" ", cafe.getAddress().getCity(), cafe.getAddress().getTown()))
                .ratingAverage(cafe.getRatingAverage())
                .favoritesCount(cafe.getFavoritesCount())
                .isFavorite(isFavorite)
                .thumbnail(cafe.getThumbnail())
                .build();
    }
}
