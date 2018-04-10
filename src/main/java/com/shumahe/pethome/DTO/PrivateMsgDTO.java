package com.shumahe.pethome.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

@Data
public class PrivateMsgDTO {

    /**互动主键 */
    private Integer id;

    /**评论主键 */
    private Integer talkId;

    /**发送人ID */
    @JsonProperty("replierFrom")
    private String userIdFrom;

    /**发送人昵称 */
    @JsonProperty("replierFromName")
    private String userIdFromName;

    /**发送人头像 */
    @JsonProperty("replierFromPhoto")
    private String userIdFromPhoto;

    /** 发送人认证类型 */
    private Integer approveType;

    /**发送人认证状态 */
    private Integer approveState;

    /**接收人ID */
    @JsonProperty("replierAccept")
    private String userIdAccept;

    /**接收人昵称 */
    @JsonProperty("replierAcceptName")
    private String userAcceptName;

    /**接收人头像 */
    @JsonProperty("replierAcceptPhoto")
    private String userAcceptPhoto;

    /**互动内容 */
    private String content;

    /**最后一次交流时间 */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastModify;

    /**互动时间 */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date talkTime;

    /**阅读状态 */
    private Integer readState;

    /**显示状态 */
    private Integer showState;
}
