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
 * 権限認証用フィルタ
 * 
 * @author System Shared
 */
public class AdminAccountCheckFilter extends HttpFilter {

	@Override
	public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		// URIと送信方式を取得する
		String requestURI = request.getRequestURI();
		String requestMethod = request.getMethod();

		// 完了画面はフィルターを通過させる
		if (requestURI.contains("/complete") && requestMethod.equals("GET")) {
			chain.doFilter(request, response);
			return;
		}

		//TODO セッションからユーザー情報を取得
		HttpSession session = request.getSession();
		//TODO セッションユーザーのIDと権限の変数をそれぞれ初期化
		Integer empid = -1;
		Integer authority = -1;
		//TODO セッションユーザーがNULLでない場合
		Employee sessionuser = (Employee) session.getAttribute("user");
		if (sessionuser != null) {
			//TODO セッションユーザーからID、権限を取得して変数に代入
			empid = sessionuser.getEmpId();
			authority = sessionuser.getAuthority();
		}

		//TODO  更新対象の社員IDをリクエストから取得 
		String strupdateempid = request.getParameter("empId");
		Integer intupdateumpid = null;
		//TODO  社員IDがNULLでない場合
		if (strupdateempid != null) {
			//TODO 社員IDを整数型に変換
			intupdateumpid = Integer.parseInt(strupdateempid);
		}

		//フィルター通過のフラグを初期化 true:フィルター通過 false:ログイン画面へ戻す
		boolean accessFlg = false;

		//TODO  管理者(セッションユーザーのIDが2)の場合、アクセス許可
		if (authority == 2) {
			accessFlg = true;
			//TODO  ログインユーザ自身(セッションユーザのIDと変更リクエストの社員IDが一致)の画面はアクセス許可
		} else if (empid == intupdateumpid) {
			accessFlg = true;
		}

		//TODO  accessFlgが立っていない場合はログイン画面へリダイレクトし、処理を終了する
		if (accessFlg == false) {
			//TODO  レスポンス情報を取得
			HttpServletResponse httpresponse = response;
			//TODO  ログイン画面へリダイレクト
			httpresponse.sendRedirect(request.getContextPath());
			//処理を終了
			return;
		}

		chain.doFilter(request, response);
		return;

	}

}
