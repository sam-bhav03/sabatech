import javax.swing.*;
import javax.swing.table.DefaultTableModel;



import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
public class adminReturn extends JFrame implements ActionListener {

	JLabel lblcmbprodname, lblquantity, lblreturnquantity, lblreason, lblreturnID;
	JComboBox cmbprodname;
	JTextField txtquantity, txtreturnquantity, txtreturnID;
	JButton btnfile, btnconfirm, btndelete, btnreturn, btnback;
	JPanel panel1, panel2, panel3, panel4, panel5, panel6, panel7;
	JRadioButton rad1, rad2, rad3;
	ButtonGroup radiogroup;
	JTable tableitems;
	JScrollPane pane;
	DefaultTableModel model = new DefaultTableModel();
	static ResultSet result = null;
	static int validatedReceiptID = 0;
	static String validated = "";
	
	
	adminReturn(){
		
		//setLayout(new FlowLayout());
		
		panel7 = new JPanel();
		panel7.setLayout(new FlowLayout());
		
		panel1 = new JPanel();
		btnfile = new JButton("Select Receipt");
		panel1.setLayout(new GridLayout(1,1));
		panel1.setPreferredSize(new Dimension(450, 40));
		panel1.add(btnfile);
		panel1.setBackground(SabaTech.backgroundColor);
		panel7.add(panel1);
		
		panel2 = new JPanel();
		panel2.setLayout(new GridLayout(2, 4, 10, 20));

		lblcmbprodname = new JLabel("Product Name");
		lblcmbprodname.setFont(new Font("Serif", Font.PLAIN, 15));
		
		lblquantity = new JLabel("Quantity Bought");
		lblquantity.setFont(new Font("Serif", Font.PLAIN, 15));
		
		lblreturnquantity = new JLabel("Return Quantity");
		lblreturnquantity.setFont(new Font("Serif", Font.PLAIN, 15));
		
		lblreturnID = new JLabel("Return ID");
		lblreturnID.setFont(new Font("Serif", Font.PLAIN, 15));
		txtreturnID = new JTextField(15);
		txtreturnID.setEditable(false);

		cmbprodname = new JComboBox();
		cmbprodname.addItemListener(
				//anonymous innerclass
				new ItemListener() {
					
					//handleJComboBox event
					public void itemStateChanged(ItemEvent event) {
						
						//determine whether item selected
						if(event.getStateChange() == ItemEvent.SELECTED) {
							setQuantity();
						}//end if
					}//end itemStateChanged
				}//end anonymous class
				);//end item listener

		
		txtquantity = new JTextField(15);
		txtquantity.setEditable(false);
		txtreturnquantity = new JTextField(15);
		
		panel2.add(lblreturnID);
		panel2.add(txtreturnID);
		panel2.add(lblcmbprodname);
		panel2.add(cmbprodname);
		panel2.add(lblquantity);
		panel2.add(txtquantity);
		panel2.add(lblreturnquantity);
		panel2.add(txtreturnquantity);
		panel2.setBackground(SabaTech.backgroundColor);
		panel7.add(panel2);
		
		panel3 = new JPanel();
		panel3.setPreferredSize(new Dimension(300, 100));
		panel3.setLayout(new GridLayout(4, 1, 10, 10));
		lblreason = new JLabel("Please select an appropriate reason to continue: ");
		lblreason.setFont(new Font("Serif", Font.PLAIN, 15));
		radiogroup = new ButtonGroup();
		rad1 = new JRadioButton("Broken");
		rad1.setBackground(SabaTech.backgroundColor);
		rad2 = new JRadioButton("Malfunctioning");
		rad2.setBackground(SabaTech.backgroundColor);
		rad3 = new JRadioButton("Not Matching Description");
		rad3.setBackground(SabaTech.backgroundColor);
	
		radiogroup.add(rad1);
		radiogroup.add(rad2);
		radiogroup.add(rad3);
		
		panel3.add(lblreason);
		panel3.add(rad1);
		panel3.add(rad2);
		panel3.add(rad3);
		panel3.setBackground(SabaTech.backgroundColor);
		
		panel7.add(panel3);
		
		panel4 = new JPanel();
		panel4.setLayout(new GridLayout(1, 2, 10, 10));
		panel4.setPreferredSize(new Dimension(650, 40));
		btnconfirm = new JButton("Confirm");
		btndelete = new JButton("Delete");
		
		panel4.add(btnconfirm);
		panel4.add(btndelete);
		panel4.setBackground(SabaTech.backgroundColor);
		panel7.add(panel4);
		
		panel5 = new JPanel();
		tableitems = new JTable();
		
		Object[] columnNames = {"Return ID", "Product Name", "Return Quantity", "Reason"};
		
		model.setColumnIdentifiers(columnNames);
		tableitems.setModel(model);
		
		
		
		pane = new JScrollPane(tableitems); 
		panel5.setLayout(new GridLayout(1,1, 10, 50));
		panel5.setPreferredSize(new Dimension(600, 200));
		panel5.setBackground(SabaTech.backgroundColor);
		panel5.add(pane);
		panel7.add(panel5);
		
		panel6 = new JPanel();
		panel6.setLayout(new GridLayout(1, 2, 10, 10));
		panel6.setPreferredSize(new Dimension(650, 40));
		btnreturn = new JButton("Validate Return");
		btnback = new JButton("Back");
		panel6.add(btnreturn);
		panel6.add(btnback);
		panel6.setBackground(SabaTech.backgroundColor);
		panel7.add(panel6);
		panel7.setBackground(SabaTech.backgroundColor);
		
		
		btnfile.addActionListener(this);
		btnconfirm.addActionListener(this);
		btndelete.addActionListener(this);
		btnreturn.addActionListener(this);
		btnback.addActionListener(this);

		autoReturnID();
		mainFrame.displayMainFrame(panel7, 850);
	}
	
	public void autoReturnID() {
		try {
			Connection con = null;
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/sabatech", "root", "");
			Statement stmt = con.createStatement();
			
			ResultSet rs = stmt.executeQuery("select Max(returnID) from returns");
			rs.next();
			rs.getString("Max(returnID)");
			
			if(rs.getString("Max(returnID)") == null) {
				txtreturnID.setText("R0001");
			}
			else {
				Long id = Long.parseLong(rs.getString("Max(returnID)").substring(2, rs.getString("Max(returnID)").length()));
				id++;
				txtreturnID.setText("R0" + String.format("%03d", id));
			}
		}catch(Exception ex) {
			JOptionPane.showMessageDialog(null, ex.getMessage());
		}
	}
	
	public void fillComboBox() {
		try {
			String res = validated;
			JTextField txtrec = new JTextField();
			Class.forName("com.mysql.jdbc.Driver");
			Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/sabatech", "root", "");
			String sql = "select * from sales";
			Statement statement = connection.prepareStatement(sql);
			txtrec.setText(res);
			String checkid = txtrec.getText();
			result = statement.executeQuery(sql);
			while (result.next()) {
				String ID = result.getString("ReceiptID");
				if(checkid.equals(ID))
					{
						String name = result.getString("productName");
						cmbprodname.addItem(name);	
					}
			
				}
		
			}catch(Exception ex) {
				JOptionPane.showMessageDialog(null, "Did not load data!");
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
		
		public ResultSet find(String s, String s2) {
			try {
				con = DriverManager.getConnection(url + dbName, username, password);
				ps = con.prepareStatement("Select * from sales where receiptID = ? and productName = ?");
				ps.setString(1, s);
				ps.setString(2, s2);
				rs = ps.executeQuery();
			}catch(Exception ex) {
				JOptionPane.showMessageDialog(null, ex.getMessage());
			}
			return rs;
		}
	}
	
	public class functionQuantity{
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
				ps = con.prepareStatement("Select * from product where productName = ?");
				ps.setString(1, s);
				rs = ps.executeQuery();
			}catch(Exception ex) {
				JOptionPane.showMessageDialog(null, ex.getMessage());
			}
			return rs;
		}
	}
	
	public void setQuantity() {
		function f = new function();
		ResultSet rs = null;
		rs = f.find((String) validated, (String) cmbprodname.getSelectedItem());
		try {
			if(rs.next()) {
				txtquantity.setText(rs.getString("quantity"));
			}
			else {
				JOptionPane.showMessageDialog(null, "No Quantity");
			}
		}catch(Exception ex) {
			JOptionPane.showMessageDialog(null, ex.getMessage());
		}
	}
	
	public String extractBarcode(String path){
		String barcode = "";
		try{
			BufferedReader File = new BufferedReader(new FileReader(path));
			BufferedReader File2 = new BufferedReader(new FileReader(path));
			int fileCount = 1;
				while(File2.readLine() != null) {
					fileCount++;
				}
				for(int i = fileCount;i>5;i--){
					File.readLine();
				}			
			barcode = File.readLine();			
			File.close();
			File2.close();
		}catch (FileNotFoundException fnfe) {
			
			JOptionPane.showMessageDialog(null, "You have brought an unexisting receipt, please try again !");
			fnfe.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return barcode;

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand() == btnfile.getActionCommand()) {			
			//#regionregionChoseFile
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setCurrentDirectory(new File("files/customer")); //sets current directory
			int response = fileChooser.showOpenDialog(null); //select file to open
			String clientFileName = "";
			String clientFilePath = "";
				if(response == JFileChooser.APPROVE_OPTION) {
					File file = new File(fileChooser.getSelectedFile().getPath());
					clientFileName = file.getName();				
					clientFilePath = file.getPath();
				}
			//#endregion

			String originFilePath = "files/receipts/".concat(clientFileName);
			
			String originBarcode = extractBarcode(originFilePath);
			String clientBarcode = extractBarcode(clientFilePath);
			
			int compareBarcode = originBarcode.compareTo(clientBarcode);
			
			boolean flag = false;
				if (compareBarcode != 0){
					JOptionPane.showMessageDialog(null, "Invalid receipt ID");
				}
				else{
					validated = clientFileName.substring(7,9);
					validatedReceiptID = Integer.parseInt(validated);
					flag = true;
					fillComboBox();

				}
		}
		
		if(e.getActionCommand() == btnconfirm.getActionCommand()) {
			try {
				String reason = "";
				int x = Integer.parseInt(txtreturnquantity.getText());
				int y = Integer.parseInt(txtquantity.getText());
				boolean flag = false;
				if(rad1.isSelected()) {
					reason = "Broken";
					flag = true;
				}
				else if (rad2.isSelected()) {
					reason = "Malfunctioning";
					flag = true;
				}
				else if (rad3.isSelected()){
					reason = "Not Matching Description";
					flag = true;
				}
				else {
					JOptionPane.showMessageDialog(null, "Please Choose a reason that best describes the return");
				}
				
				if(flag == true) {
					if(x <= y) {
						Object[] row = new Object[4];
						row[0] = txtreturnID.getText();
						row[1] = cmbprodname.getSelectedItem();
						row[2] = txtreturnquantity.getText();
						row[3] = reason;
						model.addRow(row);
					}
					else {
						JOptionPane.showMessageDialog(null, "The returned quantity is incorrect");
					}
				}
				
				

			}catch(Exception ex) {
				JOptionPane.showMessageDialog(null, "Please Fill all the Fields");
			}
			
		}
		
		if(e.getActionCommand() == btndelete.getActionCommand()) {
			int i = tableitems.getSelectedRow();
			if(i >= 0) {
				model.removeRow(i);
			}
			else {
				JOptionPane.showMessageDialog(null, "Delete Error, Please Select a Row");
			}
		}
		
		if(e.getActionCommand() == btnreturn.getActionCommand()) {
			//updating returns table
			try {
				Connection con = null;
				PreparedStatement pst = null;
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection("jdbc:mysql://localhost:3306/sabatech", "root", "");
				
				for(int i = 0; i < tableitems.getRowCount(); i++) {
					String valueID = (tableitems.getModel().getValueAt(i, 0).toString());
					String valueName = (tableitems.getModel().getValueAt(i, 1).toString());
					String valueQuantity = tableitems.getModel().getValueAt(i, 2).toString();
					String valueReason = tableitems.getModel().getValueAt(i, 3).toString();

					String sql = "Insert into returns values(?, ?, ?, ?)";
					pst = con.prepareStatement(sql);
					pst.setString(1, valueID);
					pst.setString(2, valueName);
					pst.setString(3, valueQuantity);
					pst.setString(4, valueReason);
					
					pst.executeUpdate();
				}	
				con.close();
				//JOptionPane.showMessageDialog(null, "Returns table updated");
				autoReturnID();
				
			}catch(Exception ex) {
				JOptionPane.showMessageDialog(null, "Failed to insert into returns table");
			}
			
			
			//updating products DB
			try {
				functionQuantity f = new functionQuantity();
				Connection con = null;
				ResultSet rs = null;
				PreparedStatement pst = null;
				
				JTextField jt = new JTextField();
				for(int i = 0; i < tableitems.getRowCount(); i++) {
					rs = f.find((String) cmbprodname.getSelectedItem());
					try {
						if(rs.next()) {
							jt.setText(rs.getString("quantity"));
						}
						else {
							JOptionPane.showMessageDialog(null, "Cannot Find Quantity");
						}
					}catch(Exception ex) {
						JOptionPane.showMessageDialog(null, ex.getMessage());
					}
					
					String valueID = (tableitems.getModel().getValueAt(i, 0).toString());
					String valueName = (tableitems.getModel().getValueAt(i, 1).toString());
					String valueQuantity = tableitems.getModel().getValueAt(i, 2).toString();
					String valueReason = tableitems.getModel().getValueAt(i, 3).toString();
					
					int a = 0;
					a = Integer.parseInt(valueQuantity);
					
					int b =Integer.parseInt(jt.getText());
					
					int quant = b + a;

					if(valueReason.equals("Not Matching Description")) {
					Class.forName("com.mysql.jdbc.Driver");
					con = DriverManager.getConnection("jdbc:mysql://localhost:3306/sabatech", "root", "");
						String query = "UPDATE product set quantity = ? where productName = ?";
						
						pst = con.prepareStatement(query);
						pst.setInt(1, quant);
						pst.setString(2, valueName);
						pst.executeUpdate();
						//JOptionPane.showMessageDialog(null, "Product Updated");
					}
					else {
						JOptionPane.showMessageDialog(null, "Product Database not updated as product is broken or malfunctioning");
					}
					con.close();
					JOptionPane.showMessageDialog(null, "Return Transaction Completed");
			}
		}catch(Exception ex) {
			JOptionPane.showMessageDialog(null, "ERROR");
		}
			
				mainFrame.disposeCurrentFrame();
				new adminMain();
			
			
	}
		if(e.getActionCommand() == btnback.getActionCommand()) {
			mainFrame.disposeCurrentFrame();
			new adminMain();
		}
}
}
