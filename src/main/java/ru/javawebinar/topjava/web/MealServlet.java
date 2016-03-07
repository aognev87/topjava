package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.util.UserMealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public class MealServlet extends HttpServlet {
    private static final Logger LOG = LoggerFactory.getLogger(MealServlet.class);

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LOG.debug("redirect to mealList");
        request.setAttribute("meals", UserMealsUtil.cachedMapMeals());
        request.getRequestDispatcher("/mealList.jsp").forward(request, response);
        //response.sendRedirect("mealList.jsp");
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        LOG.debug("mealList POST");

        if (req.getParameter("edit") != null) {

            UserMeal userMeal = UserMealsUtil.getUserMealFromMapById(req.getParameter("edit"));

            if (userMeal != null){
                String date = userMeal.getDateTime().toLocalDate().toString();
                String time = userMeal.getDateTime().toLocalTime().toString();
                req.setAttribute("meal", userMeal);
                req.setAttribute("date", date);
                req.setAttribute("time", time);

                req.getRequestDispatcher("/mealEdit.jsp").forward(req, resp);
            }
        }

        if (req.getParameter("update") != null) {
            UserMealsUtil.updateMealInMap(req.getParameter("update"),
                    req.getParameter("date"),
                    req.getParameter("time"),
                    req.getParameter("description"),
                    req.getParameter("calories"));

            req.setAttribute("meals", UserMealsUtil.cachedMapMeals());
            req.getRequestDispatcher("/mealList.jsp").forward(req, resp);
        }

        if (req.getParameter("delete") != null) {
            UserMealsUtil.deleteMealFromList(req.getParameter("delete"));
            req.setAttribute("meals", UserMealsUtil.cachedMapMeals());
            req.getRequestDispatcher("/mealList.jsp").forward(req, resp);
        }

        if (req.getParameter("add") != null){
            UserMealsUtil.addMealToMap(req.getParameter("date"),
                    req.getParameter("time"),
                    req.getParameter("description"),
                    req.getParameter("calories"));

            req.setAttribute("meals", UserMealsUtil.cachedMapMeals());
            req.getRequestDispatcher("/mealList.jsp").forward(req, resp);
        }
    }



}
