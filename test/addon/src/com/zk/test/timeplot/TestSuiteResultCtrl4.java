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

import org.zkforge.timeplot.Plotinfo;
import org.zkforge.timeplot.Timeplot;
import org.zkforge.timeplot.data.PlotDataSource;
import org.zkforge.timeplot.geometry.DefaultTimeGeometry;
import org.zkforge.timeplot.geometry.DefaultValueGeometry;
import org.zkforge.timeplot.geometry.TimeGeometry;
import org.zkforge.timeplot.geometry.ValueGeometry;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Tabbox;


public class TestSuiteResultCtrl4 extends GenericForwardComposer
{
    Timeplot timeplot;

    Plotinfo dfPlot1;

    Plotinfo dfPlot2;
    
    Plotinfo dfPlot3;

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

    Button btn;
    @Override
    public void doAfterCompose(Component comp) throws Exception
    {
        super.doAfterCompose(comp);
//        btn = (Button)comp.getFellow("click");
//        btn.setLabel("Click");
//        Timeplot timeplot = new Timeplot();
        timeplot.setWidth("100%");
        timeplot.setHeight("300px");
        dfPlot1 = new Plotinfo();
        dfPlot2 = new Plotinfo();
        dfPlot3 = new Plotinfo();
        dfPlot1.setParent(timeplot);
        dfPlot2.setParent(timeplot);
        dfPlot3.setParent(timeplot);
        timeplot.setParent(comp);
        showTimeplot();
//        btn.addEventListener("onClick", new EventListener(){
//
//            @Override
//            public void onEvent(Event event) throws Exception
//            {
//                // TODO Auto-generated method stub
//                showTimeplot();
//            }
//            
//        });
    }

    private void showTimeplot()
    {
        databuff = initDataMode();
        ListModelList dataModel1 = new ListModelList(databuff.get(0));
        ListModelList dataModel2 = new ListModelList(databuff.get(1));
        ListModelList dataModel3 = new ListModelList(databuff.get(2));
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
        dfPlot1.setShowValues(true);
        
        dfPlot2.setValueGeometry(vg);
        dfPlot2.setPlotDataSource(pds);
        dfPlot2.setDataModel(dataModel2);
        dfPlot2.setTimeGeometry(tg);
        dfPlot2.setShowValues(true);
        
        dfPlot3.setPlotDataSource(pds);
        dfPlot3.setValueGeometry(vg);
        dfPlot3.setTimeGeometry(tg);
        dfPlot3.setDataModel(dataModel3);
        dfPlot3.setShowValues(true);
//        dfPlot1.setZkDefault(false);
//        dfPlot2.setZkDefault(false);
//        dfPlot3.setZkDefault(false);
        plots.add(dfPlot1);
        plots.add(dfPlot2);
    }
    
    private ArrayList<ArrayList<TimeplotData>> initDataMode()
    {
        ArrayList<TimeplotData> datas1 = new ArrayList<TimeplotData>();
        ArrayList<TimeplotData> datas2 = new ArrayList<TimeplotData>();
        ArrayList<TimeplotData> datas3 = new ArrayList<TimeplotData>();
        ArrayList<ArrayList<TimeplotData>> rets = new ArrayList<ArrayList<TimeplotData>>();
        rets.add(datas1);
        rets.add(datas2);
        rets.add(datas3);
        Date date = new Date(new Date().getTime() - timeWindowSize * interval);
        for (int i = 0; i < timeWindowSize; i++)
        {
            TimeplotData pd1 = new TimeplotData();
            TimeplotData pd2 = new TimeplotData();
            TimeplotData pd3 = new TimeplotData();
            pd1.setTime(date);
            pd2.setTime(date);
            pd3.setTime(date);
            pd1.setValue((float) (0.0));
            pd2.setValue((float) (Math.random() * 100));
            pd3.setValue((float) (Math.random() * 100));
            datas1.add(pd1);
            datas2.add(pd2);
            datas3.add(pd3);
            date = new Date(date.getTime() + interval);
        }
        return rets;
    }
}
