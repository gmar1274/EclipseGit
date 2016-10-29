package CashRegister;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

public class OpenCashRegister implements Printable {
	public OpenCashRegister() {
		PrinterJob job = PrinterJob.getPrinterJob();
		job.setPrintable(this);
		try{
			job.print();
		} catch (PrinterException ex){
			ex.printStackTrace();
			
		}
	}
	public int print(Graphics g, PageFormat pf, int page) throws PrinterException{
		if (page > 0){ /* We have only one page, and 'page' is zero-based */
			return NO_SUCH_PAGE;
		}
		/*
		 * User (0,0) is typically outside the imageable area, so we must
		 * translate by the X and Y values in the PageFormat to avoid clipping
		 */
		Graphics2D g2d = (Graphics2D) g;
		g2d.translate(0,0);//pf.getImageableX(), pf.getImageableY());
		/* Now we perform our rendering */
		g.drawString("", 100, 100);
		/* tell the caller that this page is part of the printed document */
		return PAGE_EXISTS;
	}
}
