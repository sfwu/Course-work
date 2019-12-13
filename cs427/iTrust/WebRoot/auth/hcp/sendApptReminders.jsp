<%@page errorPage="/auth/exceptionHandler.jsp"%>
<%@page import="edu.ncsu.csc.itrust.dao.mysql.ApptTypeDAO"%>

<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.sql.Timestamp"%>
<%@page import="edu.ncsu.csc.itrust.beans.Email"%>
<%@page import="edu.ncsu.csc.itrust.EmailUtil"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewMyApptsAction"%>
<%@page import="edu.ncsu.csc.itrust.beans.ApptBean"%>
<%@ page import="edu.ncsu.csc.itrust.enums.TransactionType" %>


<%@include file="/global.jsp" %>

<%
    pageTitle = "iTrust - Send Appointment Reminders";
%>

<%@include file="/header.jsp" %>

<div align="center">
    <h2>Appointment Reminders</h2>

    <form action="sendApptReminders.jsp" method="post" id="apptReminderForm">
        <table>
            <tr>
                <td><input type="text" name="days"></td><td><span class="font1">Days</span></td>
            </tr>
        </table>
        <br />
        <br />
        <input type="submit" id="sendReminders" name="sendReminders" value="Send Reminders" />
    </form>
    <br />

    <br />

    <%
        if ("Send Reminders".equals(request.getParameter("sendReminders"))) {
            EmailUtil emailer = new EmailUtil(prodDAO);
            ViewMyApptsAction action = new ViewMyApptsAction(prodDAO, loggedInMID.longValue());
            List<ApptBean> appts = action.getAllAppointmentsWithin(Integer.parseInt(request.getParameter("days")));
            for(ApptBean apt : appts) {
                Email email = new Email();
                List<String> toList = new ArrayList<String>();
                toList.add(action.getName(apt.getPatient()));
                email.setToList(toList);

                email.setFrom("System Reminder");
                Timestamp ts = new Timestamp(System.currentTimeMillis());
                long ms = ts.getTime() - apt.getDate().getTime();
                long days = ms / (24*60*60*1000);
                String subject = "Reminder: upcoming appointment in" + days + "day(s)";

                String msg = "You have an appointment on " +
                        String.format("%02d:%02d, %d/%d", apt.getDate().getHours(), apt.getDate().getMinutes(), apt.getDate().getMonth(), apt.getDate().getDay()) +
                        " with Dr. " + action.getName(apt.getHcp());
                email.setBody(msg);
                email.setSubject(subject);
                emailer.sendEmail(email);

            }
            loggingAction.logEvent(TransactionType.SEND_APPOINTMENT_REMINDERS, loggedInMID.longValue(), 0, "");
            %>
            <div style="align: center; margin-bottom: 10px;">
                <span class="Success" style="font-size: 16px;"><%= StringEscapeUtils.escapeHtml("" + ("Reminders Sent Successfully!")) %></span>
            </div>
            <%
        }
    %>

</div>

<%@include file="/footer.jsp" %>
