<%@ page import="ru.javawebinar.topjava.util.TimeUtil" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <title>Meal list</title>
    <style>
        .normal {
            color: green;
        }

        .exceeded {
            color: red;
        }
    </style>

    <script type="text/javascript">
        function search(){
            document.getElementById('filterMealByName').submit();
        }

        function setCaretPosition(elemId)
        {
            var ctrl = document.getElementById(elemId);
            var pos = ctrl.value.length;
            if(ctrl.setSelectionRange)
            {
                ctrl.focus();
                ctrl.setSelectionRange(pos,pos);
            }
            else if (ctrl.createTextRange) {
                var range = ctrl.createTextRange();
                range.collapse(true);
                range.moveEnd('character', pos);
                range.moveStart('character', pos);
                range.select();
            }
        }
    </script>
</head>
<body>
<section>
    <h2><a href="index.html">Home</a></h2>
    <h3>Meal list for  ${userName}</h3>
    <a href="meals?action=create&userId=${userId}">Add Meal</a>
    <hr>
    <form method="post" id="filterMealByDate" action="filterMealByDate">
        <input type="hidden" name="userId" value="${userId}">
        <input type="hidden" name="userName" value="${userName}">
        <table>
            <tr>
                <td>From date: <input type="date" name="fromDate" value="${fromDate}"></td>
                <td>To date: <input type="date" name="toDate" value="${toDate}"></td>

            </tr>
            <tr>
                <td>From time: <input type="time" name="fromTime" value="${fromTime}"></td>
                <td>To time: <input type="time" name="toTime" value="${toTime}"></td>
            </tr>
            <tr>
                <td></td>
                <td align="right"><input type="submit" name="Filter"></td>
            </tr>
        </table>
    </form>
    <hr>
    <form method="post" id="filterMealByName" action="filterMealByName">
        <input type="hidden" name="userId" value="${userId}">
        <input type="hidden" name="userName" value="${userName}">
        Search: <input autofocus type="text" id="filterBox" name="filterText" value="${filterText}" onfocus="setCaretPosition('filterBox')" onkeyup="search()">
    </form>
    <hr>
    <table border="1" cellpadding="8" cellspacing="0">
        <thead>
        <tr>
            <th>Date</th>
            <th>Description</th>
            <th>Calories</th>
            <th></th>
            <th></th>
        </tr>
        </thead>
        <c:if test="${empty mealList}">
            <th colspan="5"> No matching records found</th>
        </c:if>
        <c:forEach items="${mealList}" var="meal">
            <jsp:useBean id="meal" scope="page" type="ru.javawebinar.topjava.model.UserMealWithExceed"/>
            <tr class="${meal.exceed ? 'exceeded' : 'normal'}">
                <td>
                        ${meal.dateTime.toLocalDate()} ${meal.dateTime.toLocalTime()}
                        <%--<%=TimeUtil.toString(meal.getDateTime())%>--%>
                </td>
                <td>${meal.description}</td>
                <td>${meal.calories}</td>
                <td><a href="meals?action=update&mealId=${meal.id}&userId=${userId}">Update</a></td>
                <td><a href="meals?action=delete&mealId=${meal.id}&userId=${userId}">Delete</a></td>
            </tr>
        </c:forEach>
    </table>
</section>
</body>
</html>