<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<jsp:include page="header.jsp"/>

<h3>User: ${order.user.username}</h3>

<h3>Currency: ${order.currency}</h3>

<h3>Amount: ${order.amount}</h3>

<h3>Price: ${order.price}</h3>

<p>Full price: ${order.price * order.amount}</p>

<table width="100%">
    <tr>
        <th>User</th>
        <td><a href="#">${order.user.username}</a></td>
    </tr>
    <tr>
        <th>Created</th>
        <td>${order.createdDate}</td>
    </tr>
    <tr>
        <th>Type</th>
        <td>${order.type}</td>
    </tr>
    <tr>
        <th>Currency</th>
        <td>${order.currency}</td>
    </tr>
    <tr>
        <th>Amount</th>
        <td>${order.amount}</td>
    </tr>
    <tr>
        <th>Price</th>
        <td>${order.price}</td>
    </tr>
    <tr>
        <th>Full price</th>
        <td>${order.price * order.amount}</td>
    </tr>
</table>

<form action="<c:url value="/deal/add/"/>">
    <input type="hidden" name="orderId" value="${order.id}">
    <input type="hidden" name="amount" value="${order.amount}">
    <button>Accept Full Order</button>
</form>

<form action="<c:url value="/deal/add/?orderId=${order.id}"/>">
    <label>
        Amount:<br/>
        <input name="amount"/>
    </label>
    <input type="hidden" name="orderId" value="${order.id}">
    <button>Accept</button>
</form>

<jsp:include page="footer.jsp"/>