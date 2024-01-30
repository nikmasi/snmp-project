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
	
	private GridBagConstraints gbc = new GridBagConstraints();
	
	public Main() {
		 init();
	     initButtons();
	}
	
	private void init() {
		this.setTitle("snmp");
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    setSize(400, 280);
	    setLocationRelativeTo(null);
	    setLayout(new GridBagLayout());
	}
	
	private void gbcSet(int varX,int varY) {
		gbc.gridx = varX;
		gbc.gridy = varY;
	    gbc.gridwidth = 1;
	    gbc.insets = new Insets(6, 6, 6, 6);
	    //razmak izmedju dugmeta
	}
	
	private void initButtons() {
		
	    JButton button1 = createButton("<html>1<br>interface<br>status</html>",1);
	    button1.setPreferredSize(new Dimension(100, 100));
	    gbcSet(0,0);
	    add(button1, gbc);

	    gbcSet(1,0);
	    add(Box.createHorizontalStrut(10), gbc);

	     for (int i = 2; i <= 5; i++) {
	    	 JButton button;
	         if(i==2) button = createButton("<html>2<br>throughput</html>",2);
	         else if(i==3)button = createButton("<html>3<br>routing<br>table</html>",3);
	         else if(i==4)button = createButton("<html>4<br>bgp<br>neighbor</html>",4);
	         else button = createButton("<html>5<br>bgp<br>attributes</html>",5);
	        	
	         gbcSet(i,0);
	         add(button, gbc);
	     }

	     for (int i = 6; i <= 9; i++) {
	         JButton button;
	         if(i==6) button = createButton("<html>6<br>snmp<br>trap</html>",6);
	         else if(i==7)button = createButton("<html>7<br>snmp<br>protocol</html>",7);
	         else if(i==8)button = createButton("<html>8<br>processor<br>memory</html>",8);
	         else button = createButton("<html>9<br>tcp<br>udp</html>",9);
	         
	         gbcSet(i-4,1);
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
            	if(num==1)snmpC=new InterfaceStatus();
            	else if(num==2)snmpC=new Throughput();
            	else if(num==3)snmpC=new RoutingTable();
            	else if(num==4)snmpC=new BgpNeighbor();
            	else if(num==5)snmpC=new BgpAttributes();
            	else if(num==6)snmpC=new SnmpTrap();
            	else if(num==7)snmpC=new SnmpProtocol();
            	else if(num==8)snmpC=new ProcessorMemory();
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
