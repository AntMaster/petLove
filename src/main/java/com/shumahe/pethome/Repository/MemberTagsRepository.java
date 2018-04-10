package com.shumahe.pethome.Repository;

import com.shumahe.pethome.Domain.MemberTags;
import com.shumahe.pethome.Domain.MemberTagsMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MemberTagsRepository extends JpaRepository<MemberTags, Integer> {

    List<MemberTags> findByIdIn(List<Integer> ids);

}
