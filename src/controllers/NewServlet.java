//new 新規作成用フォーム

package controllers;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Task;

/**
 * Servlet implementation class NewServlet
 */
@WebServlet("/new")
public class NewServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public NewServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //CSRF対策（セキュリティ対策）サイト外からPOST送信された投稿を拒否できる
        request.setAttribute("_token", request.getSession().getId());
        //Taskのインスタンスを生成してリクエストスコープに格納
        request.setAttribute("task", new Task());

        RequestDispatcher rd = request.getRequestDispatcher("WEB-INF/views/new.jsp");
        rd.forward(request, response);

    }

}
