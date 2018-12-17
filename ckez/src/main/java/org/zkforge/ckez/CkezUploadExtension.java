/* CkezUploadExtension.java

	Purpose:
		
	Description:
		
	History:
		Mon Apr 23 18:52:47     2012, Created by jimmyshiau

Copyright (C) 2012 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
}}IS_RIGHT
*/
package org.zkforge.ckez;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.web.servlet.Servlets;
import org.zkoss.zk.au.http.AuExtension;
import org.zkoss.zk.au.http.DHtmlUpdateServlet;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.UiException;
import org.zkoss.zk.ui.sys.WebAppCtrl;
/**
 * The AU extension to upload files by ckeditor.
 * It is based on Apache Commons File Upload.
 * 
 * @author jimmyshiau
 * @since 3.6.0.2
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class CkezUploadExtension implements AuExtension {

	private static final Logger logger = LoggerFactory.getLogger(CkezUploadExtension.class);

	private static final String nextURI = "~./ckez/html/fileupload-done.html.dsp";

	private ServletContext _ctx;
	
	public CkezUploadExtension() {
	}

	public void init(DHtmlUpdateServlet servlet) throws ServletException {
		_ctx = servlet.getServletContext();
	}

	public void destroy() {
	}

	public void service(HttpServletRequest request, HttpServletResponse response, String pi) throws ServletException,
			IOException {
		final Session sess = Sessions.getCurrent(false);
		if (sess == null) {
			response.setIntHeader("ZK-Error", HttpServletResponse.SC_GONE);
			return;
		}
		final String type = request.getParameter("Type");
		final String dtid = request.getParameter("dtid");
		final String uuid = request.getParameter("uuid");

		final FileItem item = parseFileItem(request);
		if (item == null) return;

		final Desktop desktop = ((WebAppCtrl) sess.getWebApp()).getDesktopCache(sess).getDesktopIfAny(dtid);
		if (desktop == null) return;

		final CKeditor ckez = (CKeditor) desktop.getComponentByUuidIfAny(uuid);
		if (ckez == null) return;

		String url;
		if ("Files".equals(type)) {
			url = ckez.getFilebrowserUploadUrl();
		} else if ("Images".equals(type)) {
			url = ckez.getFilebrowserImageUploadUrl();
		} else if ("Flash".equals(type)) {
			url = ckez.getFilebrowserFlashUploadUrl();
		} else {
			return;
		}

		url = FilebrowserController.getFolderUrl(url);
		String path = request.getContextPath() + url;

		final Map attrs = new HashMap();
		String serverPath = ckez.writeFileItem(path, desktop.getWebApp().getRealPath(url), item, type);
		String itemName = item.getName();
		attrs.put("filename", itemName.replace("\"", "\\\""));
		attrs.put("path", serverPath.replace(itemName, URLEncoder.encode(itemName, "UTF-8").replace("+", "%20")));
		Servlets.forward(_ctx, request, response, nextURI, attrs, Servlets.PASS_THRU_ATTR);
	}
	
	private FileItem parseFileItem(HttpServletRequest request) {
		if (ServletFileUpload.isMultipartContent(request)) {
			FileItemFactory factory = new DiskFileItemFactory();
			ServletFileUpload servletFileUpload = new ServletFileUpload(factory);
			servletFileUpload.setHeaderEncoding("UTF-8");
			try {
				List fileItemsList = servletFileUpload.parseRequest(request);
				for (Object f : fileItemsList) {
					FileItem item = (FileItem) f;
					if (!item.isFormField()) {
						return item;
					}
				}
			} catch (FileUploadException e) {
				logger.error("Upload failed", e);
			}
		}
		return null;
	}
}
