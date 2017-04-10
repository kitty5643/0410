
<!DOCTYPE html>
<html>
    <head>
        <title>Guest Book</title>
    </head>
    <body>
    <c:url var="logoutUrl" value="/logout"/>
    <form action="${logoutUrl}" method="post">
      <input type="submit" value="Log out" />
      <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
    </form>    
    <h1>Guest Book</h1>
    <p>Hello <!--security:authentication property="principal.username" /-->!</p>
    <c:if test="${fn:length(entries) == 0}">
        <p>There is no message yet.</p>
    </c:if>
    <c:if test="${fn:length(entries) > 0}">
        <ul>
        <c:forEach var="entry" items="${entries}">
            <li>
                ${entry.name} (<fmt:formatDate value="${entry.date}" pattern="yyyy-MM-dd" />): 
                [<a href="<c:url value="/message/edit?id=${entry.id}" />">Edit</a>] <br />
            <c:out value="${entry.message}" escapeXml="true" /><br />
            </li>
        </c:forEach>
        </ul>
    </c:if>
    <p><a href="<c:url value="/message/add" />">Add Comment</a></p>
    </body>
</html>
