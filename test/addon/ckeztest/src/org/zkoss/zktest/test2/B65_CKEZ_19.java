package org.zkoss.zktest.test2;

import org.zkforge.ckez.CKeditor;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Div;
import org.zkoss.zul.Window;

public class B65_CKEZ_19 {
	
	private Window mainWindow;
	private Div divEditor;
	private Window parent;
	
	protected CKeditor editorWriteEmail;
	
	public B65_CKEZ_19(Window _parent) {
		super();
		
		parent = _parent;
		
		createComponents();
	}

	private void createComponents() {
		mainWindow = new Window();
		mainWindow.setParent(parent);
		mainWindow.setClosable(true);
		mainWindow.setTitle("mainWindow");
		mainWindow.setHeight("50%");
		mainWindow.setWidth("50%");
		mainWindow.addEventListener(Events.ON_CLOSE, new OnCloseListener());
		
		divEditor = new Div();
		divEditor.setParent(mainWindow);
		
		setupEditor();
		
		mainWindow.doHighlighted();
	}
	
	protected void setupEditor() {
		this.editorWriteEmail = new CKeditor();
		this.editorWriteEmail.setParent(this.divEditor);
		
		this.editorWriteEmail.setHeight("100%");
		this.editorWriteEmail.setWidth("100%");
		this.editorWriteEmail.setAutoHeight(false);
	}
	
	public void doClose(Event event) {
		if (this.editorWriteEmail != null) {
			System.out.println("1");
		}
		
		if (this.editorWriteEmail != null) {
			System.out.println("2");
			this.mainWindow.onClose();
		}
		
		System.out.println("Controller has been closed\n\n\n");
	}
	
	private final class OnCloseListener implements EventListener<Event> {

		@Override
        public void onEvent(Event event) throws Exception {
	        doClose(event);
        }
		
	}

}
