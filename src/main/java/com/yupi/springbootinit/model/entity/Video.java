package com.yupi.springbootinit.model.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 视频
 *
 * @author <a href="https://github.com/JaderSun">JaderSun</a>
 */
@Data
public class Video implements Serializable {

    private String title;
    private String url;
    private String pic;

    private static final long serialVersionUID = 1L;
}
