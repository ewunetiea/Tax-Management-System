package com.tms.Security.Password;



import org.hibernate.validator.constraints.NotEmpty;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ChangePasswordDto {

	private Long id;
	
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
