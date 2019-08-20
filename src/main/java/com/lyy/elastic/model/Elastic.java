package com.lyy.elastic.model;

import lombok.Getter;
import lombok.Setter;

/**
 * @Author: lyy
 * @Date: 2019/8/2 16:56
 */
@Setter
@Getter
public class Elastic {
    private String count;
    private String deleted;
    private String sizeInBytes;
    private String throttleTimeInMillis;


}
