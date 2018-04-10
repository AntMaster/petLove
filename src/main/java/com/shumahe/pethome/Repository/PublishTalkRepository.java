package com.shumahe.pethome.Repository;


import com.shumahe.pethome.Domain.PublishTalk;

import com.shumahe.pethome.Domain.UserTalk;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;


import java.util.List;


public interface PublishTalkRepository extends JpaRepository<PublishTalk, Integer>, JpaSpecificationExecutor {


    /**
     *  评论排序
     *
     *  1 针对多个主题之间先后顺序排序 		最后回复时间（LastModify desc）倒序
     *  2 针对一个主题的多个评论间先后顺序排序	评论（talkId  ASC）顺序
     *  3 针对一个评论间多个互动间先后顺序排序	互动（replyDate ASC）顺序
     */

    /**
     * 查询我互动过的主题
     *
     * @param replierFrom
     * @param replierAccept
     * @param publisher
     * @return
     */
    List<PublishTalk> findByReplierFromOrReplierAcceptOrPublisherId(String replierFrom, String replierAccept, String publisher);


    /**
     * 查询多个发布的所有互动信息
     *
     * @param publishIds
     * @return
     */
    @Query(value = "SELECT * FROM PublishTalk WHERE PublishID IN ?1 AND ShowState = 1 ORDER BY LASTModify DESC,talkId ASC,ReplyDate ASC ", nativeQuery = true)
    List<PublishTalk> findManyPublishTalk(List<Integer> publishIds);


    /**
     * 查询一个主题的所有互动信息
     * @param publishId
     */
    @Query(value = "SELECT * FROM PublishTalk WHERE PublishID = ?1 AND ShowState = 1 ORDER BY LASTModify DESC,talkId ASC,ReplyDate ASC", nativeQuery = true)
    List<PublishTalk> findOnePublicTalk(Integer publishId);


    /**
     * 查询多个主题的互动数量
     * @param publishId
     */
    @Query(value = "SELECT PublishID,COUNT(PublishID) AS CommentCount FROM PublishTalk WHERE PublishID IN (?1) AND ShowState = 1 GROUP BY PublishID ", nativeQuery = true)
    List<Object[]> findPublishCommentCount(List<Integer> publishId);


    /**
     * 查询一个主题的互动详情
     * @param publishId
     */
    List<PublishTalk> findByPublishId(int publishId);

    /**
     * 查询多个主题的互动详情
     * @param publishIds
     */
    List<PublishTalk> findByPublishIdInAndShowState(List<Integer> publishIds,Integer showState);

    /**
     * 我的   互动未读条数
     * @param ReplierAccept
     * @return
     */
   /* @Query(value = "SELECT count(id) FROM PublishTalk WHERE PublishID in (SELECT DISTINCT PublishID FROM PublishTalk WHERE PublisherID = ?1 OR ReplierFrom = ?2 OR ReplierAccept = ?3 ) AND ReadState = ?4 ", nativeQuery = true)
    int notReadTalksCount(String PublisherID, String ReplierFrom, String ReplierAccept,Integer readCount);
*/
    @Query(value = "SELECT count(id) FROM PublishTalk  WHERE ReplierAccept = ?1 AND ReadState = ?2 ", nativeQuery = true)
    int notReadTalksCount(String ReplierAccept,Integer readCount);


    Page<PublishTalk> findByPublishIdOrderByReplyDateDesc(Integer id, Pageable pageRequest);


    List<PublishTalk>  findByPublisherIdAndReadState(String publisherId,Integer readState);


    List<PublishTalk>  findByReplierAcceptAndReadState(String publisherId,Integer readState);

}
