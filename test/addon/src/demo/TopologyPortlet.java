package demo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkforge.timeplot.Plotinfo;
import org.zkforge.timeplot.Timeplot;
import org.zkforge.timeplot.data.PlotData;
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
import org.zkoss.zul.ListModelList;

public class TopologyPortlet extends GenericForwardComposer
{
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -3766281934484553608L;

    private Date now = new Date();

    private Div win;

    @Override
    public void doAfterCompose(Component comp) throws Exception
    {
        super.doAfterCompose(comp);
        win = (Div) comp;
        showTimePlot();
    }

    public void showTimePlot()
    {
//        final SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Timeplot relatedPlot = new Timeplot();
        relatedPlot.setHeight("300px");
        relatedPlot.setWidth("90%");

        Plotinfo plot1 = new Plotinfo();
        ValueGeometry vg = new DefaultValueGeometry();
        vg.setGridColor("red");
        vg.setAxisLabelsPlacement("left");

        TimeGeometry tg = new DefaultTimeGeometry();
        tg.setAxisLabelsPlacement("bottom");
        tg.setGridColor("red");
        plot1.setValueGeometry(vg);
        plot1.setTimeGeometry(tg);
        plot1.setShowValues(true);
        
        plot1.addEventListener(OverPlotEvent.ON_OVER_PLOTDATA, new EventListener() {
			
			@Override
			public void onEvent(Event event) throws Exception {
				OverPlotEvent evt = (OverPlotEvent) event;
				Plotinfo plot = (Plotinfo) evt.getTarget();
				
				System.out.println(evt.getTime());
				System.out.println(evt.getValue());
				System.out.println(evt.getCustomValues());
				ListModelList model = (ListModelList) plot.getDataModel();
				System.out.println(model.get(evt.getPlotDataIndex()));
				System.out.println();
			}
		});

        List<Float> records = new ArrayList<Float>();

        for (int i = 0; i < 10; i++)
        {
            records.add(i + 0f);
        }
        ListModelList model = new ListModelList();
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);
        for (int j = 0; j < 10; j++)
        {
            PlotData data = new PlotData();
            Map _customValues = new HashMap();
            _customValues.put("reliability", j);
			data.setCustomValues(_customValues );
			
			
            cal.add(Calendar.DATE, j);
            data.setFormat("yyyy/MM/dd HH:mm:ss");
            data.setTime(cal.getTime());
//            data.setReliability(j);
            data.setValue(records.get(j));
            model.add(data);
        }

        plot1.setDataModel(model);
        plot1.setEventSourceUri("");
//        plot1(true);
        plot1.setRoundValues(false);
        plot1.setDotColor("#000000");
        plot1.setFillColor("#70A8E5");// E2F6F2
        plot1.setLineColor("red");

        plot1.setParent(relatedPlot);
        relatedPlot.setParent(win);
    }
}
