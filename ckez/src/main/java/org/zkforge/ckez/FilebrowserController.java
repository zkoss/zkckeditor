/* FilebrowserController.java

 {{IS_NOTE
 Purpose:
 
 Description:
 
 History:
 Thu Nov 18 14:07:45     2010, Created by Jimmy Shiau
 }}IS_NOTE

 Copyright (C) 2009 Potix Corporation. All Rights Reserved.

 {{IS_RIGHT
 }}IS_RIGHT
 */
package org.zkforge.ckez;
/**
 * 
 * @author Jimmy Shiau
 */
import java.util.*;

import org.zkoss.lang.Strings;
import org.zkoss.zk.ui.*;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.sys.WebAppCtrl;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.DefaultTreeModel;
import org.zkoss.zul.DefaultTreeNode;
import org.zkoss.zul.Div;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.TreeitemRenderer;

public class FilebrowserController extends GenericForwardComposer {
	
	private static final String[] EXCLUDE_FOLDERS = {"WEB-INF","META-INF"};
	private static final String[] EXCLUDE_FILES = {};
	private static final String[] IMAGES = {"gif","jpg","jpeg","png"};
	private static final String[] FILES = {"htm", "html", "php", "php3", 
											"php5", "phtml", "asp", "aspx", 
											"ascx", "jsp", "cfm", "cfc", "pl", 
											"bat", "exe", "dll", "reg", "cgi", "asmx"};
	private static final String[] FLASH = {"swf"};
	private static final String[] MEDIA = {"swf", "fla", "jpg", "gif", "jpeg", "png", "avi", "mpg", "mpeg"}; 

	private static final String swfPath = "~./ckez/img/flashIcon.jpg";

	private Set<String> _fileWhiteList; // depending on type param, init later
	private Set<String> _folderBlackList = new HashSet<String>(Arrays.asList(EXCLUDE_FOLDERS));

	private Tree tree;
	private Div cntDiv;
	private Toolbarbutton selBtn;
	
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);

		final Object t = param.get("Type");
		if (t == null) return;
		final String type = ((String[]) t)[0];
		_fileWhiteList = initFileWhiteList(type);

		final String url = getFolderUrl(getUrl(param, type));
		if (Strings.isBlank(url)) return;
		
		if (application.getResourcePaths(url) == null) {
			throw new UiException("Folder not found: " + url);
		}
		
		Map rootFolderMap = new TreeMap();
		Map map = new TreeMap();
		rootFolderMap.put(url, map);
		
		parseFolders(url, map);
		
		tree.setItemRenderer(new ExplorerTreeitemRenderer());
		tree.setModel(new DefaultTreeModel(new DefaultTreeNode("ROOT", initTreeModel(rootFolderMap, new ArrayList()))));
		
		showImages(map);
	}

	private String getUrl(Map param, String type) {
		final Object d = param.get("dtid");
		if (d == null) return null;
		final String dtid = ((String[]) d)[0];

		final Object u = param.get("uuid");
		if (u == null) return null;
		final String uuid = ((String[]) u)[0];

		final Session sess = Sessions.getCurrent(false);
		if (sess == null) return null;

		final Desktop desktop = ((WebAppCtrl) sess.getWebApp()).getDesktopCache(sess).getDesktopIfAny(dtid);
		if (desktop == null) return null;

		final CKeditor ckez = (CKeditor) desktop.getComponentByUuidIfAny(uuid);
		if (ckez == null) return null;

		if ("Images".equals(type)) {
			return ckez.getFilebrowserImageBrowseUrl();
		} else if ("Flash".equals(type)) {
			return ckez.getFilebrowserFlashBrowseUrl();
		} else if ("Files".equals(type)) {
			return ckez.getFilebrowserBrowseUrl();
		} else {
			return null;
		}
	}

	private Set<String> initFileWhiteList(String type) {
		if ("Images".equals(type)) {
			return new HashSet<String>(Arrays.asList(IMAGES));
		} else if ("Flash".equals(type)) {
			return new HashSet<String>(Arrays.asList(FLASH));
		} else if ("Files".equals(type)) {
			return new HashSet<String>(Arrays.asList(FILES));
		} else {
			return new HashSet<String>();
		}
	}

	/*package*/ static String getFolderUrl(String url) {
		if (Strings.isBlank(url)) return "";
		if (url.startsWith("./"))
			url = url.substring(1);
		
		if (!url.startsWith("/"))
			url = "/" + url;
		return url;
	}

	private List initTreeModel(Map parentFolderMap, List list) {
		for (Object o : parentFolderMap.entrySet()) {
			Map.Entry entry = (Map.Entry) o;
			Object value = entry.getValue();

			if (value instanceof Map)
				list.add(new DefaultTreeNode(entry, initTreeModel((Map) value, new ArrayList())));
		}
		return list;
	}
	
	private Map parseFolders(String path, Map parentFolderMap) {

		for (Object o : application.getResourcePaths(path)) {
			String pagePath = String.valueOf(o);
			if (pagePath.endsWith("/")) {
				String folderName = pagePath.substring(0, pagePath.length() - 1);
				folderName = folderName.substring(folderName.lastIndexOf("/") + 1);
				if (shallShowFolder(folderName))
					parentFolderMap.put(folderName, parseFolders(pagePath, new TreeMap()));
			} else {
				String fileName = pagePath.substring(pagePath.lastIndexOf("/") + 1);
				String extension = fileName.substring(fileName.lastIndexOf(".") + 1);
				if (shallShowFile(fileName) || shallShowFile(extension))
					parentFolderMap.put(fileName, pagePath);
			}
		}
		return parentFolderMap;
	}
	
	private boolean shallShowFolder(String folderName) {
		return !_folderBlackList.contains(folderName); // not in black list -> can show
	}
	
	private boolean shallShowFile(String fileName) {
		// B70-CKEZ-22: Ignore file extension case.
		// can't have empty folder name
		return !Strings.isEmpty(fileName) && _fileWhiteList.contains(fileName.toLowerCase()); // in white list -> can show
	}
	
	public void onSelect$tree(){
		cntDiv.getChildren().clear();
		Treeitem item = tree.getSelectedItem();
		Map map = (Map)item.getValue();
		
		showImages(map);
	}
	
	
	private void showImages(Map map) {
		for (Object o : map.entrySet()) {
			Map.Entry me = (Map.Entry) o;
			Object value = me.getValue();

			if (value instanceof Map) continue;

			final String path = String.valueOf(value);

			Toolbarbutton tb = new Toolbarbutton(String.valueOf(me.getKey()), path.endsWith("swf") ? swfPath : path);
			tb.addEventListener(Events.ON_CLICK, new EventListener() {
				public void onEvent(Event event) throws Exception {
					if (selBtn != null)
						selBtn.setSclass(null);
					selBtn = (Toolbarbutton) event.getTarget();
					selBtn.setSclass("sel");
				}
			});
			final int CKEditorFuncNum = Integer.parseInt(((String[]) param.get("CKEditorFuncNum"))[0]);
			String script = "window.opener.CKEDITOR.tools.callFunction(" +
					CKEditorFuncNum + ", '" + execution.encodeURL(path) + "'); window.close(); ";
			tb.setWidgetListener("onDoubleClick", script);

			cntDiv.appendChild(tb);
		}
	}
	
	private class ExplorerTreeitemRenderer implements TreeitemRenderer {

		public void render(Treeitem item, Object data) throws Exception {
			Map.Entry entry = (Map.Entry) ((DefaultTreeNode) data).getData();
			item.setLabel(String.valueOf(entry.getKey()));
			Object value = entry.getValue();
			item.setValue(value);
			item.setOpen(true);
			if (item.getParentItem() == null)
				item.setSelected(true);
		}

		public void render(Treeitem item, Object data, int index) throws Exception {
			render(item, data);
		}
	}
}