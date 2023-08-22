package team.moca.camo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team.moca.camo.controller.dto.ResponseDto;
import team.moca.camo.controller.dto.response.TagListResponse;
import team.moca.camo.service.TagService;

import java.util.List;

@RequestMapping("/api/tags")
@RestController
public class TagController {

    private final TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping("")
    public ResponseDto<List<TagListResponse>> allTags() {
        List<TagListResponse> tags = tagService.getAllTags();
        return ResponseDto.of(tags, "Retrieve all tags");
    }
}
