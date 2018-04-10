package com.shumahe.pethome.Config;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;


/**
 * Created by zhangyu
 * 2018-02-23 21:50
 */

@Data
@Component
@ConfigurationProperties(prefix = "wechat")
public class WechatAccountConfig {


    private String mpAppId;

    private String mpAppSecret;

    private String myAppToken;

    private String myAppEncodingAESKey;

    private String myAppMsgTemplateId;


    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this,
                ToStringStyle.MULTI_LINE_STYLE);
    }
}
