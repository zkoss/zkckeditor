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

import java.io.File;
import java.io.IOException;
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
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.zkoss.util.logging.Log;
import org.zkoss.web.servlet.Servlets;
import org.zkoss.zk.au.http.AuExtension;
import org.zkoss.zk.au.http.DHtmlUpdateServlet;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.UiException;
/**
 * The AU extension to upload files by ckeditor.
 * It is based on Apache Commons File Upload.
 * 
 * @author jimmyshiau
 * @since 3.6.0.2
 */
public class CkezUploadExtension implements AuExtension {
	private static final Log log = Log.lookup(CkezUploadExtension.class);
	
	private ServletContext _ctx;
	
	public CkezUploadExtension() {
	}

	public void init(DHtmlUpdateServlet servlet) throws ServletException {
		_ctx = servlet.getServletContext();
	}

	public void destroy() {
	}

	public void service(HttpServletRequest request,
			HttpServletResponse response, String pi) throws ServletException,
			IOException {
		final Session sess = Sessions.getCurrent(false);
		if (sess == null) {
			response.setIntHeader("ZK-Error", HttpServletResponse.SC_GONE);
			return;
		}
		String type = request.getParameter("type");
		try {
			String path = parseRequest(request);
			if (path != null) {
//				PrintWriter out = response.getWriter();
//				 out.println("<script type='text/javascript'>window.parent.CKEDITOR.tools.callFunction("+
//						 request.getParameter("CKEditorFuncNum")+",'"+path+"')</script>");  
//				final Desktop desktop = ((WebAppCtrl)sess.getWebApp())
//					.getDesktopCache(sess).getDesktopIfAny(dtid);
//				
//				final Execution exec = new ExecutionImpl(
//						_ctx, request, response, desktop, null);
//				exec.addAuResponse(new AuScript(null, "window.parent.CKEDITOR.tools.callFunction("+
//						 request.getParameter("CKEditorFuncNum")+",'"+path+"')"));
				
				
				String nextURI = "~./ckez/html/fileupload-done.html.dsp";
				final Map attrs = new HashMap();
				attrs.put("CKEditorFuncNum", request.getParameter("CKEditorFuncNum"));
				attrs.put("path", path);
				Servlets.forward(_ctx, request, response,
					nextURI, attrs, Servlets.PASS_THRU_ATTR);
				
				
//				Clients.evalJavaScript("window.parent.CKEDITOR.tools.callFunction("+
//						 request.getParameter("CKEditorFuncNum")+",'"+path+"')");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	private String parseRequest(HttpServletRequest request) throws Exception {
		if (ServletFileUpload.isMultipartContent(request)) {
		    String url = request.getParameter("url");
		    int index = url.lastIndexOf(";jsessionid");
			if (index > 0)
				url = url.substring(0, index);
		    
		    String path = request.getContextPath() + "/" + url;
		    
		    FileItemFactory factory = new DiskFileItemFactory();  
		    ServletFileUpload servletFileUpload = new ServletFileUpload(factory);  
		    servletFileUpload.setHeaderEncoding("UTF-8");
		    List fileItemsList = servletFileUpload.parseRequest(request);  
		    
		    for (Iterator it = fileItemsList.iterator(); it.hasNext();) {
		    	FileItem item = (FileItem) it.next();
		        if (!item.isFormField()) {
		        	url = request.getSession().getServletContext().getRealPath(url);
		        	writeFile(item, url);
		            return path + "/" + item.getName();
		        }  
		    }  
		}  
		return null;
	}

	private void writeFile(FileItem item, String url) throws Exception {
        File file = new File(url + "/" + item.getName());  
        if (!file.getParentFile().exists())
        	throw new UiException("Folder not found: " + url);
        item.write(file);
	}

}
