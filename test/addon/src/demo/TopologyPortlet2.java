package demo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.zkforge.timeplot.Plotinfo;
import org.zkforge.timeplot.Timeplot;
import org.zkforge.timeplot.data.PlotData;
import org.zkforge.timeplot.data.PlotDataSource;
import org.zkforge.timeplot.event.OverPlotEvent;
import org.zkforge.timeplot.geometry.DefaultTimeGeometry;
import org.zkforge.timeplot.geometry.DefaultValueGeometry;
import org.zkforge.timeplot.geometry.TimeGeometry;
import org.zkforge.timeplot.geometry.ValueGeometry;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;

public class TopologyPortlet2 extends GenericForwardComposer
{
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -3766281934484553608L;

    private Date now = new Date();
    private Plotinfo plot1;

    private Timeplot relatedPlot;
    private Label plot11;
    private Label date1;
    private Label value1;
    private Label reliability1;

    @Override
    public void doAfterCompose(Component comp) throws Exception
    {
        super.doAfterCompose(comp);
        relatedPlot = new Timeplot();
        relatedPlot.setParent(comp);
        plot1=new Plotinfo();

        plot1.setParent(relatedPlot);
   
        relatedPlot.setHideTimeFlag(false);
        showTimePlot();
    }

    public void showTimePlot()
    {
        ValueGeometry vg = new DefaultValueGeometry();
        vg.setGridColor("red");
        vg.setAxisLabelsPlacement("left");

        TimeGeometry tg = new DefaultTimeGeometry();
        tg.setAxisLabelsPlacement("bottom");
        tg.setFormat(TimeGeometry.FORMAT_YEAR, "YYYY");
        tg.setGridColor("red");
        tg.setFormat(TimeGeometry.FORMAT_HOUR,"HH:mm ");
        plot1.setValueGeometry(vg);
        plot1.setTimeGeometry(tg);
        plot1.setShowValues(true);
        plot1.setHideValueFlag(true);
        plot1.setRoundValues(false);
        

        plot1.addEventListener("onOverPlotData",new EventListener()
        {

            @Override
            public void onEvent(Event arg0) throws Exception
            {
                OverPlotEvent ove =(OverPlotEvent) arg0;
                date1.setValue(ove.getValue()+"");
                Map map =ove.getCustomValues();
                value1.setValue(map.get("txf")+"");
                
            }
            
        });


        ListModelList model = new ListModelList();
        Calendar cal = Calendar.getInstance();
        for (int j = 0; j < 10; j++)
        {

            PlotData data = new PlotData();
            cal.add(Calendar.MINUTE, j);
            data.setTime(cal.getTime());

            float i = 0.0f;
  
            i=(float) (1.0-0.42);
            System.out.println(1.0-0.42);
            System.out.println("i:"+i);
            data.setValue(i);
            model.add(data);
        }

        plot1.setDataModel(model);
        plot1.setDotColor("#000000");
        plot1.setFillColor("#70A8E5");// E2F6F2
        plot1.setLineColor("red");



    }
}
