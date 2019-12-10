<%@page import="edu.ncsu.csc.itrust.beans.PatientBean"%>
<%@page import="edu.ncsu.csc.itrust.action.EditPatientAction"%>
<%@page import="edu.ncsu.csc.itrust.BeanBuilder"%>
<%@page import="edu.ncsu.csc.itrust.exception.FormValidationException"%>
<%@ page import="edu.ncsu.csc.itrust.exception.ITrustException" %>
<%@ page import="org.apache.commons.jxpath.ri.model.beans.NullPointer" %>
<%@ page import="edu.ncsu.csc.itrust.validate.PatientValidator" %>
<%@ page import="edu.ncsu.csc.itrust.dao.mysql.PatientDAO" %>
<%@ page import="edu.ncsu.csc.itrust.beans.PreregisterBean" %>
<%@ page import="edu.ncsu.csc.itrust.beans.HealthRecord" %>
<%@ page import="edu.ncsu.csc.itrust.dao.mysql.HealthRecordsDAO" %>
<%@ page import="java.text.DateFormat" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="edu.ncsu.csc.itrust.beans.PersonnelBean" %>
<%@ page import="edu.ncsu.csc.itrust.dao.mysql.PersonnelDAO" %>
<%@ page import="edu.ncsu.csc.itrust.exception.ErrorList" %>
<%@ page import="edu.ncsu.csc.itrust.validate.ValidationFormat" %>
<%@ page import="edu.ncsu.csc.itrust.enums.Role" %>


<%@taglib prefix="itrust" uri="/WEB-INF/tags.tld"%>
<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@include file="/global.jsp"%>

<%
    pageTitle = "iTrust - Pre-register";
%>

<%@include file="/header.jsp"%>

<%
    PatientBean patient;
    PersonnelBean person;
    EditPatientAction action = null;
    HealthRecord additionalInfo;
    long mid = 0;
    try {
        /* Now take care of updating information */
        boolean formIsFilled = request.getParameter("formIsFilled") != null
                && request.getParameter("formIsFilled").equals("true");
        if (formIsFilled) {

            ErrorList errorList = new ErrorList();
            String password = request.getParameter("password");
            String confirmPassword = request.getParameter("confirmpassword");
            String email = request.getParameter("email");
            PatientDAO patientDAO = new PatientDAO(prodDAO);


            if (password == null || "".equals(password)) {
                errorList.addIfNotNull("Password cannot be empty");
            } else {
                if (!password.equals(confirmPassword))
                    errorList.addIfNotNull("Passwords don't match");
                if (!ValidationFormat.PASSWORD.getRegex().matcher(password).matches()) {
                    errorList.addIfNotNull("Password must be in the following format: "
                            + ValidationFormat.PASSWORD.getDescription());
                }
                if(!ValidationFormat.EMAIL.getRegex().matcher(email).matches()){
                    errorList.addIfNotNull("Email must be in the following format: "
                            + ValidationFormat.EMAIL.getDescription());
                }
                if(!patientDAO.checkEmail(email)){
                    errorList.addIfNotNull("Email has already been used before");
                }
            }
            if (errorList.hasErrors())
                throw new FormValidationException(errorList);


            PatientValidator validator = new PatientValidator();
            patient = new BeanBuilder<PatientBean>().build(
                    request.getParameterMap(), new PatientBean());
            validator.validate(patient);

            mid = patientDAO.setPatient(patient);
            AuthDAO authDao = new AuthDAO(prodDAO);
            authDao.addUser(mid, Role.PATIENT, password);

            boolean additionalFilled = request.getParameter("additionalFilled") != null
                    && request.getParameter("additionalFilled").equals("true")
                    && !((request.getParameter("state").equals("AL") && request.getParameter("city") == "")
                    && (request.getParameter("icState").equals("AL") && request.getParameter("icCity") == ""));
            if (additionalFilled) {
                additionalInfo = new HealthRecord();
                HealthRecordsDAO healthDao = new HealthRecordsDAO(prodDAO);
                if (request.getParameter("height") != null) {
                    additionalInfo.setHeight(Double.parseDouble(request.getParameter("height")));
                }
                if (request.getParameter("weight") != null) {
                    additionalInfo.setWeight(Double.parseDouble(request.getParameter("weight")));
                }
                if (request.getParameter("isSmoker").equals("on")) {
                    additionalInfo.setSmoker(1);
                }
                additionalInfo.setPatientID(mid);
                additionalInfo.setPersonnelID(9000000000L);
                DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
                Date date = new Date();
                additionalInfo.setOfficeVisitDateStr((dateFormat.format(date)));

                healthDao.add(additionalInfo);
            }
            loggingAction.logEvent(TransactionType.CREATE_PREREGISTERED, mid, 0, "User preregistered as a patient");

%>

<div align=center>
    <span class="iTrustMessage">Successful Pre-registration</span>
    <%out.println("<td> Your mid is: " + mid + "</td></tr>\n");%>
</div>
<%
        }
        else{
%>
<br />
<tr>
    <td>
        <div>
            <form action="preregister.jsp" method = post">
                <table>
                    <tr>
                        <td valign=top>
                            <div class="row">
                                <div class="col-md-6">

                                    <table class="fTable" align=center style="width: 350px;">
                                        <tr>
                                            <th colspan=2>Mandatory Information<input type="hidden"
                                                                                      name="formIsFilled" value="true">
                                            </th>
                                        </tr>
                                        <tr>
                                            <td class="subHeaderVertical">First Name:</td>
                                            <td><input name="firstName"
                                                       type="text" required></td>
                                        </tr>
                                        <tr>
                                            <td class="subHeaderVertical">Last Name:</td>
                                            <td><input name="lastName"
                                                       type="text" required></td>
                                        </tr>
                                        <tr>
                                            <td class="subHeaderVertical">Email:</td>
                                            <td><input name="email"
                                                       type="text" required></td>
                                        </tr>
                                        <tr>
                                            <td class="subHeaderVertical">Password:</td>
                                            <td><input name="password"
                                                       type="password" required></td>
                                        </tr>
                                        <tr>
                                            <td class="subHeaderVertical">Confirm Password:</td>
                                            <td><input name="confirmpassword"
                                                       type="password" required></td>
                                        </tr>
                                    </table>

                                </div>
                                <div class="col-md-6">
                                    <table class="fTable" align=center style="width: 350px;">
                                        <tr>
                                            <th colspan=2>Additional Information
                                                <input type="hidden" name="additionalFilled" value="true">
                                            </th>
                                        </tr>
                                        <tr>
                                            <td class="subHeaderVertical">Address:</td>
                                            <td><input name="streetAddress1"
                                                       type="text"><br />
                                                <input name="streetAddress2"
                                                       type="text"></td>
                                        </tr>
                                        <tr>
                                            <td class="subHeaderVertical">City:</td>
                                            <td><input name="city"></td>
                                        </tr>
                                        <tr>
                                            <td class="subHeaderVertical">State:</td>
                                            <td><itrust:state name="state" value=""/></td>
                                        </tr>
                                        <tr>
                                            <td class="subHeaderVertical">Zip:</td>
                                            <td><input name="zip"
                                                       maxlength="10" type="text" size="10"></td>
                                        </tr>
                                        <tr>
                                            <td class="subHeaderVertical">Phone:</td>
                                            <td><input name="phone"
                                                       type="text" size="12" maxlength="12">
                                        </tr>
                                        <tr>
                                            <td class="subHeaderVertical">Insurance Provider Name:</td>
                                            <td><input name="icName"
                                                       type="text"></td>
                                        </tr>
                                        <tr>
                                            <td class="subHeaderVertical">Insurance Provider Address:</td>
                                            <td><input name="icAddress1"
                                                       type="text"><br />
                                                <input name="icAddress2"
                                                       type="text"></td>
                                        </tr>
                                        <tr>
                                            <td class="subHeaderVertical">Insurance Provider City:</td>
                                            <td><input name="icCity"
                                                       type="text"></td>
                                        </tr>

                                        <tr>
                                            <td class="subHeaderVertical">Insurance Provider State:</td>
                                            <td><itrust:state name="icState"
                                                              value="" />
                                            </td>
                                        </tr>
                                        <tr>
                                            <td class="subHeaderVertical">Insurance Provider Zip:</td>
                                            <td><input name="icZip"
                                                       maxlength="10" type="text" size="10"></td>
                                        </tr>
                                        <tr>
                                            <td class="subHeaderVertical">Insurance Provider Phone:</td>
                                            <td><input name="icPhone"
                                                       type="text" size="12" maxlength="12"></td>
                                        </tr>
                                        <tr>
                                            <td class="subHeaderVertical">Height (in.):</td>
                                            <td><input name="height"></td>
                                        </tr>
                                        <tr>
                                            <td class="subHeaderVertical">Weight (lbs):</td>
                                            <td><input name="weight"></td>
                                        </tr>
                                        <tr>
                                            <td class="subHeaderVertical">Smoker?</td>
                                            <td><input type = "checkbox" name="isSmoker"></td>
                                        </tr>
                                    </table> <br>
                                </div>
                            </div>
                        </td>
                    </tr>
                    <tr height="15px">
                        <td colspan=3>&nbsp;</td>
                    </tr>
                </table>
                <div align= center">
                    <input type="submit" name = "action"
                           style = "display: block; margin: 0 auto;"
                           value = "Submit">
                </div>
            </form>
        </div>
    </td>
</tr>

</div>
<%      } %>
<%@include file="/footer.jsp"%>
<%
    }catch(FormValidationException e){
        e.printHTML(pageContext.getOut());
%>
<a href="preregister.jsp">
    <h2>Please Try Again</h2>
</a>
<%
    }
%>
