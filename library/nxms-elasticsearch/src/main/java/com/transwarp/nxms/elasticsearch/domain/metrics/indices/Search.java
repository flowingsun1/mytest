package com.transwarp.nxms.elasticsearch.domain.metrics.indices;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;

/**
 * @Author: lyy
 * @Date: 2019/8/9 16:50
 */
@Getter
public class Search {
    @SerializedName("open_contexts")
    private String openContexts;

    @SerializedName("query_total")
    private String queryTotal;

    @SerializedName("query_time_in_millis")
    private String queryTimeInMillis;

    @SerializedName("query_current")
    private String queryCurrent;

    @SerializedName("fetch_total")
    private String fetchTotal;

    @SerializedName("fetch_time_in_millis")
    private String fetchTimeInMillis;

    @SerializedName("fetch_current")
    private String fetchCurrent;

    @SerializedName("scroll_total")
    private String scrollTotal;

    @SerializedName("scroll_time_in_millis")
    private String scrollTimeInMillis;

    @SerializedName("scroll_current")
    private String scrollCurrent;

    @SerializedName("suggest_total")
    private String suggestTotal;

    @SerializedName("suggest_time_in_millis")
    private String suggestTimeInMillis;

    @SerializedName("suggest_current")
    private String suggestCurrent;

}
