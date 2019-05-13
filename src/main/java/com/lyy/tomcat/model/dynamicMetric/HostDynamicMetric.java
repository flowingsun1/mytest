package com.lyy.tomcat.model.dynamicMetric;

import lombok.Getter;
import lombok.Setter;

/**
 * @Author: lyy
 * @Date: 2019/1/16 14:22
 */
@Getter
@Setter
public class HostDynamicMetric {
    //活动会话数
    private Integer activeSessions;
    //无状态传输数
    private Integer counterNoStateTransfered;
    //过期会话数
    private Long expiredSessions;
    //最大活动数
    private Integer maxActive;

    //最大活动会话数
    private Integer maxActiveSessions;
    //拒绝会话数
    private Integer rejectedSessions;
    //会话平均时间
    private Integer sessionAverageAliveTime;
    //会话最大活动时间
    private Integer sessionMaxAliveTime;
    //会话替换数
    private Long sessionReplaceCounter;



}
