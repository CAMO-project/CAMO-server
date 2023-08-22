package team.moca.camo.controller.dto.response;

import lombok.Getter;
import team.moca.camo.domain.Tag;

@Getter
public class TagListResponse {

    private final String tagId;
    private final String tagName;

    protected TagListResponse(String tagId, String tagName) {
        this.tagId = tagId;
        this.tagName = tagName;
    }

    public static TagListResponse of(Tag tag) {
        return new TagListResponse(tag.getId(), tag.getTagName());
    }
}
