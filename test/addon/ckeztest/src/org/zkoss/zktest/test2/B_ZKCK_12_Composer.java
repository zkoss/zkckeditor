package org.zkoss.zktest.test2;

import org.zkforge.ckez.CKeditor;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Window;

public class B_ZKCK_12_Composer extends SelectorComposer<Window> {
	@Wire
    private CKeditor editor;
    public void doAfterCompose(Window comp) throws Exception {
        super.doAfterCompose(comp);
        editor.setValue("&amp;lt;AmpTag&amp;gt;Amp tag content&amp;lt;/AmpTag&amp;gt; &lt;br/&gt; &lt;escapetag&gt;escapetag content&lt;/escapetag&gt; &lt;br/&gt; &lt; space-tag &gt; space tag content &lt; /space-tag  &gt;");          
    }

    @Listen("onClick=#btn1")
    public void onClick$btn1(Event e) {
        editor.setValue("");
    }

    @Listen("onClick=#btn2")
    public void onClick$btn2(Event e) {
        editor.setValue("&amp;lt;AmpTag&amp;gt;Amp tag content&amp;lt;/AmpTag&amp;gt; &lt;br/&gt; &lt;escapetag&gt;escapetag content&lt;/escapetag&gt; &lt;br/&gt; &lt; space-tag &gt; space tag content &lt; /space-tag  &gt;");
    }
}
