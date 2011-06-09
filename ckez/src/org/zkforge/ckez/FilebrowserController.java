/* Filebrowser.java

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
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.UiException;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.*;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;

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
	
	private String apName = "";
	private String type = "";
	private Map fileFilterMap;
	
	private Tree tree;
	private Div cntDiv;
	private Toolbarbutton selBtn;
	
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		
		type = ((String[])param.get("Type"))[0];
		fileFilterMap = initFileFilterMap();
		String url = getFolderUrl();
		
		if (Strings.isBlank(url)) return;
		if (url.startsWith("./"))
			url = url.substring(1);
		
		if (!url.startsWith("/"))
			url = "/" + url;
		Map rootFolderMap = new TreeMap();
		Map map = new TreeMap();
		rootFolderMap.put(url, map);
		
		parseFolders(url, map);
		
		tree.setItemRenderer(new ExplorerTreeitemRenderer());
		tree.setModel(new SimpleTreeModel(new SimpleTreeNode("ROOT",initTreeModel(rootFolderMap, new ArrayList()))));
		
		showImages(map);
	}

	private String getFolderUrl() {
		String uuid = ((String[])param.get("CKEditor"))[0];
		uuid = uuid.substring(0, uuid.lastIndexOf("-cnt"));
		
		String url = null;
		if ("Images".equals(type))
			url = (String) session.getAttribute("filebrowserImageBrowseUrl"+ uuid);
		else if ("Flash".equals(type))
			url = (String) session.getAttribute("filebrowserFlashBrowseUrl"+ uuid);
		
		if (url == null) 
			url = (String) session.getAttribute("filebrowserBrowseUrl"+ uuid);
		
		String updateURI = application.getUpdateURI();
		apName = updateURI.substring(0, updateURI.indexOf("/zkau"));
		int index = url.indexOf(apName);
		if (index == 0 && apName.length() > 0)
			index = url.indexOf("/", 1);
		
		url = index < 0 ? url: url.substring(index);
		
		index = url.lastIndexOf(";jsessionid");
		if (index > 0)
			url = url.substring(0, index);
		
		if (application.getResourcePaths(url) == null)
			throw new UiException("Folder not found: " + url);
		
		return url;
	}

	private List initTreeModel(Map parentFolderMap, List list) {
		for (Iterator it = parentFolderMap.entrySet().iterator(); it.hasNext();) {
			Map.Entry entry = (Map.Entry)it.next();
			Object key = entry.getKey();
			Object value = entry.getValue();
			
			if (value instanceof Map) {
				Map map = (Map) value;
				if (map.size() == 0) continue;
				ArrayList al = new ArrayList();
				list.add(new SimpleTreeNode(entry, al));
				initTreeModel(map, al);
			}
		}
		return list;
	}
	
	private Map parseFolders(String path, Map parentFolderMap) {
		
		Iterator it = application.getResourcePaths(path).iterator();
		while (it.hasNext()) {
			String pagePath = String.valueOf(it.next());
			if (pagePath.endsWith("/")) {
				String folderName = pagePath.substring(0, pagePath.length() - 1);
				folderName = folderName.substring(folderName.lastIndexOf("/") + 1);
				if (shallShowFile(folderName))
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
	
	
	private boolean shallShowFile(String folderName) {
		return new Boolean((String) fileFilterMap.get(folderName)).booleanValue();
	}
	
	
	private Map initFileFilterMap() {
		Map fileFilterMap = new HashMap();
		
		for (int i = 0, j = EXCLUDE_FOLDERS.length; i < j; i++)
			fileFilterMap.put(EXCLUDE_FOLDERS[i], "false");
		for (int i = 0, j = EXCLUDE_FILES.length; i < j; i++)
			fileFilterMap.put(EXCLUDE_FILES[i], "false");
		
		
		if (type.equals("Flash"))
			for (int i = 0, j = FLASH.length; i < j; i++)
				fileFilterMap.put(FLASH[i], "true");
		else if (type.equals("Images"))
			for (int i = 0, j = IMAGES.length; i < j; i++)
				fileFilterMap.put(IMAGES[i], "true");
		else if (type.equals("Files")) {
			for (int i = 0, j = FLASH.length; i < j; i++)
				fileFilterMap.put(FLASH[i], "true");
			for (int i = 0, j = IMAGES.length; i < j; i++)
				fileFilterMap.put(IMAGES[i], "true");
			for (int i = 0, j = FILES.length; i < j; i++)
				fileFilterMap.put(FILES[i], "true");
		}
		
		return fileFilterMap;
	}
	

	public void onSelect$tree(){
		cntDiv.getChildren().clear();
		Treeitem item = tree.getSelectedItem();
		Map map = (Map)item.getValue();
		
		showImages(map);
	}
	
	
	private void showImages(Map map){
		for (Iterator it = map.entrySet().iterator(); it.hasNext();) {
			Map.Entry me = (Map.Entry) it.next();
			Object value = me.getValue();
			if (value instanceof Map) continue;
			String path = String.valueOf(value);
			String swfPath = "";
			if (path.endsWith("swf"))
				swfPath = "~./ckez/img/flashIcon.jpg";
			Toolbarbutton tb = new Toolbarbutton(String.valueOf(me.getKey()), "".equals(swfPath)? path: swfPath);
			tb.addEventListener("onClick", new EventListener() {
				public void onEvent(Event event) throws Exception {
					if (selBtn !=null)
						selBtn.setSclass(null);
					selBtn = (Toolbarbutton) event.getTarget();
					selBtn.setSclass("sel");
				}
			});
			int CKEditorFuncNum = 1;
			CKEditorFuncNum = new Integer(((String[])param.get("CKEditorFuncNum"))[0]).intValue();
			String script = "window.opener.CKEDITOR.tools.callFunction("+CKEditorFuncNum+", '" + apName + path + "'); window.close(); ";
			tb.setWidgetListener("onDoubleClick",script);
			
			cntDiv.appendChild(tb);
		}
		
	}
	
	private class ExplorerTreeitemRenderer implements TreeitemRenderer {
		public void render(Treeitem item, Object data) throws Exception {
			Map.Entry entry = (Map.Entry)((SimpleTreeNode)data).getData();
			item.setLabel(String.valueOf(entry.getKey()));
			Object value = entry.getValue();
			item.setValue(value);
			item.setOpen(true);
			if (item.getParentItem() == null)
				item.setSelected(true);
		}
	}
}