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
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.text.ParseException"%>


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
        String savedRequiredNum = request.getParameter("savedRequiredNum");

        if (savedDate == null) {
            savedDate = "";
        }
        if (savedPillType == null) {
            savedPillType = "";
        }
        if (savedTakenNum == null) {
            savedTakenNum = "";
        }
        if (savedRequiredNum == null) {
            savedRequiredNum = "";
        }

        String[] dateArray = savedDate.split(";");
        List<String> date = new ArrayList<>();
        String[] pillTypeArray = savedPillType.split(";");
        List<String> pillType = new ArrayList<>();
        String[] takenNumArray = savedTakenNum.split(";");
        List<String> takenNum = new ArrayList<>();
        String[] requiredNumArray = savedRequiredNum.split(";");
        List<String> requiredNum = new ArrayList<>();

        for (int i = 0; i < dateArray.length; i ++) {
            date.add(dateArray[i]);
            pillType.add(pillTypeArray[i]);
            takenNum.add(takenNumArray[i]);
            requiredNum.add(requiredNumArray[i]);
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
        if (request.getParameter("addNewElement") != null) {
            int takenNumInt = 0;
            String error = "Please enter a valid prescription! (Date should be in form MM/DD/YYYY and Required Num should be a positive integer)";
            Boolean validDate = true;
            SimpleDateFormat sdfrmt = new SimpleDateFormat("MM/dd/yyyy");
            sdfrmt.setLenient(false);
            try {
                Date javaDate = sdfrmt.parse(newDate);
            } catch (ParseException e) {
                validDate = false;
            }
            try {
                takenNumInt = Integer.parseInt(newTakenNum);
                if (takenNumInt > 0 && validDate) {
                    date.add(newDate);
                    pillType.add(newPillType);
                    takenNum.add("0");
                    requiredNum.add(newTakenNum);
                } else {
                    %>
                    <div align=center>
                        <span class="iTrustError"><%=StringEscapeUtils.escapeHtml(error)%></span>
                    </div>
                    <%
                }
            } catch (Exception e) {
                %>
                <div align=center>
                    <span class="iTrustError"><%=StringEscapeUtils.escapeHtml(error)%></span>
                </div>
                <%
            }
        }

        for (int i = 0; i < date.size(); i++) {
            if (request.getParameter("addNum" + Integer.toString(i)) != null) {
                takenNum.set(i, Integer.toString(Integer.parseInt(takenNum.get(i)) + 1));
            }
            if (request.getParameter("minusNum" + Integer.toString(i)) != null && Integer.parseInt(takenNum.get(i)) > 0) {
                takenNum.set(i, Integer.toString(Integer.parseInt(takenNum.get(i)) - 1));
            }
            if (request.getParameter("deleteRow" + Integer.toString(i)) != null) {
                date.remove(i);
                pillType.remove(i);
                takenNum.remove(i);
                requiredNum.remove(i);
                break;
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
                    <th>Taken Num</th>
                    <th> </th>
                    <th> </th>
                    <th> </th>
                </tr>
                <%
                    for (int i = 1; i < date.size(); i ++) {
                %>
                <tr>
                    <td> <p class="<%=StringEscapeUtils.escapeHtml("curRow" + i + "C1")%>"> <%= StringEscapeUtils.escapeHtml(date.get(i)) %> </p> </td>
                    <td> <p class="<%=StringEscapeUtils.escapeHtml("curRow" + i + "C2")%>"> <%= StringEscapeUtils.escapeHtml(pillType.get(i)) %> </p> </td>
                    <td> <p class="<%=StringEscapeUtils.escapeHtml("curRow" + i + "C3")%>"> <%= StringEscapeUtils.escapeHtml(takenNum.get(i)) %> </p> </td>
                    <td>
                        <input type="submit" name="<%=StringEscapeUtils.escapeHtml("addNum" + i)%>" value="Add Num">
                    </td>
                    <td>
                        <input type="submit" name="<%=StringEscapeUtils.escapeHtml("minusNum" + i)%>" value="Minus Num">
                    </td>
                    <td>
                        <input type="submit" name="<%=StringEscapeUtils.escapeHtml("deleteRow" + i)%>" value="Delete">
                    </td>
                </tr>
                <%
                    }
                %>
            </table>

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
                for (int i = 1; i < date.size(); i ++) {
            %>
            <tr>
                <td> <p class="<%=StringEscapeUtils.escapeHtml("row" + i + "C1")%>"> <%= StringEscapeUtils.escapeHtml(date.get(i)) %> </p> </td>
                <td> <p class="<%=StringEscapeUtils.escapeHtml("row" + i + "C2")%>"> <%= StringEscapeUtils.escapeHtml(pillType.get(i)) %> </p> </td>
                <td> <p class="<%=StringEscapeUtils.escapeHtml("row" + i + "C3")%>"> <%= StringEscapeUtils.escapeHtml(requiredNum.get(i)) %> </p> </td>
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
                    <input type="submit" name="<%=StringEscapeUtils.escapeHtml("addNewElement")%>" value="Add" />
                </td>
            </tr>
            <tr>
                <%
                    String dateString = "";
                    String pillTypeString = "";
                    String takenNumString = "";
                    String requiredNumString = "";
                    for (int i = 0; i < date.size(); i ++) {
                        dateString += (date.get(i) + ";");
                        pillTypeString += (pillType.get(i) + ";");
                        takenNumString += (takenNum.get(i) + ";");
                        requiredNumString += (requiredNum.get(i) + ";");
                    }
                %>
                <td> <input type="hidden" id="savedDate" name = "savedDate" value="<%= (dateString) %>"/> </td>
                <td> <input type="hidden" id="savedPillType" name = "savedPillType" value="<%= (pillTypeString) %>"/> </td>
                <td> <input type="hidden" id="savedTakenNum" name = "savedTakenNum" value="<%= (takenNumString) %>"/> </td>
                <td> <input type="hidden" id="savedRequiredNum" name = "savedRequiredNum" value="<%= (requiredNumString) %>"/> </td>
            </tr>
        </table>
        </form>
    </div>

    <%
        session.setAttribute("newDate", newDate);
        session.setAttribute("newPillType", newPillType);
        session.setAttribute("newTakenNum", newTakenNum);

        session.setAttribute("savedDate", dateString);
        session.setAttribute("savedPillType", pillTypeString);
        session.setAttribute("savedTakenNum", takenNumString);
        session.setAttribute("savedRequiredNum", requiredNumString);
    %>
</div>

<%@include file="/footer.jsp"%>

