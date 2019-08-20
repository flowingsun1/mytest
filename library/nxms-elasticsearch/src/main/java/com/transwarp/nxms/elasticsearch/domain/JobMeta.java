package com.transwarp.nxms.elasticsearch.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

/**
 * @author ksdhc
 */
@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class JobMeta {
    private Long id;
    private String description;
    private String submitted;
    private String duration;
    private String succeedRate;
    private String status;
}
