package POSObjects;
public class CombinedSale {
	private Stylist	s;
	private Haircut	h;
	private Coupon	c;
	public CombinedSale(Stylist s, Haircut h) {
		this.s = s;
		this.h = h;
		this.c=new Coupon();
	}
	public void applyCoupon(Coupon coup){
		this.c=coup;
	}
	public Coupon getCoupon(){
		return this.c;
	}
	public Stylist getStylist(){
		return this.s;
	}
	public Haircut getHaircut(){
		return this.h;
	}
	public String toString(){
		return "< " + s.toString() + " : " + h.toString() + " >";
	}
	public boolean equals(Object o){
		CombinedSale cs = (CombinedSale) o;
		return this.h.getDisplayName().equalsIgnoreCase(cs.h.getDisplayName())
				&& this.s.getID().equalsIgnoreCase(cs.s.getID());
	}
}
