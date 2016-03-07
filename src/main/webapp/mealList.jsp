<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="ru.javawebinar.topjava.model.UserMealWithExceed" %>
<%@ page import="java.time.LocalDateTime" %>

<html>
<head>
    <title>Meal list</title>
</head>
<body>

<form id="addMealForm" method="post">

    <h2>Add a new meal</h2>

    <table border="0.5" bgcolor="black">
        <tr style="background-color: white; color: black">
            <td>Date: </td>
            <td><input type="text" value="YYYY-MM-DD" pattern="\d{4}\-\d{1,2}\-\d{1,2}" name="date"></td>
        </tr>
        <tr style="background-color: white; color: black">
            <td>Time: </td>
            <td><input type="text" value="HH:SS" pattern="\d{1,2}\:\d{1,2}"name="time"></td>
        </tr>
        <tr style="background-color: white; color: black">
            <td>Description: </td>
            <td><input type="text" name="description"></td>
        </tr>
        <tr style="background-color: white; color: black">
            <td>Calories: </td>
            <td><input type="text" pattern="^[0-9]+$" name="calories"></td>
        </tr>
        <tr style="background-color: white; color: black">
            <td colspan="2" align="center"><button type="submit" name="add" value="">Add</button></td>
        </tr>
    </table>
</form>

<h2>Meals</h2>

<table border="0.5" bgcolor="black">
    <tr style="background-color: white; color: black">
        <td>Date</td>
        <td>Description</td>
        <td>Calories</td>
        <td></td>
        <td></td>
    </tr>
    <%
        List<UserMealWithExceed> meals = (List<UserMealWithExceed>)request.getAttribute("meals");
    %>

    <c:forEach items="${meals}" var="item">

        <c:if test="${item.exceed == true}">
            <tr style="background-color: white; color: red">
        </c:if>

        <c:if test="${item.exceed == false}">
            <tr style="background-color: white; color: green">
        </c:if>

            <form id="mealListForm" method="post">
                <td>${item.localDateTime}</td>
                <td>${item.description}</td>
                <td>${item.calories}</td>
                <td><button type="submit" name="edit" value="${item.id}">Edit</button></td>
                <td><button type="submit" name="delete" value="${item.id}">Delete</button></td>
            </form>
        </tr>
    </c:forEach>
</table>

</body>
</html>
