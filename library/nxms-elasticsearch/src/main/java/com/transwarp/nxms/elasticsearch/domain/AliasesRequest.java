package com.transwarp.nxms.elasticsearch.domain;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author ZGW
 * @Date 2019/3/13
 */
public class AliasesRequest {

    private List<JsonObject> actions = new ArrayList<>();

    public AliasesRequest addActions(String index, String alias, ActionType type) {
        AliasesAction add = AliasesAction.builder().index(index).alias(alias).build();
        JsonObject jsonObject = new JsonObject();
        jsonObject.add(type.name().toLowerCase(), add.convertJsonObject());
        actions.add(jsonObject);
        return this;
    }
}
