package com.shumahe.pethome.Repository;

import com.shumahe.pethome.Domain.PublishView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;


public interface PublishViewRepository extends JpaRepository<PublishView, Integer> {

    PublishView findTopByViewerAndPublishIdAndViewTimeBetweenOrderByViewTimeDesc(String viewer, Integer publishId, Date todayBegin, Date todayEnd);


    List<PublishView> findByPublishIdAndViewTimeBetweenOrderByViewTimeDesc(Integer publishId, Date begin, Date end);


    /**
     * 一个发布的浏览数
     *
     * @param publishId
     * @return
     */
    @Query(value = "select count(id) from PublishView where publish_Id =?1", nativeQuery = true)
    Integer findByPublishId(Integer publishId);


    /**
     * 多个发布的浏览数
     *
     * @param publishIds
     * @return
     */
    @Query(value = "select publish_Id, count(publish_Id) as viewCount from PublishView where publish_Id in (?1) GROUP BY publish_Id ", nativeQuery = true)
    List<Integer[]> findViewCount(List<Integer> publishIds);


    /**
     * 多个发布的浏览数
     *
     * @return
     */
    List<PublishView> findByPublishIdIn(List<Integer> publishIds);

}
