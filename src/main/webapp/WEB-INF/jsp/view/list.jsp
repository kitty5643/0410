<!DOCTYPE html>
<html>
    <head>
        <title>Customer Support</title>
    </head>
    <body>
        <security:authorize access="!hasAnyRole('USER','ADMIN')">
            <a href="<c:url value="/user/create" />">Register</a>
            <c:url var="logoutUrl" value="/login"/>
            <form action="${logoutUrl}" method="post">
                <input type="submit" value="Log in" />
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

            </form>

        </security:authorize>
       <security:authorize access="hasAnyRole('USER','ADMIN')">
            <c:url var="logoutUrl" value="/logout"/>
            <form action="${logoutUrl}" method="post">
                <input type="submit" value="Log out" />
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            </form>
        </security:authorize>

        <h2><c:out value="${category}" /> Topic</h2>
        <security:authorize access="hasRole('ADMIN')">
            <a href="<c:url value="/user" />">Manage User Accounts</a> | <a href="<c:url value="/addPoll" />">Create new forum poll</a> | 
        </security:authorize>

         <a href="<c:url value="/message/${category}/create" />">Create a Topic</a><br /><br />
        <c:choose>
            <c:when test="${fn:length(entries) == 0}">
                <i>There are no tickets in the system.</i>
            </c:when>
            <c:otherwise>
                <c:forEach var="entry" items="${entries}">
                    <a href="<c:url value="/message/${category}/view/${entry.id}" />">Topic: ${entry.subject} From: ${entry.customerName}</a>
                    [<a href="<c:url value="/message/edit/topic/${entry.id}" />">Edit</a>][<a href="<c:url value="/message/delete/topic/${entry.id}" />">Delete</a>]
                    
                   
                    <br /><br />
                </c:forEach>
            </c:otherwise>
        </c:choose>
    </body>
</html>