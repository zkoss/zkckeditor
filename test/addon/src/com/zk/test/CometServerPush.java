package com.zk.test;

import java.awt.Color;
import java.awt.geom.Ellipse2D;
import java.util.Date;

import org.zkforge.timeplot.Plotinfo;
import org.zkoss.lang.Threads;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.DesktopUnavailableException;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Image;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;

import com.zk.test.timeplot.TimeplotData;

public class CometServerPush
{

    public static void start(Plotinfo plotinfo, Component comp) throws InterruptedException
    {
        final Desktop desktop = Executions.getCurrent().getDesktop();
        if (desktop.isServerPushEnabled())
        {
            Messagebox.show("Already started");
        }
        else
        {
            desktop.enableServerPush(true);
            new WorkingThread(plotinfo, comp).start();
        }
    }

    public static void stop() throws InterruptedException
    {
        final Desktop desktop = Executions.getCurrent().getDesktop();
        if (desktop.isServerPushEnabled())
        {
            desktop.enableServerPush(false);
        }
        else
        {
            Messagebox.show("Already stopped");
        }
    }

    private static class WorkingThread extends Thread
    {
        private Plotinfo _plotinfo;

        private Long time = 1272073320169l;

        private Component _comp;
        
        private final Desktop _desktop;


        private WorkingThread(Plotinfo plotinfo, Component comp)
        {
            this._plotinfo = plotinfo;
            this._comp = comp;
            _desktop = comp.getDesktop();
        }

        public void run()
        {

            try
            {
                while (true)
                {
                    if (_comp.getDesktop() == null || !_desktop.isServerPushEnabled())
                    {
                        _desktop.enableServerPush(false);
                        return;
                    }
                    Executions.activate(_desktop);
                    try
                    {
                        ListModelList dml = (ListModelList) _plotinfo.getDataModel();
                        TimeplotData td = new TimeplotData();
//                        time = time + 1000 * 60 * 60 * 8 ;
                        time = new Date().getTime();
                        td.setTime(new Date(time));
                        td.setValue(10f);
                        dml.add(td);
                    }
                    finally
                    {
                        Executions.deactivate(_desktop);
                    }
                    Threads.sleep(10000);
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

    }
}
