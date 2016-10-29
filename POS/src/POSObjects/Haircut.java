package POSObjects;
import java.text.DecimalFormat;

public class Haircut {
	private String	name;
	private double	price;
	private int		quantity;
	private int		id;
	private String	display;
	public Haircut(String name, double price, int id, String display) {
		this.id = id;
		this.display = display;
		this.name = name;
		this.price = price;
		this.quantity = 0;
	}
	public String getDisplayName(){
		return this.display;
	}
	public int getQuantity(){
		return this.quantity;
	}
	public void addHaircut(){
		this.quantity += 1;
	}
	public void deleteAHaircut(){
		if (this.quantity > 0) this.quantity -= 1;
	}
	public double getPrice(){
		return this.price;
	}
	public int getId(){
		return this.id;
	}
	public String toString(){
		return this.name;
	}
}
