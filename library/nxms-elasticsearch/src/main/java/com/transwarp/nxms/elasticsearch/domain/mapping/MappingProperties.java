package com.transwarp.nxms.elasticsearch.domain.mapping;

import com.transwarp.nxms.elasticsearch.domain.mapping.MappingProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * @author dzg
 * @since 2018/8/28
 */
@Setter
@Getter
public class MappingProperties {
    private Map<String, MappingProperty> properties;
}
