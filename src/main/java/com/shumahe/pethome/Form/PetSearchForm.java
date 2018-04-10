package com.shumahe.pethome.Form;

import com.shumahe.pethome.Enums.SearchEnum;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;



@Data
public class PetSearchForm {


    /**
     * 频道
     */
    private int publishType = SearchEnum.NONE_VALUE.getCode();


    /**
     * 宠物类别
     */
    private int classifyId = SearchEnum.NONE_VALUE.getCode();

    /**
     * 宠物品种
     */
    private int varietyId = SearchEnum.NONE_VALUE.getCode();


    /**
     * 性别
     */
    private int petSex = SearchEnum.NONE_VALUE.getCode();


    /**
     * 搜索关键字
     */
    //@NotBlank(message = "搜索关键字不能为空")
    private String keyWord;

}
