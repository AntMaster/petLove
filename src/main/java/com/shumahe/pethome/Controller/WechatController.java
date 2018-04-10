package com.shumahe.pethome.Controller;


import com.google.gson.JsonObject;
import com.shumahe.pethome.Config.ProjectUrlConfig;
import com.shumahe.pethome.Config.WechatAccountConfig;
import com.shumahe.pethome.Domain.MemberTagsMapping;
import com.shumahe.pethome.Domain.UserApprove;
import com.shumahe.pethome.Domain.UserBasic;
import com.shumahe.pethome.Enums.ApproveStateEnum;
import com.shumahe.pethome.Enums.MemberTagsEnum;
import com.shumahe.pethome.Enums.ResultEnum;
import com.shumahe.pethome.Exception.PetHomeException;
import com.shumahe.pethome.Repository.MemberTagsMappingRepository;
import com.shumahe.pethome.Repository.UserBasicRepository;
import com.shumahe.pethome.Util.ResultVOUtil;
import com.shumahe.pethome.VO.ResultVO;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.bean.WxJsapiSignature;
import me.chanjar.weixin.common.bean.menu.WxMenu;
import me.chanjar.weixin.common.bean.menu.WxMenuButton;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpConfigStorage;
import me.chanjar.weixin.mp.api.WxMpInMemoryConfigStorage;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.menu.WxMpMenu;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import net.minidev.json.JSONObject;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


/**
 * Created by zhangyu
 */
@Controller
@RequestMapping("/wechat")
@Slf4j
public class WechatController {
    //http://girl.nat300.top/pethome/wechat/authorize?returnUrl=http://girl.nat300.top/pethome/index.html
    //http://girl.nat300.top/pethome/index.html?openid=oCLNDwc8bUgjnBUibOX1yfPh5Ni0
    @Autowired
    private WxMpService wxMpService;

    @Autowired
    private ProjectUrlConfig projectUrlConfig;

    @Autowired
    private UserBasicRepository userBasicRepository;

    @Autowired
    private WechatAccountConfig wechatAccountConfig;

    @Autowired
    private MemberTagsMappingRepository memberTagsMappingRepository;


    /**
     * 模板消息推送
     *
     * @return
     */
    /*@PostMapping("/templateMsgPush/{openid}")
    @ResponseBody*/
    public String templateMsgPush( String openId,
                                   UserApprove userApprove) throws WxErrorException {

        WxMpConfigStorage configStorage = wxMpService.getWxMpConfigStorage();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd HH:mm:ss");
        String keyword2Msg = "审核通过!";
        String reMarkMsg = "恭喜您！（"+ userApprove.getOrganizationName() +"）已通过认证，去解锁更多动物信息!";
        String reMarkColor = "#2E8800";
        if (userApprove.getApproveState() == ApproveStateEnum.FAILURE.getCode()){
            keyword2Msg = "审核失败!";
            reMarkMsg = "很遗憾！（"+ userApprove.getOrganizationName() +"）未能通过认证，"+ userApprove.getDescription() +"!";
            reMarkColor = "#E73631";
        }

        WxMpTemplateMessage templateMessage = WxMpTemplateMessage.builder()
                .toUser(openId)
                .templateId(configStorage.getTemplateId())
                .url(projectUrlConfig.getWechatMpAuthorize() + "/pethome/index.html?openid="+openId)
                .build();

        templateMessage
                .addData(new WxMpTemplateData("first",  "您好，您提交的实名审核已经完成！"))
                .addData(new WxMpTemplateData("keyword1", "实名认证审核"))
                .addData(new WxMpTemplateData("keyword2",   keyword2Msg))
                .addData(new WxMpTemplateData("keyword3", dateFormat.format(new Date())))
                .addData(new WxMpTemplateData("remark", reMarkMsg, reMarkColor));
        String msgId = wxMpService.getTemplateMsgService().sendTemplateMsg(templateMessage);
        return msgId;
    }


    /**
     * 服务器配置
     *
     * @param signature
     * @param timestamp
     * @param nonce
     * @param echostr
     * @return
     */
    @GetMapping(path = "/serverConfig")
    @ResponseBody
    public String authGet(@RequestParam(name = "signature") String signature,
                          @RequestParam(name = "timestamp") String timestamp,
                          @RequestParam(name = "nonce") String nonce,
                          @RequestParam(name = "echostr") String echostr) {


        log.info("\n接收到来自微信服务器的认证消息：[{}, {}, {}, {}]", signature, timestamp, nonce, echostr);

        if (StringUtils.isAnyBlank(signature, timestamp, nonce, echostr)) {
            throw new IllegalArgumentException("请求参数非法，请核实!");

        }

        if (wxMpService.checkSignature(timestamp, nonce, signature)) {
            return echostr;
        }

        return "非法请求";
    }


    /**
     * 网页授权
     *
     * @param returnUrl
     * @return
     */
    @GetMapping("/webAuth")
    public String authorize(@RequestParam("returnUrl") String returnUrl) {

        //1. 配置
        //2. 调用方法
        String url = projectUrlConfig.getWechatMpAuthorize() + "/pethome/wechat/userinfo";
        String redirectUrl;
        try {
            redirectUrl = wxMpService.oauth2buildAuthorizationUrl(url, WxConsts.OAuth2Scope.SNSAPI_USERINFO, URLEncoder.encode(returnUrl, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            log.error("【有异常AAAAA】{}", e);
            throw new PetHomeException(999, e.getMessage());
        }

        return "redirect:" + redirectUrl;
    }


    /**
     * 网页授权
     * <p>
     * code仅可以使用1次，第一次获取到的code使用过之后，传去别的地方用就会报这个错。要想再用，就得在代码中重新构建微信请求连接去请求获得新的code（所以5分钟之内有两次请求code的就会报）
     * <p>
     * 关于网页授权access_token和普通access_token的区别
     * 1、微信网页授权是通过OAuth2.0机制实现的，在用户授权给公众号后，公众号可以获取到一个网页授权特有的接口调用凭证（网页授权access_token），通过网页授权access_token可以进行授权后接口调用，如获取用户基本信息； *
     *
     * @param code
     * @param returnUrl
     * @return http://girl.nat300.top/pethome//detail.html?openid=oCLNDwc8bUgjnBUibOX1yfPh5Ni0
     * @throws WxErrorException
     */
    @GetMapping("/userinfo")
    public String userInfo(@RequestParam("code") String code,
                           @RequestParam("state") String returnUrl) throws WxErrorException {



        WxMpOAuth2AccessToken wxMpOAuth2AccessToken = null;//如果code使用过，5分钟内再次使用会报错。
        WxMpUser user = null;
        try {
            wxMpOAuth2AccessToken = wxMpService.oauth2getAccessToken(code);
            user = wxMpService.oauth2getUserInfo(wxMpOAuth2AccessToken, null);
        } catch (WxErrorException e) {

            log.error("【有异常BBBB】{}", e);
            //{"errcode":40001,"errmsg":"invalid credential, access_token is invalid or not latest, hints: [ req_id: 4y0XGa0264s152 ]"}
            if (e.getError().getErrorCode() == 40001){
                wxMpOAuth2AccessToken = wxMpService.oauth2refreshAccessToken(wxMpOAuth2AccessToken.getRefreshToken());
                log.error("【有异常CCCCC】{}", e);
            }
        }

        UserBasic userBasic = saveUser(user);
        if (returnUrl.contains("detail.html")) {//详情页面

            //认证
            if (userBasic.getApproveState() == 1) {
                return "redirect:" + returnUrl + "&openid=" + wxMpOAuth2AccessToken.getOpenId();
            } else {//未认证
                return "redirect:" + returnUrl.split("detail.html")[0] + "index.html?openid=" + wxMpOAuth2AccessToken.getOpenId();
            }

        } else {
            return "redirect:" + returnUrl + "?openid=" + wxMpOAuth2AccessToken.getOpenId();
        }
    }


    /**
     * JSAPI
     *
     * @param url
     * @return
     */
    @GetMapping("/jsApiSignature")
    @ResponseBody
    public ResultVO forward(@RequestParam(name = "url") String url) {

        WxJsapiSignature jsapiSignature = null;
        try {
            jsapiSignature = wxMpService.createJsapiSignature(url);
        } catch (WxErrorException e) {
            throw new PetHomeException(ResultEnum.WECHAT_MP_ERROR.getCode(), e.getMessage());
        }
        return ResultVOUtil.success(jsapiSignature);
    }


    @GetMapping("/menu")
    @ResponseBody
    public ResultVO findWxMenu() {
        try {
            WxMpMenu wxMpMenu = wxMpService.getMenuService().menuGet();
            return ResultVOUtil.success(wxMpMenu);

        } catch (WxErrorException e) {
            throw new PetHomeException(ResultEnum.WECHAT_MP_ERROR.getCode(), e.getMessage());
        }
    }

    @PostMapping("/menu")
    public ResultVO saveWxMenu() {
        try {

            WxMenuButton button = new WxMenuButton();
            button.setName("宠爱有家");
            button.setType("view");
            button.setUrl("http://girl.nat300.top/pethome/wechat/webAuth?returnUrl=http://girl.nat300.top/pethome/index.html");
            List<WxMenuButton> wxMenuButtons = Arrays.asList(button);

            WxMenu wxMenu = new WxMenu();
            wxMenu.setButtons(wxMenuButtons);

            wxMpService.getMenuService().menuCreate(wxMenu);
            return ResultVOUtil.success(wxMenu);

        } catch (WxErrorException e) {
            throw new PetHomeException(ResultEnum.WECHAT_MP_ERROR.getCode(), e.getMessage());
        }
    }


    @Transactional
    protected UserBasic saveUser(WxMpUser user) {

        UserBasic userBasic = userBasicRepository.findByAppIdAndOpenId(wechatAccountConfig.getMpAppId(), user.getOpenId());
        if (userBasic == null) {
            userBasic = new UserBasic();
            userBasic.setOpenId(user.getOpenId());
            userBasic.setAppId(wechatAccountConfig.getMpAppId());
        }

        userBasic.setNickName(user.getNickname());
        userBasic.setHeadImgUrl(user.getHeadImgUrl());
        userBasic.setSex(user.getSex());
        UserBasic save = userBasicRepository.save(userBasic);

        MemberTagsMapping tagsMapping = new MemberTagsMapping();
        tagsMapping.setMemberId(save.getId());
        tagsMapping.setTagId(MemberTagsEnum.Volunteer.getCode());
        memberTagsMappingRepository.save(tagsMapping);

        return userBasic;
    }
}
