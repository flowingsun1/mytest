package com.transwarp.nxms.elasticsearch.domain.metrics.indices;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;

/**
 * @Author: lyy
 * @Date: 2019/8/15 17:17
 */
@Getter
public class Segments {
    private String count;
    @SerializedName("memory_in_bytes")
    private String memoryInBytes;
    @SerializedName("terms_memory_in_bytes")
    private String termsMemoryInBytes;
    @SerializedName("stored_fields_memory_in_bytes")
    private String storedFieldsMemoryInBytes;
    @SerializedName("term_vectors_memory_in_bytes")
    private String termVectorsMemoryInBytes;
    @SerializedName("norms_memory_in_bytes")
    private String normsMemoryInBytes;
    @SerializedName("points_memory_in_bytes")
    private String pointsMemoryInBytes;
    @SerializedName("doc_values_memory_in_bytes")
    private String docValuesMemoryInBytes;
    @SerializedName("index_writer_memory_in_bytes")
    private String indexWriterMemoryInBytes;
    @SerializedName("version_map_memory_in_bytes")
    private String versionMapMemoryInBytes;
    @SerializedName("fixed_bit_set_memory_in_bytes")
    private String fixedBitSetMemoryInBytes;
    @SerializedName("max_unsafe_auto_id_timestamp")
    private String maxUnsafeAutoIdTimestamp;
}
