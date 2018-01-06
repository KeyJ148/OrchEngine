package game.client.login.logic;

import game.client.ClientData;
import game.client.login.gui.LoginWindow;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class MapButtonListener implements ActionListener{
	
	private LoginWindow frame;

	public MapButtonListener(LoginWindow frame) {
		this.frame = frame;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JFileChooser fileOpen = new JFileChooser();
		fileOpen.setCurrentDirectory(new File("./res/map"));
		if (fileOpen.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION){
			File file = fileOpen.getSelectedFile();
			ClientData.map = file.getPath();
		}
	}

}