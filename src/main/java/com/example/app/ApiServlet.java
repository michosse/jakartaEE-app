package com.example.app;

import com.example.app.controllers.UserController;
import com.example.app.exceptions.HttpRequestException;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@WebServlet(urlPatterns = {
        ApiServlet.Paths.API + "/*"
})
@MultipartConfig(maxFileSize = 200 * 1024)
public class ApiServlet extends HttpServlet {

    private UserController userController;
    public static final class Paths {

        /**
         * All API operations. Version v1 will be used to distinguish from other implementations.
         */
        public static final String API = "/api";

    }

    public static final class Patterns {
        private static final Pattern UUID = Pattern.compile("[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}");
        public static final Pattern USERS = Pattern.compile("/users/?");
        public static final Pattern USER = Pattern.compile("/users/(%s)".formatted(UUID.pattern()));
        public static final Pattern USERS_AVATAR = Pattern.compile("/users/(%s)/avatar".formatted(UUID.pattern()));
    }
    private final Jsonb jsonb = JsonbBuilder.create();

    @Override
    public void init() throws ServletException {
        super.init();
        userController = (UserController) getServletContext().getAttribute("userController");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo() != null ? req.getPathInfo() : "";
        String servletPath = req.getServletPath();
        if(Paths.API.equals(servletPath)){
            if(path.matches(Patterns.USER.pattern())){
                resp.setContentType("application/json");
                UUID id = extractUuid(Patterns.USER, path);
                resp.getWriter().write(jsonb.toJson(userController.getUser(id)));
                return;
            } else if (path.matches(Patterns.USERS.pattern())) {
                resp.setContentType("application/json");
                resp.getWriter().write(jsonb.toJson(userController.getUsers()));
                return;
            }
            else if (path.matches(Patterns.USERS_AVATAR.pattern())) {
                resp.setContentType("image/png");
                UUID id = extractUuid(Patterns.USERS_AVATAR, path);
                byte[] avatar = userController.getUsersAvatar(id);
                resp.setContentLength(avatar.length);
                resp.getOutputStream().write(avatar);
                return;
            }
        }
        resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo() != null ? req.getPathInfo() : "";
        String servletPath = req.getServletPath();
        if(Paths.API.equals(servletPath)){
            if(path.matches(Patterns.USERS_AVATAR.pattern())){
                UUID id = extractUuid(Patterns.USERS_AVATAR, path);
                userController.putUsersAvatar(id, req.getPart("avatar").getInputStream().readAllBytes());
                return;
            }
        }
        resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo() != null ? req.getPathInfo() : "";
        String servletPath = req.getServletPath();
        if(Paths.API.equals(servletPath)){
            if(path.matches(Patterns.USERS_AVATAR.pattern())){
                UUID id = extractUuid(Patterns.USERS_AVATAR, path);
                userController.deleteUsersAvatar(id);
                return;
            }
        }
        resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
    }

    private static UUID extractUuid(Pattern pattern, String path) {
        Matcher matcher = pattern.matcher(path);
        if (matcher.matches()) {
            return UUID.fromString(matcher.group(1));
        }
        throw new HttpRequestException("No UUID in path.",400);
    }
}
