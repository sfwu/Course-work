<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@page import="edu.ncsu.csc.itrust.action.ViewMyMessagesAction"%>
<%@page import="edu.ncsu.csc.itrust.beans.MessageBean"%>
<%@page import="java.util.List"%>
<%@page import="edu.ncsu.csc.itrust.beans.Email"%>

<%@include file="/global.jsp" %>

<%
    pageTitle = "iTrust - View Reminder Message";
%>

<%@include file="/header.jsp" %>

<%
    ViewMyMessagesAction action = new ViewMyMessagesAction(prodDAO, loggedInMID.longValue());
    Email original = null;

    if (request.getParameter("msg") != null) {
        String msgParameter = request.getParameter("msg");
        int msgIndex = 0;
        try {
            msgIndex = Integer.parseInt(msgParameter);
        } catch (NumberFormatException nfe) {
            response.sendRedirect("messageInbox.jsp");
        }
        List<Email> emails = null;
        if (session.getAttribute("emails") != null) {
            emails = (List<Email>) session.getAttribute("emails");
            if(msgIndex > emails.size() || msgIndex < 0) {
                msgIndex = 0;
                response.sendRedirect("oops.jsp");
            }
        } else {
            response.sendRedirect("messageInbox.jsp");
        }
        original = (Email)emails.get(msgIndex);
        original.setRead(1);
        session.setAttribute("email", original);
    }
    else {
        response.sendRedirect("messageInbox.jsp");
    }

    loggingAction.logEvent(TransactionType.MESSAGE_VIEW, loggedInMID.longValue(), 0, "Viewed Reminder Message: " + original.getBody());
%>
<div>
    <table width="99%">
        <tr>
            <td><b>To:</b> <%= StringEscapeUtils.escapeHtml("" + ( original.getToList().get(0) )) %></td>
        </tr>
        <tr>
            <td><b>Subject:</b> <%= StringEscapeUtils.escapeHtml("" + ( original.getSubject() )) %></td>
        </tr>
        <tr>
            <td><b>Date &amp; Time:</b> <%= StringEscapeUtils.escapeHtml("" + ( original.getTimeAdded() )) %></td>
        </tr>
    </table>
</div>

<table>
    <tr>
        <td colspan="2"><b>Message:</b></td>
    </tr>
    <tr>
        <td colspan="2"><%= StringEscapeUtils.escapeHtml("" + ( original.getBody() )).replace("\n","<br/>") %></td>
    </tr>
    <tr>
        <td colspan="2"><a href="messageInbox.jsp">Back</a></td>
    </tr>
</table>


<%@include file="/footer.jsp" %>
