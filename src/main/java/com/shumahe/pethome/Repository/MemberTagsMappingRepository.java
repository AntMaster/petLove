package com.shumahe.pethome.Repository;

import com.shumahe.pethome.Domain.MemberTagsMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MemberTagsMappingRepository extends JpaRepository<MemberTagsMapping, Integer> {


    List<MemberTagsMapping> findByMemberIdOrderByTagId(Integer memberId);
}
