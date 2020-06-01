package com.jbit.web;

import com.jbit.entity.JsonResult;
import com.jbit.pojo.AppInfo;
import com.jbit.pojo.AppVersion;
import com.jbit.pojo.DevUser;
import com.jbit.service.AppCategoryService;
import com.jbit.service.AppVersionService;
import com.jbit.service.AppinfoService;
import com.jbit.service.DataDictionaryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.Date;

@Controller
@RequestMapping("dev/app")
public class AppInfoController {

    @Resource
    private AppinfoService appinfoService;

    @Resource
    private AppCategoryService appCategoryService;

    @Resource
    private DataDictionaryService dataDictionaryService;

    @Resource
    private AppVersionService appVersionService;

    /**
     *  删除图片
     *
     * */
    @GetMapping("delfile")
    @ResponseBody
    public JsonResult delfile(Long id,String flag){
     if (flag.equals("logo")){
         //先用id查询到 是那个qpp里面的ID然后在 通过绝对路径删除掉    io流删除
         AppInfo appInfo = appinfoService.queryById(id);
         try {
             File file=new File(appInfo.getLogolocpath());
             //删除 文件
             file.delete();
             //清空数据库 储存值
             appInfo.setLogopicpath("");
             appInfo.setLogolocpath("");
             appinfoService.update(appInfo);
             return new JsonResult(true);
         } catch (Exception e) {
             return new JsonResult(false);
         }
     }else if (flag.equals("apk")){
         //删除APP版本信息 里面的apk文件
         try {
             AppVersion appVersion = appVersionService.queryById(id);
             // 将文件 路径存放在 file 然后在本地中删除  在就清空数据库( 修改)
             File file=new File(appVersion.getDownloadlink());
             file.delete();
             //清空数据库
             appVersion.setDownloadlink("");
             appVersion.setApklocpath("");
             appVersion.setApkfilename("");
             appVersionService.update(appVersion);
             return new JsonResult(true);

         } catch (Exception e) {
             return new JsonResult(false);
         }

     }
        return new JsonResult(false);
    }


    /**
     *
     * 修改的时候 查询修改的数据
     *
     *  appinfomodify/{id} 传入网页跳转时候的 所需要的的id
     *
     *  @PathVariable 标识这是网页上传入的id {id} 是这个ID
     * */
    @GetMapping("appinfomodify/{id}")
    public String  appinfomodify(Model model,@PathVariable Long id){
         model.addAttribute("appInfo",appinfoService.queryById(id));
         return "developer/appinfomodify";
    }


     /**
      *  查询数据库有没有和你输入的一样
      * 验证APK 是否注册
      * */
     @GetMapping("/apkexist")
     @ResponseBody
      public JsonResult apkexist(String apkename){
          AppInfo appInfo = appinfoService.queryapkexist(apkename);
          if (appInfo==null){
            return  new JsonResult(true);
        }
          return  new JsonResult(false);
      }

      /**
       * app 修改
       *
       * */
      @PostMapping("appinfomodify")
      public String appinfomodify(HttpSession session,AppInfo appInfo, MultipartFile attach){
          if (!attach.isEmpty()){
              //1.实现上传 （获取服务器 Tomcat 位置需要session 去取） session 是服务器的对象       这是绝对路径
            String server_path=session.getServletContext().getRealPath("/statics/uploadfiles/");
              try {
              //文件上传了
                  attach.transferTo(new File(server_path,attach.getOriginalFilename()));
                  //相对路径 加上传文件名字
                  appInfo.setLogopicpath("/statics/uploadfiles/"+attach.getOriginalFilename());
                  //绝对路径 加上传文件名字
                  appInfo.setLogolocpath(server_path+attach.getOriginalFilename());
          } catch (IOException e) {
          }
          }
          //3. App修改
          DevUser devuser = (DevUser) session.getAttribute("devuser");
          appInfo.setUpdatedate(new Date());
          appInfo.setDevid(devuser.getId());
          appInfo.setCreatedby(devuser.getId());
          appInfo.setCreationdate(new Date());
          appinfoService.update(appInfo);
          return "redirect:/dev/app/list";

      }



    /**
     * App新增
     * 上传需要调用这接口 MultipartFile 来返回这个 方法名字
     * */
    @PostMapping("appinfoadd")
    public String AppinfoAdd(HttpSession session,AppInfo appInfo, MultipartFile a_logoPicPath){
        //1.实现上传 （获取服务器 Tomcat 位置需要session 去取） session 是服务器的对象       这是绝对路径
        String server_path=session.getServletContext().getRealPath("/statics/uploadfiles");
        //2. 验证大小 和图片规格 【省略掉】         server_path 上传地址 拼接    getOriginalFilename 文件名字
        try {
             //文件上传了
            a_logoPicPath.transferTo(new File(server_path,a_logoPicPath.getOriginalFilename()));
        } catch (IOException e) {
        }
        //3. App添加
       DevUser devuser = (DevUser) session.getAttribute("devuser");
        appInfo.setUpdatedate(new Date());
        appInfo.setDevid(devuser.getId());
        appInfo.setCreatedby(devuser.getId());
        appInfo.setCreationdate(new Date());
        //相对路径 加上传文件名字
        appInfo.setLogopicpath("/statics/uploadfiles"+"/"+a_logoPicPath.getOriginalFilename());
        System.out.println(appInfo.getLogopicpath());
        //绝对路径 加上传文件名字
        appInfo.setLogolocpath(server_path+a_logoPicPath.getOriginalFilename());
        appinfoService.sev(appInfo);

        return "redirect:/dev/app/list";

    }




//    同时支持 git post  @RequestMapping 注解是跳转页面    @RequestParam(defaultValue = "1") 当 pagenum为空的时候 给他默认值  pageIndex指定个名字 传入页面
    @RequestMapping("/list")
    public String list(HttpSession session, Model model, @RequestParam(defaultValue = "1",value = "pageIndex")Integer pageunm
    ,String querySoftwareName
    ,Long   queryStatus
   ,Long   queryFlatformId
   ,Long   queryCategoryLevel1
   ,Long   queryCategoryLevel2
   ,Long   queryCategoryLevel3
    ) {

            //取出 登录时 session 里面的 id  然后传入 到 appinfo 查出相对应的数据
        DevUser devuser = (DevUser) session.getAttribute("devuser");

//   查询列表方法     防止进入页面的时候session 丢失  没有登录的时候 不允许访问controller
         model.addAttribute("pageinfo",appinfoService.queryAppInfo(pageunm,devuser.getId(),querySoftwareName,queryStatus,queryFlatformId,queryCategoryLevel1,queryCategoryLevel2,queryCategoryLevel3));

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

        //查询 状态和所属平台
        model.addAttribute("statusList",dataDictionaryService.querydataList("APP_STATUS"));
        model.addAttribute("flatFormList",dataDictionaryService.querydataList("APP_FLATFORM"));

        //点击 查询的时候 保存查询的数据资料
        model.addAttribute("querySoftwareName",querySoftwareName);
        model.addAttribute("queryStatus",queryStatus);
        model.addAttribute("queryFlatformId",queryFlatformId);
        model.addAttribute("queryCategoryLevel1",queryCategoryLevel1);
        model.addAttribute("queryCategoryLevel2",queryCategoryLevel2);
        model.addAttribute("queryCategoryLevel3",queryCategoryLevel3);

        return "developer/appinfolist";
    }

    /**
     * 删除APP应用
     *
     * */
    @GetMapping("delapp")
    @ResponseBody
    public JsonResult delectApp(Long id){
          int i= appinfoService.delectApp(id);
        System.out.println(i);
          if (i>0){
              return new JsonResult(true);
          }else {
              return new JsonResult(false);
          }
    }
}
