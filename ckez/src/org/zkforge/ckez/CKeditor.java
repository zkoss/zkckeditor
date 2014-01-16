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


import java.io.File;
import java.util.Map;

import org.apache.commons.fileupload.FileItem;
import org.zkoss.lang.Objects;
import org.zkoss.lang.Strings;
import org.zkoss.zk.ui.AbstractComponent;
import org.zkoss.zk.ui.Desktop;
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
	private static String _fileBrowserTempl = "~./ckez/html/browse.zul";
	private static String _fileUploadHandlePage = "";
	
	private String _value = "";

	private String _width = "100%";

	private String _height = "200px";

	private String _toolbar;
	
	private String _customPath;
	
	private Map<String, Object> _config;
		
	private String _filebrowserBrowseUrl;
	
	private String _filebrowserImageBrowseUrl;
	
	private String _filebrowserFlashBrowseUrl;
	
	private String _filebrowserUploadUrl;
	
	private String _filebrowserImageUploadUrl;
	
	private String _filebrowserFlashUploadUrl;
	
	private CkezFileWriter fileWriter;

	/** Used by setTextByClient() to disable sending back the value */
	private String _txtByClient;

	private String _hflex;

	private String _vflex;

	private boolean _autoHeight;
	
	static {
		// Issue 20: Sends every onChange event when ckeditor sends onChange command
		addClientEvent(CKeditor.class, Events.ON_CHANGE, CE_IMPORTANT|CE_REPEAT_IGNORE|CE_NON_DEFERRABLE);
		addClientEvent(CKeditor.class, Events.ON_CHANGING, CE_BUSY_IGNORE|CE_NON_DEFERRABLE);
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
	 * Set the url of the custom configuration .js file. See  
	 * <a href="http://docs.ckeditor.com/#!/api/CKEDITOR.config">
	 * CKEDITOR.config</a> for available configuration options.
	 * @param url the url path for the customized configuration path
	 */
	public void setCustomConfigurationsPath(String url) {
		if (!Objects.equals(_customPath, url)) {
			_customPath = url;
			smartUpdate("customConfigurationsPath", getEncodedURL(_customPath));	
		}
	}

	/**
	 * Get the url of the custom configuration .js file. See  
	 * <a href="http://docs.ckeditor.com/#!/api/CKEDITOR.config">
	 * CKEDITOR.config</a> for available configuration options.
	 */
	public String getCustomConfigurationsPath() {
		return _customPath;
	}

	/**
	 * Set the configuration for instance creation. See 
	 * <a href="http://docs.ckeditor.com/#!/api/CKEDITOR.config">
	 * CKEDITOR.config</a> for available configuration options.
	 * 
	 * @param config - a map for describing the configuration 
	 */
	public void setConfig(Map<String, Object> config) {
		if (config != null) {
			_config = config;
			smartUpdate("config", _config);
		}
	}

	/**
	 * Get the configuration for instance creation. See 
	 * <a href="http://docs.ckeditor.com/#!/api/CKEDITOR.config">
	 * CKEDITOR.config</a> for available configuration options.
	 */
	public Map<String, Object> getConfig() {
		return _config;
	}
	
	/**
	 * Set the location of the folder for the file browser, that should be launched 
	 * when "Browse Server" button is pressed.
	 * If configured, the Browse Server button will appear in the Link, Image, and Flash dialog windows. 
	 * @param filebrowserBrowseUrl
	 */
	public void setFilebrowserBrowseUrl(String filebrowserBrowseUrl) {
		if (!Objects.equals(_filebrowserBrowseUrl, filebrowserBrowseUrl)) {
			this._filebrowserBrowseUrl = filebrowserBrowseUrl;
			smartUpdate("filebrowserBrowseUrl", filebrowserBrowseUrl);
		}
	}

	/**
	 * Get the location of the folder for the file browser, that should be launched 
	 * when "Browse Server" button is pressed.
	 * If configured, the Browse Server button will appear in the Link, Image, and Flash dialog windows. 
	 * 
	 * @return String
	 */
	public String getFilebrowserBrowseUrl() {
		return _filebrowserBrowseUrl;
	}
	
	/**
	 * Set the location of the folder for the file browser, that should be launched 
	 * when "Browse Server" button is pressed in the Image dialog.
	 * If not set, CKEditor will use {@link #getFilebrowserBrowseUrl()}. 
	 * @param filebrowserImageBrowseUrl
	 */
	public void setFilebrowserImageBrowseUrl(String filebrowserImageBrowseUrl) {
		if (!Objects.equals(_filebrowserImageBrowseUrl, filebrowserImageBrowseUrl)) {
			this._filebrowserImageBrowseUrl = filebrowserImageBrowseUrl;
			smartUpdate("filebrowserImageBrowseUrl", filebrowserImageBrowseUrl);
		}
	}

	/**
	 * Get the location of the folder for the file browser, that should be launched 
	 * when "Browse Server" button is pressed in the Image dialog.
	 * @return getFilebrowserImageBrowseUrl
	 */
	public String getFilebrowserImageBrowseUrl() {
		return _filebrowserImageBrowseUrl;
	}

	/**
	 * Set the location of the folder for the file browser, that should be launched 
	 * when "Browse Server" button is pressed in the Flash dialog.
	 * If not set, CKEditor will use {@link #getFilebrowserBrowseUrl()}. 
	 * @param filebrowserFlashBrowseUrl
	 */
	public void setFilebrowserFlashBrowseUrl(String filebrowserFlashBrowseUrl) {
		if (!Objects.equals(_filebrowserFlashBrowseUrl, filebrowserFlashBrowseUrl)) {
			this._filebrowserFlashBrowseUrl = filebrowserFlashBrowseUrl;
			smartUpdate("filebrowserFlashBrowseUrl", filebrowserFlashBrowseUrl);
		}
	}

	/**
	 * Get the location of the folder for the file browser, that should be launched 
	 * when "Browse Server" button is pressed in the Flash dialog.
	 * @return filebrowserFlashBrowseUrl
	 */
	public String getFilebrowserFlashBrowseUrl() {
		return _filebrowserFlashBrowseUrl;
	}
	
	
	/**
	 * Get the location of the folder for the file uploads. 
	 * If set, the Upload tab will appear in the Link, Image, and Flash dialog windows. 
	 * @return filebrowserUploadUrl
	 */
	public String getFilebrowserUploadUrl() {
		return _filebrowserUploadUrl;
	}

	/**
	 * Set the location of the folder for the file uploads. 
	 * If set, the Upload tab will appear in the Link, Image, and Flash dialog windows.
	 * @param filebrowserUploadUrl
	 */
	public void setFilebrowserUploadUrl(String filebrowserUploadUrl) {
		if (!Objects.equals(_filebrowserUploadUrl, filebrowserUploadUrl)) {
			this._filebrowserUploadUrl = filebrowserUploadUrl;
			smartUpdate("filebrowserUploadUrl", filebrowserUploadUrl);
		}
	}

	/**
	 * Get the location of the folder for the file uploads in the Image dialog window. 
	 * If not set, CKEditor will use {@link #getFilebrowserUploadUrl()}. 
	 * @return filebrowserImageUploadUrl
	 */
	public String getFilebrowserImageUploadUrl() {
		return _filebrowserImageUploadUrl;
	}

	/**
	 * Set the location of the folder for the file uploads in the Image dialog window. 
	 * If not set, CKEditor will use {@link #getFilebrowserUploadUrl()}. 
	 * @param filebrowserImageUploadUrl
	 */
	public void setFilebrowserImageUploadUrl(String filebrowserImageUploadUrl) {
		if (!Objects.equals(_filebrowserImageUploadUrl, filebrowserImageUploadUrl)) {
			this._filebrowserImageUploadUrl = filebrowserImageUploadUrl;
			smartUpdate("filebrowserImageUploadUrl", filebrowserImageUploadUrl);
		}
	}

	/**
	 * Get the location of the script that handles file uploads in the Flash dialog window. 
	 * If not set, CKEditor will use {@link #getFilebrowserUploadUrl()}. 
	 * @return filebrowserFlashUploadUrl
	 */
	public String getFilebrowserFlashUploadUrl() {
		return _filebrowserFlashUploadUrl;
	}

	/**
	 * Set the location of the script that handles file uploads in the Flash dialog window. 
	 * If not set, CKEditor will use {@link #getFilebrowserUploadUrl()}. 
	 * @param filebrowserFlashUploadUrl
	 */
	public void setFilebrowserFlashUploadUrl(String filebrowserFlashUploadUrl) {
		if (!Objects.equals(_filebrowserFlashUploadUrl, filebrowserFlashUploadUrl)) {
			this._filebrowserFlashUploadUrl = filebrowserFlashUploadUrl;
			smartUpdate("filebrowserFlashUploadUrl", filebrowserFlashUploadUrl);
		}
	}

	private String getEncodedURL(String path) {
		  final Desktop dt = getDesktop(); //it might not belong to any desktop
		  return dt != null ? dt.getExecution().encodeURL(path): "";			 
	}
	
	public CkezFileWriter getFileWriter() {
		return fileWriter;
	}

	public void setFileWriter(CkezFileWriter fileWriter) {
		this.fileWriter = fileWriter;
	}
	
	private static final CkezFileWriter _defWriter = new CkezFileWriter() {

		public String writeFileItem(String uploadUrl, String realPath,
				FileItem item, String type) {

			String fileName = item.getName();
			File file = new File(realPath + "/" + fileName);
			
			if (!file.getParentFile().exists())
				throw new UiException("Folder not found: " + realPath);
			try {
				item.write(file);
			} catch (Exception e) {
				e.printStackTrace();
			}

			return uploadUrl + "/" + fileName;
		}
	};
	
	
	
	/**/ String writeFileItem(String uploadUrl, String realPath, FileItem item, String type) {
		return (fileWriter != null ? fileWriter: _defWriter)
			.writeFileItem(uploadUrl, realPath, item, type);
	}

	public void service(org.zkoss.zk.au.AuRequest request, boolean everError) {
		final String cmd = request.getCommand();
		if (cmd.equals(Events.ON_CHANGE)) {
			InputEvent evt = InputEvent.getInputEvent(request, _value);
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
			InputEvent evt = InputEvent.getInputEvent(request, _value);
			setValue(evt.getValue()); 
			Events.postEvent(evt);
		} else if (cmd.equals(Events.ON_CHANGING)) {
			Events.postEvent(InputEvent.getInputEvent(request, _value));
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
		render(renderer, "config", _config);
		
		render(renderer, "toolbar", _toolbar);
		render(renderer, "autoHeight", _autoHeight);
		
		if (!Strings.isBlank(_filebrowserBrowseUrl))
			render(renderer, "filebrowserBrowseUrl", _filebrowserBrowseUrl);
		if (!Strings.isBlank(_filebrowserImageBrowseUrl))
			render(renderer, "filebrowserImageBrowseUrl", _filebrowserImageBrowseUrl);
		if (!Strings.isBlank(_filebrowserFlashBrowseUrl))
			render(renderer, "filebrowserFlashBrowseUrl", _filebrowserFlashBrowseUrl);
		
		if (!Strings.isBlank(_filebrowserUploadUrl))
			render(renderer, "filebrowserUploadUrl", _filebrowserUploadUrl);
		if (!Strings.isBlank(_filebrowserImageUploadUrl))
			render(renderer, "filebrowserImageUploadUrl", _filebrowserImageUploadUrl);
		if (!Strings.isBlank(_filebrowserFlashUploadUrl))
			render(renderer, "filebrowserFlashUploadUrl", _filebrowserFlashUploadUrl);
		
		if (_hflex != null)
			render(renderer, "hflex", _hflex);
		if (_vflex != null)
			render(renderer, "vflex", _vflex);
		render(renderer, "fileBrowserTempl", getEncodedURL(_fileBrowserTempl));
		render(renderer, "fileUploadTempl", getEncodedURL(_fileUploadHandlePage));
		
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
	
	/** Sets the template used to create the file browser dialog.
	 *
	 * <p>The template must follow the default template:
	 * ~./ckez/html/browse.zul
	 *
	 * <p>In other words, just adjust the label and layout and don't
	 * change the component's ID.
	 *
	 * <p>Note: the template has no effect, ifds
	 */
	public static void setFileBrowserTemplate(String uri) {
		if (uri == null || uri.length() == 0)
			throw new IllegalArgumentException("empty");
		_fileBrowserTempl = uri;
	}
	/** Returns the template used to create the file browser dialog.
	 */
	public static String getFileBrowserTemplate() {
		return _fileBrowserTempl;
	}
	
	/** Sets the location of the script that handles file uploads.
	 */
	public static void setFileUploadHandlePage(String uri) {
		if (uri == null || uri.length() == 0)
			throw new IllegalArgumentException("empty");
		_fileUploadHandlePage = uri;
	}
	/** Returnsthe location of the script that handles file uploads.
	 */
	public static String getFileUploadHandlePage() {
		return _fileUploadHandlePage;
	}
}
