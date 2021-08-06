import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
public class adminProducts extends JFrame implements ActionListener{
	
	JLabel label, lblproductId, lblproductName, lblproductPrice, lblquantity;
	JTextField txtproductId, txtproductName, txtproductPrice, txtquantity;
	JTable productData;
	JButton btnadd, btnupdate, btndelete, btndisplay, btnback;
	JPanel panelText, panelTable, panelButton;
	static JPanel panelProduct;
	JScrollPane sp; 

	adminProducts(){
		panelProduct = new JPanel();
		panelProduct.setLayout(new FlowLayout());
		
		label = new JLabel("Add an item, Enter Product ID to Delete or Display an Item through Product ID to Update");
		label.setFont(new Font("Copperplate Gothic Light", Font.PLAIN, 15));
		panelProduct.add(label);
		
		productData = new JTable();
		panelTable = new JPanel();
		
		panelTable.add(productData);
		panelTable.setBackground(SabaTech.backgroundColor);
		panelProduct.add(panelTable);
		
		lblproductId = new JLabel("Product ID");
		lblproductName = new JLabel("Product Name");
		lblproductPrice = new JLabel("Product Price");
		lblquantity = new JLabel("Quantity");
		
		lblproductId.setFont(new Font("Copperplate Gothic Light", Font.PLAIN, 15));
		lblproductName.setFont(new Font("Copperplate Gothic Light", Font.PLAIN, 15));
		lblproductPrice.setFont(new Font("Copperplate Gothic Light", Font.PLAIN, 15));
		lblquantity.setFont(new Font("Copperplate Gothic Light", Font.PLAIN, 15));
		txtproductId = new JTextField(20);
		txtproductName = new JTextField(20);
		txtproductPrice = new JTextField(20);
		txtquantity = new JTextField(20);
		
		
		panelText = new JPanel();
		panelText.setLayout(new GridLayout(4, 2 , 25, 15));
		panelText.add(lblproductId);
		panelText.add(txtproductId);
		panelText.add(lblproductName);
		panelText.add(txtproductName);
		panelText.add(lblproductPrice);
		panelText.add(txtproductPrice);
		panelText.add(lblquantity);
		panelText.add(txtquantity);
		panelText.setBackground(SabaTech.backgroundColor);
		panelProduct.add(panelText);
		

		btndisplay = new JButton("Display");
		btnadd = new JButton("Add Item");
		btnupdate = new JButton("Update");
		btndelete = new JButton("Delete Item");
		btnback = new JButton("Back");
		panelButton = new JPanel();
		panelButton.setPreferredSize(new Dimension(650, 40));
		panelButton.setLayout(new GridLayout(1, 4, 15, 10));
		panelButton.add(btndisplay);
		panelButton.add(btnadd);
		panelButton.add(btnupdate);
		panelButton.add(btndelete);
		panelButton.add(btnback);
		panelButton.setBackground(SabaTech.backgroundColor);
		panelProduct.add(panelButton);
		panelProduct.setBackground(SabaTech.backgroundColor);
		
		btnadd.addActionListener(this);
		btnupdate.addActionListener(this);
		btndelete.addActionListener(this);
		btndisplay.addActionListener(this);
		btnback.addActionListener(this);
		loadData();
        mainFrame.displayMainFrame(panelProduct, 800);
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
		System.out.println("aaa");
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
				//refreshTable();
				//remove(sp);
				loadData();
//				panelTable.setPreferredSize(new Dimension(443,251));
//				this.setSize(550, 601);
//				this.setSize(550, 600);
			}catch(Exception e1) {
				JOptionPane.showMessageDialog(null, "Please fill all fields");
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
				//refreshTable();
				//remove(sp);
				loadData();
				this.setSize(550, 601);
				this.setSize(550, 600);
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
				//refreshTable();
				remove(sp);
				loadData();
				this.setSize(550, 601);
				this.setSize(550, 600);
			}catch(Exception e1) {
				JOptionPane.showMessageDialog(null, e1);
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
					name = rs.getString("name);
				}
			}catch(Exception ex) {
				JOptionPane.showMessageDialog(null, ex.getMessage());
			}
		}
		
		if(e.getActionCommand() == btnback.getActionCommand()) {
			mainFrame.disposeCurrentFrame();
//			new adminMain();
			new adminProducts();
		}
	}
}
