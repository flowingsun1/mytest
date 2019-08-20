package com.transwarp.nxms.elasticsearch.domain;

import java.math.BigDecimal;

/**
 * @author dzg
 * @since 2017/11/30.
 */
public interface CheckCompare {

    BigDecimal getLatestValue();

    BigDecimal getAverageValue();

    BigDecimal getMaxValue();

    BigDecimal getMinValue();
}
