import java.math.BigDecimal;
import java.math.RoundingMode;

public class NumberUtils {
    public static String englishToBanglaDigitConversion(String englishNumber) {
        if (englishNumber == null || englishNumber.isEmpty())
            return null;
        StringBuilder banglaNumber = new StringBuilder();

        char[] banglaDigits = {'০', '১', '২', '৩', '৪', '৫', '৬', '৭', '৮', '৯'};

        try {
            for (char digit : englishNumber.toCharArray()) {
                if (Character.isDigit(digit)) {
                    if ((digit - 48) <= 9) {
                        banglaNumber.append(banglaDigits[digit - 48]);
                    } else {
                        banglaNumber.append(digit);
                    }
                } else {
                    banglaNumber.append(digit);
                }
            }
        } catch (Exception e) {
            return englishNumber;
        }
        return banglaNumber.toString();
    }
}