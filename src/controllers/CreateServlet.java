package controllers;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Task;
import models.TaskValidator;
import utils.DBUtil;

/**
 * Servlet implementation class CreateServlet
 */
@WebServlet("/create")
public class CreateServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public CreateServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String _token = request.getParameter("_token");
        //CSRF対策チェック
        if (_token != null && _token.equals(request.getSession().getId())) {
            EntityManager em = DBUtil.createEntityManager();

            Task t = new Task();

            String content = request.getParameter("content");
            t.setContent(content);

            //現在日時の情報を持つ日付型のオブジェクトを取得
            Timestamp currentTime = new Timestamp(System.currentTimeMillis());
            t.setCreated_at(currentTime); //カラムにセット
            t.setUpdated_at(currentTime);

            //バリデーションを実行してエラーがあったら新規作成フォームに戻る
            List<String> errors = TaskValidator.validate(t);
            if (errors.size() > 0) {
                em.close();

                //フォームに初期値を設定しエラーメッセージを送る
                request.setAttribute("_token", request.getSession().getId());
                request.setAttribute("task", t);
                request.setAttribute("errors", errors);

                RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/new.jsp");
                rd.forward(request, response);
            } else {
                em.getTransaction().begin();
                em.persist(t); //Taskクラスのオブジェクトをデータベースにセーブ
                em.getTransaction().commit();
                request.getSession().setAttribute("flush", "新しいタスクを作成しました。");
                em.close();

                //indexページへ遷移

                response.sendRedirect(request.getContextPath() + "/index");

            }
        }

    }
}