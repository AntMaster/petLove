package com.shumahe.pethome;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.web.servlet.ErrorPage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * 拦截器
 */

@Configuration//配置拦截器
public class WebAppConfig extends WebMvcConfigurerAdapter {//配置适配器



    @Value("${web.upload-path}")
    private  String picturePath;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {


        super.addInterceptors(registry);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {


        registry.addResourceHandler("/upload/**").addResourceLocations("file:" + picturePath);
        registry.addResourceHandler("/mine/upload/**").addResourceLocations("file:" + picturePath);
        super.addResourceHandlers(registry);
    }

    @Bean
    public EmbeddedServletContainerCustomizer containerCustomizer() {
        return container -> {
            ErrorPage error401Page = new ErrorPage(HttpStatus.UNAUTHORIZED, "/sysPage/401.html");
            ErrorPage error404Page = new ErrorPage(HttpStatus.NOT_FOUND, "/sysPage/404.html");
            ErrorPage error500Page = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/sysPage/500.html");
            container.addErrorPages(error401Page, error404Page, error500Page);
        };
    }
}