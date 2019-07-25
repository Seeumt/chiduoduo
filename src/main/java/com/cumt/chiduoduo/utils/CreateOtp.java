package com.cumt.chiduoduo.utils;

import com.cumt.chiduoduo.error.BusinessException;
import com.cumt.chiduoduo.error.EnumBusinessError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;

import javax.servlet.http.HttpServletRequest;
import java.util.Random;
/**
 * @author ：Chiduoduo Team
 * @date ：Created in 2019/3/1 15:33
 * @description：创建验证码工具类
 *
 */
@Component
public class CreateOtp {
    public  String createOTP() {
        //需要按照一定的规则生成OTP验证码
        Random random = new Random();
        int randomInt =random.nextInt(89999)+10000;
        String otpCode = String.valueOf(randomInt);
        return otpCode;
    }


}
