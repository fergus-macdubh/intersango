<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="header.jsp"/>

<h3>Hello, ${currentUser.username}</h3>
<div>
    <a href="<c:url value="/"/>">All</a>
    <a href="<c:url value="/?currency=USD"/>">USD</a>
    <a href="<c:url value="/?currency=EUR"/>">EUR</a>
    <a href="<c:url value="/?currency=RUB"/>">RUB</a>
</div>
<table>
    <tr>
        <th width="200">Currency</th>
        <th width="200">Amount</th>
        <th width="200">Price</th>
        <th>Action</th>
    </tr>
    <c:forEach items="${orders}" var="order">
        <tr class="${order.type}">
            <td>${order.currency}</td>
            <td>${order.amount}</td>
            <td>${order.price}</td>
            <td>
                <c:choose>
                    <c:when test="${order.user.id == currentUser.id}">
                        <a href="<c:url value="/order/${order.id}/cancel/"/>">Cancel</a>
                    </c:when>
                    <c:otherwise>
                        <a href="<c:url value="/order/${order.id}"/>">Accept</a>
                    </c:otherwise>
                </c:choose>

            </td>
        </tr>
    </c:forEach>
</table>
<br/>
<form action="<c:url value="/order/add/"/>" method="post">
    <fieldset>
        <legend>Add order</legend>
        <table>
            <tr>
                <td>
                    Order type:<br/>
                    <label>
                        <input type="radio" name="type" value="BID" checked/> BID (I want to buy)
                    </label><br/>
                    <label>
                        <input type="radio" name="type" value="ASK"/> ASK (I want to sell)
                    </label>
                </td>
                <td>
                    <label>
                        Currency:<br/>
                        <select name="currency" style="width: 100px">
                            <option>USD</option>
                            <option>EUR</option>
                            <option>RUB</option>
                        </select>
                    </label>
                </td>
                <td>
                    <label>Amount:<br/>
                        <input name="amount"/>
                    </label>
                </td>
                <td>
                    <label>Price:<br/>
                        <input name="price"/>
                    </label>
                </td>
            </tr>
        </table>
        <br/>
        <input value="Submit" type="submit" />
    </fieldset>
</form>

<jsp:include page="footer.jsp"/>