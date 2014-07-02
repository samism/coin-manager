package org.samism.java.coinmanager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.text.DecimalFormat;

/**
 * Created with IntelliJ IDEA.
 * Author: Sameer Ismail
 * Date: 10/28/12
 * Time: 3:44 PM
 */

public class ManagerGUI extends JFrame {

	//main gui components
	JPanel mcPanel = new JPanel();
	JPanel buttonsPanel = new JPanel();

	JLabel moneyCount = new JLabel();

	JButton addButton = new JButton("Add Coins");
	JButton takeButton = new JButton("Take Coins");
	JButton setButton = new JButton("Set new count");

	JSeparator s1 = new JSeparator(SwingUtilities.VERTICAL);
	JSeparator s2 = new JSeparator(SwingUtilities.VERTICAL);

	JCheckBox coinStar = new JCheckBox("Coin star fee?", false);


	//coin change dialog components

	JDialog coinChangeDialog = new JDialog();

	JLabel dollarsLabel = new JLabel("Dollars");
	JLabel quartersLabel = new JLabel("Quarters");
	JLabel dimesLabel = new JLabel("Dimes");
	JLabel nickelsLabel = new JLabel("Nickels");
	JLabel penniesLabel = new JLabel("Pennies");

	JTextField dollars = new JTextField();
	JTextField quarters = new JTextField();
	JTextField dimes = new JTextField();
	JTextField nickels = new JTextField();
	JTextField pennies = new JTextField();

	JTextField[] fields = new JTextField[]{dollars, quarters, dimes, nickels, pennies};

	JButton okButton = new JButton("OK");

	//misc vars
	boolean add = false;

	File save = new File(System.getProperty("user.dir") + "\\cnmanager.dat");

	double changeValue = Double.parseDouble(getChangeValue());

	JFrame me = this;

	public ManagerGUI() {
		buildGUI();
		addListeners();

		//set laf
		try {
			UIManager.setLookAndFeel("org.pushingpixels.substance.api.skin.SubstanceGraphiteLookAndFeel");
		} catch (Exception e) {
			e.printStackTrace();
		}

		SwingUtilities.updateComponentTreeUI(this);
		SwingUtilities.updateComponentTreeUI(coinChangeDialog);
	}

	public void buildGUI() {
		///main gui///

		//set layouts for 2 panels
		//mcPanel.setLayout(new BorderLayout());
		buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.X_AXIS));

		//get the display to work
		moneyCount.setFont(new Font("Verdana", Font.PLAIN, 72));
		moneyCount.setText("$" + changeValue);

		//color
		mcPanel.setBackground(Color.red);
		buttonsPanel.setBackground(Color.red);

		s1.setBackground(Color.red);
		s2.setBackground(Color.red);

		//add money count label to upper panel
		mcPanel.add(moneyCount, BorderLayout.NORTH);
		mcPanel.add(coinStar, BorderLayout.SOUTH);


		//add buttons to the lower panel
		buttonsPanel.add(addButton);
		buttonsPanel.add(s1);
		buttonsPanel.add(setButton);
		buttonsPanel.add(s2);
		buttonsPanel.add(takeButton);

		//add the 2 panels to the frame
		getContentPane().add(mcPanel);
		getContentPane().add(buttonsPanel);


		//meta properties
		setLocationRelativeTo(null);
		setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		setTitle("Coin Manager");
		setSize(500, 300);
		setResizable(false);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);


		///coinChangeDialog///

		coinChangeDialog.add(dollarsLabel, BorderLayout.WEST);
		coinChangeDialog.add(dollars, BorderLayout.CENTER);
		coinChangeDialog.add(quartersLabel, BorderLayout.WEST);
		coinChangeDialog.add(quarters, BorderLayout.CENTER);
		coinChangeDialog.add(dimesLabel, BorderLayout.WEST);
		coinChangeDialog.add(dimes, BorderLayout.CENTER);
		coinChangeDialog.add(nickelsLabel, BorderLayout.WEST);
		coinChangeDialog.add(nickels, BorderLayout.CENTER);
		coinChangeDialog.add(penniesLabel, BorderLayout.WEST);
		coinChangeDialog.add(pennies, BorderLayout.CENTER);

		coinChangeDialog.add(okButton);

		coinChangeDialog.setLayout(new BoxLayout(coinChangeDialog.getContentPane(), BoxLayout.Y_AXIS));
		coinChangeDialog.setLocationRelativeTo(null);
		coinChangeDialog.setTitle("Enter amounts");
		coinChangeDialog.setSize(125, 250);
		coinChangeDialog.setResizable(false);
		coinChangeDialog.getRootPane().setDefaultButton(okButton);
	}

	private String getChangeValue() {
		//load saved number


		String value = "0.00";

		if (save.exists()) {
			BufferedReader reader;

			try {
				reader = new BufferedReader(new FileReader(save));

				if ((value = reader.readLine()) != null) {
					System.out.println(value);
					return value;
				}

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			try {
				if (save.createNewFile()) {
					System.out.println("file created successfully.");
					setChangeValue(value);
				} else {
					System.out.println("file creation unsuccessful.");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return value;
	}

	private void setChangeValue(String s) {
		if (save.exists()) {
			FileWriter writer = null;
			try {
				writer = new FileWriter(save);
				writer.write(s);
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (writer != null) {
						writer.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else {
			try {
				if (save.createNewFile()) {
					System.out.println("file created successfully.");
				} else {
					System.out.println("file creation unsuccessful.");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private double roundTwoDecimals(double d) {
		DecimalFormat twoDForm = new DecimalFormat("#.##");
		return Double.parseDouble(twoDForm.format(d));
	}

	public void addListeners() {
		coinStar.addItemListener(new ItemListener() {
			public void itemStateChanged(final ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					Double val = roundTwoDecimals(changeValue - changeValue * 0.098); //take away 9%
					moneyCount.setText("$" + val);
				} else if (e.getStateChange() == ItemEvent.DESELECTED) {
					Double val = roundTwoDecimals(Double.parseDouble(moneyCount.getText().substring(1))
							+ changeValue * 0.098);
					//give back 9% to original val
					moneyCount.setText("$" + val);
				}
			}
		});

		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				coinChangeDialog.setVisible(true);
				add = true;
			}
		});

		takeButton.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				coinChangeDialog.setVisible(true);
				add = false;
			}
		});

		setButton.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				String amount = JOptionPane.showInputDialog(me, "How much money do you have?");
				if (amount != null && !amount.equals("") && !amount.startsWith("-") && amount.length() < 7) {
					moneyCount.setText("$" + String.valueOf(changeValue = Double.parseDouble(amount)));
					setChangeValue(amount);
				}
			}
		});

		for (final JTextField f : fields) {
			f.addKeyListener(new KeyAdapter() {
				public void keyTyped(final KeyEvent e) {
					if (!Character.isDigit(e.getKeyChar()) || f.getText().length() > 4) {
						e.consume();
					}
				}
			});
		}

		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				//multiply quantity of coins by their respective multipliers
				double addThis = 0.0;

				for (JTextField field : fields) {
					if (field.getText().equals(""))
						field.setText("0");
				}
				if (add) {
					addThis += Integer.parseInt(dollars.getText()); // * 1
					addThis += Integer.parseInt(quarters.getText()) * 0.25;
					addThis += Integer.parseInt(dimes.getText()) * 0.1;
					addThis += Integer.parseInt(nickels.getText()) * 0.05;
					addThis += Integer.parseInt(pennies.getText()) * 0.01;
					System.out.println("total change: " + roundTwoDecimals(addThis));
					changeValue += roundTwoDecimals(addThis);
				} else {
					dollars.requestFocus();
					addThis -= Integer.parseInt(dollars.getText()); // * 1
					addThis -= Integer.parseInt(quarters.getText()) * 0.25;
					addThis -= Integer.parseInt(dimes.getText()) * 0.1;
					addThis -= Integer.parseInt(nickels.getText()) * 0.05;
					addThis -= Integer.parseInt(pennies.getText()) * 0.01;
					changeValue += roundTwoDecimals(addThis);
				}

				for (JTextField field : fields) {
					if (!field.getText().equals(""))
						field.setText("");
				}

				changeValue = roundTwoDecimals(changeValue);

				//update file
				setChangeValue(String.valueOf(changeValue));

				//update gui
				moneyCount.setText("$" + String.valueOf(changeValue));
				coinChangeDialog.setVisible(false);
			}
		});
	}
}
