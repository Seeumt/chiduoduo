package com.cumt.chiduoduo.controller;

import com.cumt.chiduoduo.utils.COSClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author ：Chiduoduo Team
 * @date ：Created in 2019/3/1 16:26
 * @description：上传图片到云存储
 * @modified By：
 * @version: 1$
 */
@Slf4j
@RestController("uploadimg")
@RequestMapping("/uploadimg")
@CrossOrigin(allowCredentials = "true",allowedHeaders =  "*")
public class UploadImgController extends BaseController{


    @RequestMapping("/getimgurl")
    public String uploadMethod(@RequestBody MultipartFile file) throws Exception {
        COSClientUtil cosClientUtil = new COSClientUtil();
        String name = cosClientUtil.uploadFile2Cos(file);
        String imgUrl = cosClientUtil.getImgUrl(name);
        String[] split = imgUrl.split("\\?");
        System.out.println(split[0]);
        return split[0];
    }
//@RequestMapping("/getimgurl")
//public String uploadMethod( @RequestBody MultipartFile img1,
//                            @RequestBody MultipartFile img2,
//                            @RequestBody MultipartFile img3) throws Exception {
//    COSClientUtil cosClientUtil = new COSClientUtil();
//    String name = cosClientUtil.uploadFile2Cos(img1);
//    String imgUrl = cosClientUtil.getImgUrl(name);
//    String[] split = imgUrl.split("\\?");
//    System.out.println(split[0]);
//    return split[0];
//}


}
