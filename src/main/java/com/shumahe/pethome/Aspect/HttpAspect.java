package com.shumahe.pethome.Aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class HttpAspect {

    public static void main() {
        System.out.println(12345);
    }
}
