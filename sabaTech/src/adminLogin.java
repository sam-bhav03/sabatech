import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.*;

public class adminLogin extends JFrame{
	private JLabel lblLogin;
	private JLabel lblID;
	private JLabel lblPassword;
	private JTextField txtUsername;
	private JPasswordField txtPassword;
	private JButton btnEnter;
	private JButton btnBack;
	private JPanel txtPanel;
    private JFrame loginFrame;
	private JPanel buttonPanel; 
	private JPanel CheckboxPanel; 
	private  JCheckBox  chkPassword;
    
	Connection conn;
	PreparedStatement pst;
	
	//username: saba password: tech

	public adminLogin()
	{
        loginFrame = new JFrame();
		loginFrame.setLayout(new FlowLayout());
		loginFrame.getContentPane().setBackground(SabaTech.backgroundColor);
		
		ImageIcon imageIcon = new ImageIcon("resources/icons/ST_icon.png");
		loginFrame.setIconImage(imageIcon.getImage());
		// Initialize and adding label to JFrame
		lblLogin = new JLabel("SABATECH ADMINISTRATORS LOGIN");
		lblLogin.setFont(new Font("Menlo", Font.BOLD,14));
		loginFrame.add(lblLogin);

		// Initialize and adding label and text field to JFrame
		lblID = new JLabel("Username: ");
		lblID.setFont(new Font("serif",Font.PLAIN,14));
		 
		
		lblPassword = new JLabel("Password: ");
		lblPassword.setFont(new Font("serif",Font.PLAIN,14));
		//lblPassword.setForeground(Color.WHITE);
		
		txtUsername = new JTextField(10);
		txtPassword = new JPasswordField(4);

		txtPanel = new JPanel();
        txtPanel.setBackground(SabaTech.backgroundColor);
		txtPanel.setLayout(new GridLayout(2,2));
		txtPanel.setPreferredSize(new Dimension(330,80));
		txtPanel.add(lblID);
		txtPanel.add(txtUsername);
		txtPanel.add(lblPassword);
		txtPanel.add(txtPassword);
		//txtPanel.setBackground(Color.DARK_GRAY);
		loginFrame.add(txtPanel);
		
		//Adding Button to Panel and Frame
		btnEnter = new JButton("Login");
		btnBack = new JButton("BACK");
		buttonPanel = new JPanel();
        buttonPanel.setBackground(SabaTech.backgroundColor);
		buttonPanel.setLayout(new GridLayout(1,3));
		
		buttonPanel.add(btnEnter);
		buttonPanel.add(btnBack);
		
		buttonPanel.setPreferredSize(new Dimension(280,30));
		
		loginFrame.add(buttonPanel);
		chkPassword = new JCheckBox("Show/Hide Password");
		chkPassword.setBackground(SabaTech.backgroundColor);
		//chkPassword.setBackground(Color.DARK_GRAY);
		//chkPassword.setForeground(Color.WHITE);
		chkPassword.setBorder(null);
		
		CheckboxPanel = new JPanel();
        CheckboxPanel.setBackground(SabaTech.backgroundColor);
		CheckboxPanel.setLayout(new GridLayout(1, 2));
		CheckboxPanel.setPreferredSize(new Dimension(325, 30));
		///CheckboxPanel.setBackground(Color.DARK_GRAY);
		CheckboxPanel.add(chkPassword);
		loginFrame.add(CheckboxPanel);
		
		loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		loginFrame.setSize(600, 300);
        loginFrame.setBackground(SabaTech.backgroundColor);
        loginFrame.setLocationRelativeTo(null); 
		loginFrame.setVisible(true);

		//Register event handlers
		ButtonHandler handler = new ButtonHandler();
		btnEnter.addActionListener(handler);
		btnBack.addActionListener(handler);
		chkPassword.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (chkPassword.isSelected()) {
					txtPassword.setEchoChar((char) 0);
				} else {
					txtPassword.setEchoChar('*');
				}
			}
		});
	
	}
	
	//Event handling
		private class ButtonHandler implements ActionListener
			{
				public void actionPerformed(ActionEvent event)
				{
					if(event.getActionCommand() == btnEnter.getActionCommand())
					{
						if(txtUsername.getText().equals("saba")) {
							if(txtPassword.getText().equals("tech")) {
								loginFrame.dispose();
								JOptionPane.showMessageDialog(null, "Login Successfull");
                                new adminMain();
							}
							else {
							    JOptionPane.showMessageDialog(null, "Incorrect Password");
							    txtPassword.setText("");
							}
						}
						else {
							JOptionPane.showMessageDialog(null, "Incorrect Username");
						    txtUsername.setText("");
						}
						
					}
					if(event.getActionCommand() == btnBack.getActionCommand())
					{
						loginFrame.dispose();
						new welcomeScreen();
					}
					
				}
			}
}
