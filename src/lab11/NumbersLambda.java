package lab11;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class NumbersLambda {

    // Find numbers with certain properties in a unified form
    // The property is specified in Predicate

    public static List<Integer> findNumbers
            (List<Integer> list, Predicate<Integer> predicate) {
        List<Integer> results = new ArrayList<>();
        for (int n : list) {
            if (predicate.test(n)) {
                results.add(n);
            }
        }
        return results;
    }

    public static List<Integer> calculateScore
            (List<Integer> list, Function<Integer, Integer> function) {
        return list.stream().map(function).collect(Collectors.toList());
    }

    public static Function<Integer, Integer> policy() {
        return s -> {
            int score = 0;
            if (isOdd().test(s)) score += 1;
            if (isPrime().test(s)) score += 2;
            if (isPalindrome().test(s)) score += 4;
            return score;
        };
    }

    public static Predicate<Integer> isOdd() {
        return x -> x % 2 != 0;
    }

    public static Predicate<Integer> isPrime() {
        return x -> {
            for (int i = 2; i < x; i++) {
                if (x % i == 0) {
                    return false;
                }
            }
            return true;
        };
    }

    public static Predicate<Integer> isPalindrome() {
        return x -> {
            int rev = 0, i = x;
            while (i != 0) {
                rev *= 10;
                rev += i % 10;
                i /= 10;
            }
            return rev == x;
        };
    }

    public static void main(String[] args) {
        List<Integer> numbers = Arrays.asList(480,514,484,389,709,935,328,
                 169,649,300,685,429,243,532,308,87,25,282,91,415);

        System.out.println("The odd numbers are : "
                + findNumbers(numbers, isOdd()));
        System.out.println("The prime numbers are : "
                + findNumbers(numbers, isPrime()));
        System.out.println("The palindrome numbers are : "
                + findNumbers(numbers, isPalindrome()));

        System.out.println("The score of the all numbers are :"
                + calculateScore(numbers, policy()));
    }
}
