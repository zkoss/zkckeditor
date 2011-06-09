/*
 *   Copyright (C) 2009 Huawei Tech. Co., Ltd. All rights reserved.
 *
 *   产品名:     PQM V100R002
 *   模块名:     IP性能管理
 *   文件名:     TimeplotModelCtrl.java
 *   描述信息:
 *   版本说明:
 *   版权信息:    深圳华为技术有限公司
 *   作者:       w62784
 *   创建时间:    2009-10-7
 */
package com.zk.test.timeplot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import org.zkforge.timeplot.Plotinfo;
import org.zkforge.timeplot.Timeplot;
import org.zkforge.timeplot.data.PlotDataSource;
import org.zkforge.timeplot.geometry.DefaultTimeGeometry;
import org.zkforge.timeplot.geometry.DefaultValueGeometry;
import org.zkforge.timeplot.geometry.TimeGeometry;
import org.zkforge.timeplot.geometry.ValueGeometry;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Tabbox;


public class TestSuiteResultCtrl3 extends GenericForwardComposer
{
    Timeplot timeplot;

    Plotinfo dfPlot1;

    Plotinfo dfPlot2;

    Checkbox auto;

    Datebox starttime;

    Datebox endtime;

    Button bugBtn;

    int timeWindowSize = 240; // 时间窗数据点大小

    Tabbox tbbox;

    final static int interval = 30 * 1000; // 单位 毫秒

    SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    ArrayList<ArrayList<TimeplotData>> databuff;

    ArrayList<Plotinfo> plots = new ArrayList<Plotinfo>();

    @Override
    public void doAfterCompose(Component comp) throws Exception
    {
        super.doAfterCompose(comp);
        databuff = initDataMode();
        ListModelList dataModel1 = new ListModelList(databuff.get(0));
        ListModelList dataModel2 = new ListModelList(databuff.get(1));
        ValueGeometry vg = new DefaultValueGeometry();
        vg.setGridColor("#000000");
        TimeGeometry tg = new DefaultTimeGeometry();
        tg.setGridStep(2 * 60 * 60 * 1000);
        tg.setAxisLabelsPlacement("bottom");
        tg.setGridStepRange(30 * 60 * 1000);
        PlotDataSource pds = new PlotDataSource();
        pds.setSeparator(" ");
        dfPlot1.setPlotDataSource(pds);
        dfPlot1.setValueGeometry(vg);
        dfPlot1.setTimeGeometry(tg);
        dfPlot1.setDataModel(dataModel1);
        dfPlot2.setPlotDataSource(pds);
        dfPlot2.setValueGeometry(vg);
        dfPlot2.setTimeGeometry(tg);
        dfPlot2.setDataModel(dataModel2);
        plots.add(dfPlot1);
        plots.add(dfPlot2);
        
        for (Object object : dataModel1) {
        	TimeplotData tpd = (TimeplotData) object;
        	System.out.println(tpd.getValue());
		}
        System.out.println();
        for (Object object : dataModel2) {
        	TimeplotData tpd = (TimeplotData) object;
        	System.out.println(tpd.getValue());
		}
    }

    private ArrayList<ArrayList<TimeplotData>> initDataMode()
    { Random r = new Random();
        ArrayList<TimeplotData> datas1 = new ArrayList<TimeplotData>();
        ArrayList<TimeplotData> datas2 = new ArrayList<TimeplotData>();
        ArrayList<ArrayList<TimeplotData>> rets = new ArrayList<ArrayList<TimeplotData>>();
        rets.add(datas1);
        rets.add(datas2);
        Date date = new Date(new Date().getTime() - timeWindowSize * interval);
        for (int i = 0; i < timeWindowSize; i++)
        {
            TimeplotData pd1 = new TimeplotData();
            TimeplotData pd2 = new TimeplotData();
            pd1.setTime(date);
            pd2.setTime(date);
//            pd1.setValue(10);
//            pd2.setValue(20);
            pd1.setValue(new Float(r.nextInt(10)));
            pd2.setValue(new Float(r.nextInt(100)));
            datas1.add(pd1);
            datas2.add(pd2);
            date = new Date(date.getTime() + interval);
        }
        return rets;
    }
}
