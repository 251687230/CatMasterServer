package com.zous.catmaster.controller;

import com.zous.catmaster.annotation.CheckLogin;
import com.zous.catmaster.bean.AppConstant;
import com.zous.catmaster.bean.ErrorCode;
import com.zous.catmaster.bean.Result;
import com.zous.catmaster.service.FileService;
import org.apache.commons.codec.Charsets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/file")
public class FileController {
    @Autowired
    ApplicationContext context;
    @Autowired
    FileService fileService;

    @RequestMapping(value = "/download", method = RequestMethod.GET)
    @CheckLogin
    public ResponseEntity<String> download(@RequestParam("fileId") String fileId) throws Exception {
        String path = fileService.getFilePath(fileId);
        File file = new File(path);
        FileInputStream fis = new FileInputStream(file);
        byte[] body = new byte[fis.available()];
        fis.read(body);
        String name = file.getName();
        String downloadFileName = new String(name.getBytes(Charsets.UTF_8), Charsets.ISO_8859_1);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Disposition", "attachment;filename=" + downloadFileName);
        HttpStatus httpStatus = HttpStatus.OK;
        ResponseEntity<String> responseEntity = new ResponseEntity<>(Base64Utils.encodeToString(body), httpHeaders, httpStatus);
        return responseEntity;
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    @CheckLogin
    public Result upload(@RequestAttribute("UserId") String userId, @RequestPart(value = "file") MultipartFile multipartFile) throws IOException {
        //处理逻辑
        String[] fileSplit = multipartFile.getOriginalFilename().split("\\.");
        String suffix = fileSplit[fileSplit.length - 1];
        String uuid = UUID.randomUUID().toString();
        String path = userId + "\\" + uuid + "." + suffix;
        String fullPath = AppConstant.IMAGE_STORAGE_PATH + path;
        File dirFile = new File(AppConstant.IMAGE_STORAGE_PATH + userId);
        if(!dirFile.exists()){
            dirFile.mkdirs();
        }
        multipartFile.transferTo(new File(fullPath));
        fileService.createFileIndex(uuid, path, AppConstant.TYPE_FILE_IMAGE);
        return new Result(ErrorCode.SUCCESS, "", uuid);
    }
}
