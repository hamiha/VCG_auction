import java.util.Arrays;
import java.beans.DefaultPersistenceDelegate;
import java.text.DecimalFormat;
import org.apache.commons.math3.distribution.NormalDistribution;

public class Distribution {
	
	protected static double[] value;
	protected static double[] unit;
	protected static int sellUnit = 10;
	
	protected static int numAuction = 20, numberOfBidders = 20;
	protected static double meanValue = 10, defaultPriceDeviation = 1;
	protected static double meanUnit = 2, defaultUnitDeviation = 1;
	
	protected static double startPrice = 0.1;
	protected static double minUnit = 1;
	
	private static DecimalFormat form = new DecimalFormat("#.##");
	private static DecimalFormat formInt = new DecimalFormat("#");
	
	public static void generateData(double priceDeviation, double unitDeviation) {
		value = new double[numberOfBidders];
		unit = new double[numberOfBidders];			
		
		if(priceDeviation<=0) {
			NormalDistribution disPrice = new NormalDistribution(meanValue, defaultPriceDeviation);
			for (int i = 0; i < numberOfBidders; i++) {
				value[i] = disPrice.sample();
				if(value[i] <= 0) value[i] = startPrice;
			}
		}
		else {
			NormalDistribution disPrice = new NormalDistribution(meanValue, priceDeviation);
			for (int i = 0; i < numberOfBidders; i++) {
				value[i] = disPrice.sample();
				if(value[i] <= 0) value[i] = startPrice;
			}
		}
		if(unitDeviation<=0) {
			NormalDistribution disUnit = new NormalDistribution(meanUnit, defaultUnitDeviation);
			for (int i = 0; i < numberOfBidders; i++) {
				unit[i] = (int) (double) disUnit.sample();
				if(unit[i] <= 0) unit[i] = minUnit;
			}
		}
		else {
			NormalDistribution disUnit = new NormalDistribution(meanUnit, unitDeviation);
			for (int i = 0; i < numberOfBidders; i++) {
				unit[i] = (int) (double) disUnit.sample();
				if(unit[i] <= 0) unit[i] = minUnit;
			}
		}
		
//		PRINT OUT VALUE AND QUANTITY OF EACH BIDDER
//		System.out.println("\n-----------------NEW AUCTION--------------------");
//		System.out.println("------------------------------------------------------------------------------------------------");
//		System.out.print("Bider ID   ");
//		for(int i=0; i<unit.length; i++) {
//			if(i==(unit.length-1))
//				System.out.print("|" + (i+1) + "\t|\n");
//			else System.out.print("|" + (i+1) + "    \t");
//		}
//		System.out.print("Value      ");
//		printFormatDouble(value);
//		System.out.print("Quantity   ");
//		printFormatInt(unit);
		
		
	}
//	PRINT AUCTION INFO
//	PRINT UNIT OF BIDDER
	public static void printFormatInt(double[] inputArray) {
		for(int i=0; i<inputArray.length; i++) {
			if(i==(inputArray.length-1))
				System.out.print("|" +formInt.format(inputArray[i]) + "\t|\n");
			else System.out.print("|" + formInt.format(inputArray[i])+ "     \t");
		}
	}
//	PRINT VALUE OF BIDDER
	public static void printFormatDouble(double[] inputArray) {
		for(int i=0; i<inputArray.length; i++) {
			if(i==(inputArray.length-1))
				System.out.print("|" +form.format(inputArray[i]) + "\t|\n");
			else System.out.print("|" + form.format(inputArray[i])+ " \t");
		}
		
	}
	
		
}
