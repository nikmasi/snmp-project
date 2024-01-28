package snmpProject;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
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
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.snmp4j.util.DefaultPDUFactory;
import org.snmp4j.util.TableEvent;
import org.snmp4j.util.TableUtils;

public class BgpAttributes extends SnmpTableView implements ActionListener{

	BgpAttributes(){
		super();
		this.change_back_color(new Color(255,218,185));
		this.oid=".1.3.6.1.2.1.15.6";
	}
	
	@Override
	protected void addModel() {
		model.addColumn("origin");
		model.addColumn("as-path");
		model.addColumn("next hop");
		model.addColumn("MED");
		model.addColumn("Local PreferenceAtomic aggregate");
		model.addColumn("asggregator AS");
		model.addColumn("aggregator Address");
		model.addColumn("best?");
	};
	
	
	@Override
	protected void tableRead(DefaultTableModel model,List<TableEvent> iftTableUtils) {
        int ukupno=iftTableUtils.size();
		int kolona=13;
		int red=ukupno/kolona;
		
		for(int i=0;i<ukupno/kolona;i++) {
			for(int j=0;j<13;j++) {
				if(j==0 || j==1 || j==2 || j==8 || j==11)continue;
				TableEvent event=iftTableUtils.get(i+j*red);
				VariableBinding[] varBindings = event.getColumns();
				
				 if (varBindings != null) {
					 for (VariableBinding varBinding : varBindings) {
						if(j==3) {
							model.addRow(new Object[]{varBinding.getVariable()});
							//System.out.println("    Origin: "+varBinding.getVariable());
						}else if(j==4) {
							model.setValueAt(varBinding.getVariable(), i, 1);
							//System.out.println("    AS-Path lista: "+varBinding.getVariable());
						}else if(j==5) {
							model.setValueAt(varBinding.getVariable(), i, 2);
							//System.out.println("    Next Hop: "+varBinding.getVariable());
						}else if(j==6) {
							model.setValueAt(varBinding.getVariable(), i, 3);
							//System.out.println("    MED: "+varBinding.getVariable());
						}else if(j==7) {
							model.setValueAt(varBinding.getVariable(), i, 4);
							//System.out.println("    Local PreferenceAtomic aggregate: "+varBinding.getVariable());
						}else if(j==9) {
							model.setValueAt(varBinding.getVariable(), i, 5);
							//System.out.println("    Aggregator AS: "+varBinding.getVariable());
						}else if(j==10) {
							model.setValueAt(varBinding.getVariable(), i, 6);
							//System.out.println("    Aggregator Address: "+varBinding.getVariable());
						}
						else if(j==12) {
							if(varBinding.getVariable().toInt()==2)
								model.setValueAt(true, i, 7);
							else if(varBinding.getVariable().toInt()==1)
								model.setValueAt(false, i, 7);
							else
								model.setValueAt(varBinding.getVariable(), i, 7);
							//System.out.println("    Da li je ruta najbolja: "+varBinding.getVariable());
						}
		                
		                	
		             }
				 }
			}
			
		}
	}

	
	@Override
	public void actionPerformed(ActionEvent e) {}

}
