package com.ali.pay.controler;

import com.ali.pay.entity.CardKey;
import com.ali.pay.service.CardKeyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/card-keys")
public class CardKeyController {

    private final CardKeyService cardKeyService;

    @Autowired
    public CardKeyController(CardKeyService cardKeyService) {
        this.cardKeyService = cardKeyService;
    }

    @PostMapping("/add")
    public int insertCardKey(@RequestBody CardKey cardKey) {
        log.info("scar_key添加请求成功");
        return cardKeyService.insertCardKey(cardKey);
    }

    @GetMapping("/{id}")
    public CardKey getCardKeyById(@PathVariable int id) {
        return cardKeyService.getCardKeyById(id);
    }

    @GetMapping
    public List<CardKey> getAllCardKeys() {
        return cardKeyService.getAllCardKeys();
    }

    @PutMapping("/{id}")
    public int updateCardKey(@PathVariable int id, @RequestBody CardKey cardKey) {
        cardKey.setId(id);
        return cardKeyService.updateCardKey(cardKey);
    }

    @DeleteMapping("/{id}")
    public int deleteCardKey(@PathVariable int id) {
        return cardKeyService.deleteCardKey(id);
    }
}
