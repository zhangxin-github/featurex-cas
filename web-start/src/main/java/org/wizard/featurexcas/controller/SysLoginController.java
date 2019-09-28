package org.wizard.featurexcas.controller;

import com.google.common.collect.Maps;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.subject.Subject;
import org.patchca.color.SingleColorFactory;
import org.patchca.filter.predefined.CurvesRippleFilterFactory;
import org.patchca.service.ConfigurableCaptchaService;
import org.patchca.utils.encoder.EncoderHelper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.wizard.common.model.CommonResult;
import org.wizard.security.core.shrio.MyUsernamePasswordToken;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

/**
 *
 */
@RestController
public class SysLoginController {

    /**
     * 验证码
     */
    @GetMapping("/captcha.jpg")
    public void captcha(HttpServletResponse response, HttpSession session)throws IOException {
        response.setHeader("Cache-Control", "no-store, no-cache");
        response.setContentType("image/jpeg");

        ConfigurableCaptchaService cs = new ConfigurableCaptchaService();
        cs.setColorFactory(new SingleColorFactory(new Color(25, 60, 170)));
        cs.setFilterFactory(new CurvesRippleFilterFactory(cs.getColorFactory()));

        OutputStream os = response.getOutputStream();
        String challenge = EncoderHelper.getChallangeAndWriteImage(cs, "png", os);
        session.setAttribute("captcha", challenge);
        os.close();
    }

    /**
     * 登录
     */
//    @PostMapping("/sys/login")
//    public Map<String, Object> login(@RequestBody MyUsernamePasswordToken token)throws IOException {
//        Map<String, Object> result = Maps.newHashMap();
//        try {
//            Subject subject = SecurityUtils.getSubject();
//            subject.login(token);
//            String authorization = (String) subject.getSession().getId();
//            result.put("token", authorization); //将authorization传给前端，用于MySessionManager中请求的验证
//            result.put("code",0);
//        } catch (IncorrectCredentialsException e) {
//            result.put("msg", "密码错误");
//        } catch (LockedAccountException e) {
//            result.put("msg", "该用户已被禁用");
//        } catch (AuthenticationException e) {
//            result.put("msg", "该用户不存在");
//        }
//        return result;
//    }

    /**
     *
     * @param request
     * @return
     */
    @PostMapping("/sys/login")
    public CommonResult login(HttpServletRequest request) {
        String msg = parseException(request);
        if(StringUtils.isNotBlank(msg))
            return CommonResult.error(msg);
        Subject subject = SecurityUtils.getSubject();
        return CommonResult.ok().put("token", subject.getSession().getId());
    }

    private String parseException(HttpServletRequest request) {

        String errorString = (String) request.getAttribute("shiroLoginFailure");
        Class<?> error = null;
        try {
            if (errorString != null) {
                error = Class.forName(errorString);
            }
        } catch (ClassNotFoundException e) {
        }
        String msg = "";
        if (error != null) {
//			if (error.equals(UnknownAccountException.class)) {
//				msg = "未知帐号错误！";
//			} else if (error.equals(IncorrectCredentialsException.class)) {
//				msg = "密码错误！";
//			} else
//            if (error.equals(IncorrectCaptchaException.class)) {
//                msg = "验证码错误！";
//            }
            if (error.equals(AuthenticationException.class)) {
                msg = "认证失败，请您重新输入。";
            }
//			if (error.equals(DisabledAccountException.class))
//				msg = "无效用户！";
//		    if (error.equals(SimplePasswordException.class)) {
//				msg = "SimplePasswordException";
//			}
        }
        return msg;
    }
}
