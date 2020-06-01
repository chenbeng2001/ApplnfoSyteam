package com.jbit.web;


import com.jbit.pojo.DataDictionary;
import com.jbit.service.DataDictionaryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @RestController 注释的意思是 返回json  页面可以加 这个不用加  @ResponseBody
 * */

@RestController
public class DataDictionaryController {

    //查询所属平台
        @Resource
     private DataDictionaryService dataDictionaryService;

        @GetMapping("datadictionarylist")
        public List<DataDictionary>queryList(String tcode){
            return dataDictionaryService.querydataList(tcode);
        }

}
