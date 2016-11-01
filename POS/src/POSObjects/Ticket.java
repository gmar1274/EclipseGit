package POSObjects;

public class Ticket implements Comparable {
	private long ticket_id;// global
	private long store_id;
	private int number;
	private String customer_name;
	private String stylist_name;
	private String string;
	private boolean isFinished;
	// Ticket(int ticket_id,int store_id,int stylist_id,String customer) {

	// }
	public Ticket(int ticket, long store_id) {// for getting the current ticket number for store
		this.number = ticket;
		this.store_id = store_id;
	}

	public Ticket(int ticket, String stylist_name, String customer, long store_id) {// assume the program has the store id saved as user_db in SQL
		this.number = ticket;
		this.customer_name = customer;
		this.stylist_name = stylist_name;
		this.store_id = store_id;
		this.isFinished = false;
	}

	public void setFinished() {
		this.isFinished = true;
	}

	public boolean isFinished() {
		return this.isFinished;
	}

	public int getNumber() {
		return this.number;
	}

	public long getStore_id() {
		return this.store_id;
	}

	public String toString() {
		return string;////// customized formatted string to display
	}

	public void setToString(String s) {// sets the display string to a given formmatted string
		this.string = s;
	}

	@Override
	public int compareTo(Object o) {
		Ticket oo = (Ticket) o;
		if (this.number < oo.getNumber()) {
			return -1;
		} else if (this.number > oo.getNumber()) {
			return 1;
		} else return 0;
	}
}
