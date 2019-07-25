package com.cumt.chiduoduo.controller;

import com.cumt.chiduoduo.controller.vo.UserModelVO;
import com.cumt.chiduoduo.error.BusinessException;
import com.cumt.chiduoduo.error.EnumBusinessError;
import com.cumt.chiduoduo.model.Building;
import com.cumt.chiduoduo.model.School;
import com.cumt.chiduoduo.model.User;
import com.cumt.chiduoduo.model.UserPassword;
import com.cumt.chiduoduo.response.CommonReturnType;
import com.cumt.chiduoduo.service.*;
import com.cumt.chiduoduo.service.impl.SendEmailService;
import com.cumt.chiduoduo.service.model.UserModel;
import com.cumt.chiduoduo.utils.CreateOtp;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



/**
 * @author ：Chiduoduo Team
 * @date ：Created in 2019/3/1 15:33
 * @description：用户功能模块api接口控制类
 *
 */
@Slf4j
@RestController("user")
@RequestMapping("/user")
//defalut_allowed_headers允许跨域传输所有的header参数，将用于使用token放入header域做session共享的跨域请求
@CrossOrigin(allowCredentials = "true",allowedHeaders =  "*")
public class UserController extends BaseController {

  @Autowired
  private UserService userService;
  @Autowired
  private MailService mailService;
  @Autowired
  private SchoolService schoolService;
  @Autowired
  private BuildingService buildingService;
  @Autowired
  private HttpServletRequest httpServletRequest;
  @Autowired
  private SendEmailService sendEmailService;
  @Autowired
  private UserLoginService userLoginService;

  Map<String, String> emailAndOtp = new HashMap<>();
  BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

  SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

//  //获取邮箱验证码接口
//  @RequestMapping(value = "/geteotp", method = RequestMethod.POST,
//    consumes = {CONTENT_TYPE_FORMED})
//  public CommonReturnType getEmailOtp(@RequestParam(name = "email") String email) throws BusinessException {
//    //检测此邮箱是否未被使用过
//    if (userService.validateEmail(email)) {
//      throw new BusinessException(EnumBusinessError.PARAMETER_VALIDATION_ERROR, "该用户已存在，请直接登录。");
//    }
//    //获取按照一定的规则生成OTP验证码
//    //String otpCode= CreateOTP.createOTPWords();
//    //将OTP验证码同对应用户邮箱关联,使用httpsession的方式绑定他邮箱与OTPcode
//    //httpServletRequest.getSession().setAttribute(email,otpCode);
//    //将OTP验证码通过邮箱通道发送给用户
//    String otpCode = new CreateOtp().createOTP();
//    emailAndOtp.put(email, otpCode);//将邮箱与验证码进行单一绑定
//    httpServletRequest.getSession().setAttribute(email,otpCode);//将邮箱与验证码进行单一绑定
//    mailService.sendSimpleMail(email,"【吃哆哆】邮箱验证码",
//      "【吃哆哆】您的验证码是"+otpCode+",请于30分钟之内输入，工作人员不会向您索取，请勿泄露。");
//    mailService.sendSimpleMail(email,"[吃哆哆]",
//            "您的【吃哆哆】验证码为:"+otpCode+"。");
//    return CommonReturnType.create(null);
//  }

  /**
   * 检测此邮箱是否未被使用过，向用户发送邮件验证码
   * @param email
   * @return
   * @throws BusinessException
   */
  @RequestMapping(value = "/geteotp", method = RequestMethod.POST,
          consumes = {CONTENT_TYPE_FORMED})
  public CommonReturnType getEmailOtp(@RequestParam(name = "email") String email) throws BusinessException {
    //检测此邮箱是否未被使用过
    if (userService.validateEmail(email)) {
      throw new BusinessException(EnumBusinessError.PARAMETER_VALIDATION_ERROR, "该用户已存在，请直接登录。");
    }
    //获取按照一定的规则生成OTP验证码
    String otpCode = new CreateOtp().createOTP();
   /* emailAndOtp.put(email, otpCode);//将邮箱与验证码进行单一绑定*/
    httpServletRequest.getSession().setAttribute(email,otpCode);//将邮箱与验证码进行单一绑定
    //向用户发送邮件验证码
    sendEmailService.send(email,otpCode,
      "【吃哆哆验证码】",
      "您在【吃哆哆】的验证码为：" +otpCode+"，请您在5分钟内输入，并勿向任何人提供您收到的邮箱验证码。");
    String time = format.format(new Date().getTime());
    log.warn("在"+time+"有用户通过邮箱的方式，获取验证码用于注册账号，使用的邮箱为："+email);
    return CommonReturnType.create(null);
  }

  /**
   * 验证邮箱号与验证码是否匹配
   * @param email
   * @param otpCode
   * @return
   * @throws BusinessException
   */
  @RequestMapping(value = "/checkotp", method = RequestMethod.POST,
    consumes = {CONTENT_TYPE_FORMED})
  public CommonReturnType checkOtp(@RequestParam(name = "email") String email,
                                   @RequestParam(name = "otp") String otpCode) throws BusinessException {
    if (StringUtils.isEmpty(email) || StringUtils.isEmpty(otpCode)) {
      throw new BusinessException(EnumBusinessError.PARAMETER_VALIDATION_ERROR, "邮箱或者验证码传输失败");
    }
//     String inSessionOtpCode = emailAndOtp.get(email);
    String inSessionOtpCode = (String) httpServletRequest.getSession().getAttribute(email);
    if (!com.alibaba.druid.util.StringUtils.equals(otpCode, inSessionOtpCode)) {
      throw new BusinessException(EnumBusinessError.PARAMETER_VALIDATION_ERROR, "邮箱验证码错误");
    }
    return CommonReturnType.create(null);
  }

  /**
   * 用户注册时，向前端提供所有学校信息用于用户进行选择
   * @return
   */
  @RequestMapping("/getschool")
  public CommonReturnType getSchool() {
    List<School> schoolList = schoolService.selectAllSchools();
    return CommonReturnType.create(schoolList);
  }

  /**
   * 用户注册时，前端选择学校后，通过学校编号选择宿舍楼
   * @param schoolId
   * @return
   * @throws BusinessException
   */
  @RequestMapping(value = "/getbuilding", method = RequestMethod.POST,
    consumes = {CONTENT_TYPE_FORMED})
  public CommonReturnType getBuidingBySchoolId(@RequestParam(name = "school_id") Integer schoolId) throws BusinessException {
    List<Building> buildingList = buildingService.selectBuildingBySchoolId(schoolId);
    return CommonReturnType.create(buildingList);
  }

  /**
   * 通过学校名称获取宿舍信息
   * @param schoolName
   * @return
   * @throws BusinessException
   */
  @RequestMapping(value = "/getbuildingbyschoolname", method = RequestMethod.POST,
    consumes = {CONTENT_TYPE_FORMED})
  public CommonReturnType getBuidingBySchoolName(@RequestParam(name = "school_name") String  schoolName) throws BusinessException {
    if (schoolName == null) {
      throw new BusinessException(EnumBusinessError.PARAMETER_VALIDATION_ERROR);
    }
    List<Building> buildingList = buildingService.selectBuildingBySchoolName(schoolName);
    return CommonReturnType.create(buildingList);
  }

  /**
   * 通过楼层编号获取宿舍号信息
   * 用户注册接口
   * @param name
   * @param buildingId
   * @param domitoryNo
   * @param schoolId
   * @param email
   * @param telphone
   * @param password
   * @return
   * @throws BusinessException
   */
  @RequestMapping(value = "/register", method = RequestMethod.POST,
    consumes = {CONTENT_TYPE_FORMED})
  public CommonReturnType register(@RequestParam(name = "name") String name,
                                   @RequestParam(name = "building_id") Integer buildingId,
                                   @RequestParam(name = "domitory_No") String  domitoryNo,
                                   @RequestParam(name = "school_id")Integer schoolId,
                                   @RequestParam(name = "email") String email,
                                   @RequestParam(name = "telphone") String telphone,
                                   @RequestParam(name = "password") String password
  ) throws BusinessException {
    UserModel userModel = new UserModel();
    userModel.setUserName(name);
    userModel.setSchoolId(schoolId);
    userModel.setBuildingId(buildingId);
    userModel.setEmail(email);
    userModel.setDomitoryNo(domitoryNo);
    userModel.setTelphone(telphone);
    userModel.setLastResetPasswordTime(new Date());
    //对密码进行加密处理

    String BCryptPassword=bCryptPasswordEncoder.encode(password);
    userModel.setPassword(BCryptPassword);
//将userModel的对象完善后，调用service层的注册方法进行用户注册
    userService.register(userModel);
    String time = format.format(new Date().getTime());
    log.warn("在"+time+"注册了一个用户，注册方式为：使用邮箱注册，其邮箱为："+email);
    return CommonReturnType.create(null);
  }

  /**
   * 用户登录接口
   * @param email
   * @param password
   * @return
   * @throws Exception
   */
  @RequestMapping(value = "/login", method = RequestMethod.POST,
    consumes = {CONTENT_TYPE_FORMED})
  public CommonReturnType login(@RequestParam(name = "email") String email,
                                @RequestParam(name = "password") String password) throws Exception {
    //入参校验
    if (StringUtils.isEmpty(password) ||
      StringUtils.isEmpty(email)) {
      throw new BusinessException(EnumBusinessError.PARAMETER_VALIDATION_ERROR, "邮箱或密码不能为空");
    }
    //通过邮箱在数据库查找用户
    User user = userService.selectByEmail(email);
    //通过找出的用户的id查找他存放在密码表里的密码
    UserPassword userPassword = userService.selectByUserId(user.getId());
    //密码比对，验证用户登录是否合法
    if (!bCryptPasswordEncoder.matches(password, userPassword.getPassword())) {
      throw new BusinessException(EnumBusinessError.PARAMETER_VALIDATION_ERROR, "用户邮箱或密码错误");
    }
    //将此用户的状态设置为登录状态
    this.httpServletRequest.getSession().setAttribute("IS_LOGIN",true);
    this.httpServletRequest.getSession().setAttribute("LOGIN_USER",user);

    userLoginService.userLogin(user.getId());

      String time = format.format(new Date().getTime());
      log.warn("用户"+user.getId()+"在"+time+"进行登录操作。");
    return CommonReturnType.create(null);
    }

  /**
   * 用户重置密码时的获取验证码接口
   * @param emailForReset
   * @return
   * @throws BusinessException
   */
  @RequestMapping(value = "/resetpasswordotp", method = RequestMethod.POST,
    consumes = {CONTENT_TYPE_FORMED})
  public CommonReturnType getResetPasswordOTP(@RequestParam(name = "email") String emailForReset) throws BusinessException {
    //检查数据库中，该邮箱是否已经注册
    if (!userService.validateEmail(emailForReset)) {
      String time = format.format(new Date().getTime());
      log.warn("在"+time+"一个未注册的用户通过使用邮箱想要找回密码，其邮箱为："+emailForReset+",通知其先去注册账号");
      throw new BusinessException(EnumBusinessError.PARAMETER_VALIDATION_ERROR, "用户不存在，无法找回密码，请您直接注册");
    }
    String otpCode = new CreateOtp().createOTP();
    httpServletRequest.getSession().setAttribute(emailForReset,otpCode);//将邮箱与验证码进行单一绑定
    sendEmailService.send(emailForReset,otpCode,
      "【吃哆哆】您正在通过邮箱找回密码",
      "【吃哆哆】您的验证码为：" +otpCode+"，请您在5分钟内输入，并勿向任何人提供您收到的邮箱验证码。");
//    mailService.sendSimpleMail(emailForReset,"【吃哆哆】您正在通过邮箱找回密码",
//      "【吃哆哆】您的验证码是"+otpCode+",请于30分钟之内输入，工作人员不会向您索取，请勿泄露。");
    String time = format.format(new Date().getTime());
    log.warn("在"+time+"用户通过使用邮箱修改或找回密码，其邮箱为："+emailForReset);
    return CommonReturnType.create(null);
  }

  /**
   * 用户找回密码接口
   * @param email
   * @param otpCode
   * @param newPassword
   * @return
   * @throws BusinessException
   */
    @RequestMapping(value = "/resetpassword", method = RequestMethod.POST,
      consumes = {CONTENT_TYPE_FORMED})
    public CommonReturnType resetPassword(@RequestParam(name = "email") String email,
                                          @RequestParam(name = "otp") String otpCode,
                                          @RequestParam(name = "newPassword") String newPassword ) throws BusinessException {
      if (StringUtils.isEmpty(email) || StringUtils.isEmpty(otpCode)||StringUtils.isEmpty(newPassword)) {
        throw new BusinessException(EnumBusinessError.PARAMETER_VALIDATION_ERROR, "信息传输失败，部分内容为空");
      }
    //核实是否为用户找回密码时获取的验证码
//      String inSessionOtpCode = (String) httpServletRequest.getSession().getAttribute(email);
      if (!com.alibaba.druid.util.StringUtils.equals(otpCode, String.valueOf(httpServletRequest.getSession().getAttribute(email)))) {
        String time = format.format(new Date().getTime());
        log.warn("在"+time+"一个已注册的用户通过使用邮箱想要找回密码，其邮箱为："+email+",但是验证码输入错误");
        throw new BusinessException(EnumBusinessError.PARAMETER_VALIDATION_ERROR, "邮箱验证码错误");
      }
      //重置密码
        userService.resetPassword(email, newPassword);
      String time = format.format(new Date().getTime());
      log.warn("在"+time+"一个已注册的用户通过使用邮箱成功找回或修改密码，其邮箱为："+email);

    return CommonReturnType.create(null);
    }

  /**
   * 根据email获取用户信息
   * @param email
   * @return
   * @throws BusinessException
   */
  @RequestMapping(value = "/getbyemail", method = RequestMethod.POST,
          consumes = {CONTENT_TYPE_FORMED})
  public CommonReturnType getByEmail(@RequestParam(name = "email") String email) throws BusinessException {
    if (email == null) {
      throw new BusinessException(EnumBusinessError.PARAMETER_VALIDATION_ERROR);
    }
    UserModel user = userService.getByEmail(email);
    if (user == null) {
      throw new BusinessException(EnumBusinessError.UNKNOWN_ERROR, "此email无用户注册");
    }
    UserModelVO userModelVO = new UserModelVO();
    BeanUtils.copyProperties(user,userModelVO);
    return CommonReturnType.create(userModelVO);
  }

  /**
   * 用户退出登录
   * @param userId
   * @return
   * @throws BusinessException
   */
    @RequestMapping(value = "/userlogout", method = RequestMethod.POST,
            consumes = {CONTENT_TYPE_FORMED})
    public CommonReturnType userLogout(@RequestParam(name = "user_id") Integer userId) throws BusinessException {
        if (userId == null) {
            throw new BusinessException(EnumBusinessError.PARAMETER_VALIDATION_ERROR);
        }
        boolean result=userLoginService.userLogout(userId);
        return CommonReturnType.create(result);
    }
}
