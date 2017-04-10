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
            <h2>Register</h2>
        </security:authorize>
        <security:authorize access="hasAnyRole('USER','ADMIN')">
            <c:url var="logoutUrl" value="/logout"/>
            <form action="${logoutUrl}" method="post">
                <input type="submit" value="Log out" />
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            </form>

            <h2>Create a User</h2>
        </security:authorize>
        <form:form method="POST" enctype="multipart/form-data"
                   modelAttribute="messageUser">
            <form:label path="username">Username</form:label><br/>
            <form:input type="text" path="username" /><br/><br/>
            <form:label path="password">Password</form:label><br/>
            <form:input type="text" path="password" /><br/><br/>
            <form:label path="roles">Roles</form:label><br/>
            <form:checkbox path="roles" value="ROLE_USER" />ROLE_USER
            <form:checkbox path="roles" value="ROLE_ADMIN" />ROLE_ADMIN
            <br /><br />
            <input type="submit" value="Add User"/>
        </form:form>
    </body>
</html>