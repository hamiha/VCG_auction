import java.util.Arrays;
import java.text.DecimalFormat;



//TODO: change winner array to all -1
public class VCG {
	
	private static DecimalFormat form = new DecimalFormat(".##");
		
	public static void main(String[] args) throws Exception {
		
		double[] plotTradingPrice = new double[Distribution.numAuction];
		double[] plotRatio = new double[Distribution.numAuction];
		double maxAvgPriceUnit = 0, minAvgPriceUnit = Double.MAX_VALUE, totalPriceUnit = 0;
		double maxAvgRatio = 0, minAvgRatio = Double.MAX_VALUE, totalRatio = 0;
		double deviation = 0.01, maxDeviation = 1.01;
		
		double[] plotAvgPrice = new double[100];
		double[] plotUpPrice = new double[100];
		double[] plotLowPrice = new double[100];
		double[] plotAvgRatio = new double[100];
		double[] plotUpRatio = new double[100];
		double[] plotLowRatio = new double[100];
		
		int plotIndex = 0;
		
		while(deviation <= maxDeviation) {
			
			for(int i=0; i < Distribution.numAuction; i++) {
				
				double[][] temp = processAuction(deviation,0);
				plotTradingPrice[i] = temp[0][0];
				plotRatio[i] = temp[0][1];
				
				if (maxAvgPriceUnit < plotTradingPrice[i]) maxAvgPriceUnit = plotTradingPrice[i];
				if (minAvgPriceUnit > plotTradingPrice[i]) minAvgPriceUnit = plotTradingPrice[i];
				totalPriceUnit += plotTradingPrice[i];
				
				if (maxAvgRatio < plotRatio[i]) maxAvgRatio = plotRatio[i];
				if (minAvgRatio > plotRatio[i]) minAvgRatio = plotRatio[i];
				totalRatio += plotRatio[i];
		
			}
			System.out.println("PriceUnit \n max: " + form.format(maxAvgPriceUnit-totalPriceUnit/10) + " - min: " + form.format(totalPriceUnit/10 - minAvgPriceUnit) + " - avg: " + form.format(totalPriceUnit/10));
			System.out.println("Ratio \n max: " + form.format(maxAvgRatio-totalRatio/10) + " - min: " + form.format(totalRatio/10 - minAvgRatio) + " - avg: " + form.format(totalRatio/10));		
			
			plotAvgPrice[plotIndex] = totalPriceUnit/10;
			plotUpPrice[plotIndex] = maxAvgPriceUnit-totalPriceUnit/10;
			plotLowPrice[plotIndex] = totalPriceUnit/10 - minAvgPriceUnit;
			
			plotAvgRatio[plotIndex] = totalRatio/10;
			plotUpRatio[plotIndex] = maxAvgRatio-totalRatio/10;
			plotLowRatio[plotIndex] = totalRatio/10 - minAvgRatio;
			
			totalPriceUnit = 0;
			totalRatio = 0;
					
			plotIndex++;
			deviation += 0.01;
			System.out.println(deviation);
		}
		
		System.out.println(plotAvgPrice.length);
		
		System.out.println("-------------------------------Plot Price------------------------------");
		System.out.println(Arrays.toString(plotAvgPrice));
		System.out.println(Arrays.toString(plotUpPrice));
		System.out.println(Arrays.toString(plotLowPrice));
		System.out.println("-------------------------------Plot Ratio------------------------------");
		System.out.println(Arrays.toString(plotAvgRatio));
		System.out.println(Arrays.toString(plotUpRatio));
		System.out.println(Arrays.toString(plotLowRatio));
	}
	
	public static double[][] processAuction(double priceDeviation, double unitDeviation) {
    
      double sellerUnit = Distribution.sellUnit; //total number of units of seller
      
      Distribution.generateData(priceDeviation, unitDeviation);
      
      double bidderVal[] = Distribution.value;
      double bidderUnit[] = Distribution.unit;
      
      double[] newVal = generateBiddingValue(bidderVal, bidderUnit);
      
      System.out.println(Arrays.toString(newVal));
      
      double[][] alloc = genMatrixAlloc(newVal, bidderUnit, sellerUnit, false, 0);
      
//      for (int[] rows : alloc) {
//	      for (int col : rows) {
//	          System.out.format("%5d", col);
//	      }
//	      System.out.println();
//      }
      
      getWelfare(alloc);
      
      double[] winners = determineWinner(newVal, bidderUnit, sellerUnit, alloc);
      
      double[][] price = genPrice(newVal, bidderUnit, sellerUnit, winners);
     
      System.out.println("-----------------------------------------------------------------");
      System.out.println("Winners |TPrice |BPrice |Unit   |UTotal |Revenue|Ratio  |Avg    |");
      for (double[] rows : price) {
	      for (double col : rows) {
	          System.out.print(form.format(col) + "\t|");
	      }
	      System.out.println();
      }
      System.out.println("-----------------------------------------------------------------");
//      calculate meanprice per unit
      int row = price.length;
      int col = price[0].length;
      
      System.out.println("Total revenue: " + form.format(price[row-1][5]) + " \nTotal unit sold: " + form.format(price[row-1][4]));
      System.out.println("Price per unit: " + form.format(price[row-1][5]/price[row-1][4]));
      System.out.println("Avg trading price ratio: " + form.format(price[row-1][col-1]));
      System.out.println("-----------------------------------------------------------------");
      
      double temp[][] = new double[1][2];
      //price per unit
      temp[0][0] = price[row-1][5]/price[row-1][4];
      //avg trading price
      temp[0][1] = price[row-1][col-1];
      return temp;
	}
	
	public static double[] generateBiddingValue(double val[], double unit[]) {
		int N = val.length;
		double[] newVal = new double[N]; 
		for(int i = 0; i<N; i++) {
			newVal[i] = val[i] * unit[i];
		}
		return newVal;
	}
	
	public static double[] determineWinner(double val[], double unit[], double sUnit, double[][] alloc) {
		//Determine winner
		int[] winners = new int[val.length];
		Arrays.fill(winners, -1);
		int indexWinners = 0;
		int bidder = val.length;
		int units = (int) sUnit;
		while(bidder > 0 && units > 0) {
			if(alloc[bidder][units]!=alloc[bidder-1][units]) {
				winners[indexWinners] = bidder;
//				System.out.println("Winner: " + bidder);
      			indexWinners++;
      			bidder--;
//      			System.out.println("Units of bidder = " + unit[bidder]);
      			units = units - (int) unit[bidder];
//      			System.out.println("Remaining unit = " + units);
			}
			else bidder--;
		}
		
		//remove whos do not win
		
//		System.out.println("Winner array: " + Arrays.toString(winners));
		
		double[] newWinners = new double[indexWinners];
		for(int i=0; i<winners.length; i++) {
			if(winners[i]>-1) {
				newWinners[i] = winners[i];
			}
		}
		
		System.out.println("winners = " + Arrays.toString(newWinners));
		System.out.println("Social welfare = " + form.format(alloc[val.length][(int) sUnit]));
		
		return newWinners;
	}
	
	public static double[][] genPrice(double val[], double unit[], double sUnit, double[] winners){
		double[][] allocPrice = new double[winners.length][8];
		double totalUnitSold = 0, totalRevenue = 0, totalRatio = 0;
		double welfare = getWelfare(genMatrixAlloc(val, unit, sUnit, false, 0));
//		System.out.println("welfare: " + welfare);
		for(int i=0; i<winners.length; i++) {
			//winner ID
			allocPrice[i][0] = winners[i];
			double[] tempVal = val.clone();
			double[] tempUnit = unit.clone();
			System.out.println("winner: " + winners[i]);
	    	//get welfare when bidder i is absent
			double newWelfare = getWelfare(genMatrixAlloc(tempVal, tempUnit, sUnit, true, winners[i]-1));
			System.out.println("if i were absent: " + form.format(newWelfare));
			double othersWelfare = welfare - val[(int) (winners[i]-1)];
			System.out.println("Others when i is present: " + form.format(othersWelfare));
			
			//trading price
			if(newWelfare - othersWelfare == 0) allocPrice[i][1] = val[(int) (winners[i]-1)];
			else allocPrice[i][1] = newWelfare - othersWelfare;
			//biding price
			allocPrice[i][2] = val[(int) (winners[i]-1)];
			//winning unit
			allocPrice[i][3] = unit[(int) (winners[i]-1)];
			//total unit
			totalUnitSold += unit[(int) (winners[i]-1)];
			allocPrice[i][4] = totalUnitSold;
			//total revenue
			totalRevenue += allocPrice[i][1];
			allocPrice[i][5] = totalRevenue;
			//trading price ratio
			allocPrice[i][6] = allocPrice[i][1] / allocPrice[i][2];
			//avg ratito
			totalRatio += allocPrice[i][6];
			allocPrice[i][7] = totalRatio / (i+1);
		}
		
		return allocPrice;
	}

	public static double getWelfare(double[][] alloc) {
//		System.out.println(alloc[0].length);
//		System.out.println(alloc.length);
//		System.out.println(alloc[alloc.length-1][alloc[0].length-1]);
		return alloc[alloc.length-1][alloc[0].length-1];
	}
	
    public static double[][] genMatrixAlloc(double val[], double unit[], double sUnit, boolean isAbsent, double absent) {
    	if(isAbsent) {
    		val[(int) absent] = 0;
    		unit[(int) absent] = 0;
    	}
//    	
//    	System.out.println("Absent: " + Arrays.toString(val));
//    	System.out.println("Absent: " + Arrays.toString(unit));
    	//Get total number of bidders
        int N = unit.length; 
        double[][] V = new double[N + 1][(int) (sUnit + 1)]; //Create a matrix. Items are in rows and weight at in columns +1 on each side
        //What if the knapsack's capacity is 0 - Set all columns at row 0 to be 0
        for (int col = 0; col <= sUnit; col++) {
            V[0][col] = 0;
        }
        //What if there are no items at home.  Fill the first row with 0
        for (int row = 0; row <= N; row++) {
            V[row][0] = 0;
        }

        for (int item=1;item<=N;item++){
            //Let's fill the values row by row
            for (int weight=1;weight<=sUnit;weight++){
                //Is the current items weight less than or equal to running weight
                if (unit[item-1]<=weight){
                    //Given a weight, check if the value of the current item + value of the item that we could afford with the remaining weight
                    //is greater than the value without the current item itself
                    V[item][weight]=Math.max (val[item-1]+V[item-1][(int) (weight-unit[item-1])], V[item-1][weight]);
                }
                else {
                    //If the current item's weight is more than the running weight, just carry forward the value without the current item
                    V[item][weight]=V[item-1][weight];
                }
            }
        }   
        return V;
    }
    
}
