import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class adminMain extends JFrame implements ActionListener {
	JLabel label,labelNull;
	JButton btnProduct, btnReturn, btnviewsales, btnviewreturns, btnback;
	JPanel panel;
	adminMain(){

		label = new JLabel("                         Choose a service to continue");
        label.setFont(new Font("Copperplate Gothic Light", Font.PLAIN, 30));
		btnProduct = new JButton("MANAGE PRODUCT");
		btnReturn = new JButton("RETURN PRODUCT");
		btnviewsales = new JButton("VIEW SALES");
		btnviewreturns = new JButton("VIEW RETURNS");
		btnback = new JButton("BACK");
        labelNull = new JLabel("");
		panel = new JPanel();
		
		panel.setLayout(new GridLayout(7,1,10,30));
		panel.setPreferredSize(new Dimension(850, 800));
        panel.setBackground(SabaTech.backgroundColor);
		panel.add(label);
		panel.add(btnProduct);
		panel.add(btnReturn);
		panel.add(btnviewsales);
		panel.add(btnviewreturns);
		panel.add(btnback);
        panel.add(labelNull);
		

		btnProduct.addActionListener(this);
		btnReturn.addActionListener(this);
		btnviewsales.addActionListener(this);
		btnviewreturns.addActionListener(this);
		btnback.addActionListener(this);
        mainFrame.displayMainFrame(panel, 800);
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand() == btnProduct.getActionCommand()) {
			mainFrame.disposeCurrentFrame();
			products prods = new products();
			prods.loadData();
			prods.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			prods.setSize(870, 700);
			prods.setLocationRelativeTo(null); 
			prods.setVisible(true);
		}
		
		if(e.getActionCommand() == btnReturn.getActionCommand()) {
			mainFrame.disposeCurrentFrame();
			new adminReturn();
		}
		
		if(e.getActionCommand() == btnviewsales.getActionCommand()) {
			mainFrame.disposeCurrentFrame();
			new adminViewSales();
		}
		
		if (e.getActionCommand() == btnviewreturns.getActionCommand()) {
			mainFrame.disposeCurrentFrame();
			new adminViewReturns();
		}
		if(e.getActionCommand() == btnback.getActionCommand()) {
			mainFrame.disposeCurrentFrame();
			new welcomeScreen();
		}
	}
	
}
