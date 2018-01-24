package cn.edu.pku.sei.servlet;

import cn.edu.pku.sei.Annotator.PageParameter;
import cn.edu.pku.sei.pageAppend.LabelTaskAppender;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by oliver on 2017/8/8.
 */
public class LabelTaskServlet extends HttpServlet{

    public void doGet(HttpServletRequest request , HttpServletResponse response) throws ServletException , IOException{

    }

    public void doPost(HttpServletRequest request , HttpServletResponse response) throws ServletException, IOException {
        ServletContext context = getServletConfig().getServletContext();


        String requestType = request.getParameter("requestType");
        String userID = request.getParameter("userID");
        int pageIndex = Integer.parseInt(request.getParameter("pageIndex"));
        int dataIndex = Integer.parseInt(request.getParameter("dataIndex"));
        String annotation = request.getParameter("annotation");

        LabelTaskAppender appender = (LabelTaskAppender)request.getSession().getAttribute(userID + "_" + PageParameter.labelTaskAppender);

        if(requestType.equals("addAnnotation")){
            appender.addAnnotation(pageIndex , dataIndex , annotation);
        }else if(requestType.equals("deleteAnnotation")){
            appender.deleteAnnotation(pageIndex , dataIndex , annotation);
        }

        response.getWriter().print("");
        //response.sendRedirect("userMainPage.jsp");
        //response.sendRedirect("labelTask.jsp?pageIndex=" + pageIndex + "&" + "dataIndex=" + dataIndex);

        /*RequestDispatcher dispatcher = request.getRequestDispatcher("userMainPage.jsp");
        dispatcher.forward(request , response);*/
        RequestDispatcher dispatcher = request.getRequestDispatcher("labelTask.jsp?pageIndex=" + pageIndex + "&" + "dataIndex=" + dataIndex );
        dispatcher.forward(request , response);

    }
}
