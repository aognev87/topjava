package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
        request.setAttribute("meals", UserMealsUtil.cachedListMeals());
        request.getRequestDispatcher("/mealList.jsp").forward(request, response);
        //response.sendRedirect("mealList.jsp");
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //System.out.println(req.getParameter("update"));//It works
        System.out.println(req.getParameter("delete"));//It works
        LOG.debug("mealList POST");

        if (req.getParameter("update") != null)
            resp.sendRedirect("mealEdit.jsp");

        if (req.getParameter("delete") != null) {
            UserMealsUtil.deleteMealFromList(req.getParameter("delete"));
            req.setAttribute("meals", UserMealsUtil.cachedListMeals());
            req.getRequestDispatcher("/mealList.jsp").forward(req, resp);
        }

        if (req.getParameter("add") != null){
            UserMealsUtil.addMealToList(req.getParameter("date"),
                    req.getParameter("time"),
                    req.getParameter("description"),
                    req.getParameter("calories"));

            req.setAttribute("meals", UserMealsUtil.cachedListMeals());
            req.getRequestDispatcher("/mealList.jsp").forward(req, resp);
        }

    }



}
