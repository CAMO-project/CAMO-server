package team.moca.camo.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.moca.camo.controller.dto.response.TagListResponse;
import team.moca.camo.repository.TagRepository;

import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
public class TagService {

    private final TagRepository tagRepository;

    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public List<TagListResponse> getAllTags() {
        return tagRepository.findAll().stream()
                .map(TagListResponse::of)
                .collect(Collectors.toList());
    }
}
