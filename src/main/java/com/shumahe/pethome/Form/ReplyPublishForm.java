package com.shumahe.pethome.Form;


import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;


@Data
public class ReplyPublishForm {


    private Integer talkId;

    @NotBlank(message = "回复人必填")
    private String replierFrom;


    @NotBlank(message = "接收人必填")
    private String replierAccept;


    @NotBlank(message = "评论内容必填")
    private String content;


}
