package team.moca.camo.DTO;

import lombok.Data;

@Data
public class CafeDTO {
    private Integer id;
    private String name;
    private String contact;
    private String introduction;
    private String reward;
    private Integer requiredStamps;
    private String city;
    private String town;
    private String addressDetail;
}
