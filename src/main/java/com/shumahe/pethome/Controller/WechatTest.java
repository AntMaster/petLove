package com.shumahe.pethome.Controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/test")
public class WechatTest {


    @Autowired
    WxMpService wxMpService;
    @GetMapping("/autotest")
    public String index1(@RequestParam("redirectUrl") String redirectUrl){

        wxMpService.oauth2buildAuthorizationUrl(redirectUrl, WxConsts.OAuth2Scope.SNSAPI_USERINFO, null);
        return "aaa";
    }

    /**
     *
     *
     * 1.请求下列网址获取code值
     *  https://open.weixin.qq.com/connect/oauth2/authorize?
     *  appid=wx41fd8229749f5df8&
     *  redirect_uri=http://girl.nat300.top/pethome/test/auto&
     *  response_type=code&
     *  scope=snsapi_userinfo&
     *  state=zhangyu#wechat_redirect
     *
     * @return  http://girl.nat300.top/pethome/test/auto?code=code&state=state
     */
    @GetMapping("auto")
    public Map<String,Object> auto(@RequestParam("code") String code,@RequestParam("state") String state){
        //return "hello";

        /**
         * 2.通过code值获取AccessToken
         *
         */
        String getAccessTokenUrl =  "https://api.weixin.qq.com/sns/oauth2/access_token?" +
                "appid=wx41fd8229749f5df8&secret=76cde729d3bbf04728661309549298f5&code="+ code +"&grant_type=authorization_code";

        RestTemplate restTemplate = new RestTemplate();
        String accessToken = restTemplate.getForObject(getAccessTokenUrl, String.class);

        Gson gson = new Gson();
        Map<String,Object> accessTokenMap = new HashMap<>();
        accessTokenMap = gson.fromJson(accessToken, accessTokenMap.getClass());

        /**
         * 3.根据OpenId获取用户信息
         *
         */
        String getUserInfoUrl = "https://api.weixin.qq.com/sns/userinfo?access_token="+ accessTokenMap.get("access_token") +"&openid="+ accessTokenMap.get("openid") +"&lang=zh_CN";
        RestTemplate restTemplate1 = new RestTemplate();
        String userInfo = restTemplate1.getForObject(getUserInfoUrl , String.class);

        Map<String,Object> userInfoMap = new HashMap<>();
        userInfoMap = gson.fromJson(userInfo, userInfoMap.getClass());
        return userInfoMap;
    }
}
