package com.shumahe.pethome.Config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "web.sms")
@Component
@Data
public class SMSConfig {

    private String account;

    private String password;

    private String url;

}
