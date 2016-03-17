package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.service.UserMealService;
import ru.javawebinar.topjava.service.UserService;
import ru.javawebinar.topjava.util.UserMealsUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Map;

/**
 * GKislin
 * 06.03.2015.
 */
@Controller
public class UserMealRestController {
    private static final Logger LOG = LoggerFactory.getLogger(UserMealRestController.class);
    @Autowired
    private UserMealService mealService;
    @Autowired
    private UserService userService;


    @RequestMapping("users")
    public ModelAndView getUserList(Map<String, Object> model){
        LOG.info("getUserList");
        model.put("userList", userService.getAll());
        return new ModelAndView("userList");
    }

    @RequestMapping("meals")
    public ModelAndView mealListGET(HttpServletRequest request, Map<String, Object> model){
        LOG.info("meals");

        String userIdStr = request.getParameter("userId");

        if (userIdStr == null){
            model.put("userList", userService.getAll());
            return new ModelAndView("userList");
        }

        int userId = Integer.parseInt(userIdStr);
        String userName = userService.get(userId).getName();
        String action = request.getParameter("action");

        if (action == null) {
            LOG.info("getAll");
            model.put("mealList",
                    UserMealsUtil.getWithExceeded(mealService.getRepository().getAll(userId), UserMealsUtil.DEFAULT_CALORIES_PER_DAY));
            model.put("userId", userId);
            model.put("userName", userName);
        } else if (action.equals("delete")) {
            int mealId = Integer.parseInt(request.getParameter("mealId"));
            LOG.info("Delete {}", mealId);
            mealService.getRepository().delete(mealId, userId);
            model.put("mealList",
                    UserMealsUtil.getWithExceeded(mealService.getRepository().getAll(userId), UserMealsUtil.DEFAULT_CALORIES_PER_DAY));
            model.put("userId", userId);
            model.put("userName", userName);
        } else {
            final UserMeal meal = action.equals("create") ?
                    new UserMeal(LocalDateTime.now(), "", 1000, userId) :
                    mealService.getRepository().get(Integer.parseInt(request.getParameter("mealId")), userId);
            model.put("meal", meal);
            model.put("userId", userId);
            model.put("userName", userName);
            return new ModelAndView("mealEdit");
        }



        return new ModelAndView("mealList");
    }

    @RequestMapping("mealEdit")
    public ModelAndView mealEdit(HttpServletRequest request, Map<String, Object> model){
        try {
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String id = request.getParameter("id");
        int userId = Integer.parseInt(request.getParameter("userId"));
        String userName = userService.get(userId).getName();

        UserMeal userMeal = new UserMeal(id.isEmpty() ? null : Integer.valueOf(id),
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.valueOf(request.getParameter("calories")),
                userId);
        LOG.info(userMeal.isNew() ? "Create {}" : "Update {}", userMeal);
        mealService.getRepository().save(userMeal);

        request.setAttribute("mealList",
                UserMealsUtil.getWithExceeded(mealService.getRepository().getAll(userId), UserMealsUtil.DEFAULT_CALORIES_PER_DAY));
        request.setAttribute("userId", userId);
        request.setAttribute("userName", userName);

        return new ModelAndView("mealList");
    }

    @RequestMapping("filterMealByName")
    public ModelAndView filterMealByName (HttpServletRequest request){
        try {
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        LOG.info("filterByName");
        int userId = Integer.parseInt(request.getParameter("userId"));
        String userName = request.getParameter("userName");
        String filterText = request.getParameter("filterText");

        userService.getRepository().getFilters(userId).setDescriptionFilter(filterText);


        request.setAttribute("mealList",
                UserMealsUtil.getFilteredWithExceededWithSearch(mealService.getRepository().getAll(userId),
                        userService.getRepository().getFilters(userId).getFromDate(),
                        userService.getRepository().getFilters(userId).getToDate(),
                        userService.getRepository().getFilters(userId).getFromTime(),
                        userService.getRepository().getFilters(userId).getToTime(),
                        UserMealsUtil.DEFAULT_CALORIES_PER_DAY,
                        userService.getRepository().getFilters(userId).getDescriptionFilter()));

        request.setAttribute("userId", userId);
        request.setAttribute("userName", userName);

        request.setAttribute("fromDate", userService.getRepository().getFilters(userId).getFromDate());
        request.setAttribute("toDate", userService.getRepository().getFilters(userId).getToDate());
        request.setAttribute("fromTime", userService.getRepository().getFilters(userId).getFromTime());
        request.setAttribute("toTime", userService.getRepository().getFilters(userId).getToTime());
        request.setAttribute("filterText", userService.getRepository().getFilters(userId).getDescriptionFilter());

        return new ModelAndView("mealList");
    }

    @RequestMapping("filterMealByDate")
    public ModelAndView filterMealByDate (HttpServletRequest request){
        try {
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        LOG.info("filterByDate");
        int userId = Integer.parseInt(request.getParameter("userId"));
        String userName = request.getParameter("userName");

        try {
            userService.getRepository().getFilters(userId).setFromDate(LocalDate.parse(request.getParameter("fromDate")));
        } catch (Exception e) {
            userService.getRepository().getFilters(userId).setFromDate(null);
        }

        try {
            userService.getRepository().getFilters(userId).setToDate(LocalDate.parse(request.getParameter("toDate")));
        } catch (Exception e) {
            userService.getRepository().getFilters(userId).setToDate(null);

        }

        try {
            userService.getRepository().getFilters(userId).setFromTime(LocalTime.parse(request.getParameter("fromTime")));
        } catch (Exception e) {
            userService.getRepository().getFilters(userId).setFromTime(null);
        }

        try {
            userService.getRepository().getFilters(userId).setToTime(LocalTime.parse(request.getParameter("toTime")));
        } catch (Exception e) {
            userService.getRepository().getFilters(userId).setToTime(null);
        }


        request.setAttribute("mealList",
                UserMealsUtil.getFilteredWithExceededWithSearch(mealService.getRepository().getAll(userId),
                        userService.getRepository().getFilters(userId).getFromDate(),
                        userService.getRepository().getFilters(userId).getToDate(),
                        userService.getRepository().getFilters(userId).getFromTime(),
                        userService.getRepository().getFilters(userId).getToTime(),
                        UserMealsUtil.DEFAULT_CALORIES_PER_DAY,
                        userService.getRepository().getFilters(userId).getDescriptionFilter()));

        request.setAttribute("userId", userId);
        request.setAttribute("userName", userName);

        request.setAttribute("fromDate", userService.getRepository().getFilters(userId).getFromDate());
        request.setAttribute("toDate", userService.getRepository().getFilters(userId).getToDate());
        request.setAttribute("fromTime", userService.getRepository().getFilters(userId).getFromTime());
        request.setAttribute("toTime", userService.getRepository().getFilters(userId).getToTime());
        request.setAttribute("filterText", userService.getRepository().getFilters(userId).getDescriptionFilter());

        return new ModelAndView("mealList");
    }

}
