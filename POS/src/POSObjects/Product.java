package POSObjects;

import java.util.Date;

public class Product {
	private String	sku;
	private double	price;
	private int		quantity;
	private String	name;
	private Date date;
	public Product(String sku, String prod_name) {
		this.sku = sku;
		this.name = prod_name;
	}
	public Product(String sku, String prod_name, double price) {
		this.sku = sku;
		this.price = price;
		this.quantity = 0;
		this.name = prod_name;
	}
	public void setQuantitySold(int q){
		this.quantity=q;
	}
	public Date getDateSold(){
		return this.date;
	}
	public void setPrice(double p){
		this.price=p;
	}
	public void setDate(Date d){
		this.date = d;
	}
	public void addProduct(){
		this.quantity += 1;
	}
	public double getPrice(){
		return this.price;
	}
	public int getQuantity(){
		return this.quantity;
	}
	public void deleteAProduct(){
		if (this.quantity > 0) this.quantity -= 1;
	}
	public String toString(){
		return this.name;
	}
	public String getName(){
		return this.name;
	}
	public String getSKU(){
		return this.sku;
	}
	public boolean equals(Object o){
		Product p= (Product) o;
		return this.name.equalsIgnoreCase(p.name);
	}
}
