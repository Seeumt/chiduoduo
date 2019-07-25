package com.cumt.chiduoduo.controller;

import com.cumt.chiduoduo.response.CommonReturnType;
import org.springframework.web.bind.annotation.*;
//
//@RestController("jwtlogin")
////defalut_allowed_headers允许跨域传输所有的header参数，将用于使用token放入header域做session共享的跨域请求
//@CrossOrigin(allowCredentials = "true",allowedHeaders =  "*")
//public class JwtLoginController extends BaseController {
//
//    @RequestMapping(value = "/login", method = RequestMethod.POST,
//            consumes = {CONTENT_TYPE_FORMED})
//    public CommonReturnType login(@RequestParam(name = "username") String username,
//                                  @RequestParam(name = "password") String password) throws Exception {
//
//
//        return CommonReturnType.create(null);
//    }
//}
