package org.mock.mock;

public class ATM {
	private double moneyInATM;
	private boolean validCard = true;
	private Card currentCard;

	// Можно задавать количество денег в банкомате
	public ATM(double moneyInATM) throws Exception {
		try{
			if (moneyInATM<=0) throw new Exception();
		} catch (Exception ex){
			throw ex;
		}
		this.moneyInATM = moneyInATM;
		
	}

	public double getMoneyInATM() {
		return moneyInATM;
	}

	// С вызова данного метода начинается работа с картой
	// Метод принимает карту и пин-код, проверяет пин-код карты и не
	// заблокирована ли она
	// Если неправильный пин-код или карточка заблокирована, возвращаем false.
	// При этом, вызов всех последующих методов у ATM с данной картой должен генерировать
	// исключение NoCardInserted

	public boolean validateCard(Card card, int pinCode) throws NoCardInserted {
		currentCard = card;
		try {
			validCard = !(currentCard.isBlocked());
			if (!validCard){
				throw new NoCardInserted();
			} else {
			validCard = currentCard.checkPin(pinCode);
			if (!validCard)
				throw new NoCardInserted();
			}
		} catch (NoCardInserted ex) {
			throw ex;
		}
		return validCard;
	}

	// Возвращает сколько денег есть на счету

	public double checkBalance(Card card) throws NoCardInserted {
		currentCard=card;
		return currentCard.getAccount().getBalance();

	}

	// Метод для снятия указанной суммы
	// Метод возвращает сумму, которая у клиента осталась на счету после снятия
	// Кроме проверки счета, метод так же должен проверять достаточно ли денег в
	// самом банкомате
	// Если недостаточно денег на счете, то должно генерироваться исключение
	// NotEnoughMoneyInAccount
	// Если недостаточно денег в банкомате, то должно генерироваться исключение
	// NotEnoughMoneyInATM
	// При успешном снятии денег, указанная сумма должна списываться со счета, и
	// в банкомате должно уменьшаться количество денег

	public double getCash(double amount) throws Exception {
		try {
			if (amount > checkBalance(currentCard))
				throw new NotEnoughMoneyInAccount();
			if (amount > moneyInATM)
				throw new NotEnoughMoneyInATM();
			currentCard.getAccount().withdraw(amount);
			moneyInATM -= amount;
		} catch (NotEnoughMoneyInAccount | NotEnoughMoneyInATM e) {
			throw e;
		}
		return currentCard.getAccount().getBalance();
	}
	
}
