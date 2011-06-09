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

import java.text.ParseException;
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
import org.zkoss.lang.Threads;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.DesktopUnavailableException;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Tabbox;

public class TestSuiteResultCtrl2 extends GenericForwardComposer
{
    Timeplot timeplot;

    Plotinfo dfPlot1;

    Plotinfo dfPlot2;

    Checkbox auto;

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
        vg.setMin(8);
        vg.setMax(23);
        TimeGeometry tg = new DefaultTimeGeometry();
        tg.setGridStep(2 * 60 * 60 * 1000);
        bugBtn.addEventListener("onClick", new EventListener()
        {

            @Override
            public void onEvent(Event event) throws Exception
            {
                Date begin = format.parse("2009/01/01 13:00:00");
                Date end = format.parse("2009/01/01 18:00:00");
                timeplot.invalidate();
                // new date reference
                dfPlot1.setDataModel(generateData(new Date(begin.getTime()), end));
                dfPlot2.setDataModel(generateData(new Date(begin.getTime()), end));

            }
        });
//        ValueGeometry vg2 = new DefaultValueGeometry();
//        vg2.setGridColor("#000000");
//        vg2.setMin(8);
//        vg2.setMax(23);
        // tg.setMax((int) (new Date().getTime()+2*60*60*1000));
        tg.setAxisLabelsPlacement("bottom");
        tg.setGridStepRange(30 * 60 * 1000);
        PlotDataSource pds = new PlotDataSource();
        pds.setSeparator(" ");
        dfPlot1.setRoundValues(false);
        dfPlot1.setPlotDataSource(pds);
        dfPlot1.setValueGeometry(vg);
        dfPlot1.setTimeGeometry(tg);
        dfPlot1.setDataModel(dataModel1);
        dfPlot2.setRoundValues(false);
        dfPlot2.setValueGeometry(vg);
        dfPlot2.setPlotDataSource(pds);
        dfPlot2.setDataModel(dataModel2);
        // miss TimeGeometry
        dfPlot2.setTimeGeometry(tg);
        //
        plots.add(dfPlot1);
        plots.add(dfPlot2);
        auto.setAttribute("push", false);
        auto.addEventListener("onCheck", new EventListener()
        {

            @Override
            public void onEvent(Event arg0) throws Exception
            {
                Boolean isPush = (Boolean) auto.getAttribute("push");
                Desktop desktop = Executions.getCurrent().getDesktop();
                if (!isPush)
                {
                    desktop.enableServerPush(true);

                    new WorkingThread(auto, dfPlot1, databuff.get(0), 5).start();
                    new WorkingThread(auto, dfPlot2, databuff.get(1), 10).start();
                }
                else
                {
                    desktop.enableServerPush(false);
                }
                auto.setAttribute("push", !isPush);

                // ListModelList dataModelNew=makeModel();
                //               
                // timplotModelTest.setDataModel(dataModelNew);
            }
        });
        tbbox.addEventListener("onSelect", new EventListener()
        {

            @Override
            public void onEvent(Event arg0) throws Exception
            {
                // TODO Auto-generated method stub
                for (Plotinfo plot : plots)
                {
                    plot.repaint();
                }
            }

        });

    }

    private ArrayList<ArrayList<TimeplotData>> initDataMode()
    {
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
            float rd = (float) (Math.random() * 10);
            if (i < timeWindowSize * 1 / 3)
            {
                pd1.setValue(10f + rd % 1);
                pd2.setValue(10f + rd % 1 + rd / 10);
            }
            else if (i < timeWindowSize * 2 / 3)
            {
                pd1.setValue(15f + rd % 5);
                pd2.setValue(15f + rd % 5 + 1);
            }
            else
            {
                pd1.setValue(10f + rd % 2);
                pd2.setValue(10f + rd % 2 + rd / 10);
            }

            datas1.add(pd1);
            datas2.add(pd2);
            date = new Date(date.getTime() + interval);
        }
        return rets;
    }

    private ListModelList makeModel() throws ParseException
    {
        ListModelList dataModel = new ListModelList();
        Date begin = format.parse("2009/01/01 06:00:00");
        for (int i = 0; i < 10; i++)
        {
            TimeplotData pd = new TimeplotData();
            pd.setTime(begin);
            pd.setValue((float) Math.random() * 100);
            dataModel.add(pd);
            begin = new Date(begin.getTime() + 3600000);
        }
        return dataModel;
    }

    static class WorkingThread extends Thread
    {
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

        private final Desktop _desktop;

        private final Component _info;

        private final Plotinfo _plot;

        private ArrayList<TimeplotData> _datas;

        int _interval;
        private WorkingThread(Component info, Plotinfo plot, ArrayList<TimeplotData> datas, int interval)
        {
            _desktop = info.getDesktop();
            _info = info;
            _plot = plot;
            _datas = datas;
            _interval = interval;
        }

        @Override
        public void run()
        {
            try
            {
                while (true)
                {
                    if (_info.getDesktop() == null || !_desktop.isServerPushEnabled())
                    {
                        _desktop.enableServerPush(false);
                        return;
                    }
                    Executions.activate(_desktop);
                    try
                    {
                        ListModelList model = (ListModelList) _plot.getDataModel();
                        model.remove(0);
                        TimeplotData pd = new TimeplotData();
                        pd.setTime(new Date());
                        pd.setValue((float) Math.random() * 100);
                        model.add(pd);
                        System.out.println(new Date()+": "+ this.getId());

                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                    finally
                    {
                        Executions.deactivate(_desktop);
                    }
                    Threads.sleep(_interval * 1000);
                }
            }
            catch (DesktopUnavailableException ex)
            {
                System.out.println("The server push thread interrupted");
            }
            catch (InterruptedException ex)
            {
                System.out.println("The server push thread interrupted");
            }

        }

        private void shiftDatas(ArrayList<TimeplotData> datas)
        {
            if (datas != null && !datas.isEmpty())
            {
                TimeplotData firstdata = datas.get(0);
                TimeplotData lastdata = datas.get(datas.size() - 1);
                datas.remove(0);
                firstdata.setTime(new Date(lastdata.getTime().getTime() + interval));
                datas.add(firstdata);
            }
        }

        private ListModelList makeModel() throws ParseException
        {

            ListModelList dataModel = new ListModelList();
            Date begin = format.parse("2009/01/01 06:00:00");
            for (int i = 0; i < 300; i++)
            {
                TimeplotData pd = new TimeplotData();
                pd.setTime(begin);
                pd.setValue((float) Math.random() * 100);
                dataModel.add(pd);
                begin = new Date(begin.getTime() + 3600000);
            }
            return dataModel;
        }
    }

    public ListModelList generateData(Date begin, Date end)
    {
        Date curr = begin;
        ListModelList model = new ListModelList();
        while (curr.before(end))
        {
            TimeplotData data = new TimeplotData();
            // new date reference
            data.setTime(new Date(curr.getTime()));
            Random x = new Random();
            float radomValue = x.nextFloat() * 100;
            radomValue = (radomValue < 20) ? radomValue + 20 : radomValue;
            data.setValue(radomValue);
            model.add(data);
            curr.setTime(curr.getTime() + 15 * 60 * 1000);
        }
        return model;
    }
}
