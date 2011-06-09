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
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Tabs;

import com.zk.test.timeplot.TimeplotData;

public class TimeplotTest implements Composer
{

    private Button view;

    private Tabs tabs;

    private Timeplot timeplot;

    private Plotinfo plotinfo;

    private Long time = 1272073320169l;

    @Override
    public void doAfterCompose(Component comp) throws Exception
    {
        // TODO Auto-generated method stub
        timeplot = new Timeplot();
        ValueGeometry vg = new DefaultValueGeometry();
        vg.setGridColor("#000000");
        vg.setAxisLabelsPlacement("left");
        TimeGeometry tg = new DefaultTimeGeometry();
        tg.setGridStep(2 * 60 * 60 * 1000);
        tg.setAxisLabelsPlacement("bottom");
        tg.setGridStepRange(30 * 60 * 1000);
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
        Button btn = new Button("click");
        btn.setParent(comp);
        btn.addEventListener("onClick", new EventListener()
        {

            @Override
            public void onEvent(Event event) throws Exception
            {
                // TODO Auto-generated method stub
                ListModelList dml = (ListModelList) plotinfo.getDataModel();
                TimeplotData td = new TimeplotData();
                td.setTime(new Date(time + 1000 * 60 * 60 * 8));
                td.setValue(10f);
                dml.add(td);
            }
        });
    }

    private ArrayList<TimeplotData> initDataMode()
    {
        ArrayList<TimeplotData> datas = new ArrayList<TimeplotData>();
        for (int i = 0; i < 10; i++)
        {
            TimeplotData pd = new TimeplotData();
            pd.setTime(new Date(time + 1000 * 60 * 60 * 24 * i));
            pd.setValue((float) (i*0.1));
            datas.add(pd);
        }
//        TimeplotData pd1 = new TimeplotData();
//        pd1.setTime(new Date(time + 1000 * 10));
//        pd1.setValue(-1);
//        datas.add(pd1);
        return datas;
    }

}
