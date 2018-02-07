import java.awt.Font;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.StringTokenizer;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class VCGAuctionGUI extends JFrame{
	
	public static String fileName, fileExt;
	public static File auctionDataFile;
	
	static JTabbedPane jtp = new JTabbedPane();
	static JPanel panel2 = new JPanel();
	static JPanel panel3 = new JPanel();
	static JPanel panel1 = new JPanel();
	
	// tab1
	static JLabel numAuctionLabel = new JLabel("Input the number of auctions: ");
	static JTextField numAuctionText = new JTextField();
	static JLabel numBidderLabel = new JLabel("Input the number of bidders (b): ");
	static JTextField numBidderText = new JTextField();
	static JLabel numSellerUnitLabel = new JLabel("Input the number of seller's unit (s): ");
	static JTextField numSellerUnitText = new JTextField();
	static JLabel meanValueLabel = new JLabel("Input mean value ($): ");
	static JTextField meanValueText = new JTextField();
	static JLabel meanUnitLabel = new JLabel("Input mean unit (unit): ");
	static JTextField meanUnitText = new JTextField();
	static JLabel priceDeviationLabel = new JLabel("Input value deviation: ");
	static JTextField priceDeviationText = new JTextField();
	static JLabel unitDeviationLabel = new JLabel("Input unit deviation: ");
	static JTextField unitDeviationText = new JTextField();
	
	static JButton simulateButton = new JButton("Run Simulation");
	static JButton generateDataButton = new JButton("Generate Raw Data");
	static JLabel messageTextLabel = new JLabel("Message Box:");
	public static TextArea messageText = new TextArea();
	
	private static void createAndShowGUI() {
        JFrame.setDefaultLookAndFeelDecorated(true);
        JFrame frame = new JFrame("VCG Auction Simulations");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(720, 720);
        
        Font f = new Font("Verdana",Font.PLAIN,14);
		Font fBold = new Font("Verdana",Font.BOLD,14);
        
		// tab1
		numAuctionLabel.setBounds(40, 30, 300, 25);
		numAuctionLabel.setFont(fBold);
		numAuctionText.setBounds(380, 30, 250, 25);
		numAuctionText.setFont(fBold);
		
		numBidderLabel.setBounds(40, 70, 300, 25);
		numBidderLabel.setFont(fBold);
		numBidderText.setBounds(380, 70, 250, 25);
		numBidderText.setFont(fBold);
		
		numSellerUnitLabel.setBounds(40, 110, 300, 25);
		numSellerUnitLabel.setFont(fBold);
		numSellerUnitText.setBounds(380, 110, 250, 25);
		numSellerUnitText.setFont(fBold);
		
		meanValueLabel.setBounds(40, 150, 300, 25);
		meanValueLabel.setFont(fBold);
		meanValueText.setBounds(380, 150, 250, 25);
		meanValueText.setFont(fBold);
		
		meanUnitLabel.setBounds(40, 190, 300, 25);
		meanUnitLabel.setFont(fBold);
		meanUnitText.setBounds(380, 190, 250, 25);
		meanUnitText.setFont(fBold);
		
		priceDeviationLabel.setBounds(40, 230, 300, 25);
		priceDeviationLabel.setFont(fBold);
		priceDeviationText.setBounds(380, 230, 250, 25);
		priceDeviationText.setFont(fBold);
		
		unitDeviationLabel.setBounds(40, 270, 300, 25);
		unitDeviationLabel.setFont(fBold);
		unitDeviationText.setBounds(380, 270, 250, 25);
		unitDeviationText.setFont(fBold);
		
		simulateButton.setBounds(40, 310, 250, 25);
		simulateButton.setFont(fBold);
		
		generateDataButton.setBounds(380, 310, 250, 25);
		generateDataButton.setFont(fBold);
		
		messageTextLabel.setFont(fBold);
		messageTextLabel.setBounds(40, 340, 200, 25);
		messageText.setFont(f);
		messageText.setBounds(40,370,630,280);
		
		numAuctionText.setText(Distribution.numAuction + "");
		numBidderText.setText(Distribution.numberOfBidders + "");
		numSellerUnitText.setText(Distribution.sellUnit + "");
		meanValueText.setText(Distribution.meanValue + "");
		meanUnitText.setText(Distribution.meanUnit + "");
		priceDeviationText.setText(Distribution.defaultPriceDeviation + "");
		unitDeviationText.setText(Distribution.defaultUnitDeviation + "");
    
        panel1.setLayout(null);
        panel1.add(numAuctionLabel);
        panel1.add(numAuctionText);
        panel1.add(numBidderLabel);
        panel1.add(numBidderText);
        panel1.add(numSellerUnitLabel);
        panel1.add(numSellerUnitText);
        panel1.add(meanValueLabel);
        panel1.add(meanValueText);
        panel1.add(meanUnitLabel);
        panel1.add(meanUnitText);
        panel1.add(priceDeviationLabel);
        panel1.add(priceDeviationText);
        panel1.add(unitDeviationLabel);
        panel1.add(unitDeviationText);
        panel1.add(simulateButton);
//        panel1.add(generateDataButton);
        panel1.add(messageText);
		panel1.add(messageTextLabel);
	
		jtp.setFont(fBold);
		jtp.addTab("Normal Distribution", panel1);
        frame.add(jtp);
        
        simulateButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				if (getParameter())					
				    VCGMechanism.runSimulation(messageText);
			}
		});
        
        frame.setVisible(true);
	}
	

	private static boolean getParameter() {
		boolean runSim = true;
		
		messageText.append("---------------------");
//		CHECK NUMBER OF AUCTIONS
		try {
			Distribution.numAuction = Integer.parseInt(numAuctionText.getText());
		} catch (Exception e1) {
			Distribution.numAuction = -1;
		}
		
		if (Distribution.numAuction < 1) {
			runSim = false;
			messageText.append("\nError: The number of auctions must be a positive integer!");
		} else messageText.append("\nnumAuction = " + Distribution.numAuction);
		
//		CHECK NUMBER OF BIDDERS
		try {
			Distribution.numberOfBidders = Integer.parseInt(numBidderText.getText());
		} catch (Exception e1) {
			Distribution.numberOfBidders = -1;
		}
		
		if (Distribution.numberOfBidders < 1) {
			runSim = false;
			messageText.append("\nError: The number of bidders must be a positive integer!");
		} else messageText.append("\nb = " + Distribution.numberOfBidders);
//		CHECK NUMBER OF SELLER UNIT
		try {
			Distribution.sellUnit = Integer.parseInt(numSellerUnitText.getText());
		} catch (Exception e1) {
			Distribution.sellUnit = -1;
		}
		
		if (Distribution.sellUnit < 1) {
			runSim = false;
			messageText.append("\nError: The number of sell unit must be a positive integer!");
		} else messageText.append("\ns = " + Distribution.sellUnit);
//		CHECK MEAN VALUES
		try {
			Distribution.meanValue = Double.parseDouble(meanValueText.getText());
		} catch (Exception e1) {
			Distribution.meanValue = -1;
		}
		
		if (Distribution.meanValue < 0) {
			runSim = false;
			messageText.append("\nError: The mean value must be a positive value!");
		} else messageText.append("\nmeanValue = " + Distribution.meanValue);
//		CHECK MEAN UNIT
		try {
			Distribution.meanUnit= Double.parseDouble(meanUnitText.getText());
		} catch (Exception e1) {
			Distribution.meanUnit = -1;
		}
		
		if (Distribution.meanUnit < 0) {
			runSim = false;
			messageText.append("\nError: The mean unit must be a positive value!");
		} else messageText.append("\nmeanValue = " + Distribution.meanUnit);
//		CHECK PRICE DEVIATION
		try {
			Distribution.defaultPriceDeviation = Double.parseDouble(priceDeviationText.getText());
		} catch (Exception e1) {
			Distribution.defaultPriceDeviation = -1;
		}
		
		if (Distribution.defaultPriceDeviation < 0) {
			runSim = false;
			messageText.append("\nError: The price deviation must be a positive value!");
		} else messageText.append("\nPriceDeviation = " + Distribution.defaultPriceDeviation);
//		CHECK UNIT DEVIATION
		try {
			Distribution.defaultUnitDeviation = Double.parseDouble(unitDeviationText.getText());
		} catch (Exception e1) {
			Distribution.defaultUnitDeviation = -1;
		}
		
		if (Distribution.defaultUnitDeviation < 0) {
			runSim = false;
			messageText.append("\nError: The unit deviation must be a positive value!");
		} else messageText.append("\nUnitDeviation = " + Distribution.defaultUnitDeviation);
		
		messageText.append("\n---------------------\n");
		
		return runSim;
	}

	
	public static void main(String[] argv) {
		 SwingUtilities.invokeLater(new Runnable() {
	            public void run() {
	                createAndShowGUI();
	            }
	     });
		
	}


}
