package org.zkoss.zktest.test2;

import org.zkforge.ckez.CKeditor;
import org.zkoss.xml.XMLs;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Window;

public class B_ZKCK_12_Composer extends SelectorComposer<Window> {
//	@Wire
//    private CKeditor editor;
//    public void doAfterCompose(Window comp) throws Exception {
//        super.doAfterCompose(comp);
//        editor.setValue("&amp;lt;AmpTag&amp;gt;Amp tag content&amp;lt;/AmpTag&amp;gt; &lt;br/&gt; &lt;escapetag&gt;escapetag content&lt;/escapetag&gt; &lt;br/&gt; &lt; space-tag &gt; space tag content &lt; /space-tag  &gt;");          
//    }
//
//    @Listen("onClick=#btn1")
//    public void onClick$btn1(Event e) {
//        editor.setValue("");
//    }
//
//    @Listen("onClick=#btn2")
//    public void onClick$btn2(Event e) {
//        editor.setValue("&amp;lt;AmpTag&amp;gt;Amp tag content&amp;lt;/AmpTag&amp;gt; &lt;br/&gt; &lt;escapetag&gt;escapetag content&lt;/escapetag&gt; &lt;br/&gt; &lt; space-tag &gt; space tag content &lt; /space-tag  &gt;");
//    }
//    
//    @Listen("onClick=#btn3")
//    public void onClick$btn3(Event e) {
//    	System.out.println(editor.getValue());
//    }  
	
	@Wire
	private CKeditor editor;
	private CKeditor cKeditor3;

	public void doAfterCompose(Window comp) throws Exception {
		super.doAfterCompose(comp);
		editor.setValue(doubleEscape("<tag>value</tag>") + "<br/>" + doubleEscape("<tag2>value tag2</tag2>"));
	}
	
	private String doubleEscape(String text) {
		return XMLs.escapeXML(XMLs.escapeXML(text));
	}
	
	@Listen("onClick=#btn1")
	public void click1() {
		Window window2 = (Window) editor.getParent().getFellow("window2");
		window2.setVisible(true);
		window2.doOverlapped();
		
		cKeditor3 = new CKeditor();
		Window window3 = new Window();
		window3.setParent(window2.getParent());
		window3.appendChild(cKeditor3);

		window3.setClosable(true);

		window3.setPosition("center");
		window3.setTitle("Message!!!");
		window3.doOverlapped();
	}
	
	@Listen("onClick=#btn2")
	public void click2() {
		Window window2 = (Window) editor.getParent().getFellow("window2");
		CKeditor cKeditor2 = (CKeditor) window2.getFellow("editor2");

		String s1 = "<html222>";
		String s2 = XMLs.escapeXML(s1);
		String s3 = XMLs.escapeXML(s2);
		editor.setValue(s3);
		cKeditor2.setValue(s3);
		cKeditor3.setValue(s3);
	}
	
	@Listen("onClick=#btn3")
	public void click3() {
		Window window2 = (Window) editor.getParent().getFellow("window2");
		CKeditor cKeditor2 = (CKeditor) window2.getFellow("editor2");

//		System.out.println("Before Invalidate");
		System.out.println("editor1: " + editor.getValue());
		System.out.println("editor2: " + cKeditor2.getValue());
		System.out.println("editor3: " + cKeditor3.getValue());
		editor.invalidate();
//		cKeditor3.invalidate();
//		cKeditor2.invalidate();
	}
	
	@Listen("onClick=#btn4")
	public void click4() {
		Window window2 = (Window) editor.getParent().getFellow("window2");
		CKeditor cKeditor2 = (CKeditor) window2.getFellow("editor2");

//		System.out.println("After Invalidate");
		System.out.println("editor1: " + editor.getValue());
		System.out.println("editor2: " + cKeditor2.getValue());
		System.out.println("editor3: " + cKeditor3.getValue());
	}
}
