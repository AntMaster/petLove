package com.shumahe.pethome.Repository;

import com.shumahe.pethome.Domain.UserApprove;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;



public interface UserApproveRepository extends JpaRepository<UserApprove, Integer> {

    UserApprove findByUserId(String openId);

    UserApprove findTopByUserIdOrderByCreateTimeDesc(String openId);

    Page<UserApprove> findByApproveStateOrderByCreateTimeDesc(Integer approveState, Pageable pageable);


    Page<UserApprove> findAllByOrderByCreateTimeDesc(Pageable pageable);

}
