package com.ali.pay.service.Impl;

import com.ali.pay.entity.CardKey;

import java.util.List;

public interface CardKeyServiceImpl {

    int insertCardKey(CardKey cardKey);

    CardKey getCardKeyById(int id);

    List<CardKey> getAllCardKeys();

    int updateCardKey(CardKey cardKey);

    int deleteCardKey(int id);
}
