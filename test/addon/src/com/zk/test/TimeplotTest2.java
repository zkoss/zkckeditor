package com.zk.test;

import java.util.ArrayList;
import java.util.Date;

import org.zkforge.timeplot.Plotinfo;
import org.zkforge.timeplot.Timeplot;
import org.zkforge.timeplot.geometry.DefaultTimeGeometry;
import org.zkforge.timeplot.geometry.DefaultValueGeometry;
import org.zkforge.timeplot.geometry.TimeGeometry;
import org.zkforge.timeplot.geometry.ValueGeometry;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.util.Composer;
import org.zkoss.zul.Button;
import org.zkoss.zul.ListModelList;

import com.zk.test.timeplot.TimeplotData;

public class TimeplotTest2 implements Composer
{
    private Timeplot timeplot;

    private Plotinfo plotinfo;

    @Override
    public void doAfterCompose(final Component comp) throws Exception
    {
        // TODO Auto-generated method stub
        timeplot = new Timeplot();
        ValueGeometry vg = new DefaultValueGeometry();
        vg.setGridColor("#000000");
        vg.setAxisLabelsPlacement("left");
        TimeGeometry tg = new DefaultTimeGeometry();
        tg.setAxisLabelsPlacement("bottom");
        timeplot.setWidth("100%");
        timeplot.setHeight("300px");
        plotinfo = new Plotinfo();
        plotinfo.setShowValues(true);
        ListModelList dataModel = new ListModelList(initDataMode());
        plotinfo.setDataModel(dataModel);
        plotinfo.setParent(timeplot);
        plotinfo.setTimeGeometry(tg);
        plotinfo.setValueGeometry(vg);
        timeplot.setParent(comp);
       
        Button btn1 = new Button("ChangeData");
        btn1.setParent(comp);
        btn1.addEventListener("onClick", new EventListener(){

            @Override
            public void onEvent(Event event) throws Exception
            {
                // TODO Auto-generated method stub
                ListModelList dataModel = (ListModelList)plotinfo.getDataModel();
                ArrayList<TimeplotData> datas = new ArrayList<TimeplotData>();
                TimeplotData pd = new TimeplotData();
                pd.setTime(new Date(new Date().getTime() + 1000 * 1));
                pd.setValue(10f);
                datas.add(pd);
                dataModel.clear();
                dataModel.addAll(datas);
            }});
    }

    private ArrayList<TimeplotData> initDataMode()
    {
        ArrayList<TimeplotData> datas = new ArrayList<TimeplotData>();
        for (int i = 0; i < 10; i++)
        {
            TimeplotData pd = new TimeplotData();
            pd.setTime(new Date(new Date().getTime() + 1000 * i));
            pd.setValue(0f);
            datas.add(pd);
        }
        return datas;
    }

}
