package snmpProject;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;

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
		panels.add(new JPanel());
		panels.add(new JPanel());
		System.out.println("ovde");
		tabbedPane.addTab("Router 1",panels.get(0));

		tabbedPane.addTab("Router 2",panels.get(1));

		tabbedPane.addTab("Router 3",panels.get(2));
		//mainPanel.add(tabbedPane, BorderLayout.CENTER);

		//snmpG("192.168.10.1/161");
		//snmpG("192.168.20.1/161");
		//snmpG("192.168.30.1/161");
	}
	
	@Override
	protected void tableRead(List<TableEvent> iftTableUtils){
		int ukupno=iftTableUtils.size();
		int kolona=22;
		int red=ukupno/kolona;
		
		
		for(int i=0;i<ukupno/kolona;i++) {
			for(int j=0;j<8;j++) {
				
				TableEvent event=iftTableUtils.get(i+j*red);
				VariableBinding[] varBindings = event.getColumns();

				 if (varBindings != null) {
					 for (VariableBinding varBinding : varBindings) {
						if(j==0) {
							//panels[0].add("interfejs "+varBinding.getVariable(), new JLabel());
							panels.get(0).add("interfejs "+varBinding.getVariable(),new JLabel());
							//System.out.println("interfejs "+varBinding.getVariable());
						}else if(j==1) {
							//System.out.println("    opis: "+varBinding.getVariable());
						}else if(j==2) {
							//System.out.println("    tip: "+varBinding.getVariable());
						}else if(j==3) {
							//System.out.println("    MTU: "+varBinding.getVariable());
						}else if(j==4) {
							//System.out.println("    brzinu interfejsa: "+varBinding.getVariable());
						}else if(j==5) {
							//System.out.println("    fizicka adresa: "+varBinding.getVariable());
						}else if(j==6) {
							//System.out.println("    administrativni status: "+varBinding.getVariable());
						}
						else if(j==7) {
							//System.out.println("    operativni status: "+varBinding.getVariable());
						}
		                
		                	
		             }
				 }
			}
			
		}
	}
}
