package com.yupi.springbootinit.model.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 图片
 */
@Data
public class Video implements Serializable {

    private String title;
    private String url;
    private String pic;

    private static final long serialVersionUID = 1L;
}
