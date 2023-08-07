package team.moca.camo.api.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class KakaoLocalResponseDto<T> {

    private List<T> documents;
}
