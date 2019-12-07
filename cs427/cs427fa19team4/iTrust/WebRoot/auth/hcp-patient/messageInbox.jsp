<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@page import="java.util.List"%>

<%@page import="edu.ncsu.csc.itrust.action.ViewMyMessagesAction"%>
<%@page import="edu.ncsu.csc.itrust.beans.MessageBean"%>
<%@page import="edu.ncsu.csc.itrust.dao.DAOFactory"%>
<%@page import="edu.ncsu.csc.itrust.dao.mysql.PatientDAO"%>

<%@include file="/global.jsp" %>
<%@page import="java.text.DateFormat"%>
<%@page import="java.text.SimpleDateFormat"%>

<%@page import="edu.ncsu.csc.itrust.action.EditPersonnelAction"%>
<%@page import="edu.ncsu.csc.itrust.dao.mysql.PersonnelDAO"%>
<%@page import="edu.ncsu.csc.itrust.beans.Email"%>
<%@ page import="java.io.*" %>

<%
	//log the page view
	//loggingAction.logEvent(TransactionType.DIAGNOSIS_TRENDS_VIEW, loggedInMID.longValue(), 0, "");

	//get form data
	String sender = request.getParameter("sender");
	String subject = request.getParameter("subject");
	String hasWords = request.getParameter("hasWords");
	String notWords = request.getParameter("notWords");
	String startDate = request.getParameter("startDate");
	String endDate = request.getParameter("endDate");

	String savedSender = request.getParameter("savedSender");
	String savedSubject = request.getParameter("savedSubject");
	String savedHasWords = request.getParameter("savedHasWords");
	String savedNotWords = request.getParameter("savedNotWords");
	String savedStartDate = request.getParameter("savedStartDate");
	String savedEndDate = request.getParameter("savedEndDate");

	if (sender == null)
		sender = "";

	if (subject == null)
		subject = "";
	
	if (hasWords == null)
		hasWords = "";

	if (notWords == null)
		notWords = "";
	
	if (startDate == null)
		startDate = "";
	
	if (endDate == null)
		endDate = "";

	if (request.getParameter("save") != null) {
		savedSender = sender;
		savedSubject = subject;
		savedHasWords = hasWords;
		savedNotWords = notWords;
		savedStartDate = startDate;
		savedEndDate = endDate;
    }

	if (request.getParameter("cancel") != null) {
		sender = savedSender;
		subject = savedSubject;
		hasWords = savedHasWords;
		notWords = savedNotWords;
		startDate = savedStartDate;
		endDate = savedEndDate;
	}


%>

<%
pageTitle = "iTrust - View My Message ";
session.setAttribute("outbox",false);
session.setAttribute("isHCP",userRole.equals("hcp"));
loggingAction.logEvent(TransactionType.INBOX_VIEW, loggedInMID.longValue(), 0L, "");
%>

<%@include file="/header.jsp" %>

<div align=center>
	<h2>My Messages</h2>

	<div class="filterEdit">
		<div align="center">
			<span style="font-size: 13pt; font-weight: bold;">Edit Message Filter</span>
			<form method="post" action="messageInbox.jsp?edit=true">
				<table>
					<tr style="text-align: right;">
						<td>
							<label for="sender">Sender: </label>
							<input type="text" name="sender" id="sender" value="<%= StringEscapeUtils.escapeHtml(sender) %>" />
						</td>
						<td style="padding-left: 10px; padding-right: 10px;">
							<label for="hasWords">Has the words: </label>
							<input type="text" name="hasWords" id="hasWords" value="<%= StringEscapeUtils.escapeHtml(hasWords) %>" />
						</td>
						<td>
							<label for="startDate">Start Date: </label>
							<input type="text" name="startDate" id="startDate" value="<%= StringEscapeUtils.escapeHtml(startDate) %>" />
							<input type="button" value="Select Date" onclick="displayDatePicker('startDate');" />
						</td>
					</tr>
					<tr style="text-align: right;">
						<td>
							<label for="subject">Subject: </label>
							<input type="text" name="subject" id="subject" value="<%= StringEscapeUtils.escapeHtml(subject) %>" />
						</td>
						<td style="padding-left: 10px; padding-right: 10px;">
							<label for="notWords">Does not have the words: </label>
							<input type="text" name="notWords" id="notWords" value="<%= StringEscapeUtils.escapeHtml(notWords) %>" />
						</td>
						<td>
							<label for="endDate">End Date: </label>
							<input type="text" name="endDate" id="endDate" value="<%= StringEscapeUtils.escapeHtml(endDate) %>" />
							<input type="button" value="Select Date" onclick="displayDatePicker('endDate');" />
						</td>
					</tr>
					<tr style="text-align: center;">
						<td colspan="3">
							<input type="submit" name="test" value="Test Search" />
							<input type="submit" name="save" value="Save" />
							<input type="submit" name="cancel" value="Cancel" />
						</td>
					</tr>
					<tr style="text-align: center;">
					<td colspan="3">
						<input type="hidden" id="hiddenSavedSender" name = "savedSender", value="<%= StringEscapeUtils.escapeHtml(savedSender) %>"/>
						<input type="hidden" id="hiddenSavedSubject" name = "savedSubject", value="<%= StringEscapeUtils.escapeHtml(savedSubject) %>"/>
						<input type="hidden" id="hiddenHasWords" name = "savedHasWords", value="<%= StringEscapeUtils.escapeHtml(savedHasWords) %>"/>
						<input type="hidden" id="hiddenNotWords" name = "savedNotWords", value="<%= StringEscapeUtils.escapeHtml(savedNotWords) %>"/>
						<input type="hidden" id="hiddenStartDate" name = "savedStartDate", value="<%= StringEscapeUtils.escapeHtml(savedStartDate) %>"/>
						<input type="hidden" id="hiddenEndDate" name = "savedEndDate", value="<%= StringEscapeUtils.escapeHtml(savedEndDate) %>"/>
					</td>
					</tr>
				</table>
			</form>
		</div>
	</div>
	<br />

	<%
		session.setAttribute("sender", sender);
		session.setAttribute("subject", subject);
		session.setAttribute("hasWords", hasWords);
		session.setAttribute("notWords", notWords);
		session.setAttribute("startDate",startDate);
		session.setAttribute("endDate", endDate);

		session.setAttribute("savedSender", savedSender);
		session.setAttribute("savedSubject", savedSubject);
		session.setAttribute("savedHasWords", savedHasWords);
		session.setAttribute("savedNotWords", savedNotWords);
		session.setAttribute("savedStartDate", savedStartDate);
		session.setAttribute("savedEndDate", savedEndDate);
	%>

	<%@include file="/auth/hcp-patient/mailbox.jsp" %>

<%--	<jsp:include page="/auth/hcp-patient/mailbox.jsp">--%>
<%--		<jsp:param name="articleId" value="18"/>--%>
<%--	</jsp:include>--%>


</div>

<%@include file="/footer.jsp" %>



