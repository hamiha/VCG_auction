import java.util.Arrays;
import java.text.DecimalFormat;
import org.apache.commons.math3.distribution.NormalDistribution;

public class Distribution {
	
	protected static double[] value;
	protected static double[] unit;
	protected static int sellUnit = 20;
	
	protected static int numAuction = 1, numberOfBidders = 10;
	protected static double meanValue = 40, priceDeviation = 2;
	protected static double meanUnit = 5, unitDeviation = 3;
	
	private static DecimalFormat form = new DecimalFormat(".##");
	
	public static void generateData() {
		
		NormalDistribution disPrice = new NormalDistribution(meanValue, priceDeviation);
		NormalDistribution disUnit = new NormalDistribution(meanUnit, unitDeviation);
		
		value = new double[numberOfBidders];
		unit = new double[numberOfBidders];
		
		for (int i = 0; i < numberOfBidders; i++) {
			value[i] = disPrice.sample();
			if(value[i] < 0) value[i] = 0;
			unit[i] = (int) (double) disUnit.sample();
			if(unit[i] < 0) unit[i] = 0;
		}
		System.out.println(Arrays.toString(value));
		System.out.println(Arrays.toString(unit));
		
	}
		
}
