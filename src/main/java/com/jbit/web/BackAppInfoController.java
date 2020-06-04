package com.jbit.web;


import com.jbit.pojo.AppInfo;
import com.jbit.service.AppCategoryService;
import com.jbit.service.AppVersionService;
import com.jbit.service.AppinfoService;
import com.jbit.service.DataDictionaryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("backend/app")
public class BackAppInfoController {

    @Resource
    private AppinfoService appinfoService;
    @Resource
    private AppCategoryService appCategoryService;
    @Resource
    private DataDictionaryService dataDictionaryService;
    @Resource
    AppVersionService appVersionService;

    /**
     * 审核查看
     * */

    @GetMapping("check")
    public String check(Model model,long vid,long aid){
        model.addAttribute("appInfo",appinfoService.queryById(aid));
        System.out.println(vid);
        System.out.println(aid);
        model.addAttribute("appVersion",appVersionService.queryById(vid));
     return  "backend/appcheck";

    }
    /**
     *
     * 审核
     * */
      @PostMapping("checksave")
      public String checksave(AppInfo appInfo){
          appinfoService.update(appInfo);
           return "redirect:/backend/app/list";
      }

    @RequestMapping("/list")
    public String list(HttpSession session, Model model, @RequestParam(defaultValue = "1",value = "pageIndex")Integer pageunm
            , String querySoftwareName
            , Long   queryFlatformId
            , Long   queryCategoryLevel1
            , Long   queryCategoryLevel2
            , Long   queryCategoryLevel3
    ) {

//   查询列表方法     防止进入页面的时候session 丢失  没有登录的时候 不允许访问controller

        model.addAttribute("pageinfo",appinfoService.queryAppInfo(pageunm,null,querySoftwareName,1L,queryFlatformId,queryCategoryLevel1,queryCategoryLevel2,queryCategoryLevel3));


        //处理一级分类  (下拉框绑定数据   (查询一级分类 数据库是一级分类是null 所以给它个null))
        model.addAttribute("categoryLevel1List",appCategoryService.queryBypid(null));
        //处理二级分类
        if (queryCategoryLevel1!=null){
            model.addAttribute("categoryLevel2List",appCategoryService.queryBypid(queryCategoryLevel1));
        }
        //处理三级分类
        if (queryCategoryLevel2!=null){
            model.addAttribute("categoryLevel3List",appCategoryService.queryBypid(queryCategoryLevel2));
        }

        //查询 所属平台

        model.addAttribute("flatFormList",dataDictionaryService.querydataList("APP_FLATFORM"));

        //点击 查询的时候 保存查询的数据资料
        model.addAttribute("querySoftwareName",querySoftwareName);
        model.addAttribute("queryFlatformId",queryFlatformId);
        model.addAttribute("queryCategoryLevel1",queryCategoryLevel1);
        model.addAttribute("queryCategoryLevel2",queryCategoryLevel2);
        model.addAttribute("queryCategoryLevel3",queryCategoryLevel3);

        return "backend/applist";
    }



}
