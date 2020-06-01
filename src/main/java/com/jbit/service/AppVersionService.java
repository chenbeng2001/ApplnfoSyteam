package com.jbit.service;

import com.jbit.mapper.AppVersionMapper;
import com.jbit.pojo.AppVersion;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

@Service
public class AppVersionService {

    @Resource
   private AppVersionMapper appVersionMapper;

    @Resource
    private AppinfoService appinfoService;

    @Resource
    private DataDictionaryService dataDictionaryService;


    /**
     *
     * 查询 新增版本信息
     *
     * */
     public List<AppVersion> queryByAppId(long id){

         //排序
         Example example=new Example(AppVersion.class);
         // 按照数据库中的 modifyBy  升序进行 排序
         example.orderBy("modifydate").desc();

         Example.Criteria criteria= example.createCriteria();
         criteria.andEqualTo("appid",id);

         List<AppVersion> appVersions = appVersionMapper.selectByExample(example);
         appVersions.forEach(app->{
             //处理Appname
             app.setAppname(appinfoService.queryById(app.getAppid()).getSoftwarename());
             //处理APP发布状态
             app.setPublishstatusname(dataDictionaryService.querydataDictionary("PUBLISH_STATUS",app.getPublishstatus()).getValuename());
         });
         return appVersions;
     }

    /**
     * 根据ID查询 版本号
     *
     *
     * */

    public AppVersion queryById(Long id){
        return  appVersionMapper.selectByPrimaryKey(id);

    }
 /**
  * 添加版本号
  * */
    public void save(AppVersion appVersion) {

        appVersionMapper.insertSelective(appVersion);
    }

    /**
     * 删除图片 (修改为null)
     * */
    public void update(AppVersion appVersion) {
        appVersionMapper.updateByPrimaryKeySelective(appVersion);
    }
}
