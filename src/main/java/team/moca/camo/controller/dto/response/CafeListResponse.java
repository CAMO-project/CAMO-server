package team.moca.camo.controller.dto.response;

import lombok.Builder;
import lombok.Getter;
import team.moca.camo.domain.Cafe;

@Getter
public class CafeListResponse {

    private final String cafeId;
    private final String cafeName;
    private final String location;
    private final double ratingAverage;
    private final int favoritesCount;
    private final boolean isFavorite;

    @Builder
    protected CafeListResponse(String cafeId, String cafeName, String location, double ratingAverage,
                               int favoritesCount, boolean isFavorite) {
        this.cafeId = cafeId;
        this.cafeName = cafeName;
        this.location = location;
        this.ratingAverage = ratingAverage;
        this.favoritesCount = favoritesCount;
        this.isFavorite = isFavorite;
    }

    public static CafeListResponse of(Cafe cafe, boolean isFavorite) {
        return CafeListResponse.builder()
                .cafeId(cafe.getId())
                .cafeName(cafe.getName())
                .location(String.join(" ", cafe.getAddress().getCity(), cafe.getAddress().getTown()))
                .ratingAverage(cafe.getRatingAverage())
                .favoritesCount(cafe.getFavoritesCount())
                .isFavorite(isFavorite)
                .build();
    }
}
