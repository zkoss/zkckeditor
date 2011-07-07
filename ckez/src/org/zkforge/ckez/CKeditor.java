/* CKeditor.java

 {{IS_NOTE
 Purpose:
 
 Description:
 
 History:
 Tue Oct 7 17:56:45     2009, Created by jimmyshiau
 }}IS_NOTE

 Copyright (C) 2009 Potix Corporation. All Rights Reserved.

 {{IS_RIGHT
 }}IS_RIGHT
 */
package org.zkforge.ckez;


import org.zkoss.lang.Objects;
import org.zkoss.lang.Strings;

import org.zkoss.zk.ui.AbstractComponent;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.UiException;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.InputEvent;

/**
 * The component used to represent <a href="http://ckeditor.com/">CKEditor</a>
 * 
 * @author jimmy
 * @version $Revision: 3.0 $ $Date: 2009/10/7 17:56:45 $
 */
public class CKeditor extends AbstractComponent {
	private String _value = "";

	private String _width = "100%";

	private String _height = "200px";

	private String _toolbar;
	
	private String _customPath;
	
	private String _filebrowserBrowseUrl;
	
	private String _filebrowserImageBrowseUrl;
	
	private String _filebrowserFlashBrowseUrl;

	/** Used by setTextByClient() to disable sending back the value */
	private String _txtByClient;

	private String _hflex;

	private String _vflex;

	private boolean _autoHeight;
	
	static {
		addClientEvent(CKeditor.class, Events.ON_CHANGE, CE_IMPORTANT|CE_REPEAT_IGNORE);
		addClientEvent(CKeditor.class, Events.ON_CHANGING, CE_BUSY_IGNORE);
		addClientEvent(CKeditor.class, "onSave", CE_IMPORTANT|CE_REPEAT_IGNORE);
	}
	/**
	 * Returns the value in this CKeditor.
	 */
	public String getValue() {
		return _value;
	}

	/**
	 * Sets the value for this CKeditor.
	 */
	public void setValue(String value) {
		if (value == null)
			value = "";
		if (!value.equals(_value)) {
			_value = value;
			if (_txtByClient == null || !Objects.equals(_txtByClient, value)) {
				_txtByClient = null;
				smartUpdate("value", value);
			}
		}
	}

	/**
	 * Returns the toolbar set of this CKeditor.
	 * <p>Default: null
	 */
	public String getToolbar() {
		return _toolbar;
	}
	/**
	 * Sets the toolbar set for this CKeditor.
	 */
	public void setToolbar(String toolbar) {
		if (toolbar != null && toolbar.length() == 0)
			toolbar = null;
		if (!Objects.equals(toolbar, _toolbar)) {
			_toolbar = toolbar;
			smartUpdate("toolbar", toolbar);
		}
	}	

	/**
	 * Returns the width of this CKeditor.
	 * <p>
	 * Default: 100%
	 */
	public String getWidth() {
		return _width;
	}

	/**
	 * Sets the width of this CKeditor.
	 */
	public void setWidth(String width) {
		if (width != null && width.length() == 0)
			width = null;
		if (!Objects.equals(width, _width)) {
			_width = width;
			smartUpdate("width", width);
		}
	}

	/**
	 * Returns the height of this CKeditor.
	 * <p>
	 * Default: 200px
	 */
	public String getHeight() {
		return _height;
	}

	/**
	 * Sets the height of this CKeditor.
	 */
	public void setHeight(String height) {
		if (height != null && height.length() == 0)
			height = null;
		if (!Objects.equals(height, _height)) {
			_height = height;
			smartUpdate("height", height);
		}
	}

	/**
	 * Sets vertical flexibility hint of this component. 
	 * <p>Number flex indicates how 
	 * this component's container distributes remaining empty space among its 
	 * children vertically. Flexible component grow and shrink to fit their 
	 * given space. Flexible components with larger flex values will be made 
	 * larger than components with lower flex values, at the ratio determined by 
	 * all flexible components. The actual flex value is not relevant unless 
	 * there are other flexible components within the same container. Once the 
	 * default sizes of components in a container are calculated, the remaining 
	 * space in the container is divided among the flexible components, 
	 * according to their flex ratios.</p>
	 * <p>Only positive integer allowed for ckeditor hflex value,
	 * other value make no effect of it.</p>
	 * 
	 * @param flex the vertical flex hint.
	 * @since 3.6.0
	 * @see #setHflex
	 * @see #getVflex 
	 */
	public void setVflex(String flex) {
		if (flex != null && flex.length() == 0)
			flex = null;
		if (!Objects.equals(_vflex, flex)) {
			_vflex = flex;
			smartUpdate("vflex", flex);
		}
	}
	/**
	 * Return vertical flex hint of this component.
	 * <p>Default: null
	 * @return vertical flex hint of this component.
	 * @since 3.6.0
	 * @see #setVflex 
	 */
	public String getVflex() {
		return _vflex;
	}
	/**
	 * Sets horizontal flex hint of this component.
	 * <p>Number flex indicates how 
	 * this component's container distributes remaining empty space among its 
	 * children horizontally. Flexible component grow and shrink to fit their 
	 * given space. Flexible components with larger flex values will be made 
	 * larger than components with lower flex values, at the ratio determined by 
	 * all flexible components. The actual flex value is not relevant unless 
	 * there are other flexible components within the same container. Once the 
	 * default sizes of components in a container are calculated, the remaining 
	 * space in the container is divided among the flexible components, 
	 * according to their flex ratios.</p>
	 * <p>Only positive integer allowed for ckeditor vflex value,
	 * other value make no effect of it.</p> 
	 * @param flex horizontal flex hint of this component.
	 * @since 3.6.0 
	 * @see #setVflex
	 * @see #getHflex 
	 */
	public void setHflex(String flex) {
		if (flex != null && flex.length() == 0)
			flex = null;
		if (!Objects.equals(_hflex, flex)) {
			_hflex = flex;
			smartUpdate("hflex", flex);
		}
	}
	
	/**
	 * Returns horizontal flex hint of this component.
	 * <p>Default: null
	 * @return horizontal flex hint of this component.
	 * @since 3.6.0
	 * @see #setHflex 
	 */
	public String getHflex() {
		return _hflex;
	}
	/**
	 * Returns whether enable to automatically grow the height of the component or not.
	 * <p> Default: false.
	 */
	public boolean isAutoHeight() {
		return _autoHeight;
	}

	/**
	 * Sets whether enable to automatically grow the height of the component or not.
	 * @param autoHeight
	 */
	public void setAutoHeight(boolean autoHeight) {
		if (_autoHeight != autoHeight) {
			_autoHeight = autoHeight;
			smartUpdate("autoHeight", _autoHeight);
		}
	};

	

	/**
	 * Set the url of the custom configuration .js file. See CKeditor's 
	 * <a href="http://wiki.fckeditor.net/Developer%27s_Guide/Configuration/Configurations_File">
	 * Configurtions File</a> for details.
	 * @param url the url path for the customized configuration path
	 */
	public void setCustomConfigurationsPath(String url) {
		if (!Objects.equals(_customPath, url)) {
			_customPath = url;
			smartUpdate("customConfigurationsPath", new EncodedURL(_customPath));	
		}
	}

	/**
	 * Get the url of the custom configuration .js file. See CKeditor's 
	 * <a href="http://wiki.fckeditor.net/Developer%27s_Guide/Configuration/Configurations_File">
	 * Configurtions File</a> for details.
	 */
	public String getCustomConfigurationsPath() {
		return _customPath;
	}
	
	/**
	 * Set the location of an external file browser, that should be launched when "Browse Server" button is pressed.
	 * @param filebrowserBrowseUrl
	 */
	public void setFilebrowserBrowseUrl(String filebrowserBrowseUrl) {
		boolean isSetted = false;
		if (!Objects.equals(_filebrowserBrowseUrl, filebrowserBrowseUrl)) {
			this._filebrowserBrowseUrl = filebrowserBrowseUrl;
			String url = String.valueOf(new EncodedURL(filebrowserBrowseUrl).getValue());
			Executions.getCurrent().getSession().setAttribute("filebrowserBrowseUrl"+this.getUuid(), url);
			isSetted = true;
		}
		smartUpdate("filebrowserBrowseUrl", isSetted);//won't send the url info to client
	}

	/**
	 * Get the location of an external file browser, that should be launched when "Browse Server" button is pressed.
	 * @return String
	 */
	public String getFilebrowserBrowseUrl() {
		return _filebrowserBrowseUrl;
	}
	
	/**
	 * Set the location of an external file browser, that should be launched when "Browse Server" button is pressed in the Image dialog.
	 * @param filebrowserImageBrowseUrl
	 */
	public void setFilebrowserImageBrowseUrl(String filebrowserImageBrowseUrl) {
		boolean isSetted = false;
		if (!Objects.equals(_filebrowserImageBrowseUrl, filebrowserImageBrowseUrl)) {
			this._filebrowserImageBrowseUrl = filebrowserImageBrowseUrl;
			String url = String.valueOf(new EncodedURL(filebrowserImageBrowseUrl).getValue());
			Executions.getCurrent().getSession().setAttribute("filebrowserImageBrowseUrl"+this.getUuid(),url);
			isSetted = true;
		}
		smartUpdate("filebrowserImageBrowseUrl", isSetted);//won't send the url info to client
	}

	/**
	 * Get the location of an external file browser, that should be launched when "Browse Server" button is pressed in the Image dialog.
	 * @return
	 */
	public String getFilebrowserImageBrowseUrl() {
		return _filebrowserImageBrowseUrl;
	}

	/**
	 * Set the location of an external file browser, that should be launched when "Browse Server" button is pressed in the Flash dialog.
	 * @param filebrowserFlashBrowseUrl
	 */
	public void setFilebrowserFlashBrowseUrl(String filebrowserFlashBrowseUrl) {
		boolean isSetted = false;
		if (!Objects.equals(_filebrowserFlashBrowseUrl, filebrowserFlashBrowseUrl)) {
			this._filebrowserFlashBrowseUrl = filebrowserFlashBrowseUrl;
			String url = String.valueOf(new EncodedURL(filebrowserFlashBrowseUrl).getValue());
			Executions.getCurrent().getSession().setAttribute("filebrowserFlashBrowseUrl"+this.getUuid(), url);
			isSetted = true;
		}
		smartUpdate("filebrowserFlashBrowseUrl", isSetted);//won't send the url info to client
	}

	/**
	 * Get the location of an external file browser, that should be launched when "Browse Server" button is pressed in the Flash dialog.
	 * @return
	 */
	public String getFilebrowserFlashBrowseUrl() {
		return _filebrowserFlashBrowseUrl;
	}
	
	private String getEncodedURL(String path) {
		  final Desktop dt = getDesktop(); //it might not belong to any desktop
		  return dt != null ? dt.getExecution().encodeURL(path): "";			 
	}
	
	public void service(org.zkoss.zk.au.AuRequest request, boolean everError) {
		final String cmd = request.getCommand();
		if (cmd.equals(Events.ON_CHANGE)) {
			InputEvent evt = InputEvent.getInputEvent(request);
			final String value = evt.getValue();
			_txtByClient = value;
			try {
				final Object oldval = _value;
				setValue(value); //always since it might have func even not change
				if (oldval == _value)
					return; //Bug 1881557: don't post event if not modified
			} finally {
				_txtByClient = null;
			}
			Events.postEvent(evt);
		} else if (cmd.equals("onSave")) {
			InputEvent evt = InputEvent.getInputEvent(request);
			setValue(evt.getValue()); 
			Events.postEvent(evt);
		} else if (cmd.equals(Events.ON_CHANGING)) {
			Events.postEvent(InputEvent.getInputEvent(request));
		} else
			super.service(request, everError);
	}
	
	protected void renderProperties(org.zkoss.zk.ui.sys.ContentRenderer renderer)
	throws java.io.IOException {
		super.renderProperties(renderer);

		render(renderer, "value", _value);
		if (!"100%".equals(_width))
			render(renderer, "width", _width);
		if (!"200px".equals(_height))
			render(renderer, "height", _height);

		render(renderer, "customConfigurationsPath", getEncodedURL(_customPath));
		render(renderer, "toolbar", _toolbar);
		render(renderer, "autoHeight", _autoHeight);
		if (!Strings.isBlank(_filebrowserBrowseUrl))
			render(renderer, "filebrowserBrowseUrl", true);
		if (!Strings.isBlank(_filebrowserImageBrowseUrl))
			render(renderer, "filebrowserImageBrowseUrl", true);
		if (!Strings.isBlank(_filebrowserFlashBrowseUrl))
			render(renderer, "filebrowserFlashBrowseUrl", true);
		if (_hflex != null)
			render(renderer, "hflex", _hflex);
		if (_vflex != null)
			render(renderer, "vflex", _vflex);
	}
	
	private class EncodedURL implements org.zkoss.zk.ui.util.DeferredValue {
		private String path;
		public EncodedURL(String path) {
			this.path = path;
		}
		public Object getValue() {
			return getEncodedURL(path);
		}
	}
	// -- Super --//
	/** Not childable. */
	public boolean isChildable() {
		return false;
	}
}
