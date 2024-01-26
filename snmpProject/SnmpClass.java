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
	
	
    protected void frame_configPanels() {
    	mainPanel = new JPanel(new BorderLayout());

        JPanel leftPanel = createPanel2();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        tabbedPane = new JTabbedPane();
        panel1 = new JPanel();
        panel2 = new JPanel();
        mainPanel.add(leftPanel, BorderLayout.WEST);
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        this.add(mainPanel);
        this.setVisible(true);
	}

    private JPanel panel;
    protected void change_back_color(Color c) {
    	this.panel.setBackground(c);
    }
    
	protected  JPanel createPanel2() {
		panel = new JPanel();
        panel.setLayout(new GridLayout());
        panel.setBackground(new Color(169, 182, 195));

        JPanel panLab=new JPanel();
        JPanel panOth=new JPanel();
        panLab.setLayout(new FlowLayout());
        panOth.setLayout(new FlowLayout());
        
        JLabel label = new JLabel("Address:");
        JComboBox<String> comboBox = new JComboBox<>(new String[]{"........"});
        JButton advancedButton = new JButton("Advanced...");
        advancedButton.setBackground(new Color(222, 161, 42));
       
        panLab.add(label);
        panLab.add(comboBox);
        panLab.add(advancedButton);
        JTextField textField = new JTextField(20);
        
        textField.setEditable(false);
        String ip="";
       
        advancedButton.addActionListener(new ActionListener() {
            private Frame mainPanel=this.mainPanel;

			@Override
            public void actionPerformed(ActionEvent e) {
            	JDialog dialog = new JDialog(this.mainPanel, "Advanced properties", true);
                dialog.setLayout(new FlowLayout());
                JTextField dialogTextField = new JTextField(20);
                JButton okButton = new JButton("OK");

                okButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String text = dialogTextField.getText();
                        textField.setText(text); 
                        dialog.dispose(); // Zatvaranje dijaloga
                        comboBox.addItem(text);
                        comboBox.setSelectedItem(text);
                    }
                });

                dialog.add(new JLabel("Enter Address:"));
                dialog.add(dialogTextField);
                dialog.add(okButton);
                dialog.setSize(300, 150);
                dialog.setVisible(true);
            }
        });
        
        JButton openButton = new JButton("Open");
        panOth.add(textField);
        
        openButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
            	String ip="";
            	
                if(textField.getText().equals("")) {
                	if(comboBox.getSelectedItem().equals("........")) {
                		//panel1.add("s",new JLabel("prazna textField"));
                	}else {
                		ip=(String) comboBox.getSelectedItem();
                	}
                }
                else {
                	if(comboBox.getSelectedItem().equals("........")) {
                		//panel1.add("sa",new JLabel(textField.getText()));
                	}else {
                		ip=(String) comboBox.getSelectedItem();
                	}
                }
                ip=ip+"/161";
                
                snmpG(ip);
                
                panel1.revalidate(); 
                panel1.repaint();    
            }
        });
        
        JPanel panOpen=new JPanel();
        panOpen.setLayout(new FlowLayout());
        panOpen.add(openButton);
        panel.add(panLab);
        panel.add(panOth);
        panel.add(panOpen);

        return panel;
	}
	
	protected  void snmpG(String ipAddress) {}
	
}
