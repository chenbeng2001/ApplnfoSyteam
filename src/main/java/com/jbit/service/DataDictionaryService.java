package com.jbit.service;

import com.jbit.mapper.DataDictionaryMapper;
import com.jbit.pojo.DataDictionary;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class DataDictionaryService {

    @Resource
    private DataDictionaryMapper dataDictionaryMapper;

    /**
     * 查询数据
     * typecode 类型
     * valueid 值
     * */
    public DataDictionary querydataDictionary(String typecode,Long valueid){
        DataDictionary dataDictionary=new DataDictionary();
        dataDictionary.setTypecode(typecode);
        dataDictionary.setValueid(valueid);
        return dataDictionaryMapper.selectOne(dataDictionary);
    }

    /**
     * 查询状态和所属平台
     * typecode 类型
     * valueid 值
     * */
    public List<DataDictionary> querydataList(String typecode){
        DataDictionary dataDictionary=new DataDictionary();
        dataDictionary.setTypecode(typecode);
        return dataDictionaryMapper.select(dataDictionary);
    }


}
