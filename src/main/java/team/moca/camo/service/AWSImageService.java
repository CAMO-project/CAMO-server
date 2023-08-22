package team.moca.camo.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import team.moca.camo.config.AWSProperties;
import team.moca.camo.domain.Image;
import team.moca.camo.exception.BusinessException;
import team.moca.camo.exception.error.ClientRequestError;
import team.moca.camo.repository.ImageRepository;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Slf4j
@Transactional(readOnly = true)
@Service
public class AWSImageService implements ImageService {

    private final AWSProperties awsProperties;
    private final AmazonS3 s3Client;
    private final ImageRepository imageRepository;

    public AWSImageService(AWSProperties awsProperties, AmazonS3 s3Client, ImageRepository imageRepository) {
        this.awsProperties = awsProperties;
        this.s3Client = s3Client;
        this.imageRepository = imageRepository;
    }

    @Transactional
    @Override
    public Image uploadImage(final MultipartFile multipartFile, final String directoryName)
            throws IOException {
        if (!isImageFile(multipartFile.getContentType())) {
            log.info("multipartFile.getContentType() = {}", multipartFile.getContentType());
            throw new BusinessException(ClientRequestError.UNSUPPORTED_FILE_EXTENSION);
        }

        String originalFileName = multipartFile.getOriginalFilename();
        String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String newFileName = directoryName + File.separator + UUID.randomUUID() + fileExtension;

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(multipartFile.getSize());
        metadata.setContentType(multipartFile.getContentType());

        try {
            s3Client.putObject(new PutObjectRequest(
                    awsProperties.getBucketName(), newFileName, multipartFile.getInputStream(), metadata
            ).withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (Exception e) {
            throw new IOException("Error uploading file to S3", e);
        }

        String imageUrl = s3Client.getUrl(awsProperties.getBucketName(), newFileName).toString();
        return imageRepository.save(Image.of(imageUrl));
    }

    private boolean isImageFile(final String contentType) {
        return contentType != null &&
                (contentType.startsWith(MediaType.IMAGE_JPEG_VALUE) || contentType.startsWith(MediaType.IMAGE_PNG_VALUE));
    }

    @Override
    public void removeImage(final Image image) {
    }
}
