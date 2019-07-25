package com.cumt.chiduoduo.dao;

import com.cumt.chiduoduo.model.Product;

import java.util.List;
import java.util.Map;

public interface ProductMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Product record);

    int insertSelective(Product record);

    Product selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Product record);

    int updateByPrimaryKey(Product record);

  List<Product> selectByCategoryId(Integer categoryId);

  List<Product> selectTop10Products();

  List<Product> selectAllProductsToAdmin();
}
