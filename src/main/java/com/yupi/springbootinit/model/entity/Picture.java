package com.yupi.springbootinit.model.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 图片
 *
 * @author <a href="https://github.com/JaderSun">JaderSun</a>
 */
@Data
public class Picture implements Serializable {

    private String title;
    private String url;

    private static final long serialVersionUID = 1L;
}
