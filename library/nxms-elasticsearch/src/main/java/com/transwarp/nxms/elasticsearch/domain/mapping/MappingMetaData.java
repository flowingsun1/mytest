package com.transwarp.nxms.elasticsearch.domain.mapping;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * @author dzg
 * @since 2018/8/28
 */
@Setter
@Getter
public class MappingMetaData {
    private Map<String, MappingProperties> mappings;
}
