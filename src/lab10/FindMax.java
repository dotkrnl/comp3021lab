package lab10;

/**
 *
 * COMP 3021
 *
 This is a class that prints the maximum value of a given array of 90 elements

 This is a single threaded version.

 Create a multi-thread version with 3 threads:

 one thread finds the max among the cells [0,29]
 another thread the max among the cells [30,59]
 another thread the max among the cells [60,89]

 Compare the results of the three threads and print at console the max value.

 *
 * @author valerio
 *
 */
public class FindMax {
    // this is an array of 90 elements
    // the max value of this array is 9999
    static int[] array = { 1, 34, 5, 6, 343, 5, 63, 5, 34, 2, 78, 2, 3, 4, 5,
            234, 678, 543, 45, 67, 43, 2, 3, 4543, 234, 3, 454, 1, 2, 3, 1,
            9999, 34, 5, 6, 343, 5, 63, 5, 34, 2, 78, 2, 3, 4, 5, 234, 678,
            543, 45, 67, 43, 2, 3, 4543, 234, 3, 454, 1, 2, 3, 1, 34, 5, 6,
            5, 63, 5, 34, 2, 78, 2, 3, 4, 5, 234, 678, 543, 45, 67, 43, 2,
            3, 4543, 234, 3, 454, 1, 2, 3 };

    public static void main(String[] args) {
        new FindMax().printMax();
    }

    class MaxCalculation extends Thread {
        int value, beg, end;
        MaxCalculation(int beg, int end) {
            super();
            this.beg = beg;
            this.end = end;
        }
        public void run() {
           value = findMax(beg, end);
        }
    }

    public void printMax() {
        // this is a single threaded version
        // int max = findMax(0, array.length - 1);
        MaxCalculation max1 = new MaxCalculation(0, 29);
        MaxCalculation max2 = new MaxCalculation(30, 59);
        MaxCalculation max3 = new MaxCalculation(60, 89);
        max1.setDaemon(true);
        max2.setDaemon(true);
        max3.setDaemon(true);
        max1.start();
        max2.start();
        max3.start();

        try {
            max1.join();
            max2.join();
            max3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        int max = max1.value;
        if (max2.value > max) max = max2.value;
        if (max3.value > max) max = max3.value;

        System.out.println("the max value is " + max);
    }

    /**
     * returns the max value in the array within a give range [begin,range]
     *
     * @param begin
     * @param end
     * @return
     */
    private int findMax(int begin, int end) {
        // you should NOT change this function
        int max = array[begin];
        for (int i = begin + 1; i <= end; i++) {
            if (array[i] > max) {
                max = array[i];
            }
        }
        return max;
    }
}