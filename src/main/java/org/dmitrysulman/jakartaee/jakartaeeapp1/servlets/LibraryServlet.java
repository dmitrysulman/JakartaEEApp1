package org.dmitrysulman.jakartaee.jakartaeeapp1.servlets;

import org.dmitrysulman.jakartaee.jakartaeeapp1.config.TemplateEngineUtil;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "libraryServlet", value = "/library")
public class LibraryServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter printWriter = response.getWriter();
        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(request.getServletContext());
        WebContext context = new WebContext(request, response, request.getServletContext());
//        context.setVariable("recipient", "World");

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        try (Statement statement = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/java_ee_db", "postgres", "admin")
                .createStatement()) {

            ResultSet resultSet = statement.executeQuery("SELECT title FROM books");
            List<String> books = new ArrayList<>();
            while (resultSet.next()) {
//                printWriter.println(resultSet.getString("title"));
                books.add(resultSet.getString("title"));
            }
            context.setVariable("books", books);
            engine.process("library.html", context, printWriter);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
