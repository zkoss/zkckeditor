/* CkezFileWriter.java

	Purpose:
		
	Description:
		
	History:
		May 9, 2012 10:00:00 PM, Created by jimmyshiau

Copyright (C) 2012 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
	This program is distributed under LGPL Version 3.0 in the hope that
	it will be useful, but WITHOUT ANY WARRANTY.
}}IS_RIGHT
 */
package org.zkforge.ckez;

import org.apache.commons.fileupload.FileItem;

/**
 * @author jimmy
 *
 */
public interface CkezFileWriter {
	public String writeFileItem(String uploadUrl, String realPath, FileItem item, String type);
}
