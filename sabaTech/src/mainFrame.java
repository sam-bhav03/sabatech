import javax.swing.*;
import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.Dimension;

public class mainFrame extends JFrame {

    static JFrame mainLayerFrame;

    mainFrame(){
        //constructor        
    }
    
    public static void displayMainFrame(JPanel currentPanel, int ySize){
         
        //#regionOTHERpanels - to sort later
        JPanel leftPanel = new JPanel();
        JPanel rightPanel = new JPanel();
        JPanel bottomPanel = new JPanel();

        leftPanel.setBackground(SabaTech.backgroundColor);
        rightPanel.setBackground(SabaTech.backgroundColor);
        bottomPanel.setBackground(SabaTech.backgroundColor);
        leftPanel.setPreferredSize(new Dimension(100,400));
        rightPanel.setPreferredSize(new Dimension(100,400));
        bottomPanel.setPreferredSize(new Dimension(100,100));

        ImageIcon imageAdmin = new ImageIcon("resources/icons/ST_admin.png");
        JLabel adminText = new JLabel();
        adminText.setIcon(imageAdmin);
        adminText.setOpaque(true);
        adminText.setPreferredSize(new Dimension(100,100));
        adminText.setBackground(SabaTech.backgroundColor);

        //#endregion
        
        //#regionNORTHpanel: display header & logo
        ImageIcon imageLogo = new ImageIcon("resources/images/ST_logo2.png");
        JLabel welcomeText = new JLabel();
        welcomeText.setIcon(imageLogo);
        welcomeText.setBackground(SabaTech.backgroundColor);
        welcomeText.setOpaque(true);
        welcomeText.setHorizontalAlignment(JLabel.CENTER);
        //#endregion
         
        //#regionMainFrame
        mainLayerFrame = new JFrame();
        mainLayerFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainLayerFrame.setBackground(SabaTech.backgroundColor);
        mainLayerFrame.setSize(1100,ySize);
        mainLayerFrame.setLayout(new BorderLayout());
        ImageIcon imageIcon = new ImageIcon("resources/icons/ST_icon.png");
        mainLayerFrame.setIconImage(imageIcon.getImage());
        mainLayerFrame.setLocationRelativeTo(null);  //opens frame in the middle of the screen
        mainLayerFrame.setVisible(true);
        
        mainLayerFrame.add(welcomeText,BorderLayout.NORTH);
        mainLayerFrame.add(currentPanel,BorderLayout.CENTER); //currentpanel contains the other panels(all forms)
        mainLayerFrame.add(leftPanel,BorderLayout.WEST);
        mainLayerFrame.add(adminText,BorderLayout.EAST);
        mainLayerFrame.add(bottomPanel,BorderLayout.SOUTH);
        
        
        //#endregion
    }

    public static void disposeCurrentFrame(){
        mainLayerFrame.dispose(); //hiding the current form
    }


}
