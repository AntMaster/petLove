package com.shumahe.pethome.Service.BaseService;


import com.shumahe.pethome.Domain.UserDynamic;

import java.util.List;
import java.util.Map;


public interface DynamicBaseService {


    /**
     * 返回动态数据详情
     * @param userDynamics
     * @return
     */
    List<Map<String, String>>  findLikeOrShareList(List<UserDynamic> userDynamics, int type);





}
