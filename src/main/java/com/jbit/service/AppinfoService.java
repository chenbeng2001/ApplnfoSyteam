package com.jbit.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jbit.mapper.AppInfoMapper;
import com.jbit.pojo.AppInfo;
import com.jbit.pojo.AppVersion;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

@Service
public class AppinfoService {

    @Resource
    private AppInfoMapper appInfoMapper;

    @Resource
    private DataDictionaryService dataDictionaryService;

    @Resource
    private AppCategoryService appCategoryservice;

    @Resource
    private AppVersionService appVersionService;

     /**
      * 验证  apkexist 是否相同
      * */
    public AppInfo queryapkexist(String apkename){
        AppInfo appInfo = new AppInfo();
        appInfo.setApkname(apkename);
          return appInfoMapper.selectOne(appInfo);

    }


    /**
     * App信息查询  需要查出  每一个dev登录只查看自己的 Appinfo
     * pagenum  第几页 pageSize 是每页显示多少条
     * */
    public PageInfo queryAppInfo(Integer pagenum, Long devid, String querySoftwareName, Long queryStatus, Long queryFlatformId, Long queryCategoryLevel1, Long queryCategoryLevel2,Long queryCategoryLevel3){
         //实现分页
        PageHelper.startPage(pagenum,5);
      //分类查询
        Example example=new Example(AppInfo.class);
        //排序
         example.orderBy("creationdate").desc();
        Example.Criteria criteria = example.createCriteria();
                //  !StringUtils.isEmpty(querySoftwareName) 的意思是 不等于 0  或者不等于空的时候
               if (!StringUtils.isEmpty(querySoftwareName)){
                        //模糊查询
                     criteria.andLike("softwarename","%"+querySoftwareName+"%");
               }
               if (queryStatus!=null&&queryStatus!=0){
                   criteria.andEqualTo("status",queryStatus);
               }
        if (queryFlatformId!=null&&queryFlatformId!=0){
            criteria.andEqualTo("flatformid",queryFlatformId);
        }
        if (queryCategoryLevel1!=null&&queryCategoryLevel1!=0){
            criteria.andEqualTo("categorylevel1",queryCategoryLevel1);
        }
        if (queryCategoryLevel2!=null&&queryCategoryLevel2!=0){
            criteria.andEqualTo("categorylevel2",queryCategoryLevel2);
        }
        if (queryCategoryLevel3!=null&&queryCategoryLevel3!=0){
            criteria.andEqualTo("categorylevel3",queryCategoryLevel3);
        }
        //公用的方法 如果是 后台系统管理员登录的 话 查询所有的 数据 不需要 查询个人的许菊
        if (devid!=null){
            criteria.andEqualTo("devid",devid);
        }

        
        //最开始的按照id查询
//        AppInfo appInfo = new AppInfo();
//        appInfo.setDevid(devid);    使用了 example  返回的查询必须是 example
        List<AppInfo> appInfos = appInfoMapper.selectByExample(example);
        //绑定数据
        binData(appInfos);

      //处理分页  返回类型为 pageinfo
        return new PageInfo<>(appInfos);

    }

    private void binData(List<AppInfo> appInfos) {
          appInfos.forEach(app->{
               //所属平台
               app.setFlatformname(dataDictionaryService.querydataDictionary("APP_FLATFORM",app.getFlatformid()).getValuename());
                  //一级分类
              app.setCategorylevel1name(appCategoryservice.queryById(app.getCategorylevel1()).getCategoryname());
              //二级分类
              app.setCategorylevel2name(appCategoryservice.queryById(app.getCategorylevel2()).getCategoryname());
              //三级分类
              app.setCategorylevel3name(appCategoryservice.queryById(app.getCategorylevel3()).getCategoryname());
              //状态
              app.setStatusname(dataDictionaryService.querydataDictionary("APP_STATUS",app.getStatus()).getValuename());
              //版本号
              AppVersion appVersion = appVersionService.queryById(app.getVersionid());
              if (appVersion!=null){
                  app.setVersionno(appVersion.getVersionno());
              }


          });

    }
    // @Transactional 事物 增删改 需要添加事物
   @Transactional
    public void sev(AppInfo appInfo) {
        appInfoMapper.insertSelective(appInfo);
    }

    //查询需要修改的列表
    public AppInfo queryById(Long id) {
        AppInfo appInfo = appInfoMapper.selectByPrimaryKey(id);
        appInfo.setStatusname(dataDictionaryService.querydataDictionary("APP_STATUS",appInfo.getStatus()).getValuename());
        return appInfo;
    }
    // @Transactional 事物 增删改 需要添加事物
    //清空数据库 里面的相对定位和绝对定位
    @Transactional
    public int update(AppInfo appInfo) {
       return appInfoMapper.updateByPrimaryKeySelective(appInfo);
    }

     /**
      * 删除APP应用
      * */
    public int delectApp(Long id) {
       int i=appInfoMapper.deleteByPrimaryKey(id);
       return i;
    }

    /**
     *
     * 查询App信息
     * */
    public AppInfo qeruyCk(Long id){
        AppInfo appInfo = appInfoMapper.selectByPrimaryKey(id);
        //查询所属平台
         appInfo.setFlatformname(dataDictionaryService.querydataDictionary("APP_FLATFORM",appInfo.getStatus()).getValuename());
       //查询所属分类
        appInfo.setCategorylevel1name(appCategoryservice.queryById(appInfo.getCategorylevel1()).getCategoryname());
        //二级分类
        appInfo.setCategorylevel2name(appCategoryservice.queryById(appInfo.getCategorylevel2()).getCategoryname());
        //三级分类
        appInfo.setCategorylevel3name(appCategoryservice.queryById(appInfo.getCategorylevel3()).getCategoryname());
        //APP 装填
        appInfo.setStatusname(dataDictionaryService.querydataDictionary("APP_STATUS",appInfo.getStatus()).getValuename());
               return appInfo;
    }
}
