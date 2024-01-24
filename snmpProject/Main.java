package snmpProject;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.fluent.SnmpBuilder;


public class Main extends JFrame{
	
	public Main() {
		 this.setTitle("snmp");
	     setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	     setSize(400, 280);
	     setLocationRelativeTo(null);
	     
	     setLayout(new GridBagLayout());
	     GridBagConstraints gbc = new GridBagConstraints();
	     JButton button1 = createButton("<html>1<br>interface<br>status</html>",1);

	     button1.setPreferredSize(new Dimension(100, 100));
	     gbc.gridx = 0;
	     gbc.gridy = 0;
	     gbc.gridwidth = 1;
	     gbc.insets = new Insets(6, 6, 6, 6);
	     add(button1, gbc);

	     // Razmak između prvog dugmeta i ostalih dugmadi
	     gbc.gridx = 1;
	     gbc.gridy = 0;
	     gbc.gridwidth = 1;
	     add(Box.createHorizontalStrut(10), gbc);

	        // Sledeća četiri dugmeta u prvom redu
	     for (int i = 2; i <= 5; i++) {
	    	 JButton button;
	         if(i==2) button = createButton("<html>2<br>throughput</html>",2);
	         else if(i==3)button = createButton("<html>3<br>routing<br>table</html>",3);
	         else if(i==4)button = createButton("<html>4<br>bgp<br>neighbor</html>",4);
	         else button = createButton("<html>5<br>bgp<br>attributes</html>",5);
	        	
	         gbc.gridx = i;
	         gbc.gridy = 0;
	         gbc.gridwidth = 1;
	         gbc.insets = new Insets(6, 6, 6, 6);
	         add(button, gbc);
	     }

	        // Sledeća četiri dugmeta u drugom redu
	     for (int i = 6; i <= 9; i++) {
	         JButton button;
	         if(i==6) button = createButton("<html>6<br>snmp<br>trap</html>",6);
	         else if(i==7)button = createButton("<html>7<br>snmp<br>protocol</html>",7);
	         else if(i==8)button = createButton("<html>8<br>processor<br>memory</html>",8);
	         else button = createButton("<html>9<br>tcp<br>udp</html>",9);
	            //JButton button = createButton("Dugme " + i);
	         gbc.gridx = i - 4;
	         gbc.gridy = 1;
	         gbc.gridwidth = 1;
	         gbc.insets = new Insets(6,6, 6, 6);
	         add(button, gbc);
	     }

	     this.setSize(670, 320);
	     this.setResizable(false);
	     setLocationRelativeTo(null); 
	}
	
	private JButton createButton(String text,int num) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setPreferredSize(new Dimension(100, 100));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(192,192,192));
        
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	SnmpClass snmpC=null;//new SnmpClass();
            	if(num==4)snmpC=new BgpNeighbor();
            	else if(num==9)snmpC=new TcpUdp();
            	snmpC.setVisible(true);
            	dispose();
            }
        });
       
        return button;
    }
	
	
	public static void main(String[] args) throws IOException {  
		PDU request = new PDU();
		SnmpBuilder snmpBuilder = new SnmpBuilder();
        Snmp snmp = snmpBuilder.udp().v3().usm().threads(2).build();
        snmp.listen();
		Main main=new Main();
		main.setVisible(true);
	}  
	
}
