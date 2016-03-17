package ru.javawebinar.topjava.repository.mock;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;
import ru.javawebinar.topjava.util.UserUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Антон on 13.03.2016.
 */
@Repository
public class InMemoryUserRepositoryImpl implements UserRepository {

    private Map<Integer, UserMealFilters> filters = new ConcurrentHashMap<>();
    private Map<Integer, User> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    {
        UserUtil.USER_LIST.forEach(this::save);
    }

    public InMemoryUserRepositoryImpl() {
    }

    @Override
    public User save(User user) {
        if (user.isNew()){
            user.setId(counter.incrementAndGet());
        }
        filters.put(user.getId(), new UserMealFilters());
        repository.put(user.getId(), user);
        return user;
    }

    @Override
    public boolean delete(int id) {
        return repository.remove(id) != null;
    }

    @Override
    public User get(int id) {
        return repository.get(id);
    }

    @Override
    public User getByEmail(String email) {
        for (Map.Entry<Integer, User> pair : repository.entrySet()){
            if (pair.getValue().getEmail().equalsIgnoreCase(email))
                return pair.getValue();
        }
        return null;
    }

    @Override
    public List<User> getAll() {
        List<User> result = new ArrayList<>(repository.values());

        Collections.sort(result, new Comparator<User>() {
            @Override
            public int compare(User o1, User o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });

        return result;
    }

    @Override
    public UserMealFilters getFilters(int id) {
        return filters.get(id);
    }

    public static class UserMealFilters {
        private LocalDate fromDate = null;
        private LocalDate toDate = null;
        private LocalTime fromTime = null;
        private LocalTime toTime = null;
        private String descriptionFilter = "";

        public LocalDate getFromDate() {
            return fromDate;
        }

        public LocalDate getToDate() {
            return toDate;
        }

        public LocalTime getFromTime() {
            return fromTime;
        }

        public LocalTime getToTime() {
            return toTime;
        }

        public String getDescriptionFilter() {
            return descriptionFilter;
        }

        public void setFromDate(LocalDate fromDate) {
            this.fromDate = fromDate;
        }

        public void setToDate(LocalDate toDate) {
            this.toDate = toDate;
        }

        public void setFromTime(LocalTime fromTime) {
            this.fromTime = fromTime;
        }

        public void setToTime(LocalTime toTime) {
            this.toTime = toTime;
        }

        public void setDescriptionFilter(String descriptionFilter) {
            this.descriptionFilter = descriptionFilter;
        }
    }
}
