package com.jbit.web;

import com.jbit.pojo.AppCategory;
import com.jbit.service.AppCategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

@Controller
public class AppCategoryController {

   //  查询分类
    @Resource
    private AppCategoryService appCategoryService;


    @GetMapping("categorylevellist")
    @ResponseBody     //@ResponseBody 标识为json
    public List<AppCategory>queryByid(Long id){

        return appCategoryService.queryBypid(id);
    }
}
