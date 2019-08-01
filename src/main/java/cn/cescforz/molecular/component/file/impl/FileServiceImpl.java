package cn.cescforz.molecular.component.file.impl;

import cn.cescforz.commons.lang.enums.ResponseEnum;
import cn.cescforz.commons.lang.exception.CustomRtException;
import cn.cescforz.commons.lang.toolkit.tool.KeyGenerator;
import cn.cescforz.molecular.component.file.FileService;
import cn.cescforz.molecular.properties.QiniuProperties;
import cn.cescforz.molecular.toolkit.tool.UniKeyGenerator;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

/**
 * <p>Description: 七牛云上传文件</p>
 *
 * @author cescforz@gmail.com
 * @version 1.00.00
 * @date Create in 2019/6/11 16:39
 */
@Slf4j
@Component
public class FileServiceImpl implements FileService {


    private QiniuProperties qiniuProperties;

    @Override
    public Map<String, List<String>> uploadFileToQn(MultipartFile[] multipartFile){
        Map<String, List<String>> resultMap = Maps.newHashMap();
        List<String> list = new LinkedList<>();
        Arrays.stream(multipartFile).forEach(file -> {
            String fileName = file.getOriginalFilename();
            try {
                FileInputStream inputStream = (FileInputStream) file.getInputStream();
                String result = uploadQn(inputStream, fileName);
                if (StringUtils.isNotBlank(result)) {
                    list.add(result);
                }
            } catch (IOException e) {
                log.error("获取FileInputStream异常:", e);
                throw new CustomRtException(ResponseEnum.SERVICE_EXCEPTION);
            }
        });
        if (list.isEmpty()) {
            list.add("error");
        }
        resultMap.put("result", list);
        return resultMap;
    }


    @Override
    public String uploadFileToQn(File file) {
        // 构造一个带指定Zone对象的配置类
        Configuration cfg = new Configuration(Zone.zone2());
        // 其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
        // 生成上传凭证，然后准备上传
        Auth auth = Auth.create(qiniuProperties.getAccessKey(), qiniuProperties.getSecretKey());
        String upToken = auth.uploadToken(qiniuProperties.getBucket());
        Long id = UniKeyGenerator.getInstance().nextId();
        String fileName = String.format("%s%s",file.getName(), id.toString());
        try {
            Response response = uploadManager.put(file, fileName, upToken);
            // 解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);

            return qiniuProperties.getPath() + "/" + putRet.key;
        } catch (QiniuException e) {
            log.error(ResponseEnum.SERVICE_EXCEPTION.getCnInfo(), e);
        }
        return "";
    }

    private String uploadQn(FileInputStream fileInputStream, String key) {
        // 构造一个带指定Zone对象的配置类
        Configuration cfg = new Configuration(Zone.zone2());
        // 其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
        // 生成上传凭证，然后准备上传
        Auth auth = Auth.create(qiniuProperties.getAccessKey(), qiniuProperties.getSecretKey());
        String upToken = auth.uploadToken(qiniuProperties.getBucket());
        KeyGenerator keyGenerator = UniKeyGenerator.getInstance();
        key = Optional.ofNullable(key).orElseGet(() -> keyGenerator.nextId().toString());
        try {
            Response response = uploadManager.put(fileInputStream, key, upToken, null, null);
            // 解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            return qiniuProperties.getPath() + "/" + putRet.key;
        } catch (QiniuException ex) {
            log.error(ResponseEnum.SERVICE_EXCEPTION.getCnInfo(), ex);
        }
        return "";
    }

    @Autowired
    public void setQiniuProperties(QiniuProperties qiniuProperties) {
        this.qiniuProperties = qiniuProperties;
    }
}
