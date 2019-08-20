package com.transwarp.nxms.elasticsearch.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Created by susiqian on 17/4/11.
 */
@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class MetricData implements CheckCompare {
    private String metric;
    private Map<String, BigDecimal> dps = new TreeMap<>();
    private String aggregationType;


    /**
     * Loop through the datapoints in reverse order until we find the latest non-null value
     */
    @Override
    public BigDecimal getLatestValue() {
        BigDecimal[] values = new BigDecimal[dps.size()];
        dps.values().toArray(values);
        int size = values.length;
        for (int i = size - 1; i >= 0; i--) {
            BigDecimal value = values[i];
            if (!StringUtils.isEmpty(value)) {
                return value.setScale(2, 4);
            }
        }
        return null;
    }
    @Override
    public BigDecimal getAverageValue() {
        BigDecimal total = null;
        BigDecimal[] values = new BigDecimal[dps.size()];
        dps.values().toArray(values);
        int size = values.length;
        Boolean nullTag = true;
        for (int i = size - 1; i >= 0; i--) {
            BigDecimal value = values[i];
            if (!StringUtils.isEmpty(value)) {
                nullTag = false;
                if (total == null) {
                    total = value;
                    continue;
                }
                total = total.add(value);
            } else {
                size--;
            }
        }
        if (nullTag) {
            return null;
        }
        return total.divide(BigDecimal.valueOf(size), 2).setScale(2, 4);

    }
    @Override
    public BigDecimal getMaxValue() {
        BigDecimal max = null;
        BigDecimal[] values = new BigDecimal[dps.size()];
        dps.values().toArray(values);
        int size = values.length;
        for (int i = size - 1; i >= 0; i--) {
            BigDecimal value = values[i];
            if (!StringUtils.isEmpty(value)) {
                if (max == null) {
                    max = value.setScale(2, 4);
                    continue;
                }
                max = max.max(value.setScale(2, 4));
            }
        }
        return max;
    }
    @Override
    public BigDecimal getMinValue() {
        BigDecimal min = null;
        BigDecimal[] values = new BigDecimal[dps.size()];
        dps.values().toArray(values);
        int size = values.length;
        for (int i = size - 1; i >= 0; i--) {
            BigDecimal value = values[i];
            if (!StringUtils.isEmpty(value)) {
                if (min == null) {
                    min = value.setScale(2, 4);
                    continue;
                }
                min = min.min(value.setScale(2, 4));
            }
        }
        return min;
    }

    public Long getLastUpdateAt() {
        Set<String> keySet = dps.keySet();
        Long maxTime = 0L;
        for (String key : keySet) {
            if (key != null && !key.isEmpty()) {
                Long time = Long.valueOf(key);
                if (time > maxTime) {
                    maxTime = time;
                }
            }
        }
        if (maxTime != 0L) {
            return maxTime;
        }
        return System.currentTimeMillis() / 1000;
    }

    public Map<String, String> toSimpleData() {
        Map<String, String> map = new HashMap<>();
        StringBuilder value = new StringBuilder();
        BigDecimal[] values = new BigDecimal[dps.size()];
        dps.values().toArray(values);
        int start = 0;
        int end = values.length - 1;
        if (end > 9) {
            start = end - 9;
        }
        for (int i = start; i <= end; i++) {
            BigDecimal val = values[i];
            value.append(val.setScale(2, 4).toString());
            if (i != end) {
                value.append(",");
            }
        }
        if (value.length() == 0) {
            map.put(this.metric, null);
        } else {
            map.put(this.metric, value.toString());
        }
        return map;
    }

    public BigDecimal[] yPoints() {
        BigDecimal[] values = new BigDecimal[dps.size()];
        dps.values().toArray(values);
        return values;
    }

    public BigDecimal[] xPoints() {
        BigDecimal[] values = new BigDecimal[dps.size()];
        Set<String> keySet = dps.keySet();
        int i = 0;
        for (String key : keySet) {
            values[i] = BigDecimal.valueOf(Long.valueOf(key));
            i++;
        }
        return values;
    }

    public BigDecimal[] xPointsRelative() {
        BigDecimal[] values = new BigDecimal[dps.size()];
        Set<String> keySet = dps.keySet();
        int i = 0;
        Long firstTime = 0L;
        for (String key : keySet) {
            if (i == 0) {
                firstTime = Long.valueOf(key);
                values[i] = BigDecimal.ZERO;
            } else {
                values[i] = BigDecimal.valueOf(Long.valueOf(key) - firstTime);
            }
            i++;
        }
        return values;
    }

    public void processMetricData(String body) {

    }
}
