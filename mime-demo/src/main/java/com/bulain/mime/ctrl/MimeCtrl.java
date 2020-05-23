package com.bulain.mime.ctrl;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.bulain.mime.pojo.DataResp;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/mime")
@Slf4j
public class MimeCtrl {

    @PostMapping(value = "update")
    @ResponseBody
    public DataResp<String> update(HttpServletRequest httpServletRequest) {

        log.info("update()-start");
        String ret = null;
        try {
            final String[] words = httpServletRequest.getParameterValues("word");
            ret = String.format("%s", Arrays.asList(words));
            log.info("扩展属性：{}", ret);

        } catch (Exception e) {
            log.error("update()-", e);
        }
        log.info("update()-end");

        return DataResp.ok(ret);

    }

    @PostMapping(value = "upload")
    @ResponseBody
    public DataResp<String> upload(@RequestParam("file") MultipartFile file, HttpServletRequest httpServletRequest) {

        log.info("upload()-start");

        String ret = null;
        try {
            long size = file.getSize();
            String originalFilename = file.getOriginalFilename();
            String contentType = file.getContentType();
            log.info("文件名称：{} 文件类型：{} 文件大小：{}", originalFilename, contentType, size);

            final String[] words = httpServletRequest.getParameterValues("word");
            ret = String.format("%s", Arrays.asList(words));
            log.info("扩展属性：{}", ret);

        } catch (Exception e) {
            log.error("upload()-", e);
        }
        log.info("upload()-end");

        return DataResp.ok(ret);

    }

}
