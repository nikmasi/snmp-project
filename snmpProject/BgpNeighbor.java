package snmpProject;

import java.awt.BorderLayout;
import java.awt.Color;
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

public class BgpNeighbor extends SnmpTableView implements ActionListener{

	BgpNeighbor(){
		super();
		this.change_back_color(new Color(218,165,32));
		this.oid=".1.3.6.1.2.1.15.3";
	}
	
	@Override
	protected void addModel() {
		model.addColumn("ID");
		model.addColumn("state");
		model.addColumn("version");
		model.addColumn("Remote Address");
		model.addColumn("Remote AS");
		model.addColumn("Updates Received");
		model.addColumn("Updates Sent");
		model.addColumn("Keepalive");
		model.addColumn("Elapsed time");
	};
	
	@Override
	protected void tableRead(DefaultTableModel model,List<TableEvent> iftTableUtils) {

        int ukupno=iftTableUtils.size();
		int kolona=24;
		int red=ukupno/kolona;
		
		for(int j=0;j<24;j++) {
			if(j!=23 && j!=18 && j!=0 && j!=1 && j!=3 && j!=4 && j!=6 && j!=9 && j!=8 && j!=10)continue;
			int brojac=0;
			for(int i=0;i<ukupno/kolona;i++) {
				TableEvent event=iftTableUtils.get(i+(0+j)*red);
				
				VariableBinding[] varBindings = event.getColumns();
				if (varBindings != null) {
					 for (VariableBinding varBinding : varBindings) {
						if(j==23) {
							model.setValueAt(varBinding.getVariable(), i, 8);
						}else if(j==18) {
							//System.out.println("    Keepalive : "+varBinding.getVariable());
							model.setValueAt(varBinding.getVariable(), i, 7);
						}else if(j==0) {
							model.addRow(new Object[]{varBinding.getVariable()});
						}else if(j==1) {
							model.setValueAt(varBinding.getVariable(), i, 1);
							//System.out.println("    stanje BGP sesije sa susedom: "+varBinding.getVariable());
						}else if(j==3) {
							model.setValueAt(varBinding.getVariable(), i, 2);
							//System.out.println("    verzija BGP koja se koristi: "+varBinding.getVariable());
						}else if(j==6) {
							model.setValueAt(varBinding.getVariable(), i, 3);
							//System.out.println("    IP adresa suseda: "+varBinding.getVariable());
						}else if(j==8) {
							model.setValueAt(varBinding.getVariable(), i, 4);
							//System.out.println("    Autonomni sistem u kojem je sused: "+varBinding.getVariable());
						}
						else if(j==9) {
							model.setValueAt(varBinding.getVariable(), i, 5);
							//System.out.println("    Broj primljenih update poruka: "+varBinding.getVariable());
						}
						else if(j==10) {
							model.setValueAt(varBinding.getVariable(), i, 6);
							//System.out.println("    Broj poslatih update poruka po susedu: "+varBinding.getVariable());
						}
						
		                
		                	
		             }
			}
		}
	
		
		
	}
	}

	
	@Override
	public void actionPerformed(ActionEvent e) {}

}
