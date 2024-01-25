package snmpProject;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;

import org.snmp4j.CommunityTarget;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.snmp4j.util.DefaultPDUFactory;
import org.snmp4j.util.TableEvent;
import org.snmp4j.util.TableUtils;

public class BgpNeighbor extends SnmpClass implements ActionListener{

	public static final String COMMUNITY = "si2019";
	private static Timer timer;
	
	public static JScrollPane bottomPanel;
	public static JPanel frame;
	public static JTable TableView;
	public static DefaultTableModel model = new DefaultTableModel();
	
	BgpNeighbor(){frame_config2();}
	
	private void frame_config2() {
		SwingUtilities.invokeLater(() -> {
	        createAndShowGUI();
	    });
	}
	
	private void createAndShowGUI() {
		frame = new JPanel();
        //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        
     // Gornji deo - JPanel sa JTextField i JButton
        JPanel topPanel = new JPanel();
        JTextField textField = new JTextField(20);
        JButton button = new JButton("Click Me");
        topPanel.add(textField);
        topPanel.add(button);
        
        
        button.addActionListener(new ActionListener() {
        	String ip="";
			@Override
			public void actionPerformed(ActionEvent e) {
				if(textField.getText().equals("")) {}
                else {
                	ip=textField.getText();
                }
                ip=ip+"/161";
                System.out.println("sdfafads");
                //snmpG(ip);
                conf(ip);
                //panel1.revalidate(); 
                //panel1.repaint(); 
				
			}
        	
        });
        model.addColumn("ID");
		model.addColumn("state");
		model.addColumn("version");
		model.addColumn("Remote Address");
		model.addColumn("Remote Port");
		model.addColumn("Remote AS");
		model.addColumn("Updates Received");
		model.addColumn("Updates Sent");
		model.addColumn("Keepalive");
		model.addColumn("Elapsed time");

        TableView = new JTable(model);
        bottomPanel = new JScrollPane(TableView);


        frame.add(topPanel);
        frame.add(bottomPanel);
        bottomPanel.setVisible(true);

        // Prikazivanje JFrame
        frame.setVisible(true);
        this.add(frame);
	}

	public static void conf(String ip) {
		System.out.println("sdfasasdf");
		timer = new Timer(10000, new ActionListener() {
			int cntTimer=0;
			
            @Override
            public void actionPerformed(ActionEvent e) {
                //updateTables(model,modelUDP,ipAddress);
                method(ip);
                
                System.out.println(cntTimer++);
            }
        });
        timer.start();
	}
	
	
    public static void method(String ipAddress) {
		
		
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

            OID ifTable = new OID(".1.3.6.1.2.1.15.3");
            TableUtils tableUtils = new TableUtils(snmp, new DefaultPDUFactory());
            
            List<TableEvent> iftTableUtils = tableUtils.getTable(target, new OID[]{ifTable}, null, null);
            
            //System.out.println(iftTableUtils);
            

            tableRead(iftTableUtils);
            
            snmp.close();
        }
        catch(Exception e) {}
            
	}
    
    public static void tableRead(List<TableEvent> iftTableUtils ) {
		TableView.revalidate();
		TableView.repaint();
		bottomPanel.setVisible(true);
		frame.revalidate(); 
		frame.repaint();
			
		/*
		int ukupno=iftTableUtils.size();
		int kolona=24;
		int red=ukupno/kolona;
		
		
		for(int i=0;i<ukupno/kolona;i++) {
			for(int j=0;j<24;j++) {
				if(j!=23 && j!=18 && j!=0 && j!=1 && j!=3 && j!=4 && j!=5 && j!=9 && j!=8 && j!=10)continue;
				
				TableEvent event=iftTableUtils.get(i+j*red);
				VariableBinding[] varBindings = event.getColumns();
				
				 if (varBindings != null) {
					 for (VariableBinding varBinding : varBindings) {
						if(j==23) {
							System.out.println("    Elapsed time: "+varBinding.getVariable());
						}else if(j==18) {
							System.out.println("    Keepalive : "+varBinding.getVariable());
						}else if(j==0) {
							System.out.println("    Identifikator suseda: "+varBinding.getVariable());
						}else if(j==1) {
							System.out.println("    stanje BGP sesije sa susedom: "+varBinding.getVariable());
						}else if(j==3) {
							System.out.println("    verzija BGP koja se koristi: "+varBinding.getVariable());
						}else if(j==4) {
							System.out.println("    IP adresa suseda: "+varBinding.getVariable());
						}else if(j==9) {
							System.out.println("    Autonomni sistem u kojem je sused: "+varBinding.getVariable());
						}
						else if(j==8) {
							System.out.println("    Broj primljenih update poruka: "+varBinding.getVariable());
						}
						else if(j==10) {
							System.out.println("    Broj poslatih update poruka po susedu: "+varBinding.getVariable());
						}
						
		                
		                	
		             }
				 }
			}
			
		}
		*/
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {}

}
