package by.training.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import by.training.action.DataAction;
import by.training.action.LoadDataAction;

@WebServlet(name = "page", urlPatterns = {"/dashboard/show", "/dashboard/add", "/dashboard/modify",
        "/widget/show", "/widget/add", "/widget/modify", "/chart"})
public class PageServlet extends AbstractServlet {

    private static final long serialVersionUID = 5306106036240520824L;

    @Override
    protected void performTask(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException, IOException {
        LoadDataAction.load(request, response);
        request.getRequestDispatcher(DataAction.getPath(request)).forward(request, response);
    }

}
