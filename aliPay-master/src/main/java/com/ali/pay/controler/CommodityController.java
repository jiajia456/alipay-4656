package com.ali.pay.controler;

import com.ali.pay.dto.CommodityDTO;
import com.ali.pay.dto.FindshopDTO;
import com.ali.pay.entity.Commodity;
import com.ali.pay.entity.RestBean;
import com.ali.pay.service.CommodityService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/commodities")
public class CommodityController {

    private final CommodityService commodityService;

    @Autowired
    public CommodityController(CommodityService commodityService) {
        this.commodityService = commodityService;
    }

    @PostMapping
    public int insertCommodity(@RequestBody Commodity commodity) {
        return commodityService.insertCommodity(commodity);
    }

    @GetMapping ("/find")
    public RestBean<?> getCommodityById(@Param("password") String password, boolean n) {
        if(password!=null&&n){
            Commodity commodity = commodityService.getCommodityById(password);
            if (commodity==null){
               return RestBean.failure(401,"无此商品");
            }
            if(n){
                FindshopDTO findshopDTO = new FindshopDTO();
                try {
                    findshopDTO.setName(commodity.getName());
                    findshopDTO.setMoney(commodity.getMoney());
                    findshopDTO.setPassword(commodity.getPassword());
                    return RestBean.success(findshopDTO);
                } catch (Exception e) {
                    e.printStackTrace();
                    return RestBean.failure(401,"商品出错了");
                }
            }else{
                CommodityDTO commodityDTO = new CommodityDTO();
                try {
                    commodityDTO.setBaidu_password(commodity.getBaidu_password());
                    commodityDTO.setBaidu_url(commodity.getBaidu_url());
                    return RestBean.success(commodityDTO);
                } catch (Exception e) {
                    e.printStackTrace();
                    return RestBean.failure(401,"商品出错了");
                }
            }
        }
        return RestBean.failure(401,"领取码未填写");
    }

    @GetMapping
    public List<Commodity> getAllCommodities() {
        return commodityService.getAllCommodities();
    }

    @PutMapping("/{id}")
    public int updateCommodity(@PathVariable int id, @RequestBody Commodity commodity) {
        commodity.setId(id);
        return commodityService.updateCommodity(commodity);
    }

    @DeleteMapping("/{id}")
    public int deleteCommodity(@PathVariable int id) {
        return commodityService.deleteCommodity(id);
    }
}
