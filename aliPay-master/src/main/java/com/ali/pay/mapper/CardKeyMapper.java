package com.ali.pay.mapper;

import com.ali.pay.entity.CardKey;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CardKeyMapper {

    @Insert("INSERT INTO card_key (key_password) VALUES (#{key_password})")
    int insertCardKey(CardKey cardKey);

    @Select("SELECT * FROM card_key WHERE id = #{id}")
    CardKey getCardKeyById(int id);

    @Select("SELECT * FROM card_key")
    List<CardKey> getAllCardKeys();

    @Update("UPDATE card_key SET key_password = #{key_password} WHERE id = #{id}")
    int updateCardKey(CardKey cardKey);

    @Delete("DELETE FROM card_key WHERE id = #{id}")
    int deleteCardKey(int id);
}
