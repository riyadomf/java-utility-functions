import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class TkInWord {
//    private static final Logger logger = LogManager.getLogger(MoneyUtils.class);

    private static final Map<String, String> DIGIT_TO_BANGLA_TEXT_MAP = createEnglishDigitToBanglaTextMap();
    private static final Map<String, String> DIGIT_TO_ENGLISH_TEXT_MAP = createEnglishDigitToEnglishTextMap();
    private static final Map<Integer, String> ENGLISH_TK_GROUP_MAP = createEnglishTkGroupMap();
    private static final Map<Integer, String> BANGLA_TK_GROUP_MAP = createBanglaTkGroupMap();

    private static final int CRORE_POSITION = 5;
    private static final int LAKH_POSITION = 3;
    private static final int THOUSAND_POSITION = 1;
    private static final int HUNDRED_POSITION = 0;
    private static final int UNIT_POSITION = -2;

    private static final String ENGLISH_TAKA_TEXT = " Taka ";
    private static final String ENGLISH_PAISA_TEXT = " Paisa ";
    private static final String ENGLISH_ONLY_TEXT = "Only";
    private static final String BANGLA_TAKA_TEXT = " টাকা ";
    private static final String BANGLA_PAISA_TEXT = " পয়সা ";
    private static final String BANGLA_ONLY_TEXT = "মাত্র";

    public static String convertToTkInWordByLocale(BigDecimal amount, boolean isBangla) {
        return isBangla ? convertToTkInWord(amount, BANGLA_TK_GROUP_MAP, DIGIT_TO_BANGLA_TEXT_MAP, BANGLA_TAKA_TEXT, BANGLA_PAISA_TEXT, BANGLA_ONLY_TEXT)
                : convertToTkInWord(amount, ENGLISH_TK_GROUP_MAP, DIGIT_TO_ENGLISH_TEXT_MAP, ENGLISH_TAKA_TEXT, ENGLISH_PAISA_TEXT, ENGLISH_ONLY_TEXT);
    }

    private static String convertToTkInWord(BigDecimal amount, Map<Integer, String> groupMap, Map<String, String> digitToTextMap, String takaText, String paisaText, String onlyText) {
        if (amount == null) {
            return null;
        }
        String formattedAmount = amount.toString();
        StringBuilder result = new StringBuilder();
        String[] parts = formattedAmount.split("\\.");
        String integerPart = parts[0];

        try {
            String integerPartString = convertIntegerPart(integerPart, groupMap, digitToTextMap);

            if (!digitToTextMap.get("0").contentEquals(integerPartString)) {
                result.append(integerPartString);
                result.append(takaText);
            }

            if (parts.length > 1) {
                String decimalPart = parts[1];
                String decimalPartString = convertDecimalPart(decimalPart, digitToTextMap);
                if (!digitToTextMap.get("0").contentEquals(decimalPartString)) {
                    result.append(decimalPartString);
                    result.append(paisaText);
                }
            }

            if (!result.isEmpty()) {
                result.append(onlyText);
            }
        } catch (Exception e) {
//            logger.error("Error converting amount to words for amount: {}", amount, e);
            return formattedAmount;
        }

        return removeRedundantSpace(result.toString());
    }


    private static String convertIntegerPart(String integerPart, Map<Integer, String> groupMap, Map<String, String> digitToTextMap) {
        StringBuilder result = new StringBuilder();
        StringBuilder tempString = new StringBuilder();

        for (int i = 0; i < integerPart.length(); i++) {
            char digitChar = integerPart.charAt(i);

            int positionFromRight = integerPart.length() - 1 - i;
            int positionInGroup = positionFromRight < 9 ? positionFromRight - 2 : (positionFromRight - 2) % 7;

            if (!tempString.isEmpty() || digitChar != '0') {
                tempString.append(digitChar);
            }

            switch (positionInGroup) {
                case CRORE_POSITION -> appendGroup(result, tempString, groupMap.get(CRORE_POSITION), digitToTextMap, true);
                case LAKH_POSITION -> appendGroup(result, tempString, groupMap.get(LAKH_POSITION), digitToTextMap, false);
                case THOUSAND_POSITION -> appendGroup(result, tempString, groupMap.get(THOUSAND_POSITION), digitToTextMap, false);
                case HUNDRED_POSITION -> appendGroup(result, tempString, groupMap.get(HUNDRED_POSITION), digitToTextMap, false);
                case UNIT_POSITION -> {
                    if (!tempString.isEmpty()) {
                        result.append(digitToTextMap.get(tempString.toString()));
                    }
                }
            }
        }

        if (result.isEmpty()) {
            result.append(digitToTextMap.get("0"));
        }

        return result.toString();
    }

    private static String convertDecimalPart(String decimalPart, Map<String, String> digitToTextMap) {
        int decimalPartStart = decimalPart.length() > 1 && decimalPart.charAt(0) == '0' ? 1 : 0;
        String roundedDecimalPart = decimalPart.substring(decimalPartStart, Math.min(decimalPart.length(), 2));
        return digitToTextMap.get(roundedDecimalPart);
    }

    private static void appendGroup(StringBuilder result, StringBuilder tempString, String groupText, Map<String, String> digitToTextMap, boolean isCroreGroup) {
        if (!tempString.isEmpty()) {
            result.append(digitToTextMap.get(tempString.toString())).append(' ').append(groupText).append(' ');
            tempString.setLength(0);
        } else if (isCroreGroup && !result.isEmpty()) {
            result.append(groupText).append(' ');
        }
    }

    private static String removeRedundantSpace(String result) {
        return result.trim().replaceAll(" +", " ");
    }


    private static String banglaMoneyFormatter(BigDecimal amount) {
        if (amount == null) {
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
//            logger.error("Bangla money comma formatting failed with amount: {}", amount, e);
            return amount;
        }

        return result.toString();
    }

    public static String formatToCommaSeparatedBanglaTk(BigDecimal amount) {
        return NumberUtils.englishToBanglaDigitConversion(banglaMoneyFormatter(amount));
    }



    /**
     * Formats a BigDecimal amount as a comma-separated money string,
     * using Bangla or Western (US) formatting depending on the locale flag.
     *
     * @param amount   The amount to format
     * @param isBangla True for Bangla style formatting; false for Western
     * @return Formatted string representation of the amount
     */
    public static String formatCommaSeperatedMoneyByLocale(BigDecimal amount, boolean isBangla) {
        if (amount == null) {
            return null;
        }

        amount = amount.setScale(2, RoundingMode.HALF_UP);

        if (isBangla) {
            // Format in Bangla style (e.g., ৩,১৩,১২৩.২১)
            String formattedAmount = commaSeperatedMoneyFormatter(amount.toString());
            return NumberUtils.englishToBanglaDigitConversion(formattedAmount);
        } else {
            // Format in Western style (e.g., 123,456.78)
            NumberFormat nf = NumberFormat.getNumberInstance(Locale.ENGLISH);
            nf.setGroupingUsed(true);
            nf.setMinimumFractionDigits(2);
            nf.setMaximumFractionDigits(2);
            return nf.format(amount);
        }
    }

    /**
     * Formats a number (non-currency) with comma separators and optional fractional digits.
     * Supports both Bangla and Western style formatting.
     *
     * @param number            The number to format
     * @param maxFractionDigits Max allowed decimal digits
     * @param isBangla          True for Bangla style; false for Western
     * @return Formatted number string
     */
    public static String formatCommaSeparatedNumberByLocale(BigDecimal number, int maxFractionDigits, boolean isBangla) {
        if (number == null) {
            return null;
        }

        number = number.setScale(maxFractionDigits, RoundingMode.HALF_UP);

        if (isBangla) {
            String[] parts = number.toPlainString().split("\\.");
            String integerPart = commaSeperatedMoneyFormatter(parts[0]);
            String formatted = formatFractionalPart(maxFractionDigits, parts, integerPart);
            return NumberUtils.englishToBanglaDigitConversion(formatted);
        } else {
            NumberFormat nf = NumberFormat.getNumberInstance(Locale.US);
            nf.setGroupingUsed(true);
            nf.setMaximumFractionDigits(maxFractionDigits);
            return nf.format(number);
        }
    }

    /**
     * Appends fractional part to a formatted integer part for Bangla style numbers.
     * Removes trailing zeros, and omits decimal if not needed.
     *
     * @param maxFractionDigits Number of digits after decimal
     * @param parts             Split number parts (before and after decimal)
     * @param integerPart       Already-formatted integer part
     * @return Final formatted number as string
     */
    private static String formatFractionalPart(int maxFractionDigits, String[] parts, String integerPart) {
        if (maxFractionDigits == 0 || parts.length < 2) {
            return integerPart;
        }

        StringBuilder decimalPart = new StringBuilder(parts[1]);

        // Truncate or pad to required length
        if (decimalPart.length() > maxFractionDigits) {
            decimalPart.setLength(maxFractionDigits);
        } else {
            while (decimalPart.length() < maxFractionDigits) {
                decimalPart.append("0");
            }
        }

        // Trim trailing zeros
        int i = decimalPart.length() - 1;
        while (i >= 0 && decimalPart.charAt(i) == '0') {
            i--;
        }

        if (i < 0) {
            return integerPart;
        }

        return integerPart + "." + decimalPart.substring(0, i + 1);
    }


    private static String commaSeperatedMoneyFormatter(String amount) {
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



    private static Map<Integer, String> createEnglishTkGroupMap() {
        Map<Integer, String> englishTkGroupMap = new HashMap<>();
        englishTkGroupMap.put(CRORE_POSITION, "Crore");
        englishTkGroupMap.put(LAKH_POSITION, "Lakh");
        englishTkGroupMap.put(THOUSAND_POSITION, "Thousand");
        englishTkGroupMap.put(HUNDRED_POSITION, "Hundred");

        return englishTkGroupMap;
    }

    private static Map<Integer, String> createBanglaTkGroupMap() {
        Map<Integer, String> banglaTkGroupMap = new HashMap<>();
        banglaTkGroupMap.put(CRORE_POSITION, "কোটি");
        banglaTkGroupMap.put(LAKH_POSITION, "লক্ষ");
        banglaTkGroupMap.put(THOUSAND_POSITION, "হাজার");
        banglaTkGroupMap.put(HUNDRED_POSITION, "শত");
        return banglaTkGroupMap;
    }


    private static Map<String, String> createEnglishDigitToBanglaTextMap() {
        Map<String, String> englishDigitToBanglaTextMap = new HashMap<>();
        englishDigitToBanglaTextMap.put("0", "শূন্য");
        englishDigitToBanglaTextMap.put("1", "এক");
        englishDigitToBanglaTextMap.put("2", "দুই");
        englishDigitToBanglaTextMap.put("3", "তিন");
        englishDigitToBanglaTextMap.put("4", "চার");
        englishDigitToBanglaTextMap.put("5", "পাঁচ");
        englishDigitToBanglaTextMap.put("6", "ছয়");
        englishDigitToBanglaTextMap.put("7", "সাত");
        englishDigitToBanglaTextMap.put("8", "আট");
        englishDigitToBanglaTextMap.put("9", "নয়");
        englishDigitToBanglaTextMap.put("10", "দশ");
        englishDigitToBanglaTextMap.put("11", "এগারো");
        englishDigitToBanglaTextMap.put("12", "বারো");
        englishDigitToBanglaTextMap.put("13", "তেরো");
        englishDigitToBanglaTextMap.put("14", "চৌদ্দ");
        englishDigitToBanglaTextMap.put("15", "পনেরো");
        englishDigitToBanglaTextMap.put("16", "ষোল");
        englishDigitToBanglaTextMap.put("17", "সতেরো");
        englishDigitToBanglaTextMap.put("18", "আঠারো");
        englishDigitToBanglaTextMap.put("19", "ঊনিশ");
        englishDigitToBanglaTextMap.put("20", "বিশ");
        englishDigitToBanglaTextMap.put("21", "একুশ");
        englishDigitToBanglaTextMap.put("22", "বাইশ");
        englishDigitToBanglaTextMap.put("23", "তেইশ");
        englishDigitToBanglaTextMap.put("24", "চব্বিশ");
        englishDigitToBanglaTextMap.put("25", "পঁচিশ");
        englishDigitToBanglaTextMap.put("26", "ছাব্বিশ");
        englishDigitToBanglaTextMap.put("27", "সাতাশ");
        englishDigitToBanglaTextMap.put("28", "আটাশ");
        englishDigitToBanglaTextMap.put("29", "ঊনত্রিশ");
        englishDigitToBanglaTextMap.put("30", "ত্রিশ");
        englishDigitToBanglaTextMap.put("31", "একত্রিশ");
        englishDigitToBanglaTextMap.put("32", "বত্রিশ");
        englishDigitToBanglaTextMap.put("33", "তেত্রিশ");
        englishDigitToBanglaTextMap.put("34", "চৌত্রিশ");
        englishDigitToBanglaTextMap.put("35", "পঁইত্রিশ");
        englishDigitToBanglaTextMap.put("36", "ছত্রিশ");
        englishDigitToBanglaTextMap.put("37", "সাইত্রিশ");
        englishDigitToBanglaTextMap.put("38", "আটত্রিশ");
        englishDigitToBanglaTextMap.put("39", "ঊনচল্লিশ");
        englishDigitToBanglaTextMap.put("40", "চল্লিশ");
        englishDigitToBanglaTextMap.put("41", "একচল্লিশ");
        englishDigitToBanglaTextMap.put("42", "বিয়াল্লিশ");
        englishDigitToBanglaTextMap.put("43", "তেতাল্লিশ");
        englishDigitToBanglaTextMap.put("44", "চুয়াল্লিশ");
        englishDigitToBanglaTextMap.put("45", "পঁইতাল্লিশ");
        englishDigitToBanglaTextMap.put("46", "ছেচল্লিশ");
        englishDigitToBanglaTextMap.put("47", "সাতচল্লিশ");
        englishDigitToBanglaTextMap.put("48", "আটচল্লিশ");
        englishDigitToBanglaTextMap.put("49", "ঊনপঞ্চাশ");
        englishDigitToBanglaTextMap.put("50", "পঞ্চাশ");
        englishDigitToBanglaTextMap.put("51", "একান্ন");
        englishDigitToBanglaTextMap.put("52", "বায়ান্ন");
        englishDigitToBanglaTextMap.put("53", "তিপ্পান্ন");
        englishDigitToBanglaTextMap.put("54", "চুয়ান্ন");
        englishDigitToBanglaTextMap.put("55", "পঁইচান্ন");
        englishDigitToBanglaTextMap.put("56", "ছাপ্পান্ন");
        englishDigitToBanglaTextMap.put("57", "সাতান্ন");
        englishDigitToBanglaTextMap.put("58", "আটান্ন");
        englishDigitToBanglaTextMap.put("59", "ঊনষাট");
        englishDigitToBanglaTextMap.put("60", "ষাট");
        englishDigitToBanglaTextMap.put("61", "একষট্টি");
        englishDigitToBanglaTextMap.put("62", "বাষট্টি");
        englishDigitToBanglaTextMap.put("63", "তেষট্টি");
        englishDigitToBanglaTextMap.put("64", "চৌষট্টি");
        englishDigitToBanglaTextMap.put("65", "পঁইষট্টি");
        englishDigitToBanglaTextMap.put("66", "ছেষট্টি");
        englishDigitToBanglaTextMap.put("67", "সাতষট্টি");
        englishDigitToBanglaTextMap.put("68", "আটষট্টি");
        englishDigitToBanglaTextMap.put("69", "ঊনসত্তর");
        englishDigitToBanglaTextMap.put("70", "সত্তর");
        englishDigitToBanglaTextMap.put("71", "একাত্তর");
        englishDigitToBanglaTextMap.put("72", "বাহাত্তর");
        englishDigitToBanglaTextMap.put("73", "তিয়াত্তর");
        englishDigitToBanglaTextMap.put("74", "চুয়াত্তর");
        englishDigitToBanglaTextMap.put("75", "পঁচাত্তর");
        englishDigitToBanglaTextMap.put("76", "ছিয়াত্তর");
        englishDigitToBanglaTextMap.put("77", "সাতাত্তর");
        englishDigitToBanglaTextMap.put("78", "আটাত্তর");
        englishDigitToBanglaTextMap.put("79", "ঊনআশি");
        englishDigitToBanglaTextMap.put("80", "আশি");
        englishDigitToBanglaTextMap.put("81", "একাশি");
        englishDigitToBanglaTextMap.put("82", "বিরাশি");
        englishDigitToBanglaTextMap.put("83", "তিরাশি");
        englishDigitToBanglaTextMap.put("84", "চুরাশি");
        englishDigitToBanglaTextMap.put("85", "পঁচাশি");
        englishDigitToBanglaTextMap.put("86", "ছিয়াশি");
        englishDigitToBanglaTextMap.put("87", "সাতাশি");
        englishDigitToBanglaTextMap.put("88", "আটাশি");
        englishDigitToBanglaTextMap.put("89", "ঊননব্বই");
        englishDigitToBanglaTextMap.put("90", "নব্বই");
        englishDigitToBanglaTextMap.put("91", "একানব্বই");
        englishDigitToBanglaTextMap.put("92", "বিরানব্বই");
        englishDigitToBanglaTextMap.put("93", "তিরানব্বই");
        englishDigitToBanglaTextMap.put("94", "চুরানব্বই");
        englishDigitToBanglaTextMap.put("95", "পঁচানব্বই");
        englishDigitToBanglaTextMap.put("96", "ছিয়ানব্বই");
        englishDigitToBanglaTextMap.put("97", "সাতানব্বই");
        englishDigitToBanglaTextMap.put("98", "আটানব্বই");
        englishDigitToBanglaTextMap.put("99", "নিরানব্বই");

        return englishDigitToBanglaTextMap;
    }

    private static Map<String, String> createEnglishDigitToEnglishTextMap() {
        Map<String, String> englishDigitToEnglishTextMap = new HashMap<>();
        englishDigitToEnglishTextMap.put("0", "Zero");
        englishDigitToEnglishTextMap.put("1", "One");
        englishDigitToEnglishTextMap.put("2", "Two");
        englishDigitToEnglishTextMap.put("3", "Three");
        englishDigitToEnglishTextMap.put("4", "Four");
        englishDigitToEnglishTextMap.put("5", "Five");
        englishDigitToEnglishTextMap.put("6", "Six");
        englishDigitToEnglishTextMap.put("7", "Seven");
        englishDigitToEnglishTextMap.put("8", "Eight");
        englishDigitToEnglishTextMap.put("9", "Nine");
        englishDigitToEnglishTextMap.put("10", "Ten");
        englishDigitToEnglishTextMap.put("11", "Eleven");
        englishDigitToEnglishTextMap.put("12", "Twelve");
        englishDigitToEnglishTextMap.put("13", "Thirteen");
        englishDigitToEnglishTextMap.put("14", "Fourteen");
        englishDigitToEnglishTextMap.put("15", "Fifteen");
        englishDigitToEnglishTextMap.put("16", "Sixteen");
        englishDigitToEnglishTextMap.put("17", "Seventeen");
        englishDigitToEnglishTextMap.put("18", "Eighteen");
        englishDigitToEnglishTextMap.put("19", "Nineteen");
        englishDigitToEnglishTextMap.put("20", "Twenty");
        englishDigitToEnglishTextMap.put("21", "Twenty One");
        englishDigitToEnglishTextMap.put("22", "Twenty Two");
        englishDigitToEnglishTextMap.put("23", "Twenty Three");
        englishDigitToEnglishTextMap.put("24", "Twenty Four");
        englishDigitToEnglishTextMap.put("25", "Twenty Five");
        englishDigitToEnglishTextMap.put("26", "Twenty Six");
        englishDigitToEnglishTextMap.put("27", "Twenty Seven");
        englishDigitToEnglishTextMap.put("28", "Twenty Eight");
        englishDigitToEnglishTextMap.put("29", "Twenty Nine");
        englishDigitToEnglishTextMap.put("30", "Thirty");
        englishDigitToEnglishTextMap.put("31", "Thirty One");
        englishDigitToEnglishTextMap.put("32", "Thirty Two");
        englishDigitToEnglishTextMap.put("33", "Thirty Three");
        englishDigitToEnglishTextMap.put("34", "Thirty Four");
        englishDigitToEnglishTextMap.put("35", "Thirty Five");
        englishDigitToEnglishTextMap.put("36", "Thirty Six");
        englishDigitToEnglishTextMap.put("37", "Thirty Seven");
        englishDigitToEnglishTextMap.put("38", "Thirty Eight");
        englishDigitToEnglishTextMap.put("39", "Thirty Nine");
        englishDigitToEnglishTextMap.put("40", "Forty");
        englishDigitToEnglishTextMap.put("41", "Forty One");
        englishDigitToEnglishTextMap.put("42", "Forty Two");
        englishDigitToEnglishTextMap.put("43", "Forty Three");
        englishDigitToEnglishTextMap.put("44", "Forty Four");
        englishDigitToEnglishTextMap.put("45", "Forty Five");
        englishDigitToEnglishTextMap.put("46", "Forty Six");
        englishDigitToEnglishTextMap.put("47", "Forty Seven");
        englishDigitToEnglishTextMap.put("48", "Forty Eight");
        englishDigitToEnglishTextMap.put("49", "Forty Nine");
        englishDigitToEnglishTextMap.put("50", "Fifty");
        englishDigitToEnglishTextMap.put("51", "Fifty One");
        englishDigitToEnglishTextMap.put("52", "Fifty Two");
        englishDigitToEnglishTextMap.put("53", "Fifty Three");
        englishDigitToEnglishTextMap.put("54", "Fifty Four");
        englishDigitToEnglishTextMap.put("55", "Fifty Five");
        englishDigitToEnglishTextMap.put("56", "Fifty Six");
        englishDigitToEnglishTextMap.put("57", "Fifty Seven");
        englishDigitToEnglishTextMap.put("58", "Fifty Eight");
        englishDigitToEnglishTextMap.put("59", "Fifty Nine");
        englishDigitToEnglishTextMap.put("60", "Sixty");
        englishDigitToEnglishTextMap.put("61", "Sixty One");
        englishDigitToEnglishTextMap.put("62", "Sixty Two");
        englishDigitToEnglishTextMap.put("63", "Sixty Three");
        englishDigitToEnglishTextMap.put("64", "Sixty Four");
        englishDigitToEnglishTextMap.put("65", "Sixty Five");
        englishDigitToEnglishTextMap.put("66", "Sixty Six");
        englishDigitToEnglishTextMap.put("67", "Sixty Seven");
        englishDigitToEnglishTextMap.put("68", "Sixty Eight");
        englishDigitToEnglishTextMap.put("69", "Sixty Nine");
        englishDigitToEnglishTextMap.put("70", "Seventy");
        englishDigitToEnglishTextMap.put("71", "Seventy One");
        englishDigitToEnglishTextMap.put("72", "Seventy Two");
        englishDigitToEnglishTextMap.put("73", "Seventy Three");
        englishDigitToEnglishTextMap.put("74", "Seventy Four");
        englishDigitToEnglishTextMap.put("75", "Seventy Five");
        englishDigitToEnglishTextMap.put("76", "Seventy Six");
        englishDigitToEnglishTextMap.put("77", "Seventy Seven");
        englishDigitToEnglishTextMap.put("78", "Seventy Eight");
        englishDigitToEnglishTextMap.put("79", "Seventy Nine");
        englishDigitToEnglishTextMap.put("80", "Eighty");
        englishDigitToEnglishTextMap.put("81", "Eighty One");
        englishDigitToEnglishTextMap.put("82", "Eighty Two");
        englishDigitToEnglishTextMap.put("83", "Eighty Three");
        englishDigitToEnglishTextMap.put("84", "Eighty Four");
        englishDigitToEnglishTextMap.put("85", "Eighty Five");
        englishDigitToEnglishTextMap.put("86", "Eighty Six");
        englishDigitToEnglishTextMap.put("87", "Eighty Seven");
        englishDigitToEnglishTextMap.put("88", "Eighty Eight");
        englishDigitToEnglishTextMap.put("89", "Eighty Nine");
        englishDigitToEnglishTextMap.put("90", "Ninety");
        englishDigitToEnglishTextMap.put("91", "Ninety One");
        englishDigitToEnglishTextMap.put("92", "Ninety Two");
        englishDigitToEnglishTextMap.put("93", "Ninety Three");
        englishDigitToEnglishTextMap.put("94", "Ninety Four");
        englishDigitToEnglishTextMap.put("95", "Ninety Five");
        englishDigitToEnglishTextMap.put("96", "Ninety Six");
        englishDigitToEnglishTextMap.put("97", "Ninety Seven");
        englishDigitToEnglishTextMap.put("98", "Ninety Eight");
        englishDigitToEnglishTextMap.put("99", "Ninety Nine");

        return englishDigitToEnglishTextMap;
    }

}
