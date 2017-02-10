package com.malone.greendaodemo.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Transient;

/**
 * Created by luchunyang on 2017/2/9.
 */

@Entity//用于标识这是一个需要Greendao帮我们生成代码的bean
public class Girl {

    //标明主键，括号里可以指定是否自增
    @Id(autoincrement = true)
    private Long id;

    @NotNull //@NotNull:这个就简单了，就是限制这个列的插入不能为空
    private String name;

    //标识这个字段是自定义的不会创建到数据库表里
    @Transient
    private String desc;

    @Property(nameInDb = "image")
    private String url;

    private byte[]data;

    public Girl(Long id, String name, String url) {
        this.id = id;
        this.name = name;
        this.url = url;
    }

    @Generated(hash = 1142557228)
    public Girl(Long id, @NotNull String name, String url, byte[] data) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.data = data;
    }
    @Generated(hash = 1070094766)
    public Girl() {
    }
    
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getUrl() {
        return this.url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getDesc() {
        return this.desc;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getId() {
        return this.id;
    }

    @Override
    public String toString() {
        return "Girl{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
    public byte[] getData() {
        return this.data;
    }
    public void setData(byte[] data) {
        this.data = data;
    }
}
