public class NumberUtils {
    private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger(NumberUtils.class);

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
            logger.error("English to Bangla digit conversion failed with English number: {}", englishNumber);
            return englishNumber;
        }
        return banglaNumber.toString();
    }

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
            logger.error("Bangla money comma formatting failed with amount: {}", amount);
            return amount;
        }

        return result.toString();
    }

    public static String englishToBanglaMoneyFormatter(BigDecimal amount) {
        return englishToBanglaDigitConversion(banglaMoneyFormatter(amount));
    }
}