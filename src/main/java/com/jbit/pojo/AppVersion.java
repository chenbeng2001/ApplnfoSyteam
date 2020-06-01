package com.jbit.pojo;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Table(name = "app_version")
public class AppVersion implements Serializable {
    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;

    @Column(name = "appId")
    private Long appid;

    @Column(name = "versionNo")
    private String versionno;

    @Column(name = "versionInfo")
    private String versioninfo;

    @Column(name = "publishStatus")
    private Long publishstatus;

    @Column(name = "downloadLink")
    private String downloadlink;

    @Column(name = "versionSize")
    private BigDecimal versionsize;

    @Column(name = "createdBy")
    private Long createdby;

    @Column(name = "creationDate")
    private Date creationdate;

    @Column(name = "modifyBy")
    private Long modifyby;

    @Column(name = "modifyDate")
    private Date modifydate;

    @Column(name = "apkLocPath")
    private String apklocpath;

    @Column(name = "apkFileName")
    private String apkfilename;

    @Transient  //忽略类 App名字
    private  String appname;

    @Transient  //忽略类 发布状态
    private  String publishstatusname;

    private static final long serialVersionUID = 1L;
}