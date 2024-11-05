package test1;


public class DepositThreadReentrantLock implements Runnable {
	private AccountReentrantLock account;
	private double money;

	public DepositThreadReentrantLock(AccountReentrantLock account, double money) {
		this.account = account;
		this.money = money;
	}

	@Override
	public void run() {
			account.deposit(money);
		}
}
