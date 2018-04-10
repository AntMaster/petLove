package com.shumahe.pethome.Config;

import me.chanjar.weixin.mp.api.WxMpConfigStorage;
import me.chanjar.weixin.mp.api.WxMpInMemoryConfigStorage;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * Created by zhangyu
 * 2018-02-23 21:50
 */

@Component
public class WechatMpConfig {

    @Autowired
    private WechatAccountConfig accountConfig;

    @Bean
    public WxMpService wxMpService() {

        WxMpService wxMpService = new WxMpServiceImpl();
        wxMpService.setWxMpConfigStorage(wxMpConfigStorage());
        return wxMpService;
    }

    @Bean
    public WxMpConfigStorage wxMpConfigStorage() {

        WxMpInMemoryConfigStorage config = new WxMpInMemoryConfigStorage();
        config.setAppId(accountConfig.getMpAppId());
        config.setSecret(accountConfig.getMpAppSecret());
        config.setToken(accountConfig.getMyAppToken());
        config.setAesKey(accountConfig.getMyAppEncodingAESKey());
        config.setTemplateId(accountConfig.getMyAppMsgTemplateId());
        return config;
    }
}
