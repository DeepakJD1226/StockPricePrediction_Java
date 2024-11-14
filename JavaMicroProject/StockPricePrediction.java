import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StockPricePrediction {

    public static void main(String[] args) {
        String filePath = "stock_price_data.csv";
        
        
        List<Double> prices = readStockPrices(filePath);
        
        
        if (prices == null || prices.isEmpty()) {
            System.out.println("No data found in the CSV file or an error occurred.");
            return;
        }
        
       
        List<Double> indices = createIndices(prices.size());
        
        
        double[] coefficients = linearRegression(indices, prices);
        
        
        System.out.printf("Linear Regression Model:\nSlope (m): %.4f\nIntercept (b): %.4f\n", coefficients[0], coefficients[1]);
        
        
        double predictedPrice = predictNextPrice(coefficients, indices.size());
        System.out.printf("Predicted Price for the next index: %.4f\n", predictedPrice);
    }

    
    public static List<Double> readStockPrices(String filePath) {
        List<Double> prices = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            br.readLine(); 
            
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                
                
                if (values.length > 1) {
                    try {
                        prices.add(Double.parseDouble(values[1]));  
                    } catch (NumberFormatException e) {
                        System.out.println("Skipping invalid data: " + values[1]);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading the CSV file: " + e.getMessage());
            return null;
        }
        
        return prices;
    }

    
    public static List<Double> createIndices(int size) {
        List<Double> indices = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            indices.add((double) i);
        }
        return indices;
    }

    
    public static double[] linearRegression(List<Double> x, List<Double> y) {
        double sumX = 0, sumY = 0, sumXY = 0, sumXX = 0;
        int n = x.size();

        for (int i = 0; i < n; i++) {
            double xi = x.get(i);
            double yi = y.get(i);
            sumX += xi;
            sumY += yi;
            sumXY += xi * yi;
            sumXX += xi * xi;
        }

        double slope = (n * sumXY - sumX * sumY) / (n * sumXX - sumX * sumX);
        double intercept = (sumY - slope * sumX) / n;
        return new double[]{slope, intercept};
    }


    public static double predictNextPrice(double[] coefficients, int nextIndex) {
        double slope = coefficients[0];
        double intercept = coefficients[1];
        return slope * nextIndex + intercept;
    }
}
