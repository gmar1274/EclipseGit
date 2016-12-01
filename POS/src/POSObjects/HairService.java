package POSObjects;

import java.math.BigDecimal;
import java.sql.Time;

/**
 * This object holds the details of the salons/barbershops reservation services. ie hair coloring etc...
 * 
 * 
 */
public class HairService {
	private int id;
	private String name;
	private BigDecimal price;
	private String duration;//should be in 00:00:00 form

	public HairService() {

	}

	public HairService(int id, String name, BigDecimal price, String duration) {
		this.id = id;
		this.price = price;
		this.name = name;
		this.duration = duration;
	}

	public String getName() {
		return this.name;
	}

	public BigDecimal getPrice() {
		return this.price;
	}

	public String getDuration() {
		return this.duration;
	}
}
