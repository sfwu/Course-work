<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@page import="java.util.Date"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="edu.ncsu.csc.itrust.beans.LabelBean"%>
<%@page import="edu.ncsu.csc.itrust.action.LabelAction"%>
<%@page import="java.lang.Long"%>
<%@page import="java.lang.NumberFormatException"%>
<%@page import="edu.ncsu.csc.itrust.exception.ITrustException"%>
<%@page import="java.lang.IllegalArgumentException"%>
<%@page import="edu.ncsu.csc.itrust.exception.FormValidationException"%>


<%@include file="/global.jsp"%>

<%
    pageTitle = "iTrust - View My Prescription Diary";
%>

<%@include file="/header.jsp"%>

<div align=center>
    <h2>My Prescription Diary</h2>

    <%
        String savedDate = request.getParameter("savedDate");
        String savedPillType = request.getParameter("savedPillType");
        String savedTakenNum = request.getParameter("savedTakenNum");
        
        if (savedDate == null) {
            savedDate = "a;b";
        }
        
        if (savedPillType == null) {
            savedPillType = "b;c";
        }
        
        if (savedTakenNum == null) {
            savedTakenNum = "0;1";
        }
        
        String[] dateArray = savedDate.split(";");
        List<String> date = new ArrayList<>();
        String[] pillTypeArray = savedPillType.split(";");
        List<String> pillType = new ArrayList<>();
        String[] takenNumArray = savedTakenNum.split(";");
        List<String> takenNum = new ArrayList<>();
        
        for (int i = 0; i < dateArray.length; i ++) {
            date.add(dateArray[i]);
            pillType.add(pillTypeArray[i]);
            takenNum.add(takenNumArray[i]);
        }

        String newDate = request.getParameter("newDate");
        String newPillType = request.getParameter("newPillType");
        String newTakenNum = request.getParameter("newTakenNum");

        if (newDate == null) {
            newDate = "";
        }
        if (newPillType == null) {
            newPillType = "";
        }
        if (newTakenNum == null) {
            newTakenNum = "0";
        }
    %>

    <%

         // cannot add here
        if (request.getParameter("submitNew") != null) {
            date.add(newDate);
            pillType.add(newPillType);
            takenNum.add(newTakenNum);
        }

        for (int i = 0; i < date.size(); i++) {
            if (request.getParameter("addNum" + Integer.toString(i)) != null) {
                takenNum.set(i, Integer.toString(Integer.parseInt(takenNum.get(i)) + 1));
            }
            if (request.getParameter("minusNum" + Integer.toString(i)) != null) {
                takenNum.set(i, Integer.toString(Integer.parseInt(takenNum.get(i)) - 1));
            }
        }

       
    %>

    <br />
    <div style="margin-left: 5px;">
        <!-- Lower-case IDs are for edits. Each line's ACTUAL ID starts with an upper-case letter. -->
        <form id="beanForm" action="myPrescriptionDiary.jsp" method="post">
            <table class="fTable" border=1 align="center">
                <tr>
                    <th>Date</th>
                    <th>Pill Type</th>
                    <th>Token Num</th>
                    <th> </th>
                    <th> </th>
                </tr>
                <%
                    for (int i = 0; i < date.size(); i ++) {
                %>
                <tr>
                    <td> <%= StringEscapeUtils.escapeHtml(date.get(i)) %> </td>
                    <td> <%= StringEscapeUtils.escapeHtml(pillType.get(i)) %> </td>
                    <td> <%= StringEscapeUtils.escapeHtml(takenNum.get(i)) %> </td>
                    <td>
                        <input type="submit" name="<%=StringEscapeUtils.escapeHtml("addNum" + i)%>" value="Add Num">
                    </td>
                    <td>
                        <input type="submit" name="<%=StringEscapeUtils.escapeHtml("minusNum" + i)%>" value="Minus Num">
                    </td>
                </tr>
                <%
                    }
                %>
            </table>
        </form>
        <br>
        <hr>
        <table class="fTable" border=1 align="center">
            <caption>
                <h2>Given Totals</h2>
            </caption>
            <tr>
                <th>Date</th>
                <th>Pill Type</th>
                <th>Taken Num</th>
            </tr>
            <%
                for (int i = 0; i < date.size(); i ++) {
            %>
            <tr>
                <td> <%= StringEscapeUtils.escapeHtml(date.get(i)) %> </td>
                <td> <%= StringEscapeUtils.escapeHtml(pillType.get(i)) %> </td>
                <td> <%= StringEscapeUtils.escapeHtml(takenNum.get(i)) %> </td>
            </tr>
            <%
                }
            %>
        </table>

        <br>
        <hr>
        <table class="fTable" border=1 align="center">
            <caption>
                <h2>Add Prescription</h2>
            </caption>
            <tr>
                <th>Date</th>
                <th>Pill Type</th>
                <th>Total Num</th>
            </tr>
            <tr>
                <td> <input type="text" name="newDate" id="newDate" value="<%= StringEscapeUtils.escapeHtml(newDate) %>" /> </td>
                <td> <input type="text" name="newPillType" id="newPillType" value="<%= StringEscapeUtils.escapeHtml(newPillType) %>" /> </td>
                <td> <input type="text" name="newTakenNum" id="newTakenNum" value="<%= StringEscapeUtils.escapeHtml(newTakenNum) %>" /> </td>
            </tr>
            <tr style="text-align: center;">
                <td colspan="3">
                    <input type="submit" name="submitNew" value="Add" />
                </td>
            </tr>
            <tr>
                <%
                    String dateString = "";
                    String pillTypeString = "";
                    String takenNumString = "";
                    for (int i = 0; i < date.size(); i ++) {
                        dateString += (date.get(i) + ";");
                        pillTypeString += (pillType.get(i) + ";");
                        takenNumString += (takenNum.get(i) + ";");
                    }
                %>
                <td> <input type="hidden" id="savedDate" name = "savedDate", value="<%= (dateString) %>"/> </td>
                <td> <input type="hidden" id="savedPillType" name = "savedPillType", value="<%= (pillTypeString) %>"/> </td>
                <td> <input type="hidden" id="savedTakenNum" name = "savedTakenNum", value="<%= (takenNumString) %>"/> </td>
            </tr>
        </table>

    </div>

    <%
        session.setAttribute("newDate", newDate);
        session.setAttribute("newPillType", newPillType);
        session.setAttribute("newTakenNum", newTakenNum);

        session.setAttribute("savedDate", date);
        session.setAttribute("savedPillType", pillType);
        session.setAttribute("savedTakenNum", takenNum);
    %>
</div>
<script type="text/javascript">


</script>

<%@include file="/footer.jsp"%>

