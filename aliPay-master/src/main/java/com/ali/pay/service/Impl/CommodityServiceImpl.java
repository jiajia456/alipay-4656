package com.ali.pay.service.Impl;

import com.ali.pay.entity.Commodity;

import java.util.List;

public interface CommodityServiceImpl {

    int insertCommodity(Commodity commodity);

    Commodity getCommodityById(String password);

    Commodity getCommodityById1(Integer id);


    List<Commodity> getAllCommodities();

    int updateCommodity(Commodity commodity);

    int deleteCommodity(int id);

    public boolean getCommodityByName(String name);


    List<Commodity> getCommodityByPassword(String password);
}
