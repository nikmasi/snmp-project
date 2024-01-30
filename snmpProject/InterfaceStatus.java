package snmpProject;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.snmp4j.smi.VariableBinding;
import org.snmp4j.util.TableEvent;

public class InterfaceStatus extends SnmpListView{

	protected ArrayList<JPanel> panels=new ArrayList<>();
	
	InterfaceStatus(){
		this.oid=".1.3.6.1.2.1.2.2";
		addTabs();
	}
	
	
	protected void addTabs() {
		panels.add(new JPanel());
		panels.get(0).setBackground(new Color(250,240,230));
		panels.add(new JPanel());
		panels.add(new JPanel());
		panels.get(0).setLayout(new BoxLayout(panels.get(0),BoxLayout.Y_AXIS));
		panels.get(1).setLayout(new BoxLayout(panels.get(1),BoxLayout.Y_AXIS));
		panels.get(2).setLayout(new BoxLayout(panels.get(2),BoxLayout.Y_AXIS));

		JScrollPane scrollPane1 = new JScrollPane(panels.get(0));
		JScrollPane scrollPane2 = new JScrollPane(panels.get(1));
		JScrollPane scrollPane3 = new JScrollPane(panels.get(2));

		tabbedPane.addTab("Router 1",scrollPane1);
		tabbedPane.addTab("Router 2",scrollPane2);
		tabbedPane.addTab("Router 3",scrollPane3);

		snmpG("192.168.10.1/161");
		//snmpG("192.168.20.1/161");
		//snmpG("192.168.30.1/161");
	}
	
	@Override
	protected void tableRead(List<TableEvent> iftTableUtils,String ipAddress){
		int ukupno=iftTableUtils.size();
		int kolona=22;
		int red=ukupno/kolona;
		
		JPanel panel=null;
		System.out.println(ipAddress);
		if(ipAddress=="192.168.10.1/161") {
			panel=panels.get(0);
		}else if(ipAddress=="192.168.20.1/161") {
			panel=panels.get(1);
		}else if(ipAddress=="192.168.30.1/161") {
			panel=panels.get(2);
		}
		
		
		for(int i=0;i<ukupno/kolona;i++) {
			for(int j=0;j<8;j++) {
				
				TableEvent event=iftTableUtils.get(i+j*red);
				VariableBinding[] varBindings = event.getColumns();

				 if (varBindings != null) {
					 for (VariableBinding varBinding : varBindings) {
						if(j==0) {
							//panels[0].add("interfejs "+varBinding.getVariable(), new JLabel());
							panel.add("interfejs ",new JLabel("interfejs "+varBinding.getVariable()));
							//System.out.println("interfejs "+varBinding.getVariable());
						}else if(j==1) {
							panel.add("opis ",new JLabel("    opis: "+varBinding.getVariable()));
							//System.out.println("    opis: "+varBinding.getVariable());
						}else if(j==2) {
							panel.add("tip ",new JLabel("    tip: "+varBinding.getVariable()));
							//System.out.println("    tip: "+varBinding.getVariable());
						}else if(j==3) {
							panel.add("MTU ",new JLabel("    MTU: "+varBinding.getVariable()));
							//System.out.println("    MTU: "+varBinding.getVariable());
						}else if(j==4) {
							panel.add("brzina interfejsa: ",new JLabel("    brzinu interfejsa: "+varBinding.getVariable()));
							//System.out.println("    brzinu interfejsa: "+varBinding.getVariable());
						}else if(j==5) {
							panel.add("opis ",new JLabel("    opis: "+varBinding.getVariable()));
							//System.out.println("    fizicka adresa: "+varBinding.getVariable());
						}else if(j==6) {
							panel.add("administrativni status ",new JLabel("    administrativni status: "+varBinding.getVariable()));
							//System.out.println("    administrativni status: "+varBinding.getVariable());
						}
						else if(j==7) {
							panel.add("operativni status ",new JLabel("    operativni status: "+varBinding.getVariable()));
							//System.out.println("    operativni status: "+varBinding.getVariable());
						}
		                
		                	
		             }
				 }
			}
			
		}
	}
}
