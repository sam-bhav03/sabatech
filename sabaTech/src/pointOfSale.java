import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Formatter;
import java.util.Random;
import java.util.Scanner;

public class pointOfSale extends JFrame implements ActionListener{

	JLabel lblname, lblquantity, lbltotal;
	JTextField  txtquantity, txttotal;
	JButton btnadd, btnremove, btnproceed, btnback;
	JList cart;
	JPanel panel1, panel2, panel3, panelPOS;
	JComboBox productBox = new JComboBox();
	DefaultListModel model = new DefaultListModel();
	static ResultSet result = null;

	pointOfSale(){
		 

		panelPOS = new JPanel();
		panelPOS.setBackground(SabaTech.backgroundColor);

		//#regionA 
		lblname = new JLabel("Product Name:");
		lblname.setFont(new Font("Copperplate Gothic Light", Font.PLAIN, 20));
		productBox.setPreferredSize(new Dimension(150, 20));
		lblquantity = new JLabel("Quantity:");
		lblquantity.setFont(new Font("Copperplate Gothic Light", Font.PLAIN, 20));
		txtquantity = new JTextField(19);

		panel1 = new JPanel();
		panel1.setBackground(SabaTech.backgroundColor);
		panel1.setLayout(new GridLayout(1, 4, 10, 45));

		panel1.add(lblname);
		panel1.add(productBox);
		panel1.add(lblquantity);
		panel1.add(txtquantity);
		panelPOS.add(panel1);
		//#endregion

		//#regionB 
		cart = new JList(model);
		cart.setPreferredSize(new Dimension(350, 150));

		panel2 = new JPanel();
		panel2.setBackground(SabaTech.backgroundColor);
		panel2.add(cart);
		panelPOS.add(panel2);
		//#endregion

		//#regionC 
		btnadd = new JButton("Add to Cart");
		btnremove = new JButton("Remove Selected");
		btnproceed = new JButton("Proceed");
		btnback = new JButton("Back");

		panel3 = new JPanel();
		panel3.setBackground(SabaTech.backgroundColor);
		panel3.setLayout(new GridLayout(1 ,3, 10, 15));
		panel3.setPreferredSize(new Dimension(700, 40));

		panel3.add(btnadd);
		panel3.add(btnremove);
		panel3.add(btnproceed);
		panel3.add(btnback);
		panelPOS.add(panel3);
		//#endregion
		

		btnadd.addActionListener(this);
		btnremove.addActionListener(this);
		btnproceed.addActionListener(this);
		btnback.addActionListener(this);
        fillcombobox();
        
		mainFrame.displayMainFrame(panelPOS,800); 

	}


	public void fillcombobox() {
		try {	
			Class.forName("com.mysql.jdbc.Driver");
			Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/sabaTech", "root", "");
			String sql = "select * from product";
			Statement statement = connection.prepareStatement(sql);

			result = statement.executeQuery(sql);
			while (result.next()) {
				String name = result.getString("productName"); //retrieving the product name from database
				productBox.addItem(name); //adding product to JComboBox
			}
		}
		catch (SQLException exe) {
			JOptionPane.showMessageDialog(null, "Cannot Load Data,  SQL Exception" + exe);
			exe.printStackTrace();
		} catch (ClassNotFoundException nul) {
		} catch (NumberFormatException num) {
		}
		finally {
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
				ps = con.prepareStatement("Select productPrice, quantity from product where productName = ?");
				ps.setString(1, s);
				rs = ps.executeQuery();
			}catch(Exception ex) {
				JOptionPane.showMessageDialog(null, ex.getMessage());
			}
			return rs;
		}
	}
	
	int count = -1; //index for array
	int[] [] aryProducts = new int [100][2]; 	// a 2D array to store the index and product name for removing purposes	
	static int total = 0;
	static int amount = 0;
	static Formatter productFile,receipt;

	@Override
	public void actionPerformed(ActionEvent e) {

		function f = new function();
		ResultSet rs = null;
		String price = "productPrice";
		rs = f.find((String) productBox.getSelectedItem());
		JTextField Jquant = new JTextField();
		int tempQuantity = 0;
		boolean flag = false;  //to validate whether textfield is null or not
		
		//#regionADD
		if(e.getActionCommand() == btnadd.getActionCommand()) {
			
			try {
				tempQuantity = Integer.parseInt(txtquantity.getText());
			}catch(Exception ex) {
				JOptionPane.showMessageDialog(null, "Please fill all the fields correctly");
				flag = true;
			}
			
			if(flag == false) {
				int quantity = Integer.parseInt(txtquantity.getText());
				if(quantity > 0) {
					try {
						if(rs.next()) {
							count++;
							JTextField Jprice = new JTextField();	
							Jprice.setText(rs.getString("productPrice"));
							Jquant.setText(rs.getString("quantity"));
							int quantityDB = Integer.parseInt(Jquant.getText());  //getting quantity value from database 

							if(quantity <= quantityDB) {
								amount = Integer.parseInt(Jprice.getText()) * Integer.parseInt(txtquantity.getText()); 
								
								aryProducts[count][0] = count + 1;  //incrementing the index
								aryProducts[count][1] = amount;  //writing the amount of the product
								total += amount;  //calculating total price

								model.addElement(productBox.getSelectedItem() + " - (x" + txtquantity.getText() + ") - " + amount);
								cart.setModel(model);	//adding to JList 
							}
							else {
								JOptionPane.showMessageDialog(null, "Sorry, Only " + quantityDB + " available in stock.");
							}

						}
						else {
							JOptionPane.showMessageDialog(null, "NO DATA FOR THIS NAME");
						}
					}catch(Exception ex) {
						JOptionPane.showMessageDialog(null, ex.getMessage());
					}
				}
				else {
					JOptionPane.showMessageDialog(null, "Quantity should be greater than zero");
				}
			}
			
		

		}
		//#endregion

		//#regionRemove
		if(e.getActionCommand() == btnremove.getActionCommand()) {
			if (JOptionPane.showConfirmDialog( null,"Are you sure you want to remove selected product?","Warning",
			        JOptionPane.YES_NO_OPTION ,JOptionPane.WARNING_MESSAGE)==JOptionPane.YES_OPTION) {
	            total -= aryProducts[cart.getSelectedIndex()][1];
				model.removeElementAt(cart.getSelectedIndex());
				}

			
		}
		//#endregion

		//#regionPROCEED
		if(e.getActionCommand() == btnproceed.getActionCommand()) {
			//JOptionPane.showConfirmDialog(null, "The total is: Rs " + total + ". Do you wish to continue?" );
			if (JOptionPane.showConfirmDialog( null,"The total is: Rs " + total + ". Do you wish to continue?","Warning",
			        JOptionPane.YES_NO_OPTION ,JOptionPane.WARNING_MESSAGE)==JOptionPane.YES_OPTION) {
				int cartSize = cart.getModel().getSize(); //size of JList
				
				//#regionProduct file
				try {
					//prepares the file to be opened in the Jtable
					productFile = new Formatter(new File("files/product.txt")); //creates file
					productFile.format("%s %n", "Product Name ,Product Quantity ,Price"); //header

					for(int i=0;i<cartSize;i++){
						String line = String.valueOf(cart.getModel().getElementAt(i)); //extracts each line
						String[] arrayLine = line.split("- ", 3); //split into an array
						String fName, fQuantity, fPrice,finalString = "";
						
						fName = arrayLine[0];
						fQuantity = "/".concat(arrayLine[1].substring(2,(arrayLine[1].length() -2))); //takes out the brackets
						fPrice = "/".concat(arrayLine[2]);

						finalString += fQuantity;
						finalString += fPrice;
						productFile.format("%s %s %n", fName, finalString); 
						//file ready for JTable in checkout
					}
				} catch (FileNotFoundException fnfe) {
					fnfe.printStackTrace();
				}
				productFile.close();
				//#endregion
				try {
					Receipt(cart);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				mainFrame.disposeCurrentFrame();
				new checkout();
				}
			
			
		}
		//#endregion
		
		if(e.getActionCommand() == btnback.getActionCommand()) {
			if (JOptionPane.showConfirmDialog( null,"Are you sure you want to go back?","Warning",
		        JOptionPane.YES_NO_OPTION ,JOptionPane.WARNING_MESSAGE)==JOptionPane.YES_OPTION) {
				mainFrame.disposeCurrentFrame();
				new welcomeScreen();
			}
			
		}
	}
	
	public void Receipt(JList a) throws IOException{
		int cartSize = cart.getModel().getSize(); //size of JList
		int totalItemsBought = 0;
		int receiptID;
		
		receiptID = getReceiptID(true);
		String path = "files/receipts/receipt" + receiptID +  ".txt"; //formats path for shop copy
		String path2 = "files/customer/receipt" + receiptID +  ".txt"; //formats path for customer copy
		String dates = SabaTech.currentDate.toString();
		try {
			
			receipt = new Formatter(new File(path)); //creates file
			receipt.format("%s %n", "--                   Welcome to sabatech                --"); //header
			receipt.format("%s %d %n", "Receipt ID: ", receiptID); 
			receipt.format("%n", "" );
			receipt.format("%s %n", "Product                          Quantity    Price(rs)");

			for(int i=0;i<cartSize;i++){

				String line = String.valueOf(cart.getModel().getElementAt(i)); //extracts each line
				
				String[] arrayLine = line.split("- ", 3); //split into an array
				String fName, fQuantity, fPrice = " ";
				
				fName = arrayLine[0];
				int fNameLength = fName.length();
				int offset = (30 - fNameLength); //30 = total space 
					for(int j = 0;j < offset;j++){ 
						fName += " "; // adds space to beautify receipt
					}
				
				fQuantity = arrayLine[1].substring(2,(arrayLine[1].length() -2)); 
				totalItemsBought += Integer.parseInt(fQuantity); //convert quantity
				fQuantity += "         ";
				fPrice = arrayLine[2];

				receipt.format("%d %s %s %s %n", (i+1), fName, fQuantity, fPrice);
				
			}
			receipt.format("%s %d %s %d %n", "Total items bought: ", totalItemsBought,"  Total :", total);
			receipt.format("%n", "" );
			receipt.format("%s %n", barcodeGen());  //calling the barcode function 
			receipt.format("%n", "" );
			receipt.format("%s %n", dates); // 
			receipt.format("%s %n", "--       Thank you for your visit, see you soon :)      --"); //footer

		} catch (FileNotFoundException fnfe) {
				
			fnfe.printStackTrace();
		}
		receipt.close();
		File receipt1 = new File(path);
		File receipt2 = new File(path2);
		copyFileUsingStream(receipt1,receipt2);
	}
	
	public String barcodeGen(){
		String barcode = "";
		String characters = "._"; //barcode characters
		Random r = new Random();
		for (int i=0;i<40;i++){ //40 length barcode
			barcode += characters.charAt(r.nextInt(2)); //randomisation
		}
		return barcode;	
	}

	public static int getReceiptID(boolean flag){
		// the boolean parameter flag is used to either only read the ID(false)
		// or to update it when being called for the receipt(true)
		int id = 1;
			try{
				Scanner receiptFile = new Scanner(new File("files/receiptIndex.txt"));
				while(receiptFile.hasNextLine()){
					id += Integer.parseInt(receiptFile.nextLine()); //increment ID
				}
				receiptFile.close();
			}catch (FileNotFoundException fnfe) {
				fnfe.printStackTrace();
			}	

		if(flag == true){
			try{
				Formatter receiptFile2 = new Formatter(new File("files/receiptIndex.txt")); 
				receiptFile2.format("%d", id); //overwrites the ID by the latest one
				receiptFile2.close();
			} catch (FileNotFoundException fnfe) {
					fnfe.printStackTrace();
			}
			return id;
		}
		else {
			return (id -1); 
		}	
	}
	
	private static void copyFileUsingStream(File source, File dest) throws IOException {
		InputStream inputfile = null;
		OutputStream outputfile = null;
		try {
			inputfile = new FileInputStream(source);
			outputfile = new FileOutputStream(dest);
			byte[] buffer = new byte[1024];  //copy the String of the file into the buffer
			int length;
			while ((length = inputfile.read(buffer)) > 0) {
				outputfile.write(buffer, 0, length);  //writes a copy of the receipt
			}
		} finally {
			inputfile.close();
			outputfile.close();
		}
	}

}