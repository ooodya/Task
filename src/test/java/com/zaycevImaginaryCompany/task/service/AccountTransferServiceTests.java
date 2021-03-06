package com.zaycevImaginaryCompany.task.service;

import com.zaycevImaginaryCompany.task.dto.AccountDTO;
import com.zaycevImaginaryCompany.task.dto.AccountDTOLite;
import com.zaycevImaginaryCompany.task.dto.UserDTO;
import com.zaycevImaginaryCompany.task.exceptions.AccountNotFoundException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class AccountTransferServiceTests
{
    @Autowired
    private UserCRUDService userCRUDService;

    @Autowired
    private AccountCRUDService accountCRUDService;

    @Autowired
    private AccountTransferService transferService;

    private final Long destinationAccountNumber = 1L;
    private final int destinationAmount = 1000;
    private final Long sourceAccountNumber = 2L;
    private final int sourceAmount = 2000;

    private final AccountDTOLite accountDTOLite1 = new AccountDTOLite(destinationAccountNumber, destinationAmount);
    private final AccountDTOLite accountDTOLite2 = new AccountDTOLite(sourceAccountNumber, sourceAmount);
    private final UserDTO userDTO = new UserDTO("firstame", "lastname", "username", "password",
            Set.of(accountDTOLite1, accountDTOLite2));

    @BeforeEach
    public void setUp()
    {
        userCRUDService.create(userDTO);
    }

    @AfterEach
    public void cleanUp()
    {
        userCRUDService.delete(userDTO);
    }

    @Test
    @DisplayName("Transfer fails if transfer amount < 0")
    public void transferFailsIfNegativeAmount()
    {
        assertFalse(transferService.transfer(destinationAccountNumber, sourceAccountNumber, -100).isPresent());
    }

    @Test
    @DisplayName("Transfer throws AccountNotFoundException if no source account")
    public void transferThrowsAccountNotFoundExceptionIfNoSuchSourceExists()
    {
        assertThrows(AccountNotFoundException.class, () -> transferService.transfer(destinationAccountNumber, 4656234234L, 100));
    }

    @Test
    @DisplayName("Transfer throws AccountNotFoundException if no destination account")
    public void transferThrowsAccountNotFoundExceptionIfNoSuchDestinationExists()
    {
        assertThrows(AccountNotFoundException.class, () -> transferService.transfer(867435233L, sourceAccountNumber, 100));
    }

    @Test
    @DisplayName("Transfer fails if source dont have enough money")
    public void transferFailsIfSourceHasNotEnoughMoney()
    {
        assertFalse(transferService.transfer(destinationAccountNumber, sourceAccountNumber, sourceAmount + 1000).isPresent());
    }

    @Test
    @DisplayName("Money can be transferred")
    public void canBeTransferred()
    {
        int amount = 200;

        assertTrue(transferService.transfer(destinationAccountNumber, sourceAccountNumber, amount).isPresent());

        Optional<AccountDTO> sourceAccountDTO = accountCRUDService.findByAccountNumber(sourceAccountNumber);
        Optional<AccountDTO> destinationAccountDTO = accountCRUDService.findByAccountNumber(destinationAccountNumber);

        assertEquals(sourceAmount - amount, sourceAccountDTO.get().getAmount());
        assertEquals(destinationAmount + amount, destinationAccountDTO.get().getAmount());
    }

    @Test
    @DisplayName("Cant add negative amount")
    public void addingMoneyFailsIfNegativeAmount()
    {
        assertFalse(transferService.addMoney(sourceAccountNumber, -100).isPresent());
    }

    @Test
    @DisplayName("Adding money throws AccountNotFoundException if no source account")
    public void addingMoneyThrowsAccountNotFoundExceptionIfNoSuchSourceExists()
    {
        assertThrows(AccountNotFoundException.class, () -> transferService.addMoney(1341244324L, 1000));
    }

    @Test
    @DisplayName("Money can be added to account")
    public void canAddMoneyToAccount()
    {
        transferService.addMoney(sourceAccountNumber, 1000);

        final Optional<AccountDTO> accountDTOOptional = accountCRUDService.findByAccountNumber(sourceAccountNumber);

        assertEquals(sourceAmount + 1000, accountDTOOptional.get().getAmount());
    }
}
