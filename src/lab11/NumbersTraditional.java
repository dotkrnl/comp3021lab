package lab11;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NumbersTraditional {

    public static List<Integer> isOdd(List<Integer> numbers) {
        List<Integer> results = new ArrayList<Integer>();
        for (int n : numbers) {
            if (n % 2 != 0) {
                results.add(n);
            }
        }
        return results;
    }

    public static List<Integer> isPrime(List<Integer> numbers) {
        List<Integer> results = new ArrayList<Integer>();

        // Find out all the prime numbers
        next_number: for (Integer n : numbers) {
            for (int i = 2; i < n; i++) {
                if (n % i == 0) {
                    continue next_number;
                }
            }
            results.add(n);
        }

        return results;
    }

    public static List<Integer> isPalindrome(List<Integer> numbers) {
        List<Integer> results = new ArrayList<Integer>();

        // Find out all the palindrome numbers, such as 484 and 121.
        next_number: for (Integer n : numbers) {
            int rev = 0, i = n;
            while (i != 0) {
                rev *= 10;
                rev += i % 10;
                i /= 10;
            }
            if (rev == n) {
                results.add(n);
            }
        }

        return results;
    }


    public static void main(String[] args) {
        List<Integer> numbers = Arrays.asList(480,514,484,389,709,935,328,
                 169,649,300,685,429,243,532,308,87,25,282,91,415);

        System.out.println("The odd numbers are : " + isOdd(numbers));
        System.out.println("The prime numbers are : " + isPrime(numbers));
        System.out.println("The palindrome numbers are : "
                + isPalindrome(numbers));

    }
}