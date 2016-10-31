package ReservationScreenCustomerLogin;

public class Customer implements Comparable<Customer> {
	private String name;
	private String stylist_id;
	private long id;
	private String email;
	private String stylist_name, phone;

	public Customer(String name, String email) {
		this.id = -1;
		this.name = name;
		this.email = email;
	}

	public Customer(String name, String stylist_id, String phone) {
		this.name = name;
		this.stylist_id = stylist_id;
		this.id = -1;
		stylist_name = null;
		this.phone = phone;
	}

	public String getPhoneNumber() {
		return this.phone;
	}

	public void setStylistName(String name) {
		this.stylist_name = name;
	}

	public String getEmail() {
		return this.email;
	}

	public boolean hasPreferenceStylist() {
		return this.stylist_id != null && this.stylist_id.length() > 0;
	}

	public String getName() {
		return this.name;
	}

	public String getStylistID() {
		return this.stylist_id;
	}

	public long getID() {
		return this.id;
	}

	@Override
	public int compareTo(Customer o) {
		if (this.id < o.id) return -1;
		else if (this.id == o.id) return 0;
		else return 1;
	}

	public String getStylistName() {
		return this.stylist_name;
	}

}
