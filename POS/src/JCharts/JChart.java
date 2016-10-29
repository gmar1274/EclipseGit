package JCharts;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.category.CategoryDataset;

import JCharts.JChart.OVERVIEW;
import MainFrame.POSFrame;
import POS_Panels.ManagerSettingsPanel;
import SQLclass.SQL;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class JChart extends JDialog {
	public static enum OVERVIEW {
		HOURLY, DAILY, MONTHLY
	}

	private JTextPane[] tarr;
	private String text;
	private SQL sql;
	private int i;
	private JPanel[] chartpanels, textpanels;
	private JScrollPane chart_scrollpane, text_scrollpane;
	private JTextPane[] tparr;
	private JButton exit;
	private JButton next;

	public JChart(OVERVIEW o) {
		this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		// this.setTitle("Press the exit button to close window");
		text_scrollpane = new JScrollPane();
		text_scrollpane.setBounds(0, 500, 1012, 200);
		this.setModal(true);
		text_scrollpane.setVisible(true);
		i = 0;
		chart_scrollpane = new JScrollPane();
		chart_scrollpane.setBounds(0, 0, 1020, 500);

		chart_scrollpane.setVisible(true);
		this.getContentPane().removeAll();
		this.getContentPane().add(chart_scrollpane);
		this.setSize(1100, 800);
		this.setLocationRelativeTo(null);

		getContentPane().setLayout(null);
		this.getContentPane().add(text_scrollpane);
		next = new JButton("<html>Next Day<font size=6> &rarr</font></html>");
		next.setFont(new Font("Tahoma", Font.PLAIN, 16));
		next.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (o == OVERVIEW.MONTHLY) { return; }
				i += 1;

				if (i % 7 == 0) {
					i = 0;
				}
				chart_scrollpane.setViewportView(chartpanels[i]);
				text_scrollpane.setViewportView(textpanels[i]);

			}
		});
		next.setBounds(713, 702, 307, 39);
		getContentPane().add(next);

		exit = new JButton("Close Window");
		exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		exit.setBounds(0, 702, 209, 39);
		getContentPane().add(exit);

		if (POSFrame.SQL != null) {
			this.sql = POSFrame.SQL;
		} else {
			this.sql = new SQL();
		}
		if (o == OVERVIEW.HOURLY) {
			hourly();
		} else if (o == OVERVIEW.DAILY) {
			daily();
		} else {
			monthly();
		}

		text_scrollpane.getVerticalScrollBar().setValue(0);
		chart_scrollpane.getVerticalScrollBar().setValue(0);
		POSFrame.loading.dispose();
		this.setVisible(true);
	}

	private void daily() {
		this.next.setVisible(false);
		JTextPane t = new JTextPane();
		t.setEditable(false);
		t.setContentType("text/html");
		JTextPane tp = new JTextPane();
		tp.setEditable(false);
		tp.setContentType("text/html");
		
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout());
		JPanel textpanel = new JPanel();
		textpanel.setLayout(new GridLayout(1, 2, 0, 0));

		JFreeChart barChart = ChartFactory.createBarChart3D("Weekly Haircuts", "Stylist", "Number of Haircuts", this.sql.getDailyCustomersForCharts(t), PlotOrientation.VERTICAL, true, true, false);
		// add the chart to a panel...
		ChartPanel chartPanel = new ChartPanel(barChart);
		chartPanel.setPreferredSize(new java.awt.Dimension(1100, 500));
		CategoryPlot pp = barChart.getCategoryPlot();

		pp.getDomainAxis().setTickLabelFont(new Font("Arial", Font.PLAIN, 10));
		pp.getRangeAxis().setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		/////////////////////// inventory
		JFreeChart barChartp = ChartFactory.createBarChart3D("Weekly Inventory Sales", "Product", "Units Sold", this.sql.getWeeklyProductsForCharts(tp), PlotOrientation.VERTICAL, true, true, false);
		// add the chart to a panel...
		ChartPanel chartPanelp = new ChartPanel(barChartp);
		chartPanelp.setPreferredSize(new java.awt.Dimension(1100, 500));
		CategoryPlot ppp = barChartp.getCategoryPlot();

		ppp.getDomainAxis().setTickLabelFont(new Font("Arial", Font.PLAIN, 10));
		ppp.getRangeAxis().setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		panel.add(chartPanel);
		panel.add(chartPanelp);
		panel.setVisible(true);
		t.setCaretPosition(0);
		tp.setCaretPosition(0);
		textpanel.add(t);
		textpanel.add(tp);
		textpanel.setVisible(true);
		this.chart_scrollpane.setViewportView(panel);
		this.text_scrollpane.setViewportView(textpanel);
		
		
		
	}

	private void monthly() {
		this.next.setVisible(false);
		JTextPane t = new JTextPane();
		t.setEditable(false);
		t.setContentType("text/html");
		JTextPane tp = new JTextPane();
		tp.setEditable(false);
		tp.setContentType("text/html");
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout());
		JPanel textpanel = new JPanel();
		textpanel.setLayout(new GridLayout(1, 2, 0, 0));

		JFreeChart barChart = ChartFactory.createBarChart3D("Monthly Haircuts", "Stylist", "Number of Haircuts", this.sql.getMonthlyCustomersForCharts(t), PlotOrientation.VERTICAL, true, true, false);
		// add the chart to a panel...
		ChartPanel chartPanel = new ChartPanel(barChart);
		chartPanel.setPreferredSize(new java.awt.Dimension(1100, 500));
		CategoryPlot pp = barChart.getCategoryPlot();

		pp.getDomainAxis().setTickLabelFont(new Font("Arial", Font.PLAIN, 10));
		pp.getRangeAxis().setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		/////////////////////// inventory
		JFreeChart barChartp = ChartFactory.createBarChart3D("Monthly Inventory Sales", "Product", "Units Sold", this.sql.getMonthlyProductsForCharts(tp), PlotOrientation.VERTICAL, true, true, false);
		// add the chart to a panel...
		ChartPanel chartPanelp = new ChartPanel(barChartp);
		chartPanelp.setPreferredSize(new java.awt.Dimension(1100, 500));
		CategoryPlot ppp = barChartp.getCategoryPlot();

		ppp.getDomainAxis().setTickLabelFont(new Font("Arial", Font.PLAIN, 10));
		ppp.getRangeAxis().setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		panel.add(chartPanel);
		panel.add(chartPanelp);
		panel.setVisible(true);
		t.setCaretPosition(0);
		tp.setCaretPosition(0);
		textpanel.add(t);
		textpanel.add(tp);
		textpanel.setVisible(true);
		this.chart_scrollpane.setViewportView(panel);
		this.text_scrollpane.setViewportView(textpanel);

	}

	private void hourly() {
		tarr = new JTextPane[7];
		tparr = new JTextPane[7];
		SimpleDateFormat sdf = new SimpleDateFormat("EEEE MM/dd/yyyy");
		Date date = new Date();
		Calendar c = Calendar.getInstance();
		c.set(c.DAY_OF_WEEK, c.MONDAY);
		date = c.getTime();
		chartpanels = new JPanel[7];
		this.textpanels = new JPanel[7];
		for (int i = 0; i < 7; ++i) {

			chartpanels[i] = new JPanel();
			chartpanels[i].setLayout(new FlowLayout());
			chartpanels[i].setSize(getSize());
			textpanels[i] = new JPanel();
			textpanels[i].setLayout(new GridLayout(1, 2, 0, 0));
			textpanels[i].setSize(getSize());
			tarr[i] = new JTextPane();
			tarr[i].setSize(800, 200);
			tarr[i].setEditable(false);
			tarr[i].setContentType("text/html");
			tparr[i] = new JTextPane();
			tparr[i].setSize(tarr[i].size());
			tparr[i].setEditable(false);
			tparr[i].setContentType("text/html");
			CategoryDataset dataset = sql.getHourlyData(date, tarr[i]);
			JFreeChart barChart = ChartFactory.createBarChart3D(sdf.format(date), "Gross Profits", "Amount Earned ($)", dataset, PlotOrientation.VERTICAL, true, true, false);

			// add the chart to a panel...
			ChartPanel chartPanel = new ChartPanel(barChart);
			chartPanel.setPreferredSize(new java.awt.Dimension(1100, 500));
			CategoryPlot pp = barChart.getCategoryPlot();

			pp.getDomainAxis().setTickLabelFont(new Font("Arial", Font.PLAIN, 10));
			pp.getRangeAxis().setStandardTickUnits(NumberAxis.createIntegerTickUnits());

			////////////////////////////////// product
			CategoryDataset datasetp = sql.getDailyProductsForCharts(date, tparr[i]);
			JFreeChart barChartp = ChartFactory.createBarChart3D(sdf.format(date), "Product", "Units Sold", datasetp, PlotOrientation.VERTICAL, true, true, false);
			// add the chart to a panel...
			ChartPanel chartPanelp = new ChartPanel(barChartp);
			chartPanelp.setPreferredSize(new java.awt.Dimension(1100, 500));
			CategoryPlot ppp = barChartp.getCategoryPlot();
			ppp.getDomainAxis().setTickLabelFont(new Font("Arial", Font.PLAIN, 10));
			ppp.getRangeAxis().setStandardTickUnits(NumberAxis.createIntegerTickUnits());
			date.setDate(date.getDate() + 1);

			chartpanels[i].add(chartPanel); // this.getContentPane().add(chartPanel);
			chartpanels[i].add(chartPanelp);
			chartpanels[i].setVisible(true);
			tarr[i].setCaretPosition(0);
			tparr[i].setCaretPosition(0);
			textpanels[i].add(tarr[i]);
			textpanels[i].add(tparr[i]);
			textpanels[i].setVisible(true);

		}
		this.chart_scrollpane.setViewportView(chartpanels[i]);

		this.text_scrollpane.setViewportView(this.textpanels[i]);

	}

}