package com.zous.catmaster.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zous.catmaster.annotation.CheckLogin;
import com.zous.catmaster.annotation.Frequency;
import com.zous.catmaster.bean.AppConstant;
import com.zous.catmaster.bean.ErrorCode;
import com.zous.catmaster.bean.Result;
import com.zous.catmaster.entity.Store;
import com.zous.catmaster.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/business")
public class BusinessController {
    @Autowired
    StoreService storeService;

    @RequestMapping(value = "/getStores",method = RequestMethod.GET)
    @Frequency(name = "getStores",limit = 1,time = 1)
    @CheckLogin(requestRoles = AppConstant.ROLE_TYPE_MANAGER)
    public Result getStores(@RequestAttribute("UserId") String userId) throws JsonProcessingException {
        List<Store> stores = storeService.getStores(userId);
        ObjectMapper objectMapper = new ObjectMapper();
        String storesStr = objectMapper.writeValueAsString(stores);
        Result result = new Result(ErrorCode.SUCCESS);
        result.setData(storesStr);
        return result;
    }

}
