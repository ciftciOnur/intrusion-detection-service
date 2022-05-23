package tr.edu.yeditepe.intrusiondetection.domain.model;


import javax.crypto.spec.SecretKeySpec;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginInfo {
	
	private String password;
	private String IV;
	private boolean isLogin;
	SecretKeySpec passwordKey;
	
	
    public LoginInfo() {
        password = "";
        IV="";
        isLogin=false;
        passwordKey=null;
    }
}
