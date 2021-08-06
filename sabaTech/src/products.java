import javax.swing.*;
import javax.swing.table.DefaultTableModel;

//import pointofSale.function;

import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
public class products extends JFrame implements ActionListener{
	
	JLabel label, lblproductId, lblproductName, lblproductPrice, lblquantity;
	JTextField txtproductId, txtproductName, txtproductPrice, txtquantity;
	JTable productData;
	JButton btnadd, btnupdate, btndelete, btndisplay, btnback;
	JPanel panel1, panel2, panel3;
	JScrollPane sp; 

	
	products(){

		setLayout(new FlowLayout());
		getContentPane().setBackground(SabaTech.backgroundColor);
		ImageIcon imageIcon = new ImageIcon("resources/icons/ST_icon.png");
		this.setIconImage(imageIcon.getImage());
		 //#regionNORTHpanel: display header & logo
        ImageIcon imageLogo = new ImageIcon("resources/images/ST_logo2.png");
        JLabel welcomeText = new JLabel();
        welcomeText.setIcon(imageLogo);
        // welcomeText.setHorizontalTextPosition(JLabel.CENTER);
        // welcomeText.setVerticalTextPosition(JLabel.BOTTOM);
        welcomeText.setBackground(SabaTech.backgroundColor);
        welcomeText.setOpaque(true);
        // welcomeText.setVerticalAlignment(JLabel.TOP);
        welcomeText.setHorizontalAlignment(JLabel.CENTER);
        //#endregion
        add(welcomeText);
		label = new JLabel("Add an item, Enter Product ID to delete, Display an Item through Product ID to Update");
		label.setFont(new Font("Serif", Font.PLAIN, 15));
		add(label);
		
		productData = new JTable();
		panel2 = new JPanel();
		panel2.add(productData);
		

		lblproductId = new JLabel("Product ID");
		lblproductId.setFont(new Font("Serif", Font.PLAIN, 15));
		
		lblproductName = new JLabel("Product Name");
		lblproductName.setFont(new Font("Serif", Font.PLAIN, 15));
		
		lblproductPrice = new JLabel("Product Price");
		lblproductPrice.setFont(new Font("Serif", Font.PLAIN, 15));
		
		lblquantity = new JLabel("Quantity");
		lblquantity.setFont(new Font("Serif", Font.PLAIN, 15));
		
		txtproductId = new JTextField(15);
		txtproductName = new JTextField(15);
		txtproductPrice = new JTextField(15);
		txtquantity = new JTextField(15);
		
		
		panel1 = new JPanel();
		panel1.setLayout(new GridLayout(4, 2 , 25, 15));
		panel1.add(lblproductId);
		panel1.add(txtproductId);
		panel1.add(lblproductName);
		panel1.add(txtproductName);
		panel1.add(lblproductPrice);
		panel1.add(txtproductPrice);
		panel1.add(lblquantity);
		panel1.add(txtquantity);
		panel1.setBackground(SabaTech.backgroundColor);
		add(panel1);
		
		
		//buttons
		btndisplay = new JButton("Display");
		btnadd = new JButton("Add Item");
		btnupdate = new JButton("Update");
		btndelete = new JButton("Delete Item");
		btnback = new JButton("Back");
		panel3 = new JPanel();
		panel3.setPreferredSize(new Dimension(600, 40));
		panel3.setLayout(new GridLayout(1, 4, 15, 10));
		panel3.add(btndisplay);
		panel3.add(btnadd);
		panel3.add(btnupdate);
		panel3.add(btndelete);
		panel3.add(btnback);
		panel3.setBackground(SabaTech.backgroundColor);
		add(panel3);

		btnadd.addActionListener(this);
		btnupdate.addActionListener(this);
		btndelete.addActionListener(this);
		btndisplay.addActionListener(this);
		btnback.addActionListener(this);

	}

	
	public void loadData() {
		//open connection
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/sabatech", "root", "");
			
			Statement stmt = con.createStatement();
			String sql = "select * from product";
			ResultSet rs = stmt.executeQuery(sql);
			
			DefaultTableModel model = new DefaultTableModel();
			model.addColumn("Product ID");
			model.addColumn("ProductName");
			model.addColumn("ProductPrice");
			model.addColumn("Quantity");
			
			while (rs.next()) {
				model.addRow(new Object[] {
						rs.getString("ProductID"),
						rs.getString("ProductName"),
						rs.getDouble("ProductPrice"),
						rs.getInt("Quantity")	
					});
				
			}
			productData.setModel(model);
			productData.setAutoResizeMode(0);
			productData.getColumnModel().getColumn(0).setPreferredWidth(65);
			productData.getColumnModel().getColumn(1).setPreferredWidth(200);
			productData.getColumnModel().getColumn(2).setPreferredWidth(100);
			productData.getColumnModel().getColumn(3).setPreferredWidth(75);
			con.close();
			
		}catch(Exception ex) {
			JOptionPane.showMessageDialog(null, ex.getMessage());
		}
		
//		JScrollPane sp = new JScrollPane(productData);
		sp = new JScrollPane(productData);
//		sp.setPreferredSize(new Dimension(443,150));
		add(sp);
		sp.setPreferredSize(new Dimension(443,250));
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
				ps = con.prepareStatement("Select * from product where productID = ?");
				ps.setString(1, s);
				rs = ps.executeQuery();
			}catch(Exception ex) {
				JOptionPane.showMessageDialog(null, ex.getMessage());
			}
			return rs;
		}
	}
	
	@Override	
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand() == btnadd.getActionCommand()) {
			try {
				Class.forName("com.mysql.jdbc.Driver");
				Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/sabatech", "root", "");
				
				String sql = "insert into product values (?, ?, ?, ?)";
				PreparedStatement ps = con.prepareStatement(sql);
				ps.setString(1, txtproductId.getText());
				ps.setString(2, txtproductName.getText());
				ps.setDouble(3, Double.parseDouble(txtproductPrice.getText()));
				ps.setInt(4, Integer.parseInt(txtquantity.getText()));
				
				ps.executeUpdate();
				JOptionPane.showMessageDialog(null, "Product Added!");
				con.close();

				remove(sp);
				loadData();

				this.setSize(870, 701);
				this.setSize(870, 700);

			}catch(Exception e1) {
				JOptionPane.showMessageDialog(null, "Please fill all the fields");
			}
		}	
		
		if(e.getActionCommand() == btndelete.getActionCommand()) {
			try {
				Class.forName("com.mysql.jdbc.Driver");
				Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/sabatech", "root", "");
				
				String sql = "delete from product where productID = ?";
				PreparedStatement ps = con.prepareStatement(sql);
				ps.setString(1, txtproductId.getText());
				
				ps.executeUpdate();
				JOptionPane.showMessageDialog(null, "Product Deleted!");
				con.close();

				remove(sp);
				loadData();
				this.setSize(870, 701);
				this.setSize(870, 700);
			}catch(Exception e1) {
				JOptionPane.showMessageDialog(null, e1);
			}
		}
		
		if(e.getActionCommand() == btnupdate.getActionCommand()) {
			try {
				Class.forName("com.mysql.jdbc.Driver");
				Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/sabatech", "root", "");
				
				String sql = "Update product Set productName = ?, productPrice = ?, quantity = ? where productID = ?";
				PreparedStatement ps = con.prepareStatement(sql);
				ps.setString(1, txtproductName.getText());
				ps.setString(2, txtproductPrice.getText());
				ps.setString(3, txtquantity.getText());
				ps.setString(4, txtproductId.getText());
				
				ps.executeUpdate();
				JOptionPane.showMessageDialog(null, "Product Updated!");
				con.close();

				remove(sp);
				loadData();
				this.setSize(870, 701);
				this.setSize(870, 700);
			}catch(Exception e1) {
				JOptionPane.showMessageDialog(null, "Please fill all the fields");
			}
		}
		
		if(e.getActionCommand() == btndisplay.getActionCommand()) {
			function f = new function();
			ResultSet rs = null;
			String pname = "productName";
			String pprice = "productPrice";
			String quant = "quantity";
			
			rs = f.find((String) txtproductId.getText());

			try {
				if(rs.next()) {
					txtproductName.setText(rs.getString("productName"));
					txtproductPrice.setText(rs.getString("productPrice"));
					txtquantity.setText(rs.getString("quantity"));
				}
				else {
					JOptionPane.showMessageDialog(null, "NO DATA FOR THIS ID");
				}
			}catch(Exception ex) {
				JOptionPane.showMessageDialog(null, ex.getMessage());
			}
		}
		
		if(e.getActionCommand() == btnback.getActionCommand()) {
			dispose();
			new adminMain();
		}
	}
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		products prods = new products();
		prods.loadData();
		prods.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		prods.setSize(870, 700);
		prods.setLocationRelativeTo(null); 
		prods.setVisible(true);
		
	}
	
}
