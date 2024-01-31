package snmpProject;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.table.DefaultTableModel;

import org.snmp4j.smi.VariableBinding;
import org.snmp4j.util.TableEvent;

public class RoutingTable extends SnmpTableView {

	RoutingTable(){
		super();
		this.oid=".1.3.6.1.2.1.4.21";
		snmpG("192.168.10.1/161");
        snmpG("192.168.20.1/161");
        snmpG("192.168.30.1/161");
	}
	
	@Override
	protected void frame_configPanels() {
    	mainPanel = new JPanel(new BorderLayout());

        //JPanel leftPanel = createPanel2();
        //leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        //leftPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        tabbedPane = new JTabbedPane();
        panel1 = new JPanel();
        panel2 = new JPanel();
        //mainPanel.add(leftPanel, BorderLayout.WEST);
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        this.add(mainPanel);
        this.setVisible(true);
        
	}
	

	@Override
	protected void addModel() {
		model.addColumn("adresa ruta");
		model.addColumn("maksa");
		model.addColumn("next hop");
		model.addColumn("poreklo");
	};
	
	@Override
	protected void tableRead(DefaultTableModel model,List<TableEvent> iftTableUtils) {
		int ukupno=iftTableUtils.size();
		int kolona=13;
		int red=ukupno/kolona;
		
		
		for(int i=0;i<ukupno/kolona;i++) {
			
			for(int j=0;j<11;j++) {
				
				if(j!=0 && j!=10 && j!=6 && j!=8)continue;
				
				TableEvent event=iftTableUtils.get(i+j*red);
				VariableBinding[] varBindings = event.getColumns();
				
				 if (varBindings != null) {
					 for (VariableBinding varBinding : varBindings) {
						if(j==0) {
							model.addRow(new Object[]{varBinding.getVariable()});
							//System.out.println("   adresa ruta "+varBinding.getVariable());
						}else if(j==10) {
							model.setValueAt(varBinding.getVariable(), i, 1);
							//System.out.println("    maska: "+varBinding.getVariable());
						}else if(j==6) {
							model.setValueAt(varBinding.getVariable(), i, 2);
							//System.out.println("    nextHop: "+varBinding.getVariable());
						}else if(j==8) {
							model.setValueAt(varBinding.getVariable(), i, 3);
							//System.out.println("    poreklo: "+varBinding.getVariable());
						}

		             }
				 }
			}
			
		}
	}
	
}
