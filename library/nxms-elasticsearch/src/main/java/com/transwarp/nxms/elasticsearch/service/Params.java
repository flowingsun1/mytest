package com.transwarp.nxms.elasticsearch.service;

import org.elasticsearch.action.admin.cluster.health.ClusterHealthRequest;
import org.elasticsearch.action.support.ActiveShardCount;
import org.elasticsearch.action.support.IndicesOptions;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.client.Request;
import org.elasticsearch.cluster.health.ClusterHealthStatus;
import org.elasticsearch.common.Priority;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.lucene.uid.Versions;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.VersionType;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.elasticsearch.tasks.TaskId;

import java.util.*;

/**
 * @Author ZGW
 * @Date 2019/3/18
 */
public class Params {
    private final Request request;

    Params(Request request) {
        this.request = request;
    }

    Params putParam(String name, String value) {
        if (Strings.hasLength(value)) {
            request.addParameter(name, value);
        }
        return this;
    }

    Params putParam(String key, TimeValue value) {
        if (value != null) {
            return putParam(key, value.getStringRep());
        }
        return this;
    }

    Params withDocAsUpsert(boolean docAsUpsert) {
        if (docAsUpsert) {
            return putParam("doc_as_upsert", Boolean.TRUE.toString());
        }
        return this;
    }

    Params withFetchSourceContext(FetchSourceContext fetchSourceContext) {
        if (fetchSourceContext != null) {
            if (fetchSourceContext.fetchSource() == false) {
                putParam("_source", Boolean.FALSE.toString());
            }
            if (fetchSourceContext.includes() != null && fetchSourceContext.includes().length > 0) {
                putParam("_source_include", join(",", fetchSourceContext.includes()));
            }
            if (fetchSourceContext.excludes() != null && fetchSourceContext.excludes().length > 0) {
                putParam("_source_exclude", join(",", fetchSourceContext.excludes()));
            }
        }
        return this;
    }

    Params withFields(String[] fields) {
        if (fields != null && fields.length > 0) {
            return putParam("fields", join(",", fields));
        }
        return this;
    }

    Params withMasterTimeout(TimeValue masterTimeout) {
        return putParam("master_timeout", masterTimeout);
    }

    Params withParent(String parent) {
        return putParam("parent", parent);
    }

    Params withPipeline(String pipeline) {
        return putParam("pipeline", pipeline);
    }

    Params withPreference(String preference) {
        return putParam("preference", preference);
    }

    Params withRealtime(boolean realtime) {
        if (realtime == false) {
            return putParam("realtime", Boolean.FALSE.toString());
        }
        return this;
    }

    Params withRefresh(boolean refresh) {
        if (refresh) {
            return withRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);
        }
        return this;
    }

    Params withRefreshPolicy(WriteRequest.RefreshPolicy refreshPolicy) {
        if (refreshPolicy != WriteRequest.RefreshPolicy.NONE) {
            return putParam("refresh", refreshPolicy.getValue());
        }
        return this;
    }

    Params withRetryOnConflict(int retryOnConflict) {
        if (retryOnConflict > 0) {
            return putParam("retry_on_conflict", String.valueOf(retryOnConflict));
        }
        return this;
    }

    Params withRouting(String routing) {
        return putParam("routing", routing);
    }

    Params withStoredFields(String[] storedFields) {
        if (storedFields != null && storedFields.length > 0) {
            return putParam("stored_fields", join(",", storedFields));
        }
        return this;
    }

    Params withTimeout(TimeValue timeout) {
        return putParam("timeout", timeout);
    }

    Params withUpdateAllTypes(boolean updateAllTypes) {
        if (updateAllTypes) {
            return putParam("update_all_types", Boolean.TRUE.toString());
        }
        return this;
    }

    Params withVersion(long version) {
        if (version != Versions.MATCH_ANY) {
            return putParam("version", Long.toString(version));
        }
        return this;
    }

    Params withVersionType(VersionType versionType) {
        if (versionType != VersionType.INTERNAL) {
            return putParam("version_type", versionType.name().toLowerCase(Locale.ROOT));
        }
        return this;
    }

    Params withWaitForActiveShards(ActiveShardCount currentActiveShardCount, ActiveShardCount defaultActiveShardCount) {
        if (currentActiveShardCount != null && currentActiveShardCount != defaultActiveShardCount) {
            return putParam("wait_for_active_shards", currentActiveShardCount.toString().toLowerCase(Locale.ROOT));
        }
        return this;
    }

    Params withIndicesOptions(IndicesOptions indicesOptions) {
        withIgnoreUnavailable(indicesOptions.ignoreUnavailable());
        putParam("allow_no_indices", Boolean.toString(indicesOptions.allowNoIndices()));
        String expandWildcards;
        if (!indicesOptions.expandWildcardsOpen() && !indicesOptions.expandWildcardsClosed()) {
            expandWildcards = "none";
        } else {
            StringBuilder builder = new StringBuilder();
            if (indicesOptions.expandWildcardsOpen()) {
                builder.append("open");
            }
            if (indicesOptions.expandWildcardsClosed()) {
                if (builder.length() > 0) {
                    builder.append(",");
                }
                builder.append("closed");
            }
            expandWildcards = builder.toString();
        }
        putParam("expand_wildcards", expandWildcards);
        return this;
    }

    Params withIgnoreUnavailable(boolean ignoreUnavailable) {
        // Always explicitly place the ignore_unavailable value.
        putParam("ignore_unavailable", Boolean.toString(ignoreUnavailable));
        return this;
    }

    Params withHuman(boolean human) {
        if (human) {
            putParam("human", Boolean.toString(human));
        }
        return this;
    }

    Params withLocal(boolean local) {
        if (local) {
            putParam("local", Boolean.toString(local));
        }
        return this;
    }

    Params withIncludeDefaults(boolean includeDefaults) {
        if (includeDefaults) {
            return putParam("include_defaults", Boolean.TRUE.toString());
        }
        return this;
    }

    Params withPreserveExisting(boolean preserveExisting) {
        if (preserveExisting) {
            return putParam("preserve_existing", Boolean.TRUE.toString());
        }
        return this;
    }

    Params withDetailed(boolean detailed) {
        if (detailed) {
            return putParam("detailed", Boolean.TRUE.toString());
        }
        return this;
    }

    Params withWaitForCompletion(boolean waitForCompletion) {
        if (waitForCompletion) {
            return putParam("wait_for_completion", Boolean.TRUE.toString());
        }
        return this;
    }

    Params withNodes(String[] nodes) {
        if (nodes != null && nodes.length > 0) {
            return putParam("nodes", join(",", nodes));
        }
        return this;
    }

    Params withActions(String[] actions) {
        if (actions != null && actions.length > 0) {
            return putParam("actions", join(",", actions));
        }
        return this;
    }

    Params withTaskId(TaskId taskId) {
        if (taskId != null && taskId.isSet()) {
            return putParam("task_id", taskId.toString());
        }
        return this;
    }

    Params withParentTaskId(TaskId parentTaskId) {
        if (parentTaskId != null && parentTaskId.isSet()) {
            return putParam("parent_task_id", parentTaskId.toString());
        }
        return this;
    }

    Params withVerify(boolean verify) {
        if (verify) {
            return putParam("verify", Boolean.TRUE.toString());
        }
        return this;
    }

    Params withWaitForStatus(ClusterHealthStatus status) {
        if (status != null) {
            return putParam("wait_for_status", status.name().toLowerCase(Locale.ROOT));
        }
        return this;
    }

    Params withWaitForNoRelocatingShards(boolean waitNoRelocatingShards) {
        if (waitNoRelocatingShards) {
            return putParam("wait_for_no_relocating_shards", Boolean.TRUE.toString());
        }
        return this;
    }

    Params withWaitForNoInitializingShards(boolean waitNoInitShards) {
        if (waitNoInitShards) {
            return putParam("wait_for_no_initializing_shards", Boolean.TRUE.toString());
        }
        return this;
    }

    Params withWaitForNodes(String waitForNodes) {
        return putParam("wait_for_nodes", waitForNodes);
    }

    Params withLevel(ClusterHealthRequest.Level level) {
        return putParam("level", level.name().toLowerCase(Locale.ROOT));
    }

    Params withWaitForEvents(Priority waitForEvents) {
        if (waitForEvents != null) {
            return putParam("wait_for_events", waitForEvents.name().toLowerCase(Locale.ROOT));
        }
        return this;
    }

    static String join(String delimiter, String... params) {
        StringBuilder builder = new StringBuilder();
        for (String param : params) {
            if (builder.length() == 0) {
                builder.append(param);
            } else {
                builder.append(delimiter).append(param);
            }
        }
        return builder.toString();
    }
}
