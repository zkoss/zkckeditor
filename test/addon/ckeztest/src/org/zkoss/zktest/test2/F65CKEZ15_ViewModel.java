package org.zkoss.zktest.test2;

import java.util.HashMap;
import java.util.Map;

public class F65CKEZ15_ViewModel {
	
	public Map<String, Object> getConfig1() {
		Map<String, Object> config =
			new HashMap<String, Object>();
		
		config.put("toolbar", new String[][] {
			{ "Source", "-", "Save", "NewPage", "Preview", "-", "Templates" }
		});
		return config;
	}
	
	public Map<String, Object> getConfig2() {
		Map<String, Object> config =
			new HashMap<String, Object>();

		config.put("toolbar", new String[][] {
			{	"Bold", "Italic", "Underline", "Strike", "-", "Subscript", "Superscript" }	
		});
		return config;
	}
}
