package com.hndt.dao;

import com.hndt.pojo.Send;

public interface SendMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Send record);

    int insertSelective(Send record);

    Send selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Send record);

    int updateByPrimaryKey(Send record);
}