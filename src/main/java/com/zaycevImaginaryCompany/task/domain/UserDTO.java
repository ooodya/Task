package com.zaycevImaginaryCompany.task.domain;

import java.util.HashSet;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO
{
	private String firstname;
	
	private String lastname;
	
	private String username;
	
	private String password;
	
	private Set<AccountDTO> accountDTOs = new HashSet<>();
}
