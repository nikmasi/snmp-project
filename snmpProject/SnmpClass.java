package snmpProject;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.Timer;

public class SnmpClass extends JFrame {
	public static final String COMMUNITY = "si2019";
	public static final String R1_HOST = "192.168.10.1";
	public static final String R2_HOST = "192.168.20.1";
	public static final String R3_HOST = "192.168.30.1";
	public static final int PORT = 161;
	public static final int LISTENER_PORT = 1620;
	public static final String[] HOSTS = {R1_HOST, R2_HOST, R3_HOST};
	
	
	protected static JTabbedPane tabs;
	protected static JPanel mainPanel;
	protected static JTabbedPane tabbedPane;
	protected static JPanel panel1;
	protected static JPanel panel2;
	
	protected static Timer timer;
	
	
	SnmpClass(){this.frame_config();}
	
	protected void frame_config() {
		frame_configBasic();
		frame_configPanels();
	}

	protected void frame_configBasic() {
		this.getContentPane().setBackground(new Color(17, 39, 49));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setSize(1060,500);
		this.setLayout(new GridLayout(1,2));
		this.setVisible(true);
	}
	
	
    protected void frame_configPanels() {}

    protected JPanel panel;
    protected void change_back_color(Color c) {
    	this.panel.setBackground(c);
    }
    
	protected  JPanel createPanel2() {
        return panel;
	}
	
	protected  void snmpG(String ipAddress) {}
	
}
