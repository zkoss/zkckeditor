<%@ page contentType="application/json;charset=UTF-8" %>
<%--
fileupload-done.html.dsp

	Purpose:
		The page after the user submits the fileupload dialog of the ckeditor.
	Description:

	History:
		Thu Apr 25 17:39:25     2012, Created by jimmyshiau

Copyright (C) 2012 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
	This program is distributed under LGPL Version 2.1 in the hope that
	it will be useful, but WITHOUT ANY WARRANTY.
}}IS_RIGHT
--%>
<%@ taglib uri="http://www.zkoss.org/dsp/web/core" prefix="c" %>
<c:set var="arg" value="${requestScope.arg}"/>
<c:choose>
<c:when test="${!empty arg.path}">
{
    "uploaded": 1,
    "fileName": "${arg.filename}",
    "url": "${arg.path}"
}
</c:when>
<c:otherwise>
{
    "uploaded": 0,
    "error": {
        "message": "Upload failed."
    }
}
</c:otherwise>
</c:choose>
