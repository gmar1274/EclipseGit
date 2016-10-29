package POSObjects;

import java.math.BigDecimal;
import java.util.ArrayList;

public class Data {
private	int[] customer;
private	BigDecimal[] amount;
private boolean product;
private long[] prod;


	public Data(boolean prod,int size){
		this(size);
		this.product=true;
		this.prod=new long[size];
	}
	public Data( int size) {
		
		this.customer = new int[size];
		this.amount = new BigDecimal[size];
		for(int i=0;i<size;++i){
			if(this.product){
				this.prod[i]=0;
			}
			this.customer[i]=0;
			this.amount[i]=new BigDecimal(0);
		}
		
	}
	public void addCustomer(int index){
		this.customer[index] += 1;
	}
	public void addTotalAmount(int index, BigDecimal a){
		this.amount[index] = a.add(this.amount[index]);
	}
	public int[] getCustomerList(){
		return this.customer;
	}
	public BigDecimal[] getTotalAmountList(){
		return this.amount;
	}
	public void addProduct(int index, long qty) {
		this.prod[index] += qty;
		
	}
	public long[] getProductList(){
		return this.prod;
	}
}
