package org.mock.mock;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import static org.mockito.Mockito.*;

public class testATM {
	public ATM ATMobject;
	public Card unblockedCard;
	public Card blockedCard;
	public Account currentAccount;
	public double currentCardBalance;
	public double sumInATM;
	double sumToGet;

	@Before
	public void setUp() throws Exception {
		sumInATM = 1000;
		ATMobject = new ATM(sumInATM);
	}

	@Test
	public void getMoneyInATM_RightSum() throws Exception {
		assertEquals(sumInATM, ATMobject.getMoneyInATM(), 0.1);
	}

	@Test(expected = Exception.class)
	public void getMoneyInATM_WrongSum() throws Exception {
		sumInATM = -8;
		ATMobject = new ATM(sumInATM);
		assertEquals(sumInATM, ATMobject.getMoneyInATM(), 0.1);
	}

	@Test
	public void validateCard_UnblockedCardAndRightPass() throws Exception {
		unblockedCard = Mockito.mock(Card.class);
		Mockito.when(unblockedCard.isBlocked()).thenReturn(false);
		Mockito.when(unblockedCard.checkPin(1)).thenReturn(true);
		assertTrue(ATMobject.validateCard(unblockedCard, 1));
		verify(unblockedCard, atLeastOnce()).isBlocked();
		verify(unblockedCard, atLeastOnce()).checkPin(1);
	}

	@Test(expected = NoCardInserted.class)
	public void validateCard_UnblockedCardAndWrongPass() throws Exception {
		unblockedCard = Mockito.mock(Card.class);
		Mockito.when(unblockedCard.isBlocked()).thenReturn(false);
		Mockito.when(unblockedCard.checkPin(0)).thenReturn(false);
		assertFalse(ATMobject.validateCard(unblockedCard, 0));
	}

	@Test(expected = NoCardInserted.class)
	public void validateCard_blockedCardAndRightPass() throws Exception {

		blockedCard = Mockito.mock(Card.class);
		Mockito.when(blockedCard.isBlocked()).thenReturn(true);
		Mockito.when(blockedCard.checkPin(1)).thenReturn(true);
		ATMobject.validateCard(blockedCard, 1);
	}

	@Test(expected = NoCardInserted.class)
	public void validateCard_blockedCardAndWrongPass() throws Exception {
		blockedCard = Mockito.mock(Card.class);
		Mockito.when(blockedCard.isBlocked()).thenReturn(true);
		Mockito.when(blockedCard.checkPin(0)).thenReturn(false);
		ATMobject.validateCard(blockedCard, 0);
	}

	@Test
	public void checkBalance_ValidCard() throws Exception {
		currentCardBalance = 47;
		unblockedCard = Mockito.mock(Card.class);
		currentAccount = Mockito.mock(Account.class);
		Mockito.when(unblockedCard.getAccount()).thenReturn(currentAccount);
		Mockito.when(currentAccount.getBalance())
				.thenReturn(currentCardBalance);
		assertEquals(currentCardBalance, ATMobject.checkBalance(unblockedCard),
				0.0001);
	}

	@Test(expected = NoCardInserted.class)
	public void checkBalance_InValidCard() throws Exception {
		currentCardBalance = 47;
		blockedCard = Mockito.mock(Card.class);
		currentAccount = Mockito.mock(Account.class);
		Mockito.when(blockedCard.getAccount()).thenReturn(currentAccount);
		Mockito.when(currentAccount.getBalance())
				.thenReturn(currentCardBalance);
		ATMobject.validateCard(blockedCard, 1);
		ATMobject.checkBalance(blockedCard);
	}

	@Test
	public void getCash_UnblockedCardEnoughMoney() throws Exception {
		sumToGet = 45;
		currentCardBalance = 47;
		currentAccount = Mockito.mock(Account.class);
		unblockedCard = Mockito.mock(Card.class);
		Mockito.when(unblockedCard.getAccount()).thenReturn(currentAccount);
		Mockito.when(currentAccount.withdraw(sumToGet)).thenReturn(sumToGet);
		Mockito.when(currentAccount.getBalance())
				.thenReturn(currentCardBalance);
		assertEquals(ATMobject.checkBalance(unblockedCard),
				ATMobject.getCash(sumToGet), 0.001);
		assertEquals(sumInATM - sumToGet, ATMobject.getMoneyInATM(), 0.001);
		verify(currentAccount, atLeastOnce()).getBalance();
	}

	@Test(expected = NotEnoughMoneyInAccount.class)
	public void getCash_NotEnoughMoneyInAccountException() throws Exception {
		sumToGet = 48;
		currentCardBalance = 47;
		currentAccount = Mockito.mock(Account.class);
		unblockedCard = Mockito.mock(Card.class);
		Mockito.when(unblockedCard.getAccount()).thenReturn(currentAccount);
		Mockito.when(currentAccount.withdraw(sumToGet)).thenReturn(sumToGet);
		Mockito.when(currentAccount.getBalance())
				.thenReturn(currentCardBalance);
		Mockito.when(unblockedCard.isBlocked()).thenReturn(false);
		Mockito.when(unblockedCard.checkPin(1)).thenReturn(true);
		ATMobject.validateCard(unblockedCard, 1);
		ATMobject.getCash(sumToGet);
	}

	@Test(expected = NotEnoughMoneyInATM.class)
	public void getCash_NotEnoughMoneyInATMException() throws Exception {
		sumToGet = 1200;
		currentCardBalance = 1500;
		currentAccount = Mockito.mock(Account.class);
		unblockedCard = Mockito.mock(Card.class);
		Mockito.when(unblockedCard.getAccount()).thenReturn(currentAccount);
		Mockito.when(currentAccount.withdraw(sumToGet)).thenReturn(sumToGet);
		Mockito.when(currentAccount.getBalance())
				.thenReturn(currentCardBalance);
		Mockito.when(unblockedCard.isBlocked()).thenReturn(false);
		Mockito.when(unblockedCard.checkPin(1)).thenReturn(true);
		ATMobject.validateCard(unblockedCard, 1);
		ATMobject.getCash(sumToGet);
	}
}
