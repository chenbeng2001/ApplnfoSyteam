package com.jbit.service;

import com.jbit.mapper.AppCategoryMapper;
import com.jbit.pojo.AppCategory;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

@Service
public class AppCategoryService {

    @Resource
    private AppCategoryMapper appCategoryMapper;

     /**
      * 查询分类
      * */
    public AppCategory queryById(Long id) {
        return appCategoryMapper.selectByPrimaryKey(id);
    }

    /**
     * 查询分类 ID
     *  页面数据
     * */
    public List<AppCategory>queryBypid(Long id){
         //  Example 的作用是 差不多是在sql 语句后面添加  where 判断
        Example example=new Example(AppCategory.class);
        Example.Criteria criteria = example.createCriteria();
        if (id==null){
         //id 等于空的话查询一级分类  是谁等于空 property 输入的是数据库的表单名字
            criteria.andIsNull("parentid");
        }else {
            criteria.andEqualTo("parentid",id);

        }
        return appCategoryMapper.selectByExample(example);
    }



}
