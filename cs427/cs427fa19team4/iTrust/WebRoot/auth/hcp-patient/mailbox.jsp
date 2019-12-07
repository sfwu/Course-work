<%@page import="java.util.List"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.text.SimpleDateFormat"%>

<%@page import="edu.ncsu.csc.itrust.beans.MessageBean"%>
<%@page import="edu.ncsu.csc.itrust.dao.DAOFactory"%>
<%@page import="edu.ncsu.csc.itrust.action.EditPersonnelAction"%>
<%@page import="edu.ncsu.csc.itrust.dao.mysql.PersonnelDAO"%>
<%@page import="edu.ncsu.csc.itrust.dao.mysql.PatientDAO"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewMyMessagesAction"%>
<%@page import="edu.ncsu.csc.itrust.action.EditPatientAction"%>
<%@page import="edu.ncsu.csc.itrust.action.EditPersonnelAction"%>
<%@page import="edu.ncsu.csc.itrust.beans.Email"%>
<%@page errorPage="/auth/exceptionHandler.jsp"%>

<script src="/iTrust/DataTables/media/js/jquery.dataTables.min.js" type="text/javascript"></script>
<script type="text/javascript">
	jQuery.fn.dataTableExt.oSort['lname-asc']  = function(x,y) {
		var a = x.split(" ");
		var b = y.split(" ");
		return ((a[1] < b[1]) ? -1 : ((a[1] > b[1]) ?  1 : 0));
	};

<script src="/iTrust/DataTables/media/js/jquery.dataTables.min.js" type="text/javascript"></script>
<script type="text/javascript">
	jQuery.fn.dataTableExt.oSort['lname-asc']  = function(x,y) {
		var a = x.split(" ");
		var b = y.split(" ");
		return ((a[1] < b[1]) ? -1 : ((a[1] > b[1]) ?  1 : 0));
	};

	jQuery.fn.dataTableExt.oSort['lname-desc']  = function(x,y) {
		var a = x.split(" ");
		var b = y.split(" ");
		return ((a[1] < b[1]) ? 1 : ((a[1] > b[1]) ?  -1 : 0));
	};
</script>
<script type="text/javascript">
	$(document).ready(function() {
		$("#mailbox").dataTable( {
			"aaColumns": [ [2,'dsc'] ],
			"aoColumns": [ { "sType": "lname" }, null, null, {"bSortable": false} ],
			"sPaginationType": "full_numbers"
		});
	});
</script>
<style type="text/css" title="currentStyle">
	@import "/iTrust/DataTables/media/css/demo_table.css";
</style>

<%

	boolean outbox=(Boolean)session.getAttribute("outbox");
	boolean isHCP=(Boolean)session.getAttribute("isHCP");

	String pageName="messageInbox.jsp";
	if(outbox){
		pageName="messageOutbox.jsp";
	}

	PersonnelDAO personnelDAO = new PersonnelDAO(prodDAO);
	PatientDAO patientDAO = new PatientDAO(prodDAO);

	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

	ViewMyMessagesAction action = new ViewMyMessagesAction(prodDAO, loggedInMID.longValue());

	List<MessageBean> messages = null;

	if(request.getParameter("sortby") != null) {
		if(request.getParameter("sortby").equals("name")) {
			if(request.getParameter("sorthow").equals("asce")) {
				messages = outbox?action.getAllMySentMessagesNameAscending():action.getAllMyMessagesNameAscending();
			} else {
				messages = outbox?action.getAllMySentMessagesNameDescending():action.getAllMyMessagesNameDescending();
			}
		} else if(request.getParameter("sortby").equals("time")) {
			if(request.getParameter("sorthow").equals("asce")) {
				messages = outbox?action.getAllMySentMessagesTimeAscending():action.getAllMyMessagesTimeAscending();
			} else {
				messages = outbox?action.getAllMySentMessages():action.getAllMyMessages();
			}
		}
	} else {
		messages = outbox?action.getAllMySentMessages():action.getAllMyMessages();
	}

	List<Email> emails = (outbox || isHCP)?null:action.getAllMyReminders();

	session.setAttribute("messages", messages);
	session.setAttribute("emails", emails);

	if(messages.size() > 0) {
		if (outbox) { %>
<form method="post" action="messageOutbox.jsp">
		<%} else {%>
	<form method="post" action="messageInbox.jsp">
		<%}%>
		<table>
			<tr>
				<td>
					<select name="sortby">
						<option value="time">Sort</option>
						<option value="name">Name</option>
						<option value="time">Time</option>
					</select>
				</td>
				<td>
					<select name="sorthow">
						<option value="desc">Order</option>
						<option value="asce">Ascending</option>
						<option value="desc">Descending</option>
					</select>
				</td>
				<td>
					<input type="submit" value="Sort" />
				</td>
			</tr>
		</table>
	</form>
	<table id="mailbox" class="display fTable">
		<thead>
		<tr>
			<th><%= outbox?"Receiver":"Sender" %></th>
			<th>Subject</th>
			<th><%= outbox?"Sent":"Received" %></th>
			<th></th>
		</tr>
		</thead>
		<tbody>
		<%
			int index=-1;
			for(MessageBean message : messages) {
				String style = "";
				if(message.getRead() == 0) {
					style = "style=\"font-weight: bold;\"";
				}

				if(!outbox || message.getOriginalMessageId()==0){
					index ++;
					String primaryName = action.getName(outbox?message.getTo():message.getFrom());
					List<MessageBean> ccs = action.getCCdMessages(message.getMessageId());
					String ccNames = "";
					int ccCount = 0;
					for(MessageBean cc:ccs){
						ccCount++;
						long ccMID = cc.getTo();
						ccNames += action.getPersonnelName(ccMID) + ", ";
					}
					ccNames = ccNames.length() > 0?ccNames.substring(0, ccNames.length()-2):ccNames;
					String toString = primaryName;
					if(ccCount>0){
						String ccNameParts[] = ccNames.split(",");
						toString = toString + " (CC'd: ";
						for(int i = 0; i < ccNameParts.length-1; i++) {
							toString += ccNameParts[i] + ", ";
						}
						toString += ccNameParts[ccNameParts.length - 1] + ")";
					}
		%>
		<tr <%=style%>>
			<td><%= StringEscapeUtils.escapeHtml("" + ( toString)) %></td>
			<td><%= StringEscapeUtils.escapeHtml("" + ( message.getSubject() )) %></td>
			<td><%= StringEscapeUtils.escapeHtml("" + ( dateFormat.format(message.getSentDate()) )) %></td>
			<td><a href="<%= outbox?"viewMessageOutbox.jsp?msg=" + StringEscapeUtils.escapeHtml("" + ( index )):"viewMessageInbox.jsp?msg=" + StringEscapeUtils.escapeHtml("" + ( index )) %>">Read</a></td>
		</tr>
		<%
				}
				toString += ccNameParts[ccNameParts.length - 1] + ")";
			}

			//sender
			//******************
			boolean checkSender = session.getAttribute("sender").equals("") || toString.equals(session.getAttribute("sender"));
			//subject
			//******************
			boolean checkSubject = session.getAttribute("subject").equals("") || message.getSubject().equals(session.getAttribute("subject"));
			
			//haswords
			//******************
			String isWord = " "+session.getAttribute("hasWords").toString().toLowerCase()+" ";
			String body = message.getBody().toString().toLowerCase();
			// replace all special char by whitespace.
			body = " "+body.replaceAll("[^a-zA-Z0-9]", " ")+" ";
			// add extra space for subject and converit it into lowercase
			String subjectWords = " "+message.getSubject().toLowerCase()+" ";
			boolean checkHasWord = isWord.equals("  ") || body.contains(isWord) || subjectWords.contains(isWord);
			
			//notWords
			//******************
			String noWord = " "+session.getAttribute("notWords").toString().toLowerCase()+" ";
			boolean checkNoWord = noWord.equals("  ") || (!body.contains(noWord) && !subjectWords.contains(noWord));

			//date
			//******************

			//No matter how, we can get the specific date of a message
			String trueDateString = message.getSentDate().toString();
			trueDateString = trueDateString.split(" ")[0];
			String [] trueHelper= trueDateString.split("-");
			int trueCompare = Integer.parseInt(trueHelper[0])*10000 + Integer.parseInt(trueHelper[1])*100 + Integer.parseInt(trueHelper[2]);

			//now, we deal with start date and end date
			boolean checkStartDate = true;
			String startDateString = session.getAttribute("startDate").toString();

			if (!startDateString.equals("")) {

				startDateString = startDateString.replaceAll("[^a-zA-Z0-9]", " ");
				String [] startHelper = startDateString.split(" ");
				int startCompare = Integer.parseInt(startHelper[0])*100 + Integer.parseInt(startHelper[1]) + Integer.parseInt(startHelper[2])*10000;

				checkStartDate= startCompare <= trueCompare ? true : false;
			}

			boolean checkEndDate = true;
			String endDateString = session.getAttribute("endDate").toString();

			if (!endDateString.equals("")) {

				endDateString = endDateString.replaceAll("[^a-zA-Z0-9]", " ");
				String [] endHelper = endDateString.split(" ");
				int endCompare = Integer.parseInt(endHelper[0])*100 + Integer.parseInt(endHelper[1]) + Integer.parseInt(endHelper[2])*10000;

				checkEndDate = endCompare >= trueCompare ? true : false;
			}


			
			if (checkSender && checkSubject && checkHasWord && checkNoWord && checkStartDate && checkEndDate) {
				%>
					<tr <%=style%>>
						<td><%= StringEscapeUtils.escapeHtml("" + ( toString)) %></td>
						<td><%= StringEscapeUtils.escapeHtml("" + ( message.getSubject() )) %></td>
						<td><%= StringEscapeUtils.escapeHtml("" + ( dateFormat.format(message.getSentDate()) )) %></td>
						<td><a href="<%= outbox?"viewMessageOutbox.jsp?msg=" + StringEscapeUtils.escapeHtml("" + ( index )):"viewMessageInbox.jsp?msg=" + StringEscapeUtils.escapeHtml("" + ( index )) %>">Read</a></td>
					</tr>
				<%
			}
		}
		
	}
	index = -1;
	if (emails != null) {
		for(Email email : emails) {
			String style = "";
			if(email.getRead() == 0) {
				style = "style=\"font-weight: bold;\"";
			}

			}
			index = -1;
			if (emails != null) {
				for(Email email : emails) {
					String style = "";
					if(email.getRead() == 0) {
						style = "style=\"font-weight: bold;\"";
					}

					if(!outbox){
						index ++;
						String primaryName = email.getFrom();
						String toString = primaryName;
		%>
		<tr <%=style%>>
			<td><%= StringEscapeUtils.escapeHtml("" + ( toString)) %></td>
			<td><%= StringEscapeUtils.escapeHtml("" + ( email.getSubject() )) %></td>
			<td><%= StringEscapeUtils.escapeHtml("" + ( dateFormat.format(email.getTimeAdded()) )) %></td>
			<td><a href="<%="viewReminderMessageInbox.jsp?msg=" + StringEscapeUtils.escapeHtml("" + ( index )) %>">Read</a></td>
		</tr>
		<%
					}

				}
			}
		%>
		</tbody>
	</table>
		<%} else { %>
	<div>
		<i>You have no messages</i>
	</div>
<%	} %>