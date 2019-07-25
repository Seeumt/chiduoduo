package com.cumt.chiduoduo.dao;

import com.cumt.chiduoduo.model.OrderComment;

import java.util.List;

public interface OrderCommentMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OrderComment record);

    int insertSelective(OrderComment record);

    OrderComment selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OrderComment record);

    int updateByPrimaryKey(OrderComment record);

    List<OrderComment> selectByOrderMasterId(Integer orderMasterId);
}