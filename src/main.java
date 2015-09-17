import java.awt.EventQueue;
import java.io.File;

import ui.Mainwindow;

public class main {

	
	public static void main(String[] args) {

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Mainwindow mainwindow = new Mainwindow();
					mainwindow.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
