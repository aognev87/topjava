<%@ page import="ru.javawebinar.topjava.model.UserMeal" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>MealEdit</title>
</head>
<body>
    <h2>Meal Edit</h2>

    <%
        UserMeal meal = (UserMeal) request.getAttribute("meal");
        String date = (String) request.getAttribute("date");
        String time = (String) request.getAttribute("time");
    %>

    <form id="mealEditForm" method="post">
        <table border="0.5" bgcolor="black">
            <tr style="background-color: white; color: black">
                <td>Date: </td>
                <td><input type="text" value="${date}" pattern="\d{4}\-\d{1,2}\-\d{1,2}" name="date"></td>
            </tr>
            <tr style="background-color: white; color: black">
                <td>Time: </td>
                <td><input type="text" value="${time}" pattern="\d{1,2}\:\d{1,2}"name="time"></td>
            </tr>
            <tr style="background-color: white; color: black">
                <td>Description: </td>
                <td><input type="text" value="${meal.description}" name="description"></td>
            </tr>
            <tr style="background-color: white; color: black">
                <td>Calories: </td>
                <td><input type="text" value="${meal.calories}" pattern="^[0-9]+$" name="calories"></td>
            </tr>
            <tr style="background-color: white; color: black">
                <td colspan="2" align="center"><button type="submit" name="update" value="${meal.id}">Update</button></td>
            </tr>
        </table>
    </form>


</body>
</html>
