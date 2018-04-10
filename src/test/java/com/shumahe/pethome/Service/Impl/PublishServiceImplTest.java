package com.shumahe.pethome.Service.Impl;

import com.fasterxml.jackson.databind.util.BeanUtil;
import com.shumahe.pethome.Domain.PetPublish;
import com.shumahe.pethome.Domain.UserBasic;
import com.shumahe.pethome.Enums.ResultEnum;
import com.shumahe.pethome.Exception.PetHomeException;
import com.shumahe.pethome.Repository.PetPublishRepository;
import com.shumahe.pethome.Repository.PublishTalkRepository;
import com.shumahe.pethome.Repository.UserBasicRepository;
import com.shumahe.pethome.VO.PublishTalkingVO;
import com.shumahe.pethome.VO.PublishVO;
import com.shumahe.pethome.VO.UserBasicVO;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import java.beans.Beans;
import java.util.ArrayList;

import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest
public class PublishServiceImplTest {

    @Autowired
    private PetPublishRepository petPublishRepository;

    @Autowired
    private PublishTalkRepository publishTalkRepository;

    @Autowired
    private UserBasicRepository userBasicRepository;

    @Test
    public void findAll(){

    }


}