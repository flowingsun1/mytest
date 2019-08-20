package com.transwarp.nxms.elasticsearch.util;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.transwarp.nxms.elasticsearch.domain.MetricData;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author ZGW
 * @Date 2019/3/20
 */
public class MetricDataProcessor {
    private static final String MINUTE_AVG = "minute_avg";

    private static final String AGGREGATIONS = "aggregations";

    private static final String DATE_HISTOGRAM_DPS = "date_histogram#dps";

    private static final String MOV_AVG = "mov_avg";

    private static final String BUCKETS = "buckets";

    private static final String VALUE = "value";

    public static void processorMetricData(String data, MetricData metricData, Gson gson) {
        JsonObject jsonObject = gson.fromJson(data, JsonObject.class);
        boolean has = jsonObject.get(AGGREGATIONS).getAsJsonObject().has("dps");
        JsonElement jsonDps;
        String avgField = MINUTE_AVG;
        if (has) {
            jsonDps = jsonObject.get(AGGREGATIONS).getAsJsonObject().get("dps");
        } else {
            jsonDps = jsonObject.get(AGGREGATIONS).getAsJsonObject().get(DATE_HISTOGRAM_DPS);
            avgField = "avg#" + avgField;
        }
        JsonArray asJsonArray = jsonDps.getAsJsonObject().get(BUCKETS).getAsJsonArray();
        for (JsonElement next : asJsonArray) {
            long millis = next.getAsJsonObject().get("key").getAsLong();
            long second = millis / 1000;
            JsonObject minuteAvg = next.getAsJsonObject().get(avgField).getAsJsonObject();
            BigDecimal value = minuteAvg.get(VALUE).getAsBigDecimal();
            metricData.getDps().put(Long.toString(second), value);
        }
    }

    public static void processorMetricDatas(String index, String data, List<MetricData> list, Gson gson) {
        MetricData avgData = new MetricData();
        MetricData varianceData = new MetricData();
        MetricData maxData = new MetricData();
        MetricData minData = new MetricData();
        MetricData stdDeviationData = new MetricData();
        MetricData upperData = new MetricData();
        MetricData lowerData = new MetricData();
        JsonObject jsonObject = gson.fromJson(data, JsonObject.class);
        JsonElement jsonDps;
        boolean has = jsonObject.get(AGGREGATIONS).getAsJsonObject().has("dps");
        String avgField = MINUTE_AVG;
        if (has) {
            jsonDps = jsonObject.get(AGGREGATIONS).getAsJsonObject().get("dps");
        } else {
            avgField = "extended_stats#" + avgField;
            jsonDps = jsonObject.get(AGGREGATIONS).getAsJsonObject().get(DATE_HISTOGRAM_DPS);
        }
        JsonArray asJsonArray = jsonDps.getAsJsonObject().get(BUCKETS).getAsJsonArray();
        for (JsonElement next : asJsonArray) {
            try {
                long millis = next.getAsJsonObject().get("key").getAsLong();
                long second = millis / 1000;
                JsonObject minuteAvg = next.getAsJsonObject().get(avgField).getAsJsonObject();
                BigDecimal min = minuteAvg.get("min").getAsBigDecimal();
                BigDecimal max = minuteAvg.get("max").getAsBigDecimal();
                BigDecimal avg = minuteAvg.get("avg").getAsBigDecimal();
                BigDecimal stdDeviation = minuteAvg.get("std_deviation").getAsBigDecimal();
                BigDecimal variance = minuteAvg.get("variance").getAsBigDecimal();
                JsonObject stdObject = minuteAvg.getAsJsonObject("std_deviation_bounds");
                BigDecimal upper = stdObject.get("upper").getAsBigDecimal();
                BigDecimal lower = stdObject.get("lower").getAsBigDecimal();
                avgData.getDps().put(Long.toString(second), avg);
                varianceData.getDps().put(Long.toString(second), variance);
                maxData.getDps().put(Long.toString(second), max);
                minData.getDps().put(Long.toString(second), min);
                stdDeviationData.getDps().put(Long.toString(second), stdDeviation);
                upperData.getDps().put(Long.toString(second), upper);
                lowerData.getDps().put(Long.toString(second), lower);
            } catch (Exception e) {
            }
        }
        avgData.setMetric(index + ".average");
        varianceData.setMetric(index + ".variance");
        maxData.setMetric(index + ".max");
        minData.setMetric(index + ".min");
        stdDeviationData.setMetric(index + ".msd");
        upperData.setMetric(index + ".intervalUp");
        lowerData.setMetric(index + ".intervalLow");
        if (asJsonArray.size() != 0) {
            list.add(avgData);
            list.add(varianceData);
            list.add(stdDeviationData);
            list.add(maxData);
            list.add(minData);
            list.add(lowerData);
            list.add(upperData);
        }
    }

    public static void processorPipelineAggregationData(String data, MetricData metricData, Gson gson) {
        String field = "simple_value#mov_avg";
        JsonObject jsonObject = gson.fromJson(data, JsonObject.class);
        JsonObject jsonObjectAgg = jsonObject.get(AGGREGATIONS).getAsJsonObject();
        JsonElement jsonDps = jsonObjectAgg.get(DATE_HISTOGRAM_DPS) != null ? jsonObjectAgg.get(DATE_HISTOGRAM_DPS) : jsonObjectAgg.get("dps");
        JsonArray asJsonArray = jsonDps.getAsJsonObject().get(BUCKETS).getAsJsonArray();
        for (JsonElement next : asJsonArray) {
            if (!next.getAsJsonObject().has(field) && !next.getAsJsonObject().has(MOV_AVG)) {
                continue;
            }
            String movAvgField = next.getAsJsonObject().has(field) ? field : MOV_AVG;
            long second = next.getAsJsonObject().get("key").getAsLong();
            BigDecimal movAvg = next.getAsJsonObject().get(movAvgField).getAsJsonObject().get(VALUE).getAsBigDecimal();
            metricData.getDps().put(Long.toString(second), movAvg);
        }
    }
}
