<!DOCTYPE html>
<html>
    <head>
        <title>Customer Support</title>
    </head>
    <body>
        <security:authorize access="!hasAnyRole('USER','ADMIN')">
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

        <h2>Edit Reply #${message.id}</h2>
        <form:form method="POST" enctype="multipart/form-data"
                   modelAttribute="editForm">   
            
            <form:label path="body">Body</form:label><br/>
            <form:textarea path="body" rows="5" cols="30" ></form:textarea>><br/><br/>
            <c:if test="${fn:length(message.attachments) > 0}">
                <b>Attachments:</b><br/>
                <ul>
                    <c:forEach items="${message.attachments}" var="attachment">
                        <li>
                            <c:out value="${attachment.name}" />
                            [<a href="<c:url value="/message/${messageId}/delete/${attachment.name}" />">Delete</a>]
                        </li>
                    </c:forEach>
                </ul>
            </c:if>
            <b>Add attachments</b><br />
            <input type="file" name="attachments" multiple="multiple"/><br/><br/>
            <input type="submit" value="Save"/><br/><br/>
        </form:form>
        <a href="<c:url value="/message" />">Return to list topics</a>
    </body>
</html>