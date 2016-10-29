package Dialogs;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import javax.swing.border.EmptyBorder;

public class test extends JFrame {
	private JPanel contentPane;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args){
		EventQueue.invokeLater(new Runnable() {
			public void run(){
				try{
					test frame = new test();
					frame.setVisible(true);
				} catch (Exception e){
					e.printStackTrace();
				}
			}
		});
	}
	/**
	 * Create the frame.
	 * @throws ParseException 
	 */
	public test() throws ParseException {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		JSpinner timeSpinner = new JSpinner( new SpinnerDateModel() );
		
		JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(timeSpinner, "hh:mm a");
		timeSpinner.setEditor(timeEditor);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm a");
		Date now = new Date();
		timeSpinner.setValue(now); // will only show the current time
		setContentPane(timeSpinner);
		JButton b=new JButton("press");
		b.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0){
				System.out.println(timeSpinner.getValue().toString());
				now.setDate(now.getDate()+1);//this doestn appear to have side effects!!
				System.out.println(now);
			}
			
		});
		b.setSize(80, 30);
		b.setVisible(true);
		this.getContentPane().add(b);
		//this.getContentPane().setLayout(null);
	}
}
