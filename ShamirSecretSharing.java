import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class ShamirSecretSharing {

    public static void main(String[] args) {
        // Test Case 1 data
        List<DataPoint> testCase1 = new ArrayList<>();
        testCase1.add(new DataPoint(1, 10, "4"));
        testCase1.add(new DataPoint(2, 2, "111"));
        testCase1.add(new DataPoint(3, 10, "12"));
        testCase1.add(new DataPoint(6, 4, "213"));
        
        // Test Case 2 data
        List<DataPoint> testCase2 = new ArrayList<>();
        testCase2.add(new DataPoint(1, 6, "13444211440455345511"));
        testCase2.add(new DataPoint(2, 15, "aed7015a346d63"));
        testCase2.add(new DataPoint(3, 15, "6aeeb69631c227c"));
        testCase2.add(new DataPoint(4, 16, "e1b5e05623d881f"));
        testCase2.add(new DataPoint(5, 8, "316034514573652620673"));
        testCase2.add(new DataPoint(6, 3, "2122212201122002221120200210011020220200"));
        testCase2.add(new DataPoint(7, 3, "20120221122211000100210021102001201112121"));
        testCase2.add(new DataPoint(8, 6, "20220554335330240002224253"));
        testCase2.add(new DataPoint(9, 12, "45153788322a1255483"));
        testCase2.add(new DataPoint(10, 7, "1101613130313526312514143"));
        
        // Setting k values for both test cases
        int k1 = 3; // for test case 1
        int k2 = 7; // for test case 2

        // Process and print secrets for both test cases
        System.out.println("Secret for Test Case 1: " + findSecret(testCase1, k1));
        System.out.println("Secret for Test Case 2: " + findSecret(testCase2, k2));
    }

    private static BigInteger findSecret(List<DataPoint> testCase, int k) {
        // Decode each point's value based on the provided base
        List<BigInteger[]> points = new ArrayList<>();
        for (DataPoint point : testCase) {
            BigInteger x = BigInteger.valueOf(point.x);
            BigInteger y = new BigInteger(point.value, point.base); // Decode value in the specified base
            points.add(new BigInteger[]{x, y});
        }

        // Use Lagrange interpolation to find the constant term
        return lagrangeInterpolation(points, k);
    }

    private static BigInteger lagrangeInterpolation(List<BigInteger[]> points, int k) {
        BigInteger secret = BigInteger.ZERO;

        for (int i = 0; i < k; i++) {
            BigInteger xi = points.get(i)[0];
            BigInteger yi = points.get(i)[1];
            BigInteger li = BigInteger.ONE;

            for (int j = 0; j < k; j++) {
                if (i != j) {
                    BigInteger xj = points.get(j)[0];
                    // Compute the Lagrange basis polynomial li
                    li = li.multiply(xj.negate()).multiply(xi.subtract(xj).modInverse(BigInteger.valueOf(1L << 256)));
                }
            }

            // Add the current term to the secret
            secret = secret.add(yi.multiply(li));
        }

        // Apply modulo to ensure result fits within 256-bit
        return secret.mod(BigInteger.valueOf(1L << 256));
    }

    // Helper class to hold each data point with x, base, and value
    static class DataPoint {
        int x;
        int base;
        String value;

        DataPoint(int x, int base, String value) {
            this.x = x;
            this.base = base;
            this.value = value;
        }
    }
}