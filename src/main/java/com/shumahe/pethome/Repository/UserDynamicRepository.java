package com.shumahe.pethome.Repository;


import com.shumahe.pethome.Domain.UserDynamic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface UserDynamicRepository extends JpaRepository<UserDynamic, Integer> {

    /**
     * 某个发布 我对它的动态
     *
     * @param openId
     * @param publishId
     * @return
     */
    List<UserDynamic> findByUserIdFromAndPublishId(String openId, Integer publishId);


    UserDynamic findByUserIdFromAndPublishIdAndDynamicType(String openId, Integer publishId,Integer dynamicType);


    List<UserDynamic> findByPublishIdAndDynamicType(Integer publisherId , Integer dynamicType);


    List<UserDynamic> findByPublishIdIn(List<Integer> publisherIds);


    /**
     * 我的关注
     *
     * @param openId
     * @param dynamicType
     * @return
     */
    List<UserDynamic> findByUserIdFromAndDynamicTypeOrderByCreateTimeDesc(String openId, int dynamicType);


    /**
     * 关注我的
     *
     * @param openId
     * @param dynamicType
     * @return
     */
    List<UserDynamic> findByUserIdArriveAndDynamicTypeOrderByCreateTimeDesc(String openId, int dynamicType);


    /**
     * 某个发布的    转发 || 关注
     */
    Page<UserDynamic> findByPublishIdAndDynamicTypeOrderByCreateTimeDesc(Integer publishId, int dynamicType, Pageable pageable);



    List<UserDynamic> findByPublishIdAndDynamicTypeAndCreateTimeBetweenOrderByCreateTimeDesc(Integer publishId, int dynamicType, Date beginTime,Date endTime);


}
