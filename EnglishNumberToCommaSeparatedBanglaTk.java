import java.math.BigDecimal;
import java.math.RoundingMode;

public class EnglishNumberToCommaSeparatedBanglaTk {
    public static String banglaMoneyFormatter(BigDecimal amount) {
        if(amount == null) {
            return null;
        }
        String formattedAmount = amount.setScale(2, RoundingMode.CEILING).toString();
        return banglaMoneyCommaFormatter(formattedAmount);
    }

    private static String banglaMoneyCommaFormatter(String amount) {
        StringBuilder result = new StringBuilder();
        String[] parts = amount.split("\\.");
        String integerPart = parts[0];

        try {
            for (int i = 0; i < integerPart.length(); i++) {
                char c = integerPart.charAt(i);
                result.append(c);

                int positionFromRight = integerPart.length() - 1 - i;
                int positionInGroup = positionFromRight % 7;
                if (positionFromRight > 0 && positionInGroup == 0 || positionInGroup == 3 || positionInGroup == 5) {
                    result.append(",");
                }
            }

            // Add the decimal part if present
            if (parts.length > 1) {
                result.append(".").append(parts[1]);
            }
        } catch (Exception e) {
            return amount;
        }

        return result.toString();
    }

    public static String englishToBanglaMoneyFormatter(BigDecimal amount) {
        return NumberUtils.englishToBanglaDigitConversion(banglaMoneyFormatter(amount));
    }
}