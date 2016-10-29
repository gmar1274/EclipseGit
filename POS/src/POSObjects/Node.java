package POSObjects;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Node implements Comparable<Node> {
	private int yval;
	private Date date;
	private BigDecimal amount;

	public Node(int yval,BigDecimal amount ,Date date) {
		this.amount=amount;
		this.yval = yval;
		this.date = date;
	}
	public Date getDate(){
		return this.date;
	}
	public int getYValue(){
		return this.yval;
	}
	public String toString(){
		return new SimpleDateFormat("h:mma").format(date);
	}
	public double getAmount(){
		return this.amount.doubleValue();
	}
	@Override
	public int compareTo(Node o) {
		if(this.date.getTime() < o.date.getTime()){
			return -1;
		}else if(this.date.getTime() == o.date.getTime()){
			return 0;
		}else{
			return 1;
		}
	}
}
