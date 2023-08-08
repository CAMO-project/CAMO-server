package team.moca.camo.controller.dto;

import lombok.Builder;
import lombok.Getter;
import team.moca.camo.domain.Cafe;

@Getter
public class CafeListResponse {

    private String cafeId;
    private String cafeName;
    private String location;
    private double ratingAverage;
    private int favoritesCount;
    private boolean isFavorite;

    protected CafeListResponse() {
    }

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
