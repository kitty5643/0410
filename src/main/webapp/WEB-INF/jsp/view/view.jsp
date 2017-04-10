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
            

        </security:authorize>
        <security:authorize access="hasAnyRole('USER','ADMIN')">
            <c:url var="logoutUrl" value="/logout"/>
            <form action="${logoutUrl}" method="post">
                <input type="submit" value="Log out" />
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            </form>
        </security:authorize>

        <h2>Message #${messageId}: <c:out value="${message.subject}" /></h2>

  
                    <h4>From: ${topic.customerName}           Date: ${topic.date}<br/><br/>
                        Topic: ${topic.subject}<br/><br/></h4>
                        <p>Message: ${topic.body}</p>
                       
                    <br /><br />
                    
            Attachments:
            
            <c:choose>
            <c:when test="${fn:length(attachment) == 0}">
                <i>There are no attachment in the system.</i>
               
            </c:when>
            <c:otherwise>
                <c:forEach var="attachment" items="${attachment}">
                    in view::  <a href="<c:url value="/message/topic/${topic.id}/attachment/${attachment.name}" />">${attachment.name}</a>



                    <br /><br />
                </c:forEach>
            </c:otherwise>
        </c:choose>
               

        

        <br /><br />
        <c:choose>
            <c:when test="${fn:length(entries) == 0}">
                <i>There are no tickets in the system.</i>
            </c:when>
            <c:otherwise>
                <c:forEach var="entry" items="${entries}">
                    in view::  ${entry.customerName}:: ${entry.body} 
                    [<a href="<c:url value="/message/edit/reply/${entry.id}" />">Edit</a>]
                    [<a href="<c:url value="/message/delete/reply/${entry.id}" />">Delete</a>]


                    <br /><br />
                </c:forEach>
            </c:otherwise>
        </c:choose>




        <a href="<c:url value="/message/${category}/list" />">Return to list topics</a>
        <a href="<c:url value="/message/${category}/view/${messageId}/reply" />">Reply message</a>
        
        
        
</body>
</html>