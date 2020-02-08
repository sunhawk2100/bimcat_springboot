package com.niuge.bimcat.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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

    /**
     * 登录逻辑处理
     */
    @RequestMapping("/login")
    public String login(String name,String password,Boolean isRememberMe,Model model){

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
