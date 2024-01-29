package snmpProject;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;

import org.snmp4j.CommunityTarget;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.snmp4j.util.DefaultPDUFactory;
import org.snmp4j.util.TableEvent;
import org.snmp4j.util.TableUtils;

public class SnmpTableView extends SnmpClass implements ActionListener{
	protected String oid;
	
	protected DefaultTableModel model;
	
	@Override
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
	@Override
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
	
	protected void addModel() {};
	@Override
    protected void snmpG(String ipAddress) {
		model = new DefaultTableModel();
		
		timer = new Timer(10000, new ActionListener() {
			int cntTimer=0;
            @Override
            
            public void actionPerformed(ActionEvent e) {
                method(model,ipAddress);
                
                System.out.println(cntTimer++);
            }
        });
        timer.start();

        this.addModel();
		
		JTable TableView = new JTable(model);
		TableView.setAutoCreateColumnsFromModel(true);

		JScrollPane tcpTablePane = new JScrollPane(TableView);

		
		tabbedPane.addTab(" "+ipAddress,tcpTablePane);   
		TableView.setAutoCreateColumnsFromModel(true);

		method(model,ipAddress);
	}
	
	protected void method(DefaultTableModel model,String ipAddress){
		CommunityTarget target = new CommunityTarget();
		System.out.println(ipAddress);
        target.setAddress(new UdpAddress(ipAddress));
        System.out.println("Router "+ipAddress);
        target.setRetries(2);
        target.setTimeout(1500);
        target.setVersion(SnmpConstants.version1);
        target.setCommunity(new OctetString(COMMUNITY));
        
        try {
        	TransportMapping<?> transport = new DefaultUdpTransportMapping();
            Snmp snmp = new Snmp(transport);
            transport.listen();

            OID ifTable = new OID(oid);
            TableUtils tableUtils = new TableUtils(snmp, new DefaultPDUFactory());
            
            List<TableEvent> iftTableUtils = tableUtils.getTable(target, new OID[]{ifTable}, null, null);
            
            model.setRowCount(0);
            tableRead(model,iftTableUtils);
            
            snmp.close();
        }
        catch(Exception e) {}
            
	}
	
	protected void tableRead(DefaultTableModel model,List<TableEvent> iftTableUtils) {}
	
	@Override
	public void actionPerformed(ActionEvent e) {}

}
