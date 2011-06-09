/*
 *   Copyright (C) 2009 Huawei Tech. Co., Ltd. All rights reserved.
 *
 *   产品名:     PQM V100R002
 *   模块名:     IP性能管理
 *   文件名:     TimeplotData.java
 *   描述信息:
 *   版本说明:
 *   版权信息:    深圳华为技术有限公司
 *   作者:       w62784
 *   创建时间:    2009-10-9
 */
package com.zk.test.timeplot;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import org.zkforge.json.simple.JSONObject;
import org.zkforge.timeplot.data.PlotData;

public class TimeplotData extends PlotData
{
    private final static SimpleDateFormat timeformat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.CHINA);

    static DecimalFormat static_nf = new DecimalFormat(".####");

    @SuppressWarnings("unchecked")
    public String toString()
    {
        JSONObject json = new JSONObject();
        json.put("id", String.valueOf(getId()));
        TimeZone zone = Calendar.getInstance().getTimeZone();
        timeformat.setTimeZone(zone);
        String formattedTime = timeformat.format(getTime());
        json.put("time", formattedTime);
        if (getValue() < 0)
        {
            json.put("value", "NaN");
        }
        else
        {
            json.put("value", static_nf.format(getValue()));
        }
//        if (getReliability() < 0)
//        {
//            json.put("reliability", "NaN");
//        }
//        else
//        {
//            json.put("reliability", static_nf.format(getReliability()));
//        }
//        System.out.println(json.toString());
        return json.toString();
    }
}
