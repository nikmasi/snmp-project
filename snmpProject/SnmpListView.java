package snmpProject;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
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

public class SnmpListView extends SnmpClass implements ActionListener{
	protected String oid;
	
	SnmpListView(){super();}
	
	protected void addTabs() {}
	
	@Override
	protected void frame_configPanels() {
    	mainPanel = new JPanel(new BorderLayout());
    	
        tabbedPane = new JTabbedPane();
        //tabbedPane.addTab("adas", new JLabel());


        //mainPanel.add(leftPanel, BorderLayout.WEST);
        //System.out.println("Uslui");
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        
        this.add(mainPanel);
        this.setVisible(true);
	}
	

	@Override
    protected void snmpG(String ipAddress) {
		//model = new DefaultTableModel();
		
		timer = new Timer(10000, new ActionListener() {
			int cntTimer=0;
            @Override
            
            public void actionPerformed(ActionEvent e) {
                method(ipAddress);
                
                System.out.println(cntTimer++);
            }
        });
        timer.start();

		method(ipAddress);
	}
	
	protected void method(String ipAddress){
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
            
            //model.setRowCount(0);
            tableRead(iftTableUtils);
            
            snmp.close();
        }
        catch(Exception e) {}
            
	}
	
	protected void tableRead(List<TableEvent> iftTableUtils) {}

	
	@Override
	public void actionPerformed(ActionEvent e) {}

}
