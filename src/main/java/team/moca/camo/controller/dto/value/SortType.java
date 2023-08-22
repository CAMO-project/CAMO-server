package team.moca.camo.controller.dto.value;

import lombok.Getter;

@Getter
public enum SortType {

    RATING("ratingAverage"),
    DISTANCE("distance"),
    FAVORITE("favoritesCount");

    private final String sortPropertyName;

    SortType(String sortPropertyName) {
        this.sortPropertyName = sortPropertyName;
    }
}
