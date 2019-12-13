<%@taglib uri="/WEB-INF/tags.tld" prefix="itrust"%>
<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@page import="edu.ncsu.csc.itrust.action.ViewDiagnosisStatisticsAction"%>
<%@page import="edu.ncsu.csc.itrust.beans.DiagnosisBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.DiagnosisStatisticsBean"%>
<%@page import="edu.ncsu.csc.itrust.exception.FormValidationException"%>

<%
	//log the page view
	loggingAction.logEvent(TransactionType.DIAGNOSIS_TRENDS_VIEW, loggedInMID.longValue(), 0, "");

	ViewDiagnosisStatisticsAction diagnoses = new ViewDiagnosisStatisticsAction(prodDAO);
	DiagnosisStatisticsBean dsBean = null;

	//get form data
	String startDate = request.getParameter("startDate");
	String endDate = request.getParameter("endDate");
	String gender = request.getParameter("gender");;

	//try to get the statistics. If there's an error, print it. If null is returned, it's the first page load
	try{
		dsBean = diagnoses.getCauseOfDeath(startDate, endDate, gender);
	} catch(FormValidationException e){
		e.printHTML(pageContext.getOut());
	}

	if (startDate == null)
		startDate = "";
	if (endDate == null)
		endDate = "";
	if (gender == null)
		gender = "";

%>
<br />
<form action="viewDiagnosisStatistics.jsp" method="post" id="formMain">
	<input type="hidden" name="viewSelect" value="CauseOfDeathTrends" />
	<table class="fTable" align="center" id="diagnosisStatisticsSelectionTable">
		<tr>
			<th colspan="4">Diagnosis Statistics</th>
		</tr>
		<tr class="subHeader">
			<td>Start Date:</td>
			<td>
				<input name="startDate" value="<%= StringEscapeUtils.escapeHtml("" + (startDate)) %>" size="10">
				<input type=button value="Select Date" onclick="displayDatePicker('startDate');">
			</td>
			<td>End Date:</td>
			<td>
				<input name="endDate" value="<%= StringEscapeUtils.escapeHtml("" + (endDate)) %>" size="10">
				<input type=button value="Select Date" onclick="displayDatePicker('endDate');">
			</td>
			
		</tr>

		<tr class="subHeader">
			<td>Gender:</td>
			<td>
				<select name="gender" style="font-size:10" onchange="hideThreshold(this.options[this.selectedIndex].value)">
					<option value="">-- None Selected --</option>
					<%if (gender.equals("All")) { %>
					<option selected="selected" value="">All</option>
					<option value="Male">Male</option>
					<option value="Female">Female</option>
					<% } else if (gender.equals("Male")) { %>
					<option value="All">All</option>
					<option selected="selected" value="Male">Male</option>
					<option value="Female">Female</option>
					<% } else if (gender.equals("Female")) { %>
					<option value="All">All</option>
					<option value="Male">Male</option>
					<option selected="selected" value="Female">Female</option>
					<% } else { %>
					<option value="All">All</option>
					<option value="Male">Male</option>
					<option value="Female">Female</option>
					<% } %>
				</select>
			</td>

			<td> </td>
			<td> </td>
		</tr>
		
		
		<tr>
			<td colspan="4" style="text-align: center;"><input type="submit" id="select_diagnosis" value="View Statistics"></td>
		</tr>
	</table>

</form>

<br />

<% if (dsBean != null) { %>



<table class="fTable" align="center" id="diagnosisStatisticsTable">
	<tr>
		<th>Top 1 Cause of Death</th>
		<th>Top 2 Cause of Death</th>
		<th>Start Date</th>
		<th>End Date</th>
	</tr>
	<tr style="text-align:center;">
		<td><%= dsBean.getTopOneCauseOfDeath() %></td>
		<td><%= dsBean.getTopTwoCauseOfDeath() %></td>
		<td><%= startDate %></td>
		<td><%= endDate %></td>
	</tr>

</table>

<br />

<p style="display:block; margin-left:auto; margin-right:auto; width:600px;">
	<%@include file="DiagnosisCauseOfDeathTrendsChart.jsp" %>
</p>

<% } %>
<br />
<br />


