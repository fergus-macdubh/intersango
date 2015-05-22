<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="header.jsp"/>

<h3>Hello, ${currentUser.username}</h3>

<h4>Deals for my orders</h4>
<table>
    <tr>
        <th width="200">Currency</th>
        <th width="200">Amount</th>
        <th width="200">Price</th>
        <th>Action</th>
    </tr>
    <c:forEach items="${requestedDeals}" var="deal">
        <tr>
            <td>${deal.order.currency}</td>
            <td>${deal.amount}</td>
            <td>${deal.order.price}</td>
            <td>
                <a href="<c:url value="/deal/${deal.id}/confirm/"/>">Confirm</a>
                <br/>
                <a href="<c:url value="/deal/${deal.id}/reject/"/>">Reject</a>
            </td>
        </tr>
    </c:forEach>
</table>

<br/>

<h4>My deals</h4>

<table>
    <tr>
        <th width="200">Currency</th>
        <th width="200">Amount</th>
        <th width="200">Price</th>
        <th width="200">State</th>
        <th>Action</th>
    </tr>
    <c:forEach items="${myDeals}" var="deal">
        <tr>
            <td>${deal.order.currency}</td>
            <td>${deal.amount}</td>
            <td>${deal.order.price}</td>
            <td>${deal.state}</td>
            <td>
                <c:choose>
                    <c:when test="${deal.state == 'REQUESTED'}">
                        <a href="<c:url value="/deal/${deal.id}/cancel/"/>">Cancel</a>
                    </c:when>
                    <c:otherwise>
                        -
                    </c:otherwise>
                </c:choose>
            </td>
        </tr>
    </c:forEach>
</table>

<jsp:include page="footer.jsp"/>