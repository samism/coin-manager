package org.samism.java.coinmanager;

import javax.swing.*;

/**
 * Created with IntelliJ IDEA.
 * Author: Sameer Ismail
 * Date: 10/28/12
 * Time: 3:38 PM
 */

public class Main {

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new ManagerGUI().setVisible(true);
			}
		});
	}
}
