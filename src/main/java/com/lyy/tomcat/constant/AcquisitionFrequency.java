package com.lyy.tomcat.constant;


/**
 * @author dzg.
 * @since 16/11/10.
 */
public enum AcquisitionFrequency {
    ONE_MINUTE("0 %s/1 * * * ?", 1, "min"),
    FIVE_MINUTE("0 %s/5 * * * ?", 5, "min"),
    TEN_MINUTE("0 %s/10 * * * ?", 10, "min"),
    FIFTEEN_MINUTE("0 %s/15 * * * ?", 15, "min"),
    HALF_HOUR("0 %s/30 * * * ?", 30, "min"),
    ONE_HOUR("0 %s 0/1 * * ?", 1, "h"),
    ONE_DAY("0 %s 0 1/1 * ?", 1, "d"),
    HALF_MINUTE("%s/30 * * * * ?", 30, "s"),
    TEN_SECOND("%s/10 * * * * ?", 10, "s");

    AcquisitionFrequency(String value, Integer period, String carbon) {
        this.value = value;
        this.carbon = carbon;
        this.period = period;
    }

    private String value;

    private String carbon;
    //graphite数据开始时间
    private Integer period;

    public String getValue() {
        return this.value;
    }

    public String getCarbon() {
        return carbon;
    }

    public Integer getPeriod() {
        return period;
    }

}
