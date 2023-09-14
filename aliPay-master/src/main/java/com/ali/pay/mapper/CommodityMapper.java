package com.ali.pay.mapper;

import com.ali.pay.entity.Commodity;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CommodityMapper {

    @Insert("INSERT INTO commodity (password, name, money, baidu_url, baidu_password) " +
            "VALUES (#{password}, #{name}, #{money}, #{baidu_url}, #{baidu_password})")
    int insertCommodity(Commodity commodity);

    @Select("SELECT * FROM commodity WHERE password = #{password}")
    Commodity getCommodityById(String password);

    @Select("SELECT * FROM commodity")
    List<Commodity> getAllCommodities();

    @Update("UPDATE commodity SET password = #{password}, name = #{name}, " +
            "money = #{money}, baidu_url = #{baidu_url}, baidu_password = #{baidu_password} " +
            "WHERE id = #{id}")
    int updateCommodity(Commodity commodity);

    @Delete("DELETE FROM commodity WHERE id = #{id}")
    int deleteCommodity(int id);
}
