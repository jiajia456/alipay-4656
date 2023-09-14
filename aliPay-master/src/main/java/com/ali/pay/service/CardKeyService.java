package com.ali.pay.service;

import com.ali.pay.entity.CardKey;
import com.ali.pay.mapper.CardKeyMapper;
import com.ali.pay.service.Impl.CardKeyServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CardKeyService implements CardKeyServiceImpl {

    private CardKeyMapper cardKeyMapper;

    @Autowired
    public void CardKeyServiceImpl(CardKeyMapper cardKeyMapper) {
        this.cardKeyMapper = cardKeyMapper;
    }

    public CardKeyService(CardKeyMapper cardKeyMapper) {
        this.cardKeyMapper = cardKeyMapper;
    }

    @Override
    public int insertCardKey(CardKey cardKey) {
        if(cardKey != null){

        }
        return cardKeyMapper.insertCardKey(cardKey);
    }

    @Override
    public CardKey getCardKeyById(int id) {
        if(id >= 0){

        }
        return cardKeyMapper.getCardKeyById(id);
    }

    @Override
    public List<CardKey> getAllCardKeys() {
        return cardKeyMapper.getAllCardKeys();
    }

    @Override
    public int updateCardKey(CardKey cardKey) {
        return cardKeyMapper.updateCardKey(cardKey);
    }

    @Override
    public int deleteCardKey(int id) {
        return cardKeyMapper.deleteCardKey(id);
    }
}
