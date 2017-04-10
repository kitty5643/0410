<!DOCTYPE html>
<html>
    <head>
        <title>Course Discussion Forum</title>
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
        <h2>Index</h2>
        <p>Hello 
            <security:authorize access="hasAnyRole('USER','ADMIN')">
                <security:authentication property="principal.username" />
            </security:authorize>!</p>
            <form:form method="POST" enctype="multipart/form-data"
                       modelAttribute="ticketForm">
            <ul>
                <li><a href="<c:url value="/message/lecture/list"/>">
                        Lecture
                        <form:hidden path="category" value="Lecture"/></a></li> 
                <!--input type="input" id="category" name="category" value="lecture"/></a></li-->
                <li><a href="<c:url value="/message/lab/list"/>">
                        Lab
                        <!--form:hidden path="category" value="lab"/></a></li--> 
                        <li><a href="<c:url value="/message/other/list"/>">
                                Other
                                <!--form:hidden path="category" value="other"/></a></li-->
                                </ul>
                            </form:form>
                            </body>
                            </html>