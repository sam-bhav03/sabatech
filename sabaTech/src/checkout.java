import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;

public class checkout extends JFrame implements ActionListener{

	private javax.swing.JTable infotable = new JTable();
	JLabel lblcustname, lblcustphonenum, lblcarddetails, lbltotal;
	JTextField txtcustname, txtcustphonenum, txtcarddetails, txttotal;
	JButton btncalctotal, btnconfirm;
	JPanel panel1, panel2, panel3, panel4, panelCheckout;
	static ResultSet result = null;
    String dates = SabaTech.currentDate.toString();
    
	checkout(){
		panelCheckout = new JPanel();
        panelCheckout.setBackground(SabaTech.backgroundColor);
		panelCheckout.setLayout(new FlowLayout());

        //#regionA
		lblcustname = new JLabel("Customer name");
		lblcustname.setFont(new Font("Copperplate Gothic Light", Font.PLAIN, 15));
		txtcustname = new JTextField(15);
		
		lblcustphonenum = new JLabel("Phone Number");
		lblcustphonenum.setFont(new Font("Copperplate Gothic Light", Font.PLAIN, 15));
		txtcustphonenum = new JTextField(15);
		
		lblcarddetails = new JLabel("Card Details");
		lblcarddetails.setFont(new Font("Copperplate Gothic Light", Font.PLAIN, 15));
		txtcarddetails = new JTextField(15);
		
		panel1 = new JPanel();
		panel1.setLayout(new GridLayout(4,2,10,15));
        panel1.setBackground(SabaTech.backgroundColor);
		panel1.add(lblcustname);
		panel1.add(txtcustname);
		panel1.add(lblcustphonenum);
		panel1.add(txtcustphonenum);
		panel1.add(lblcarddetails);
		panel1.add(txtcarddetails);

        lbltotal = new JLabel("Total Price ");
		lbltotal.setFont(new Font("Copperplate Gothic Light", Font.PLAIN, 15));
		txttotal = new JTextField(10);
		txttotal.setEditable(false);
		
		panel1.add(lbltotal);
		panel1.add(txttotal);

		panelCheckout.add(panel1, BorderLayout.NORTH);
        //#endregion

        //#regionB
        panel3 = new JPanel();
        panel3.add(new JScrollPane(infotable));
        panel3.setPreferredSize(new Dimension(600,300));
        panel3.setBackground(SabaTech.backgroundColor);
        panelCheckout.add(panel3, BorderLayout.CENTER);
		
		panel2 = new JPanel();
        panel2.setPreferredSize(new Dimension(450, 50));
        panel2.setLayout(new GridLayout(1,2,20,20));
        panel2.setBackground(SabaTech.backgroundColor);
        
        btnconfirm = new JButton("Confirm Payment");
        panel2.add(btnconfirm);
        panelCheckout.add(panel2, BorderLayout.SOUTH);
        //#endregion

		btnconfirm.addActionListener(this);
        fillTable();
		calctotal();
        mainFrame.displayMainFrame(panelCheckout,800);
	}

	public void fillTable() {
		String filepath = "files/product.txt";
		File file = new File(filepath);
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String firstLine = br.readLine().trim();   //reads the first line 
			String[] columnNames = firstLine.split(","); //splits the string after each ","
			DefaultTableModel model = (DefaultTableModel) infotable.getModel();
			
			model.setColumnIdentifiers(columnNames);  //write the titles 
			Object[] tableLines = br.lines().toArray();
	
			for(int i = 0; i < tableLines.length; i++) {
				String line = tableLines[i].toString().trim();  

				String[] datarow = line.split("/"); //splits the string after each "/"
				model.addRow(datarow);
			}
		} catch (FileNotFoundException e1) {	
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

	}
	
	public class function{
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		String url = "jdbc:mysql://localhost:3306/";
		String dbName = "sabatech";
		String driver = "com.mysql.jdbc.Driver";
		String username = "root";
		String password = "";
		
		public ResultSet find(String s) {
			try {
				con = DriverManager.getConnection(url + dbName, username, password);
				ps = con.prepareStatement("Select quantity from product where productName = ?");
				ps.setString(1, s);
				rs = ps.executeQuery();
			}catch(Exception ex) {
				JOptionPane.showMessageDialog(null, ex.getMessage());
			}
			return rs;
		}
	}

	public void calctotal() {
		int total = 0;
		for(int i = 0; i < infotable.getRowCount(); i++) {
					String valuePrice = infotable.getModel().getValueAt(i, 2).toString();
					int amount = Integer.parseInt(valuePrice); 
					total += amount;   //calculate total price		
										
			}
		String x = String.valueOf(total);
		txttotal.setText(x);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand() == btnconfirm.getActionCommand()) {
			boolean flag = false;
			//update customer DB
			try {
				Class.forName("com.mysql.jdbc.Driver");
				Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/sabatech", "root", "");
				
				String sql = "insert into customer values (?, ?, ?)";
				PreparedStatement ps = con.prepareStatement(sql);
				ps.setString(1, txtcustname.getText());
				ps.setInt(2, Integer.parseInt(txtcustphonenum.getText()));
				ps.setInt(3, Integer.parseInt(txtcarddetails.getText()));
				
				ps.executeUpdate();
				con.close();
			}catch(Exception e1) {
				JOptionPane.showMessageDialog(null, "Please fill all the fields");
				flag = true;
			}
			
			if(flag == false) {
				//update product DB
				try {
					Class.forName("com.mysql.jdbc.Driver");
					Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/sabatech", "root", "");
					function f = new function();
					ResultSet rs = null;
					int quant = 0;
					
					for(int i = 0; i < infotable.getRowCount(); i++) {

							String valueName = (infotable.getModel().getValueAt(i, 0).toString());
							String valueQuantity = infotable.getModel().getValueAt(i, 1).toString();
							String valuePrice = infotable.getModel().getValueAt(i, 2).toString();

							JTextField Jquant = new JTextField();  //temporary textfield for calculating quantity 
							
							int intValueQuantity = 0;
							intValueQuantity = Integer.parseInt(valueQuantity);

							rs = f.find((String) valueName);
							if(rs.next()) {
								Jquant.setText(rs.getString("quantity"));  //fetching quantity value from database
							}
							
							quant = Integer.parseInt(Jquant.getText()) - intValueQuantity;  //calculating new stock
							String sql = "UPDATE product set quantity = ? where productName = ?";
							PreparedStatement ps = con.prepareStatement(sql);
							ps.setInt(1, (quant));
							ps.setString(2, (valueName));
							ps.executeUpdate();

					}
					con.close();
				}catch(Exception ex) {
					JOptionPane.showMessageDialog(null, "Failed");
				}
				
				
				//insert into table sales
				try {
					
					Class.forName("com.mysql.jdbc.Driver");
					Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/sabatech", "root", "");
					int id = pointOfSale.getReceiptID(false);
					for(int i = 0; i < infotable.getRowCount(); i++) {
			
						String valueName = (infotable.getModel().getValueAt(i, 0).toString());
						String valueQuantity = infotable.getModel().getValueAt(i, 1).toString();
						String sql = "INSERT into sales values (?, ?, ?, ?)";
						PreparedStatement ps = con.prepareStatement(sql);
						ps.setInt(1, id);
						ps.setString(2, valueName);
						ps.setString(3, valueQuantity);
						ps.setString(4,  dates);
				
						ps.executeUpdate();
					}
					con.close();
					JOptionPane.showMessageDialog(null, "Successfully Checked-Out");
				}catch(Exception ex) {
					JOptionPane.showMessageDialog(null, "Sales table not updated");
				}

				mainFrame.disposeCurrentFrame();
				new welcomeScreen();
			}
		}
			
		
	}
}
