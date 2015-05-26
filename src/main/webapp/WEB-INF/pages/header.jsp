<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Intersango - Currency Exchange House</title>
    <link href="<c:url value="/resources/css/layout.css" />" rel="stylesheet">
</head>
<body>
<div id="header">
    <h2>Intersango Currency Exchange House</h2>

    <div style="float: right; font-size: 10px">
        <a href="<c:url value="/logout"/>">Logout</a>
    </div>
</div>

<div id="left_column">
    <ul>
        <li><a href="<c:url value="/deal/"/>">My Deals</a></li>
        <li><a href="<c:url value="/"/>">Current Orders</a></li>
    </ul>
</div>

<div id="content">

