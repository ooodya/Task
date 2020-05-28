package com.zaycevImaginaryCompany.glebTask1.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zaycevImaginaryCompany.glebTask1.domain.Account;
import com.zaycevImaginaryCompany.glebTask1.domain.User;
import com.zaycevImaginaryCompany.glebTask1.repository.AccountRepository;
import com.zaycevImaginaryCompany.glebTask1.repository.UserRepository;

@Service
@Transactional
public class AccountServiceImpl implements AccountService
{
	@Autowired
	private AccountRepository accountRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Override
	@Transactional(readOnly = true)
	public Optional<Account> findByAccountNumber(long accountNumber)
	{
		return accountRepository.findByAccountNumber(accountNumber);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Account> findAll()
	{
		List<Account> accounts = new ArrayList<>();
		accountRepository.findAll().forEach(accounts::add);
		return accounts;
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<Account> findById(Long id)
	{
		return accountRepository.findById(id);
	}

	@Override
	public Account save(Account account)
	{
		Optional<User> owner = userRepository.findByUsername(account.getOwner().getUsername());
		if (!owner.isPresent())
		{
			userRepository.save(account.getOwner());
		}
		
		return accountRepository.save(account);
	}

	@Override
	public void delete(Account account)
	{
		accountRepository.delete(account);
	}

}