package com.shumahe.pethome.Repository;

import com.shumahe.pethome.Domain.UserTalk;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface UserTalkRepository extends JpaRepository<UserTalk, Integer> {


    @Query(value = "SELECT * FROM UserTalk WHERE PublishID IN (SELECT  DISTINCT PublishID FROM UserTalk WHERE publisherId = ?1) AND ShowState = 1 ORDER BY LASTModify DESC,Talkid DESC ", nativeQuery = true)
    List<UserTalk> findMyCreate(String publisherId);


    @Query(value = "SELECT * FROM UserTalk WHERE PublishID IN (SELECT  DISTINCT PublishID FROM UserTalk WHERE publisherId <> ?1 AND useridfrom = ?2) AND  (useridfrom = ?3 OR UserIDAccept = ?4 ) AND ShowState = 1 ORDER BY LASTModify DESC,Talkid DESC ", nativeQuery = true)
    List<UserTalk> findMyJoin(String publisherId,String userIdFrom,String userIdFrom2,String userIdAccept);

    /**查询多条发布的私信数量*/
    List<UserTalk> findByPublishIdInAndShowState(List<Integer> publishIds,Integer showState);


    List<UserTalk> findByUserIdAcceptAndReadState(String publisherId,Integer readState);


    List<UserTalk> findByPublishIdOrderByTalkId(Integer publishId);


    Page<UserTalk> findByPublishIdOrderByIdDesc(Integer publishId, Pageable pageable);


    @Query(value = "SELECT * FROM usertalk where publishId = ?1 AND talkId in (select top(50) talkId from UserTalk  where publishId = ?2 AND useridfrom = ?3 ORDER  BY talktime ) AND showstate = 1;", nativeQuery = true)
    List<UserTalk> findByPublishIdAndTalkIdOrderByTalkTime(Integer publishIdA, Integer publishIdB, String userIdFrom);



    @Query(value = "select count(id) from UserTalk  where publishId = ?1 AND  showstate = 1", nativeQuery = true)
    int findPrivateMsgCount(Integer publishId);



    @Query(value = "select count(id) from UserTalk  where useridaccept = ?1 AND  readstate = ?2", nativeQuery = true)
    int notReadTalksCount(String openId,Integer readState);



}
