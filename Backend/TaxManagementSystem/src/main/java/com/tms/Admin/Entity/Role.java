package com.tms.Admin.Entity;

import java.util.Collection;
import com.tms.Common.Entity.Functionalities;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Role {

	private Long id;

	private String code;

	private String name;

	private String description;

	private Collection<Functionalities> functionalities;
	private Collection<JobPosition> jobPositions;
	private boolean status;
	private boolean dynamic_menu;
	private String role_position;
}
