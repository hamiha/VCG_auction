import java.util.Arrays;
import java.beans.DefaultPersistenceDelegate;
import java.text.DecimalFormat;
import org.apache.commons.math3.distribution.NormalDistribution;

public class Distribution {
	
	protected static double[] value;
	protected static double[] unit;
	protected static int sellUnit = 20;
	
	protected static int numAuction = 10, numberOfBidders = 10;
	protected static double meanValue = 10, defaultPriceDeviation = 1;
	protected static double meanUnit = 5, defaultUnitDeviation = 1;
	
	private static DecimalFormat form = new DecimalFormat("#.##");
	
	public static void generateData(double priceDeviation, double unitDeviation) {
			
		value = new double[numberOfBidders];
		unit = new double[numberOfBidders];
		
		if(priceDeviation<=0) {
			NormalDistribution disPrice = new NormalDistribution(meanValue, defaultPriceDeviation);
			for (int i = 0; i < numberOfBidders; i++) {
				value[i] = disPrice.sample();
				if(value[i] < 0) value[i] = 0;
			}
		}
		else {
			NormalDistribution disPrice = new NormalDistribution(meanValue, priceDeviation);
			for (int i = 0; i < numberOfBidders; i++) {
				value[i] = disPrice.sample();
				if(value[i] < 0) value[i] = 0;
			}
		}
		if(unitDeviation<=0) {
			NormalDistribution disUnit = new NormalDistribution(meanUnit, defaultUnitDeviation);
			for (int i = 0; i < numberOfBidders; i++) {
				unit[i] = (int) (double) disUnit.sample();
				if(unit[i] < 0) unit[i] = 0;
			}
		}
		else {
			NormalDistribution disUnit = new NormalDistribution(meanUnit, unitDeviation);
			for (int i = 0; i < numberOfBidders; i++) {
				unit[i] = (int) (double) disUnit.sample();
				if(unit[i] < 0) unit[i] = 0;
			}
		}
		
		System.out.println(Arrays.toString(value));
		System.out.println(Arrays.toString(unit));
		
	}
	
		
}
