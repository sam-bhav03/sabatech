import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
public class adminViewSales extends JFrame {

	private JLabel LblMain;
	private JTable Table;
	private JButton btnBack;
	private JPanel panel, panelmain;
	
	static ResultSet result = null;
	
	
	public adminViewSales(){
		
		panelmain = new JPanel();
		
		panelmain.setLayout(new FlowLayout());
		panelmain.setBackground(SabaTech.backgroundColor);
		//Adding buttons

		btnBack = new JButton("BACK");

		
		//creating table and loading detail with database connection
		Table = new JTable();	
		Table.setEnabled(false);
		DefaultTableModel model = new DefaultTableModel();
		try {

			Class.forName("com.mysql.jdbc.Driver");
			Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/sabatech", "root", "");
			String sql = "select * from sales";
			Statement statement = connection.prepareStatement(sql);
			
			model.addColumn("ReceiptID");
			model.addColumn("ProductName");
			model.addColumn("Quantity");
			model.addColumn("Time");
		
			
			result = statement.executeQuery(sql);
			while (result.next()) {
				model.addRow(new Object[] {
					result.getInt("receiptID"),
					result.getString("productName"),
					result.getInt("quantity"),
					result.getString("time")
					
				});
			
					
			}
			
			Table.setModel(model);
			Table.setAutoResizeMode(0);
			Table.getColumnModel().getColumn(0).setPreferredWidth(65);
			Table.getColumnModel().getColumn(1).setPreferredWidth(250);
			Table.getColumnModel().getColumn(2).setPreferredWidth(60);
			Table.getColumnModel().getColumn(3).setPreferredWidth(172);

		}

		catch (SQLException exe) {
			JOptionPane.showMessageDialog(null, "Cannot Load Data,  SQL Exception" + exe);
			exe.printStackTrace();
		} catch (ClassNotFoundException nul) {
			JOptionPane.showMessageDialog(null, "SQL connection Error" );
		}
		
	
		JScrollPane sp = new JScrollPane(Table);
		sp.setBackground(SabaTech.backgroundColor);
		sp.setPreferredSize(new Dimension(550,350));
		panelmain.add(sp);
		
		//Panel to group buttons
		panel = new JPanel();
		panel.setBackground(SabaTech.backgroundColor);
		panel.setLayout(new GridLayout(1,1));

		panel.add(btnBack);
		panel.setPreferredSize(new Dimension(400, 40));
		panelmain.add(panel);
		
		mainFrame.displayMainFrame(panelmain, 800);
		//Register Event Handlers
		ButtonHandler handler = new ButtonHandler();
		btnBack.addActionListener(handler);
		
	}
	
	//Event handling
	private class ButtonHandler implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
				if (e.getActionCommand() == btnBack.getActionCommand()){
					mainFrame.disposeCurrentFrame();
					new adminMain();

				}
 			
		}
	}
}
