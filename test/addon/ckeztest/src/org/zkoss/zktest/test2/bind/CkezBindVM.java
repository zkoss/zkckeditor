package org.zkoss.zktest.test2.bind;


public class CkezBindVM {
	private String _value = "mvvm test";
	public String getWidth () {
		return "700px";
	}
	public String getHeight () {
		return "100px";
	}
	public String getValue() {
		return _value;
	}
	public void setValue(String value) {
		_value = (String)value;
	}
	public String getIdone() {
		return "idOne";
	}
	public String getConfigPath () {
		return "/test2/data/config.js";
	}
}
