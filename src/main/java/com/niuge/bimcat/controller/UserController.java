package com.niuge.bimcat.controller;

import com.niuge.bimcat.utils.ValidateCodeUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

@Controller
public class UserController {

    private static final transient Logger log = LoggerFactory.getLogger(CookieRememberMeManager.class);
    /**
     * 测试方法
     */
    @RequestMapping("/hello")
    @ResponseBody
    public String hello(){
        System.out.println("UserController.hello()");
        return "ok";
    }

    @RequestMapping("/add")
    public String add(){
        return "/user/add";
    }

    @RequestMapping("/update")
    public String update(){
        return "/user/update";
    }

    @RequestMapping("/toLogin")
    public String toLogin(){
        return "/login";
    }

    @RequestMapping("/noAuth")
    public String noAuth(){
        return "/noAuth";
    }

    /**
     * 测试thymeleaf
     */
    @RequestMapping("/index")
    public String index(){
        //返回index.html
        return "index";
    }

    @RequestMapping("/cloth")
    public String cloth(){
        //返回index.html
        return "cloth";
    }

    @RequestMapping("/logout")
    public String logout() {
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()) {
            subject.logout(); // session 会销毁，在SessionListener监听session销毁，清理权限缓存
            if (log.isDebugEnabled()) {
                log.debug("用户退出登录");
            }
        }
        return "/login";
    }

    /**
     * 生成验证码
     * @param request
     * @param response
     * @throws IOException
     * @ValidateCode.generateTextCode(验证码字符类型,验证码长度,需排除的特殊字符)
     * @ValidateCode.generateImageCode(文本验证码,图片宽度,图片高度,干扰线的条数,字符的高低位置是否随机,图片颜色,字体颜色,干扰线颜色)
     */
    @RequestMapping(value = "/validateCode")
    public void validateCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setHeader("Cache-Control", "no-cache");
        String verifyCode = ValidateCodeUtils.generateTextCode(ValidateCodeUtils.TYPE_NUM_LOWER, 4, null);
        request.getSession().setAttribute("validateCode", verifyCode);
        response.setContentType("image/jpeg");
        BufferedImage bim = ValidateCodeUtils.generateImageCode(verifyCode, 90, 30, 5, true, Color.WHITE, Color.BLUE, null);
        ImageIO.write(bim, "JPEG", response.getOutputStream());
    }

    /**
     * 登录逻辑处理
     */
    @RequestMapping("/login")
    public String login(String name,String password,String randomcode,Boolean isRememberMe,Model model){

        Session session = SecurityUtils.getSubject().getSession();
        String code = (String) session.getAttribute("validateCode");
        if (StringUtils.isEmpty(randomcode) || !code.equals(randomcode.toLowerCase())) {
            model.addAttribute("msg", "验证码错误");
            return "login";
        }

        log.info("name="+name);
        /**
         * 使用Shiro编写认证操作
         */
        //1.获取Subject
        Subject subject = SecurityUtils.getSubject();

        //2.封装用户数据
        UsernamePasswordToken token = new UsernamePasswordToken(name,password);
        if (null!=isRememberMe&&isRememberMe==true){
            token.setRememberMe(true);
        }
        else {
            token.setRememberMe(false);
        }

        //3.执行登录方法
        try {
            subject.login(token);

            //登录成功
            //跳转到test.html
            return "redirect:/index";
        } catch (UnknownAccountException e) {
            //e.printStackTrace();
            //登录失败:用户名不存在
            model.addAttribute("msg", "用户名不存在");
            return "login";
        }catch (IncorrectCredentialsException e) {
            //e.printStackTrace();
            //登录失败:密码错误
            model.addAttribute("msg", "密码错误");
            return "login";
        }
        catch (Exception e){
            model.addAttribute("msg", "未知错误，请联系管理员！");
            return "login";
        }
    }
}
