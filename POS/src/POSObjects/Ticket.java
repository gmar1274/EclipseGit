package POSObjects;

public class Ticket implements Comparable {
	private long ticket_id;// global
	private long store_id;
	private int number;///////////ticket number main variable
	private String customer_name;
	private String stylist_name, stylist_id, email, phone;
	private String string;
	private boolean isFinished;
	private String datetime;

	public Ticket(String dt, String name, int ticket, String sty_id, String email, String phone) {
		this.datetime = dt;
		this.customer_name = name;
		this.number = ticket;
		this.stylist_id = sty_id;
		this.email = email;
		this.phone = phone;
	}

	public String getEmail() {
		return this.email;
	}

	public String getPhone() {
		return this.phone;
	}

	public String getStylistID() {
		return this.stylist_id;
	}

	public String getName() {
		return this.customer_name;
	}

	public String getDateTime() {
		return this.datetime;
	}

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
