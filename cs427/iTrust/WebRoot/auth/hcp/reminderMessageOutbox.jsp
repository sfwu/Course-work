<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@page import="java.util.List"%>

<%@page import="edu.ncsu.csc.itrust.action.ViewMyMessagesAction"%>
<%@page import="edu.ncsu.csc.itrust.dao.DAOFactory"%>
<%@page import="edu.ncsu.csc.itrust.beans.Email"%>

<%@include file="/global.jsp" %>

<%
    pageTitle = "iTrust - View My Sent Reminder Messages";
%>

<%@include file="/header.jsp" %>

<div align=center>
    <h2>My Sent Reminder Messages</h2>
    <a href="/iTrust/auth/hcp/sendApptReminders.jsp">Send a Reminder Message</a><br /><br />
    <%
        loggingAction.logEvent(TransactionType.REMINDER_OUTBOX_VIEW, loggedInMID.longValue(), 0, "");

        ViewMyMessagesAction action = new ViewMyMessagesAction(prodDAO, loggedInMID.longValue());
        List<Email> emails = null;
        emails = action.getAllMySentReminders();
        session.setAttribute("email", emails);
        if (emails.size() > 0) { %>

    <br />
    <table class="fancyTable">
        <tr>
            <th>To</th>
            <th>Subject</th>
            <th>Sent</th>
            <th></th>
        </tr>
        <%		int index = 0; %>
        <%		for(Email email : emails) { %>
        <tr <%=(index%2 == 1)?"class=\"alt\"":"" %>>
            <td><%= StringEscapeUtils.escapeHtml("" + ( email.getToList().get(0)) ) %></td>
            <td><%= StringEscapeUtils.escapeHtml("" + ( email.getSubject() )) %></td>
            <td><%= StringEscapeUtils.escapeHtml("" + ( email.getTimeAdded() )) %></td>
            <td><a href="viewReminderMessageOutbox.jsp?msg=<%= StringEscapeUtils.escapeHtml("" + ( index )) %>">Read</a></td>
        </tr>
        <%			index ++; %>
        <%		} %>
    </table>
    <%	} else { %>
    <div>
        <i>You have no messages</i>
    </div>
    <%	} %>
    <br />
</div>

<%@include file="/footer.jsp" %>
