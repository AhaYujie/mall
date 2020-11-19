package online.ahayujie.mall.admin.service.impl;

import com.aliyun.oss.OSS;
import lombok.extern.slf4j.Slf4j;
import online.ahayujie.mall.admin.bean.dto.UploadFileDTO;
import online.ahayujie.mall.admin.service.FileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * @author aha
 * @since 2020/11/19
 */
@Slf4j
@Service
public class FileServiceImpl implements FileService {
    private static final int FILE_NAME_LENGTH = 50;

    @Value("${oss.bucket-name}")
    private String bucketName;

    @Value("${oss.prefix-url}")
    private String prefixUrl;

    private final OSS oss;

    public FileServiceImpl(OSS oss) {
        this.oss = oss;
    }

    @Override
    public UploadFileDTO upload(MultipartFile file) throws IOException {
        if (file.getOriginalFilename() == null) {
            throw new IOException("文件名不存在");
        }
        String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf('.'));
        String fileName = getRandomFileName(suffix);
        try {
            oss.putObject(bucketName, fileName, file.getInputStream());
        }
        catch (IOException e) {
            e.printStackTrace();
            throw new IOException("文件上传失败");
        }
        UploadFileDTO uploadFileDTO = new UploadFileDTO();
        uploadFileDTO.setUrl(prefixUrl + "/" + fileName);
        return uploadFileDTO;
    }

    /**
     * 生成随机文件名。
     * @param suffix 文件后缀
     * @return 文件名
     */
    private static String getRandomFileName(String suffix) {
        StringBuilder fileName = new StringBuilder();
        fileName.append(new SimpleDateFormat("yyyyMMddHHmmss-").format(new Date()));
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random=new Random();
        for(int i = 0; i < FILE_NAME_LENGTH - fileName.length() - suffix.length(); i++){
            int index = random.nextInt(str.length());
            fileName.append(str.charAt(index));
        }
        fileName.append(suffix);
        return fileName.toString();
    }
}
