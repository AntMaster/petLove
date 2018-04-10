package com.shumahe.pethome.Config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by zhangyu
 * 2018-02-23 21:50
 */


@Data
@ConfigurationProperties(prefix = "projectUrl")
@Component
public class ProjectUrlConfig {

    /**
     * 微信公众平台授权url
     */
    public String wechatMpAuthorize;

    /**
     * 微信开放平台授权url
     */
    //public String wechatOpenAuthorize;

    /**
     * 寻主寻宠系统url
     */
    public String petHome;
}
