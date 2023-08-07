package team.moca.camo.controller.dto;

import lombok.Builder;
import lombok.Getter;
import team.moca.camo.domain.Cafe;

@Getter
public class CafePreviewListResponse {

    private String cafeId;
    private String cafeName;
    private String location;
    private double ratingAverage;
    private int favoritesCount;
    private boolean isFavorite;

    protected CafePreviewListResponse() {
    }

    @Builder
    protected CafePreviewListResponse(String cafeId, String cafeName, String location, double ratingAverage,
                                      int favoritesCount, boolean isFavorite) {
        this.cafeId = cafeId;
        this.cafeName = cafeName;
        this.location = location;
        this.ratingAverage = ratingAverage;
        this.favoritesCount = favoritesCount;
        this.isFavorite = isFavorite;
    }

    public static CafePreviewListResponse of(Cafe cafe) {
        return null;
    }
}
