package com.transwarp.nxms.elasticsearch.domain;

import com.google.gson.JsonObject;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author ZGW
 * @Date 2019/3/13
 */
@Getter
@Setter
@Builder
public class AliasesAction {

    private String alias;

    private String index;

    public JsonObject convertJsonObject() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("index", index);
        jsonObject.addProperty("alias", alias);
        return jsonObject;
    }

}
