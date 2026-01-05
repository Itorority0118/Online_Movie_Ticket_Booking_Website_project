package controller;

import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

@WebListener
public class OnlineUserListener implements HttpSessionListener {

    private static final Map<String, Set<String>> onlineUsersMap = new ConcurrentHashMap<>();

    public static synchronized void addUser(String email, HttpSession session) {
        session.setAttribute("email", email);
        onlineUsersMap.compute(email, (k, v) -> {
            if (v == null) v = new ConcurrentSkipListSet<>();
            v.add(session.getId());
            return v;
        });
        System.out.println("User login: " + email + " | Online Users: " + getOnlineUsersCount());
    }

    public static synchronized void removeSession(String email, String sessionId) {
        Set<String> sessions = onlineUsersMap.get(email);
        if (sessions != null) {
            sessions.remove(sessionId);
            if (sessions.isEmpty()) {
                onlineUsersMap.remove(email);
            }
            System.out.println("User logout: " + email + " | Online Users: " + getOnlineUsersCount());
        }
    }

    public static synchronized int getOnlineUsersCount() {
        return onlineUsersMap.size();
    }

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        System.out.println("Session created: " + se.getSession().getId());
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        HttpSession session = se.getSession();
        String email = (String) session.getAttribute("email");
        if (email != null) {
            removeSession(email, session.getId());
        }
        System.out.println("Session destroyed: " + session.getId() + " | Online Users: " + getOnlineUsersCount());
    }
}