<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name=viewport content="width=device-width">
    <title>${msg}</title>
</head>
<body>
<%@ page import="java.util.Enumeration" %>
<h1>${msg}</h1>
<%
    Enumeration enu = request.getHeaderNames();//取得全部头信息
    while (enu.hasMoreElements()) {//以此取出头信息
        String headerName = (String) enu.nextElement();
        String headerValue = request.getHeader(headerName);//取出头信息内容
%>

    <p><%=headerName%><font color="red">--></font><font color="blue"><%=headerValue%></font></p>
<%
    }
%>
</body>
</html>