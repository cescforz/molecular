package cn.cescforz.molecular.component.file;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * <p>Description: 文件操作接口</p>
 *
 * @author cescforz@gmail.com
 * @version 1.00.00
 * @date Create in 2019/6/11 15:15
 */
public interface FileService {

    /**
     * 上传文件到七牛图床
     * @param multipartFile :
     * @return java.lang.String
     */
    Map<String, List<String>> uploadFileToQn(MultipartFile[] multipartFile);

    /**
     * 上传文件到七牛图床
     * @param file :
     * @return java.lang.String
     * @throws
     */
    String uploadFileToQn(File file);
}
