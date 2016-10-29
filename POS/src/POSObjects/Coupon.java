package POSObjects;
public class Coupon {
	private String	sku;
	private double	percent;
	private String	name;
	private double	discount;
	public Coupon(){
		sku="";
		discount=percent=0;
		name="";
		
	}
	public Coupon(String sku, String name, double percent, double discount) {
		this.sku = sku;
		this.name = name;
		this.percent = percent;
		this.discount = discount;
	}
	public String getSKU(){
		return this.sku;
	}
	public String getName(){
		return this.name;
	}
	public double getDiscount(){
		return this.discount;
	}
	public double getPercent(){
		return this.percent;
	}
	public String toString(){
		return this.name;
	}
}
