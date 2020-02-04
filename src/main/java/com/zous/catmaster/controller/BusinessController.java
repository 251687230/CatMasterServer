package com.zous.catmaster.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.zous.catmaster.annotation.CheckLogin;
import com.zous.catmaster.annotation.Frequency;
import com.zous.catmaster.bean.AppConstant;
import com.zous.catmaster.bean.ErrorCode;
import com.zous.catmaster.bean.Result;
import com.zous.catmaster.entity.Manager;
import com.zous.catmaster.entity.Store;
import com.zous.catmaster.service.AccountService;
import com.zous.catmaster.service.ManagerService;
import com.zous.catmaster.service.StoreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/business")
public class BusinessController {

    @Autowired
    StoreService storeService;
    @Autowired
    ManagerService managerService;
    @Autowired
    ApplicationContext context;

    Gson gson = new Gson();

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

    @RequestMapping(value = "/deleteStore",method = RequestMethod.POST)
    @CheckLogin(requestRoles = AppConstant.ROLE_TYPE_MANAGER)
    @Frequency(name = "deleteStore",limit = 1,time = 10)
    public Result deleteStore(@RequestParam("StoreId") String storeId) throws Exception {
        //FIXME 关联表的删除关系需要进一步检测
        storeService.deleteStore(storeId);
        return new Result(ErrorCode.SUCCESS);
    }

    @RequestMapping(value = "/saveStore",method = RequestMethod.POST,produces =  "application/json;charset=UTF-8")
    @CheckLogin(requestRoles = AppConstant.ROLE_TYPE_MANAGER)
    @Frequency(name = "saveStore",limit = 1,time = 1)
    public Result saveStore(@RequestAttribute("UserId")String userId,@RequestBody Store store) throws JsonProcessingException {
        String id = store.getId();
        if(id == null){
            Optional<Manager> managerOpt = managerService.getManager(userId);
            if(managerOpt.isPresent()){
                store.setManager(managerOpt.get());
                try {
                    Store returnStore = storeService.saveStore(store);
                    return new Result(ErrorCode.SUCCESS,"",gson.toJson(returnStore));
                } catch (Exception e) {
                    Result result = new Result(ErrorCode.FAIL_PARAMS_ERROR);
                    result.setDescription(context.getMessage("fail_params_error",null, LocaleContextHolder.getLocale()));
                    return result;
                }
            }else {
                Result result = new Result(ErrorCode.FAIL_ACCOUNT_NOT_EXIST);
                result.setDescription(context.getMessage("fail_account_not_exist",null, LocaleContextHolder.getLocale()));
                return result;
            }
        }else {
            Optional<Store> storeOpt = storeService.getStore(userId,id);
            if(!storeOpt.isPresent()){
                Result result = new Result(ErrorCode.FAIL_STORE_NOT_EXIST);
                result.setDescription(context.getMessage("fail_store_not_exist",null,LocaleContextHolder.getLocale()));
                return result;
            }
            try {
                Store returnStore = storeService.saveStore(store);
                return new Result(ErrorCode.SUCCESS,"",gson.toJson(returnStore));
            } catch (Exception e) {
                Result result = new Result(ErrorCode.FAIL_PARAMS_ERROR);
                result.setDescription(context.getMessage("fail_params_error",null, LocaleContextHolder.getLocale()));
                return result;
            }
        }
    }
}
