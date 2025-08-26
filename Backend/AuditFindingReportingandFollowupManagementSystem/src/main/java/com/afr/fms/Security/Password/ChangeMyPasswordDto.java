package com.afr.fms.Security.Password;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

import lombok.Data;

@Data
public class ChangeMyPasswordDto {

	private Long id;
	
	@NotEmpty(message = "{oldpassword.required}")
	private String oldPassword;
	
	@NotEmpty(message = "{password.required}")
	@Size(min=5, message = "{password.min.length}")
	private String password;

	@NotEmpty(message = "{confirmPassword.required}")
	private String confirmPassword;
	
	@AssertTrue(message = "{confirmPassword.not.match}")
	private boolean isValid() {
		if (password == null) {
			return confirmPassword == null;
		} else {
			return password.equals(confirmPassword);
		}
	}
	
}
