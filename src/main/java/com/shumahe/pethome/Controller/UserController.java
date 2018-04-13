package com.shumahe.pethome.Controller;

import com.shumahe.pethome.Config.SMSConfig;
import com.shumahe.pethome.DTO.UserDTO;
import com.shumahe.pethome.Domain.UserApprove;
import com.shumahe.pethome.Domain.UserBasic;
import com.shumahe.pethome.Enums.ApproveStateEnum;
import com.shumahe.pethome.Enums.ApproveTypeEnum;
import com.shumahe.pethome.Enums.BooleanEnum;
import com.shumahe.pethome.Enums.ResultEnum;
import com.shumahe.pethome.Exception.PetHomeException;
import com.shumahe.pethome.Form.UserApproveForm;
import com.shumahe.pethome.Repository.UserApproveRepository;
import com.shumahe.pethome.Repository.UserBasicRepository;
import com.shumahe.pethome.Service.UserService;
import com.shumahe.pethome.Util.MathUtil;
import com.shumahe.pethome.Util.PhoneFormatCheckUtil;
import com.shumahe.pethome.Util.ResultVOUtil;
import com.shumahe.pethome.VO.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserBasicRepository userBasicRepository;

    @Autowired
    UserService userService;

    @Autowired
    SMSConfig smsConfig;


    @Autowired
    UserApproveRepository userApproveRepository;

    @Autowired
    RestTemplate restTemplate;

    /**
     * 用户中心
     *
     * @param openId
     * @return
     */
    @GetMapping("/{openid}")
    public ResultVO findUserCenter(@PathVariable("openid") String openId) {

        UserDTO userDTO = userService.findMyInfo(openId);
        return ResultVOUtil.success(userDTO);
    }

    /**
     * 判断用户认证
     *
     * @param openId
     * @return
     */
    @GetMapping("/auth/{openId}")
    public ResultVO findUserAuto(@PathVariable("openId") String openId) {

        UserBasic user = userBasicRepository.findByOpenId(openId);

        Map<String, String> res = new HashMap<>();
        if (user == null) {
            throw new PetHomeException(ResultEnum.RESULT_EMPTY);
        }

        res.put("mobile", String.valueOf(user.getMobile()));

        if (user.getApproveType() == ApproveTypeEnum.PERSONAGE.getCode()) {

            res.put("type", "person");

            if (user.getApproveState() == ApproveStateEnum.SUCCESS.getCode()) {

                res.put("state", String.valueOf(ApproveStateEnum.SUCCESS.getCode()));

            } else {
                res.put("state", String.valueOf(ApproveStateEnum.FAILURE.getCode()));
            }

        } else {

            /**
             * 协会
             */
            res.put("type", "association");
            if (user.getApproveState() == ApproveStateEnum.WAITING.getCode()) {

                res.put("state", String.valueOf(ApproveStateEnum.WAITING.getCode()));

            } else if (user.getApproveState() == ApproveStateEnum.SUCCESS.getCode()) {

                res.put("state", String.valueOf(ApproveStateEnum.SUCCESS.getCode()));

            } else if (user.getApproveState() == ApproveStateEnum.FAILURE.getCode()) {

                UserApprove approve = userApproveRepository.findTopByUserIdOrderByCreateTimeDesc(openId);
                res.put("state", String.valueOf(ApproveStateEnum.FAILURE.getCode()));
                res.put("msg", approve.getDescription());
            }
        }
        return ResultVOUtil.success(res);
    }


    /**
     * 获取短信验证码1
     */
    @GetMapping("/sms/{openId}")
    public ResultVO getShortMessage(@PathVariable("openId") String openId,
                                    HttpServletRequest request,
                                    @RequestParam("mobile") String mobile) {

        boolean chinaPhoneLegal = PhoneFormatCheckUtil.isChinaPhoneLegal(mobile);
        if (!chinaPhoneLegal) {
            throw new PetHomeException(ResultEnum.PARAM_ERROR.getCode(), "手机号码不正确");
        }

        List<UserBasic> users = userBasicRepository.findByMobile(mobile);
        if (!users.isEmpty()){
            throw new PetHomeException(ResultEnum.RESULT_EMPTY.getCode(),"该手机号已经存在");
        }

        Integer code = MathUtil.getRandomNumber();
        String massage = "【宠爱有家】您的验证码：" + code + "，请在10分钟内按页面提示提交验证码";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> p = new LinkedMultiValueMap<>();
        p.add("account", smsConfig.getAccount());
        p.add("pswd", smsConfig.getPassword());
        p.add("mobile", mobile);
        p.add("msg", massage);

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(p, headers);
        String smsResult = restTemplate.postForObject(smsConfig.getUrl(), entity, String.class);

        String hasError = smsResult.split(",")[1];
        if (!String.valueOf(0).equals(hasError)) {
            throw new PetHomeException(ResultEnum.PARAM_ERROR.getCode(), "发送短信验证码失败!");
        }

        HttpSession session = request.getSession();
        Map<String, String> userSms = new HashMap<>();
        userSms.put("code", code.toString());
        userSms.put("mobile", mobile);
        session.setAttribute(openId, userSms);
        session.setMaxInactiveInterval(10 * 60);
        return ResultVOUtil.success(true);
    }

    /**
     * 个人认证
     */
    @PostMapping("/sms/{openId}")
    public ResultVO checkShortMessage(HttpServletRequest request,
                                      @PathVariable("openId") String openId,
                                      @RequestParam("code") String code,
                                      @RequestParam("mobile") String mobile) {

        HttpSession session = request.getSession();
        Map<String, String> userSms = (Map<String, String>) session.getAttribute(openId);
        if (userSms == null || !code.equals(userSms.get("code")) || !mobile.equals(userSms.get("mobile"))) {
            throw new PetHomeException(ResultEnum.RESULT_EMPTY.getCode(), "验证码已过期,请稍后再获取");
        }

        UserBasic user = userBasicRepository.findByOpenId(openId);
        if (user == null) {
            throw new PetHomeException(ResultEnum.RESULT_EMPTY.getCode(), "没有该用户");
        }

        user.setMobile(String.valueOf(mobile));
        user.setApproveState(ApproveStateEnum.SUCCESS.getCode());
        user.setApproveType(ApproveTypeEnum.PERSONAGE.getCode());
        userBasicRepository.save(user);
        session.removeAttribute(openId);
        return ResultVOUtil.success(true);
    }

    /**
     * 组织提交认证资料
     *
     * @param request
     * @param userApproveForm
     * @param bindingResult
     * @return
     */
    @PutMapping("/auth")
    public ResultVO approveOrganization(HttpServletRequest request, @Valid UserApproveForm userApproveForm, BindingResult bindingResult) {

        //验证表单数据是否正确
        if (bindingResult.hasErrors()) {
            log.error("【组织认证】参数不正确,userApproveForm={}", userApproveForm);
            throw new PetHomeException(ResultEnum.PARAM_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage());
        }

        String code = String.valueOf(userApproveForm.getMessageCode());
        String mobile = userApproveForm.getDutyerPhone();
        String openId = userApproveForm.getUserId();

        HttpSession session = request.getSession();
        Map<String, String> userSms = (Map<String, String>) session.getAttribute(openId);
        if (userSms == null || !code.equals(userSms.get("code")) || !mobile.equals(userSms.get("mobile"))) {
            throw new PetHomeException(ResultEnum.RESULT_EMPTY.getCode(), "验证码已过期,请稍后再获取");
        }

        UserBasic user = userBasicRepository.findByOpenId(openId);
        if (user == null) {
            throw new PetHomeException(ResultEnum.RESULT_EMPTY.getCode(), "没有该用户");
        }

        userService.saveOrganization(userApproveForm, user);
        return ResultVOUtil.success(true);
    }

    /**
     * 我的私信列表
     *
     * @param openId
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/private/{openId}")
    public ResultVO findPrivateTalk(@PathVariable("openId") String openId,
                                    @RequestParam(value = "page", defaultValue = "0") Integer page,
                                    @RequestParam(value = "size", defaultValue = "200") Integer size) {

        PageRequest pageRequest = new PageRequest(page, size);
        List<Map<String, Object>> myPrivateTalk = userService.findMyPrivateTalk(openId, pageRequest);

        return ResultVOUtil.success(myPrivateTalk);
    }


    /**
     * 我的评论列表
     *
     * @param openId
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/public/{openId}")
    public ResultVO findPublicTalk(@PathVariable("openId") String openId,
                                   @RequestParam(value = "page", defaultValue = "0") Integer page,
                                   @RequestParam(value = "size", defaultValue = "200") Integer size) {

        PageRequest pageRequest = new PageRequest(page, size);
        List<Map<String, Object>> myPrivateTalk = userService.findMyPublicTalk(openId, pageRequest);

        return ResultVOUtil.success(myPrivateTalk);
    }


    /**
     * 关注列表(我的关注 + 关注我的)
     *
     * @param
     * @return
     */
    @GetMapping("/like/{openid}")
    public ResultVO<List<Map<String, Object>>> mylikes(@PathVariable(name = "openid") String openid,
                                                       @RequestParam(value = "type", defaultValue = "1") Integer type) {
        if (type != 1 && type != 2) {
            throw new PetHomeException(ResultEnum.PARAM_ERROR.getCode(), "参数不正确,type=1为我的关注,type=2为关注我的");
        }

        List<Map<String, String>> likeResult = userService.findMyDynamic(openid, type);

        return ResultVOUtil.success(likeResult);
    }


    /**
     * 转发列表(我的关注 + 关注我的)
     *
     * @param
     * @return
     */
    @GetMapping("/share/{openid}")
    public ResultVO<List<Map<String, Object>>> myshares(@PathVariable(name = "openid") String openid,
                                                        @RequestParam(value = "type", defaultValue = "7") Integer type) {
        if (type != 7 && type != 8) {
            throw new PetHomeException(ResultEnum.PARAM_ERROR.getCode(), "参数不正确,type=7为我的关注,type=8为关注我的");
        }

        List<Map<String, String>> likeResult = userService.findMyDynamic(openid, type);
        return ResultVOUtil.success(likeResult);
    }


    /**
     * 获取认证信息
     *
     * @param openId
     * @return
     */
    @GetMapping("/organization/{openId}")
    public ResultVO findOrganization(@PathVariable("openId") String openId) {
        UserApprove approve = userApproveRepository.findTopByUserIdOrderByCreateTimeDesc(openId);
        return ResultVOUtil.success(approve);
    }







}


