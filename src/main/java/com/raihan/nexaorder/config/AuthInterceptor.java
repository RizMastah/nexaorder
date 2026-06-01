package com.raihan.nexaorder.config;

import com.raihan.nexaorder.enums.UserRole;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String path = request.getRequestURI();
        String contextPath = request.getContextPath();

        if (!contextPath.isBlank() && path.startsWith(contextPath)) {
            path = path.substring(contextPath.length());
        }

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(contextPath + "/auth/login");
            return false;
        }

        String userRole = (String) session.getAttribute("userRole");
        if (userRole == null) {
            response.sendRedirect(contextPath + "/auth/login");
            return false;
        }

        if (path.startsWith("/admin") && !UserRole.ADMIN.name().equals(userRole)) {
            response.sendRedirect(contextPath + "/customer/dashboard");
            return false;
        }

        if (path.startsWith("/customer") && !UserRole.CUSTOMER.name().equals(userRole)) {
            response.sendRedirect(contextPath + "/admin/dashboard");
            return false;
        }

        return true;
    }
}
