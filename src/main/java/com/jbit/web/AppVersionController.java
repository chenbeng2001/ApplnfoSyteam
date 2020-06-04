package com.jbit.web;

import com.jbit.pojo.AppInfo;
import com.jbit.pojo.AppVersion;
import com.jbit.pojo.DevUser;
import com.jbit.service.AppVersionService;
import com.jbit.service.AppinfoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("dev/app")
public class AppVersionController {

    @Resource
    private AppVersionService appVersionService;

    @Resource
    private AppinfoService appinfoService;

    /**
     * 查询需要添加的  APP版本信息 查询跳转网页
     *
     * */
     @GetMapping("appversionadd/{id}") // @PathVariable 表示页面传入的id
     public String appversionadd(@PathVariable Long id, Model model){
         List<AppVersion> appVersions = appVersionService.queryByAppId(id);
         model.addAttribute("appVersionList",appVersions);
         //添加的时候 传入查询的id 因为添加的时候需要添加上 是那个版本号的id 标识上是那个人的版本
         model.addAttribute("appId",id);
         return "developer/appversionadd";

     }

     /**
      *
      * 修改 的时候绑定数据
      * */
     @GetMapping("appversionmodify")
     public String  appversionmodify(Model model,Long vid,long aid){
         model.addAttribute("appVersionList",appVersionService.queryByAppId(aid));
         model.addAttribute("appVersion",appVersionService.queryById(vid));
       return "developer/appversionmodify";
     }

    /**
     * 查看
     *
     * */
    @GetMapping("appview/{id}")
    public String  appview(Model model,@PathVariable Long id){

        AppInfo appInfo = appinfoService.qeruyCk(id);
        List<AppVersion> appVersions = appVersionService.queryByAppId(id);
        model.addAttribute("appInfo",appInfo);
        model.addAttribute("appVersionList",appVersions);
        return "developer/appinfoview";
    }


     /**
      *
      *  修改版本信息       AppVersion appVersion 获取上传的表单对象
      * */
     @PostMapping("appversionmodifysave")
     public String appversionmodifysave(MultipartFile attach,HttpSession session, AppVersion appVersion){
         System.out.println(appVersion);

         // attach 如果是null 的话等于 true 或者修改的时候有文件也等于 true  上传文件上去的时候就是false
        if (!attach.isEmpty()){
            //绝对路径    获取 服务器存储的路径
            String server_path=session.getServletContext().getRealPath("/statics/uploadfiles/");
            try {
                //上传文件      上传文件的路径和 文件名字
                attach.transferTo(new File(server_path,attach.getOriginalFilename()));
                 //相对存入相对路径
                appVersion.setDownloadlink("/statics/uploadfiles/"+attach.getOriginalFilename());
                //绝对路径
                appVersion.setApklocpath(server_path+attach.getOriginalFilename());
                //
                appVersion.setApkfilename(attach.getOriginalFilename());
            } catch (Exception e) {
            }
        }
         //取出 session 里面的devuser对象
         DevUser devUser= (DevUser) session.getAttribute("devuser");
         appVersion.setModifyby(devUser.getId());
         appVersion.setModifydate(new Date());
         appVersionService.update(appVersion);
         return "redirect:/dev/app/list";
     }

     /**
      *  添加App版本信息
      *
      * */
      @PostMapping("addversionsave")
       public String addversionsave(MultipartFile a_downloadlink, HttpSession session,AppVersion appVersion){

          //1.实现上传 （获取服务器 Tomcat 位置需要session 去取） session 是服务器的对象       这是绝对路径
          String server_path=session.getServletContext().getRealPath("/statics/uploadfiles/");
          try {
              //文件上传了
              a_downloadlink.transferTo(new File(server_path,a_downloadlink.getOriginalFilename()));

          } catch (IOException e) {
          }
          DevUser devuser =(DevUser) session.getAttribute("devuser");
          appVersion.setDownloadlink("/statics/uploadfiles/"+a_downloadlink.getOriginalFilename());

          //是那个用户进行的添加
          appVersion.setCreatedby(devuser.getId());
          appVersion.setCreationdate(new Date());
          appVersion.setModifydate(new Date());
          appVersion.setApkfilename(a_downloadlink.getOriginalFilename());
          appVersion.setApklocpath(server_path+a_downloadlink.getOriginalFilename());
          appVersionService.save(appVersion);


          //更新版本号
          AppInfo appInfo=new AppInfo();
          appInfo.setId(appVersion.getAppid());
          appInfo.setVersionid(appVersion.getId());
          appinfoService.update(appInfo);

          return "redirect:/dev/app/appversionadd/"+appVersion.getAppid();

      }



}
