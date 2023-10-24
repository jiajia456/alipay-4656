package com.ali.pay.controler;

import com.ali.pay.dto.CommodityDTO;
import com.ali.pay.dto.FindshopDTO;
import com.ali.pay.entity.Commodity;
import com.ali.pay.entity.RestBean;
import com.ali.pay.service.CommodityService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/commodities")
public class CommodityController {

    private final CommodityService commodityService;

    @Autowired
    public CommodityController(CommodityService commodityService) {
        this.commodityService = commodityService;
    }

//    @PostMapping
//    public int insertCommodity(@RequestBody Commodity commodity) {
//        return commodityService.insertCommodity(commodity);
//    }

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
    public RestBean<Map<String,Object>> getAllCommodities(
                                                        @RequestParam(value = "password",required = false) String password,
                                                        @RequestParam(value = "pageNo") Integer pageNo,
                                                        @RequestParam(value = "pageSize") Integer pageSize) {

        log.info("password={}",password);

        Map<String,Object> data = new HashMap<>();

        if (password==null||password.equals("")) {

            List<Commodity> allCommodities = commodityService.getAllCommodities();
            data.put("total",allCommodities.size());

            int start=(pageNo-1)*pageSize;
            int end=pageNo*pageSize;

            if(end>allCommodities.size()){
                end=allCommodities.size();
            }
            data.put("rows",allCommodities.subList(start,end));
            return RestBean.success(data);

        }else {
            List<Commodity> commodityByPassword = commodityService.getCommodityByPassword(password);
            data.put("total",1);
            data.put("rows",commodityByPassword);
            return RestBean.success(data);
        }
    }

    @GetMapping("/{id}")
    public RestBean<Commodity> getCommodityById(@PathVariable Integer id){
        Commodity commodityById = commodityService.getCommodityById1(id);
        if(commodityById!=null){

            return RestBean.success(commodityById);
        }else {
            return RestBean.error("错误");

        }
    }

    @PutMapping("/up")
    public RestBean<String> updateCommodity(@RequestBody Commodity commodity) {

        log.info("进入更新。。。。");
        int isExist=isExist(commodity);
        if (isExist==1){
            return RestBean.error("关键码冲突");
        }else if (isExist==2){
            return RestBean.error("商品名冲突");
        }else {
            int i = commodityService.updateCommodity(commodity);
            if (i > 0) {
                return RestBean.success();
            } else {
                return RestBean.error("出错了");
            }
        }
    }


    @DeleteMapping("/{id}")
    public RestBean<String> deleteCommodity(@PathVariable Integer id) {
        int i = commodityService.deleteCommodity(id);
        if(i>0){
            return RestBean.success("删除成功");
        }else {
            return RestBean.error("删除失败");
        }

    }

    @PostMapping("/add")
    public RestBean<String> save(@RequestBody Commodity commodity){
       int isExist=isExist(commodity);
        if (isExist==1){
            return RestBean.error("关键码已存在");
        }else if (isExist==2){
            return RestBean.error("商品名冲突");
        }else{
            int i = commodityService.insertCommodity(commodity);
            if(i>0){
                return RestBean.success("添加成功");
            }else{

                return RestBean.error("插入失败");
            }
        }
    }


    public int isExist(Commodity commodity){
        boolean commodityIsExist = commodityService.getCommodityByName(commodity.getName());
        Commodity commodityById = commodityService.getCommodityById(commodity.getPassword());
        if (commodityById!=null){
            return 1;//关键码存在
        }else if (commodityIsExist){
            return 2;//商品名存在
        }else {
            return 0;
        }
    }
}
