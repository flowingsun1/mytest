package com.transwarp.nxms.elasticsearch.domain.metrics.thread;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;

/**
 * @Author: lyy
 * @Date: 2019/8/9 16:50
 */
@Getter
public class ThreadPool {
    private Bulk bulk;
    @SerializedName("fetch_shard_started")
    private FetchShardStarted fetchShardStarted;
    @SerializedName("fetch_shard_store")
    private FetchShardStore fetchShardStore;
    private Flush flush;
    @SerializedName("force_merge")
    private ForceMerge forceMerge;
    private Generic generic;
    private Get get;
    private Index index;
    private Listener listener;
    private Management management;
    private Refresh refresh;
    private Search search;
    private Snapshot snapshot;
    private Warmer warmer;
}
