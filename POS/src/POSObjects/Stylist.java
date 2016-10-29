package POSObjects;

import java.math.BigDecimal;
import java.util.ArrayList;

public class Stylist {
	private double comm, total_amount;
	private String id;
	private String fname,mname,lname;
	public Stylist(){
		this.fname="No Preference";
		this.mname="";
		this.lname="";
		this.id="-1";
	}
	public Stylist(String id, String fname,String mname,String lname,double commission){
		this.id=id;
		this.comm=commission;
		this.fname=fname;
		this.mname=mname;
		this.lname=lname;
		
	}
	public double getCommission() {
		return this.comm;
	}

	public String getID() {
		return this.id;
	}

	public boolean equals(Object o) {
		Stylist s = (Stylist) o;
		return this.id.equalsIgnoreCase(s.id);
	}

	public String getName() {
		if(this.mname.length()>0)return this.fname+" "+this.mname+" "+this.lname;
		return  this.fname+" "+this.lname;//because i need to fix SQL
	}

	public String toString() {
		return this.getName();
	}

}
