package snmpProject;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BgpAttributes extends SnmpClass implements ActionListener{

	BgpAttributes(){super();this.change_back_color(new Color(255,218,185));}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {}

}
