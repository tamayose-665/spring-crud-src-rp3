package jp.co.sss.crud.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.sss.crud.entity.Employee;
import jp.co.sss.crud.form.LoginForm;
import jp.co.sss.crud.mapper.EmployeeMapper;
import jp.co.sss.crud.util.Constant;
import jp.co.sss.crud.util.LoginErrorType;

/**
 * ログイン処理
 */
@Service
public class LoginService {

	@Autowired
	private EmployeeMapper mapper;

	/**
	 * ログイン処理
	 * 
	 * mapper#findByEmpIdAndEmpPassメソッドを呼び出し、DBから該当社員を取得する。
	 * 取得した社員オブジェクトがnullの場合はログイン失敗、そうでない場合はログイン成功としてLoginResultのメソッドを呼び出す。
	 * 
	 * @return LoginResult ログイン失敗時はLoginResult.failLogin,ログイン成功時はLoginResult.succeedLoginを呼び出す。
	 */
	public LoginResult execute(LoginForm loginForm) {

		// Formの中身を受け取る
		Integer empId = loginForm.getEmpId();
		String empPass = loginForm.getEmpPass();
		// Formの中身を基に社員情報を取得
		Employee emp = mapper.findByEmpIdAndEmpPass(empId, empPass);
		// 該当社員は存在したか
		if (emp != null) {
			// ログイン成功
			return LoginResult.succeedLogin(emp);
		} else {
			// ログイン失敗
			return LoginResult.failLogin(Constant.LOGIN_ERR_MSG, LoginErrorType.USER_NOT_FOUND);
		}
	}
}
