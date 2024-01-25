package snmpProject;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;

import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.smi.Variable;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.util.DefaultPDUFactory;
import org.snmp4j.util.TableEvent;
import org.snmp4j.util.TableUtils;

public class TcpUdp extends SnmpClass implements ActionListener{
	
	                          //1          2      3          4              5           6           7
	private String[] values= {"closed","listen","synSent","synReceived","established","finWait1","finWait2",
			"closeWait","lastAck","closing","timeWait","deleteTCB"};

	public static void tcpTableRead(List<TableEvent> tcpTableEvents,DefaultTableModel model) {
		int x=tcpTableEvents.size();
        int brojPoKoloni=x/5;
    	int kolona=0;
    	int cnt=0;
        for (TableEvent event : tcpTableEvents) {
            VariableBinding[] varBindings = event.getColumns();
            
            if (varBindings != null) {
            	
                for (VariableBinding varBinding : varBindings) {
                	if(kolona==0) {
                		 model.addRow(new Object[]{varBinding.getVariable()}); 
              
                		 if(cnt%brojPoKoloni+1==brojPoKoloni)kolona++;
                		 cnt++;
                	}
                	else {
                		//System.out.println("cnt brojPo" +cnt%brojPoKoloni);
                		//System.out.println("kolona "+kolona);
                		try {
                			model.setValueAt(varBinding.getVariable(), cnt%brojPoKoloni, kolona);
                		}catch(Exception e) {
                		}
                		
                		if(cnt%brojPoKoloni+1==brojPoKoloni)kolona++;
                		cnt++;
                	}
                }
            }
        }
	}
	
	public static void udpTableRead(List<TableEvent> udpTableEvents,DefaultTableModel modelUDP) {
		int y=udpTableEvents.size();
        int brojPoKoloni2=y/2;
    	int kolona2=0;
    	int cnt2=0;
        for (TableEvent event : udpTableEvents) {
        	VariableBinding[] varBindings2 = event.getColumns();
        	
            if (varBindings2 != null) {
                for (VariableBinding varBinding : varBindings2) {
                	if(kolona2==0) {
                		 modelUDP.addRow(new Object[]{varBinding.getVariable()}); 
                		 if(cnt2%brojPoKoloni2+1==brojPoKoloni2)kolona2++;
                		 cnt2++;
                	}
                	else {
                		//System.out.println("cnt brojPo" +cnt2%brojPoKoloni2);
                		//System.out.println("kolona "+kolona2);
                		modelUDP.setValueAt(varBinding.getVariable(), cnt2%brojPoKoloni2, kolona2);
                		if(cnt2%brojPoKoloni2+1==brojPoKoloni2)kolona2++;
                		cnt2++;
                	}
                }
            }
        }
	}
	
	private static void updateTables(DefaultTableModel tcpTableModel,DefaultTableModel udpTableModel,String ipAddress) {
        
        updateTable(tcpTableModel,udpTableModel, ".1.3.6.1.2.1.6.13",ipAddress);
    }
	
	private static void updateTable(DefaultTableModel model, DefaultTableModel modelUDp,String oidString,String ipAddress) {
		CommunityTarget target = new CommunityTarget();
        target.setAddress(new UdpAddress(ipAddress));
        target.setRetries(2);
        target.setTimeout(1500);
        target.setVersion(SnmpConstants.version1);
        target.setCommunity(new OctetString(COMMUNITY));
        
        try {
        	TransportMapping<?> transport = new DefaultUdpTransportMapping();
            Snmp snmp = new Snmp(transport);
            transport.listen();

            OID tcpConnTableOID = new OID(".1.3.6.1.2.1.6.13");
            TableUtils tableUtils = new TableUtils(snmp, new DefaultPDUFactory());
            
            List<TableEvent> tcpTableEvents = tableUtils.getTable(target, new OID[]{tcpConnTableOID}, null, null);
            
            //System.out.println(tcpTableEvents);
            System.out.println(ipAddress);
            
            model.setRowCount(0);
            
            //tabbedPane.addTab("tcp"+ipAddress,tcpTablePane);      
    		
    		tcpTableRead(tcpTableEvents,model);
    		
    		modelUDp.setRowCount(0);
    		OID udpListenerTableOID = new OID(".1.3.6.1.2.1.7.5");
            List<TableEvent> udpTableEvents = tableUtils.getTable(target, new OID[]{udpListenerTableOID}, null, null);

            //System.out.println(udpTableEvents);
            udpTableRead(udpTableEvents,modelUDp);
            
            snmp.close();
       
       }
       catch (IOException e) {e.printStackTrace();}
	}

	public static void snmpG(String ipAddress) {
		DefaultTableModel model = new DefaultTableModel();
		DefaultTableModel modelUDP = new DefaultTableModel();
		
		timer = new Timer(5000, new ActionListener() {
			int cntTimer=0;
            @Override
            
            public void actionPerformed(ActionEvent e) {
                updateTables(model,modelUDP,ipAddress);
                
                System.out.println(cntTimer++);
            }
        });
        timer.start();

		model.addColumn("tcpConnState");
		model.addColumn("tcpConnLocalAddress");
		model.addColumn("tcpConnLocalPort");
		model.addColumn("tcpConnRemAddress");
		model.addColumn("tcpConnRemPort");
		
		JTable tcpTableView = new JTable(model);
     
		String column1Value = "Value 1";
		String column2Value = "Value 2";

		tcpTableView.setAutoCreateColumnsFromModel(true);

		JScrollPane tcpTablePane = new JScrollPane(tcpTableView);

		
		tabbedPane.addTab("tcp"+ipAddress,tcpTablePane);      
	
		modelUDP.addColumn("udpLocalAddress");
		modelUDP.addColumn("udpLocalPort");
		
		JTable udpTableView = new JTable(modelUDP);
		udpTableView.setAutoCreateColumnsFromModel(true);

		JScrollPane udpTablePane = new JScrollPane(udpTableView);
		tabbedPane.addTab("udp"+ipAddress,udpTablePane);
		updateTables(model,modelUDP,ipAddress);
    }
	
    //frameConfig
	@Override
	public void frame_configBasic() {
		super.frame_configBasic();
		this.setTitle("tcpUdp");	
	}
	
	@Override
	public void frame_configPanels() {
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
	
	
	
	private static JPanel createPanel1() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        return panel;
    }
	
	private static JPanel createPanel2() {
        JPanel panel = new JPanel();
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
	
	TcpUdp(){ super(); }

	@Override
	public void actionPerformed(ActionEvent e) {}
}
