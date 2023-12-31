package com.ali.pay.service;

import com.ali.pay.entity.Commodity;
import com.ali.pay.mapper.CommodityMapper;

import com.ali.pay.service.Impl.CommodityServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommodityService implements CommodityServiceImpl {

    private CommodityMapper commodityMapper;

    @Autowired
    public void CommodityServiceImpl(CommodityMapper commodityMapper) {
        this.commodityMapper = commodityMapper;
    }

    public CommodityService(CommodityMapper commodityMapper) {
        this.commodityMapper = commodityMapper;
    }


    @Override
    public int insertCommodity(Commodity commodity) {
        return commodityMapper.insertCommodity(commodity);
    }

    @Override
    public Commodity getCommodityById(String password) {
        return commodityMapper.getCommodityById(password);
    }

    @Override
    public Commodity getCommodityById1(Integer id) {
        return commodityMapper.getCommodityById1(id);
    }


    @Override
    public List<Commodity> getAllCommodities() {

        List<Commodity> allCommodities = commodityMapper.getAllCommodities();
        return  allCommodities;
    }

    @Override
    public int updateCommodity(Commodity commodity) {
        return commodityMapper.updateCommodity(commodity);
    }

    @Override
    public int deleteCommodity(int id) {
        return commodityMapper.deleteCommodity(id);
    }

    @Override
    public boolean getCommodityByName(String name) {
        Commodity commodityByName = commodityMapper.getCommodityByName(name);
        if(commodityByName!=null){
            return true;
        }
        return false;
    }

    @Override
    public List<Commodity> getCommodityByPassword(String password) {
        List<Commodity> commodityArrayList = new ArrayList<>();
        Commodity commodityByPassword = commodityMapper.getCommodityByPassword(password);
        commodityArrayList.add(commodityByPassword);
        return commodityArrayList;
    }
}
