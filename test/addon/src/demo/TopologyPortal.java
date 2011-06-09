package demo;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkmax.zul.api.Portalchildren;
import org.zkoss.zul.Button;
import org.zkoss.zul.Panel;

public class TopologyPortal  extends GenericForwardComposer
{
    private static final long serialVersionUID = 6787049834402697480L;

    private Portalchildren left = null;

    private Portalchildren right = null;

    private Portalchildren left1;// = null;

    private Portalchildren right1;// = null;

    Button btn;
    Button btn2;
    private Set<Integer> portletKeySet = new HashSet<Integer>();

    @SuppressWarnings("unchecked")
    @Override
    public void doAfterCompose(Component comp) throws Exception
    {
        super.doAfterCompose(comp);
        bindComponent(comp);
        final Map params = comp.getDesktop().getExecution().getArg();        
        createPortlet(params);
        btn.addEventListener("onClick", new EventListener() {

            @Override
            public void onEvent(Event event) throws Exception
            {
                createPortlet(params);
            }
            
        });
        btn2.addEventListener("onClick", new EventListener() {

            @Override
            public void onEvent(Event event) throws Exception
            {
                createPortlet2(params);
            }
            
        });
    }

    @SuppressWarnings("unchecked")
    private void createPortlet(Map params)
    {
        final Panel topoPanel = (Panel)execution.createComponents("/topoPortlet.zul", null, params);
       
        topoPanel.addEventListener("onClose", new EventListener()
        {
            @Override
            public void onEvent(Event arg0) throws Exception
            {
                portletKeySet.remove(topoPanel.getAttribute("portletKey"));
            }
        });
        
        int leftChildCounter = left.getChildren().size();
        int rightChildCounter = right.getChildren().size();
        
        if (leftChildCounter <= rightChildCounter)
        {
            topoPanel.setParent(left);
        }
        else
        {
            topoPanel.setParent(right);
        }

    }
    
    
    @SuppressWarnings("unchecked")
    private void createPortlet2(Map params)
    {
        final Panel topoPanel = (Panel)execution.createComponents("/topoPortlet.zul", null, params);
       
        topoPanel.addEventListener("onClose", new EventListener()
        {
            @Override
            public void onEvent(Event arg0) throws Exception
            {
                portletKeySet.remove(topoPanel.getAttribute("portletKey"));
            }
        });
        
        int leftChildCounter1 = left1.getChildren().size();
        int rightChildCounter1 = right1.getChildren().size();
        
        if (leftChildCounter1 <= rightChildCounter1)
        {
            topoPanel.setParent(left1);
        }
        else
        {
            topoPanel.setParent(right1);
        }
    }
  
}