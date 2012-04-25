/* WebAppInit.java

	Purpose:
		
	Description:
		
	History:
		Mon Apr 23 18:51:47     2012, Created by jimmyshiau

Copyright (C) 2012 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
}}IS_RIGHT
*/
package org.zkforge.ckez;

import org.zkoss.zk.au.http.DHtmlUpdateServlet;
import org.zkoss.zk.ui.WebApp;
/**
 * The initialization of ckez.
 *
 * @author jimmyshiau
 * @since 3.6.0.2
 */
public class WebAppInit implements org.zkoss.zk.ui.util.WebAppInit {

	public void init(WebApp wapp) throws Exception {
		if (DHtmlUpdateServlet.getAuExtension(wapp, "/ckezupload") == null) {
			DHtmlUpdateServlet.addAuExtension(wapp, "/ckezupload",
					new CkezUploadExtension());
			CKeditor.setFileUploadHandlePage(wapp.getUpdateURI(false) + "/ckezupload");
		}
	}

}
