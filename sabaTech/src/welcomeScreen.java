import javax.swing.*;
import java.awt.Color;
import java.awt.*;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class welcomeScreen implements ActionListener {

    Color cyan = Color.decode("#00C4CC"); //color according to our color palette
    Color highlight = Color.decode("#568FA2"); //color according to our color palette
    
    JButton continueButton,adminButton;
    
    welcomeScreen(){
        
        //#regionCENTERPanel
        JPanel welcomePanel = new JPanel();
        welcomePanel.setLayout(new GridLayout(3,1,10,30));
        welcomePanel.setPreferredSize(new Dimension(850, 800));
        welcomePanel.setBackground(SabaTech.backgroundColor); //calling the static variable background color
        
        continueButton = new JButton("BUY A PRODUCT");
        adminButton = new JButton("ADMINISTRATION");
        continueButton.setFocusable(false);
        continueButton.setBackground(cyan);
        continueButton.setBorder(BorderFactory.createEtchedBorder(highlight,cyan));


        adminButton.setFocusable(false);
        adminButton.setBackground(cyan);
        adminButton.setBorder(BorderFactory.createEtchedBorder(highlight,cyan));

        continueButton.addActionListener(this);
        adminButton.addActionListener(this);

        welcomePanel.add(continueButton);
        welcomePanel.add(adminButton);
        //#endregion
 
        mainFrame.displayMainFrame(welcomePanel,800); //method taking panel and y-size(height) of the main Frame 
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==continueButton){
            mainFrame.disposeCurrentFrame();
            new pointOfSale();
            
        }
        if(e.getSource()==adminButton){
            mainFrame.disposeCurrentFrame();
            new adminLogin();
        }
        
    }
    

}
