package jp.co.sss.crud.filter;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import jp.co.sss.crud.entity.Employee;

/**
 * ログインチェック用フィルタ
 * 
 * @author System Shared
 */
public class LoginCheckFilter extends HttpFilter {

	@Override
	public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		// セッション情報を準備
		HttpSession session = request.getSession(false);
		Employee emp = null;
		// セッションが切れていなかったらユーザ情報を取得
		if (session != null) {
			emp = (Employee) session.getAttribute("user");
		}
		// ユーザーがNULLの場合、ログイン画面にリダイレクトする
		if (emp == null) {
			response.sendRedirect(request.getContextPath());
			return;
		}
		// 次の処理へ移行
		chain.doFilter(request, response);
		return;

	}
}
