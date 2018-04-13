package com.shumahe.pethome.DTO;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;


import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class PublishDTO {

    /**
     * 发布ID
     */
    private Integer id;

    /**
     * 发布人ID
     */
    private String publisherId;

    /**
     * 发布类型
     */
    private Integer publishType;

    /**
     * 发布人昵称
     */
    private String publisherName;

    /**
     * 发布人头像
     */
    private String publisherPhoto;


    /**
     * 创建时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 宠物名称
     */
    private String petName;

    /**
     * 宠物图片
     */
    private String petImage;

    /**
     * 宠物性别
     */
    private Integer petSex;

    /**
     * 宠物类别
     */
    private Integer classifyId;

    /**
     * 宠物类别名称
     */
    private String classifyName;

    /**
     * 宠物品种
     */
    private Integer varietyId;

    /**
     * 宠物品种名称
     */
    private String varietyName = "不告诉你哟~";

    /**
     * 丢失时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date lostTime;

    /**
     * 丢失地点
     */
    private String lostLocation;

    /**
     * 丢失经度
     */
    private Float latitude;

    /**
     * 丢失纬度
     */
    private Float longitude;

    /**
     * 联系人
     */
    private String ownerName;

    /**
     * 联系方式
     */
    private String ownerContact;

    /**
     * 宠物描述
     */
    private String petDescription;

    /**
     * 浏览量
     */
    private Long viewCount = Long.valueOf(0);

    /**
     * 转发量
     */
    private Long shareCount = Long.valueOf(0);

    /**
     * 关注量
     */
    private Long likeCount = Long.valueOf(0);

    /**
     * 互动量
     */
    private Long publicMsgCount = Long.valueOf(0);

    /**
     * 私信量
     */
    private Long privateMsgCount = Long.valueOf(0);

    /**
     * 私信信息
     */
    private List<List<PrivateMsgDTO>> privateTalk;


    /**
     * 互动信息
     */
    private List<List<PublicMsgDTO>> publicTalk;


    /**
     * 用户认证状态
     */
    private Integer approveState = 0;

    /**
     * 用户认证类型
     */
    private Integer approveType = 0;

    /**
     * 当前用户关注状态
     */
    private boolean likeState;

    /**
     * 当前用户关注状态
     */
    private boolean isShowMsgPoint;

    /**
     * 发布显示状态
     */
    private Integer publishState;

    /**
     * 发现状态
     */
    private Integer findState;

    //---------------------------------------------------------------------------------------------------
 /*   public Stream<List<PrivateMsgDTO>> getPrivateStream() {
        return privateTalk.stream();
    }

    public Stream<List<PublicMsgDTO>> getPublicStream() {
        return publicTalk.stream();
    }*/

    private List<PrivateMsgDTO> privateMsg;
    private List<PublicMsgDTO> publicMsg;

   /* public Stream<PrivateMsgDTO> getPrivateMsg() {
        return privateMsg.stream();
    }

    public Stream<PublicMsgDTO> getPublishMsg() {
        return publicMsg.stream();
    }*/

}


