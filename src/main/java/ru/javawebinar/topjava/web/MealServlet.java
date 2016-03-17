package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.repository.UserRepository;
import ru.javawebinar.topjava.repository.mock.InMemoryUserMealRepositoryImpl;
import ru.javawebinar.topjava.repository.UserMealRepository;
import ru.javawebinar.topjava.repository.mock.InMemoryUserRepositoryImpl;
import ru.javawebinar.topjava.util.UserMealsUtil;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * User: gkislin
 * Date: 19.08.2014
 */
public class MealServlet extends HttpServlet {
    private static final Logger LOG = LoggerFactory.getLogger(MealServlet.class);

    private UserMealRepository repository;
    private UserRepository userUepository;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        repository = new InMemoryUserMealRepositoryImpl();
        userUepository = new InMemoryUserRepositoryImpl();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        if (request.getParameterMap().containsKey("requestText")){
            LOG.info("getSearch");
            int userId = Integer.parseInt(request.getParameter("userId"));
            String userName = userUepository.get(userId).getName();
            String requestText = request.getParameter("requestText");

            request.setAttribute("mealList",
                    UserMealsUtil.getFilteredWithExceededWithSearch(repository.getAll(userId),
                            userUepository.getFilters(userId).getFromDate(),
                            userUepository.getFilters(userId).getToDate(),
                            userUepository.getFilters(userId).getFromTime(),
                            userUepository.getFilters(userId).getToTime(),
                            UserMealsUtil.DEFAULT_CALORIES_PER_DAY,
                            requestText));
            request.setAttribute("userId", userId);
            request.setAttribute("userName", userName);
            request.setAttribute("requestText", requestText);
            request.getRequestDispatcher("/mealList.jsp").forward(request, response);


            //response.sendRedirect("index.html");
        }else if (request.getParameterMap().containsKey("id")) {
            String id = request.getParameter("id");
            int userId = Integer.parseInt(request.getParameter("userId"));
            String userName = userUepository.get(userId).getName();

            UserMeal userMeal = new UserMeal(id.isEmpty() ? null : Integer.valueOf(id),
                    LocalDateTime.parse(request.getParameter("dateTime")),
                    request.getParameter("description"),
                    Integer.valueOf(request.getParameter("calories")),
                    userId);
            LOG.info(userMeal.isNew() ? "Create {}" : "Update {}", userMeal);
            repository.save(userMeal);

            request.setAttribute("mealList",
                    UserMealsUtil.getWithExceeded(repository.getAll(userId), UserMealsUtil.DEFAULT_CALORIES_PER_DAY));
            request.setAttribute("userId", userId);
            request.setAttribute("userName", userName);
            request.getRequestDispatcher("/mealList.jsp").forward(request, response);
        }
        //response.sendRedirect("meals");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        String strUserId = request.getParameter("userId");

        if (strUserId == null)
            request.getRequestDispatcher("users").forward(request, response);

        int userId = Integer.parseInt(strUserId);
        String userName = userUepository.get(userId).getName();

        if (action == null) {
            LOG.info("getAll");
            request.setAttribute("mealList",
                    UserMealsUtil.getWithExceeded(repository.getAll(userId), UserMealsUtil.DEFAULT_CALORIES_PER_DAY));
            request.setAttribute("userId", userId);
            request.setAttribute("userName", userName);
            request.getRequestDispatcher("/mealList.jsp").forward(request, response);
        } else if (action.equals("delete")) {
            int id = getId(request);
            LOG.info("Delete {}", id);
            repository.delete(id, userId);
            request.setAttribute("mealList",
                    UserMealsUtil.getWithExceeded(repository.getAll(userId), UserMealsUtil.DEFAULT_CALORIES_PER_DAY));
            request.setAttribute("userId", userId);
            request.setAttribute("userName", userName);
            request.getRequestDispatcher("/mealList.jsp").forward(request, response);
            //response.sendRedirect("meals");
        } else {
            final UserMeal meal = action.equals("create") ?
                    new UserMeal(LocalDateTime.now(), "", 1000, userId) :
                    repository.get(getId(request), userId);
            request.setAttribute("meal", meal);
            request.setAttribute("userId", userId);
            request.setAttribute("userName", userName);
            request.getRequestDispatcher("mealEdit.jsp").forward(request, response);
        }
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.valueOf(paramId);
    }
}
