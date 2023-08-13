package team.moca.camo.service;

import org.springframework.web.multipart.MultipartFile;
import team.moca.camo.domain.Image;

import java.io.IOException;

public interface ImageService {

    Image uploadImage(MultipartFile multipartFile, String directoryName) throws IOException;

    void removeImage(Image image);
}
