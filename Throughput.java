package snmpProject;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

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
import org.snmp4j.util.TreeUtils;

public class Throughput extends SnmpClass {
	
	Throughput(){
		super();
		//conf();
		

		this.setLayout(new BorderLayout());
		setTitle("Graph");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel chartPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                // Crta X osu
                g.drawLine(50, getHeight() - 50, getWidth() - 50, getHeight() - 50);

                // Crta Y osu
                g.drawLine(50, getHeight() - 50, 50, 50);

                // Oznake na X osi
                for (int i = 1; i <= 10; i++) {
                    int x = 50 + i * (getWidth() - 100) / 10;
                    int y = getHeight() - 45;
                    g.drawLine(x, y - 5, x, y + 5);
                    g.drawString(Integer.toString(i), x - 5, y + 20);
                }

                // Oznake na Y osi
                for (int i = 1; i <= 10; i++) {
                    int x = 45;
                    int y = getHeight() - 50 - i * (getHeight() - 100) / 10;
                    g.drawLine(x - 5, y, x + 5, y);
                    g.drawString(Integer.toString(i), x - 30, y + 5);
                }
            }
        };

        add(chartPanel,BorderLayout.CENTER);
        JPanel panel=new JPanel();
        panel.setBackground(Color.GRAY);
        panel.add(new JLabel("Tekst"));
        add(panel,BorderLayout.SOUTH);

	}

	
	public static void conf() {
		timer = new Timer(10000, new ActionListener() {
			int cntTimer=0;
           
            @Override
            public void actionPerformed(ActionEvent e) {

            	method();
                System.out.println(cntTimer++);
            }
        });
        timer.start();
		
	}
	
	private static int preIN=0;
	private static int preOUT=0;
	public static void method() {
		String ip1="192.168.10.1/161";
		String ip2="192.168.20.1/161";
		String ip3="192.168.30.1/161";
		int[] v1=racunaj(ip1);
		int[] v2=racunaj(ip2);
		int[] v3=racunaj(ip3);
		System.out.println(v1[0]+" "+v1[1]+" "+v1[2]+" "+v1[3]+" "+v1[4]+" "+v1[5]);
		System.out.println(v2[0]+" "+v2[1]+" "+v2[2]+" "+v2[3]+" "+v2[4]+" "+v2[5]);
		System.out.println(v3[0]+" "+v3[1]+" "+v3[2]+" "+v3[3]+" "+v3[4]+" "+v3[5]);
		
		System.out.println("Ukupan broj bita IN:" +v1[0]+v2[0]+v3[0]);
		System.out.println("Ukupan broj paketa Unikast IN:" +v1[1]+v2[1]+v3[1]);
		System.out.println("Ukupan broj paketa NonUnikast IN:" +v1[2]+v2[2]+v3[2]);
		
		System.out.println("Ukupan broj bita OUT:" +v1[3]+v2[3]+v3[3]);
		System.out.println("Ukupan broj paketa Unikast OUT:" +v1[4]+v2[4]+v3[4]);
		System.out.println("Ukupan broj paketa NonUnikast OUT:" +v1[5]+v2[5]+v3[5]);
		
		System.out.println("Protok IN: "+8*(v1[0]+v2[0]+v3[0]-preIN)/10);
		System.out.println("Protok OUT: "+8*(v1[3]+v2[3]+v3[0]-preOUT)/10);
		preIN=v1[0]+v2[0]+v3[0];
		preOUT=v1[3]+v2[3]+v3[3];
		
	}
	public static int[] racunaj(String ipAddress) {
		CommunityTarget target = new CommunityTarget();
        target.setAddress(new UdpAddress(ipAddress));
        System.out.println("Router "+ipAddress);
        target.setRetries(2);
        target.setTimeout(1500);
        target.setVersion(SnmpConstants.version1);
        target.setCommunity(new OctetString(COMMUNITY));
        int v[]= {};
        try {
        	TransportMapping<?> transport = new DefaultUdpTransportMapping();
            Snmp snmp = new Snmp(transport);
            transport.listen();

            OID ifTable = new OID(".1.3.6.1.2.1.2.2");
            TableUtils tableUtils = new TableUtils(snmp, new DefaultPDUFactory());
            TreeUtils tre=new TreeUtils(snmp,new DefaultPDUFactory());
            System.out.println(tre.getSubtree(target, ifTable));
            
            
            
            List<TableEvent> iftTableUtils = tableUtils.getTable(target, new OID[]{ifTable}, null, null);
            
            System.out.println(iftTableUtils);
            

            v=tableRead(iftTableUtils);
            
            
            snmp.close();
        }
        catch(Exception e) {}
        return v;
	}
	
	public static int[] tableRead(List<TableEvent> iftTableUtils) {
		int ukupno=iftTableUtils.size();
		int kolona=22;
		int red=ukupno/kolona;
		
		int INbita=0;
		int INpaketaUnikast=0;
		int INpaketaNonUnikast=0;
		
		int OUTbita=0;
		int OUTpaketaUnikast=0;
		int OUTpaketaNonUnikast=0;
		
		//System.out.println(iftTableUtils);
		
		for(int k=0;k<9;k++) {
			if(k==3 || k==4 || k==5)continue;
			int brojac=0;
			for(int i=0;i<ukupno/kolona;i++) {
				TableEvent event=iftTableUtils.get(i+(9+k)*red);
				
				VariableBinding[] varBindings = event.getColumns();
				
				
				if (varBindings != null) {
					 for (VariableBinding varBinding : varBindings) {
						 if(k==0) {
							 INbita+=varBinding.getVariable().toInt();
							 //System.out.println(INbita);
						 }else if(k==1) {
							INpaketaUnikast+=varBinding.getVariable().toInt();
							//System.out.println(INpaketaUnikast);
						 }else if(k==2){
							 INpaketaNonUnikast+=varBinding.getVariable().toInt();
						     //System.out.println(INpaketaNonUnikast);
						 }
						 else if(k==6) {
							 OUTbita+=varBinding.getVariable().toInt();
							 //System.out.println(OUTbita);
						 }
						 else if(k==7) {
							 OUTpaketaUnikast+=varBinding.getVariable().toInt();
							 //System.out.println(OUTpaketaUnikast);
						 }
						 else if(k==8) {
							 OUTpaketaNonUnikast+=varBinding.getVariable().toInt();
						     //System.out.println(OUTpaketaNonUnikast);
						 }
					 }
			    }
				
			}
		}
		int[] v= {INbita,INpaketaUnikast,INpaketaNonUnikast,OUTbita,OUTpaketaUnikast,OUTpaketaNonUnikast};
		return v;
	}
}
