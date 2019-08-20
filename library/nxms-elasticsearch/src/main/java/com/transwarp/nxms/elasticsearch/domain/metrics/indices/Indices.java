package com.transwarp.nxms.elasticsearch.domain.metrics.indices;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;

/**
 * @Author: lyy
 * @Date: 2019/8/9 16:50
 */
@Getter
public class Indices {
    private Docs docs;
    private Store store;
    private Indexing indexing;
    private Get get;
    private Search search;
    private Merges merges;
    private Refresh refresh;
    private Flush flush;
    private Warmer warmer;
    @SerializedName("query_cache")
    private QueryCache queryCache;
    private FieldData fielddata;
    private Completion completion;
    private Segments segments;
    private TransLog translog;
    @SerializedName("request_cache")
    private RequestCache requestCache;
    private Recovery recovery;
}
