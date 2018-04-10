package com.shumahe.pethome.Aspect;

import com.shumahe.pethome.Domain.UserBasic;
import com.shumahe.pethome.Enums.ResultEnum;
import com.shumahe.pethome.Exception.PetHomeException;
import com.shumahe.pethome.Repository.UserBasicRepository;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.condition.RequestConditionHolder;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class AuthAspect {


    @Autowired
    UserBasicRepository userBasicRepository;

    //@Before("execution(public * com.shumahe.pethome.Controller.PetController.*(..))")
    //@Before("execution(public * com.shumahe.pethome.Controller.PublishController.*(..))")


    @Before("execution(public * com.shumahe.pethome.Controller.DynamicController.*(..))")
    public void validaOpenId() {

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String[] URI = request.getRequestURI().split("/");
        String openId = URI[URI.length - 1];

        UserBasic user = userBasicRepository.findByOpenId(openId);
        if (user == null) {
            throw new PetHomeException(ResultEnum.OPENID_ERROR);
        }
    }


    @Pointcut("execution(public * com.shumahe.pethome.Controller.PublishController.*(..))")
    public void validateAutoInfo() {

    }

 /*   @Before("validateAutoInfo()")
    public void doBefore() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String openId = request.getParameter("openId");
        if (StringUtils.isEmpty(openId)) {
            throw new PetHomeException(ResultEnum.OPENID_EMPTY);
        }
    }

    @After("validateAutoInfo()")
    public void doAfter() {
        log.info("22222");
    }*/
}
