package com.rmq.controller;

import com.rmq.service.IGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author yurui
 * @date 2022-06-14 19:43
 */
@RestController
@RequestMapping("goods/")
public class GoodsController {

    @Autowired
    private IGoodsService goodsService;

    // 修改
//    get、post、delete、put
    @GetMapping("{id}")
    public void audit(@PathVariable("id") String id) {
        goodsService.audit();
        return;
    }

    @GetMapping()
    public void getMsg() {
        String msg = goodsService.getMsg();


    }

}
