<%@taglib uri="/WEB-INF/tags.tld" prefix="itrust"%>
<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="edu.ncsu.csc.itrust.beans.PatientBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.PatientVisitBean"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewPatientOfficeVisitHistoryAction"%>
<%@ page import="edu.ncsu.csc.itrust.action.ViewPreregisteredAction" %>
<%@ page import="edu.ncsu.csc.itrust.dao.mysql.PatientDAO" %>

<%@include file="/global.jsp" %>

<%
    pageTitle = "iTrust - View Pre-registered Patients";
%>

<%@include file="/header.jsp" %>
<%!
    String getParameterOrSessionAttr(String name, HttpServletRequest request) {
        String r = (String)request.getParameter(name);
            if (r!=null) {
            } else {
                r = "";
            }
        return r;
}
%>

<%
    String mid = getParameterOrSessionAttr("mid", request);

    if(mid != ""){
        PatientDAO patientDao = new PatientDAO(prodDAO);
        EventLoggingAction ev= new EventLoggingAction(prodDAO);
        ViewPreregisteredAction action = new ViewPreregisteredAction(prodDAO);
        List<PatientBean> preregisteredPatients = action.getPreregisteredPatients();
        for(PatientBean pb : preregisteredPatients) {
            if (pb.getMID() == Long.parseLong(mid) && pb.getPreregistered())  {
                patientDao.setPatientHCP(Long.parseLong(mid), loggedInMID);
                patientDao.setPreregisteredActivated(Long.parseLong(mid));
                ev.logEvent(TransactionType.ACTIVATE_PREREGISTERED, loggedInMID, Long.parseLong(mid), TransactionType.ACTIVATE_PREREGISTERED.getActionPhrase());
            }
            if (pb.getActivated() && pb.getMID() == Long.parseLong(mid)) {
                patientDao.delPatientHCP(Long.parseLong(mid));
                patientDao.setPreregisteredDeactivated(Long.parseLong(mid));
                ev.logEvent(TransactionType.DEACTIVATE_PREREGISTERED, loggedInMID, Long.parseLong(mid), TransactionType.DEACTIVATE_PREREGISTERED.getActionPhrase());
            }
        }
    }
    ViewPreregisteredAction action = new ViewPreregisteredAction(prodDAO);
    List<PatientBean> preregisteredPatients = action.getPreregisteredPatients();

%>

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
        $("#patientList").dataTable( {
            "aaColumns": [ [2,'dsc'] ],
            "aoColumns": [ { "sType": "lname" }, null, null],
            "bStateSave": true,
            "sPaginationType": "full_numbers"
        });
    });
</script>
<style type="text/css" title="currentStyle">
    @import "/iTrust/DataTables/media/css/demo_table.css";
</style>

<br />
<h2>Preregistered Patients</h2>
    <table class="display fTable" id="patientList" align="center">

        <thead>


        <tr class="">
            <th>Patient</th>
            <th>Email</th>
            <th>Activated</th>

        </tr>
        </thead>
        <tbody>
        <%
            int index = 0;
            for (PatientBean bean : preregisteredPatients) {

        %>
        <tr>
            <td >
                <a href="editPHR.jsp?patient=<%= StringEscapeUtils.escapeHtml("" + (index)) %>">


                    <%= StringEscapeUtils.escapeHtml("" + (bean.getFullName())) %>


                </a>
            </td>
            <td ><%= StringEscapeUtils.escapeHtml("" + (bean.getEmail())) %></td>
            <td>
<%
                if(bean.getPreregistered() == true){
%>
                <form action="/iTrust/auth/hcp-uap/viewPreregisteredPatients.jsp" id="editReferralForm" method="post">
                    <input type="hidden" name="mid" value="<%= bean.getMID() %>" />
                    <input type="submit" value="Activate" >
                </form>
<%                }
                    else{           %>
                    <form action="/iTrust/auth/hcp-uap/viewPreregisteredPatients.jsp" id="editReferralForm" method="post">
                        <input type="hidden" name="mid" value="<%= bean.getMID() %>" />
                        <input type="submit" value="Deactivate" >
                    </form>
<%                    }
                    %>
            </td>
        </tr>
        <%
                index ++;
            }
            session.setAttribute("patients", preregisteredPatients);
        %>
        </tbody>
    </table>
<br />
<br />

<%@include file="/footer.jsp" %>
