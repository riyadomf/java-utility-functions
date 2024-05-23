import java.util.HashMap;
import java.util.Map;

public class BanglaTkInText {
    private static final Map<String, String> ENGLISH_DIGIT_TO_BANGLA_TEXT_MAP = createEnglishDigitToBanglaTextMap();

    private static final int CRORE_POSITION = 5;
    private static final int LAKH_POSITION = 3;
    private static final int THOUSAND_POSITION = 1;
    private static final int HUNDRED_POSITION = 0;
    private static final int UNIT_POSITION = -2;

    private static String convertToBanglaTkInText(String amount) {
        StringBuilder result = new StringBuilder();
        String[] parts = amount.split("\\.");
        String integerPart = parts[0];

        try {
            result.append(convertIntegerPart(integerPart));

            result.append(" টাকা ");

            if (parts.length > 1) {
                String decimalPart = parts[1];
                result.append(convertDecimalPart(decimalPart));
                result.append(" পয়সা ");
            }

            result.append("মাত্র");
        } catch (Exception e) {
            return amount;
        }

        return removeRedundantSpace(result.toString());
    }


    private static String convertIntegerPart(String integerPart) {
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
                case CRORE_POSITION -> appendGroup(result, tempString, "কোটি", true);
                case LAKH_POSITION -> appendGroup(result, tempString, "লক্ষ", false);
                case THOUSAND_POSITION -> appendGroup(result, tempString, "হাজার", false);
                case HUNDRED_POSITION -> appendGroup(result, tempString, "শত", false);
                case UNIT_POSITION -> {
                    if (!tempString.isEmpty()) {
                        result.append(ENGLISH_DIGIT_TO_BANGLA_TEXT_MAP.get(tempString.toString()));
                    }
                }
            }
        }

        if (result.isEmpty()) {
            result.append(ENGLISH_DIGIT_TO_BANGLA_TEXT_MAP.get("0"));
        }

        return result.toString();
    }

    private static String convertDecimalPart(String decimalPart) {
        String roundedDecimalPart = decimalPart.substring(0, Math.min(decimalPart.length(), 2));
        return ENGLISH_DIGIT_TO_BANGLA_TEXT_MAP.get(roundedDecimalPart);
    }

    private static void appendGroup(StringBuilder result, StringBuilder tempString, String groupText, boolean isCroreGroup) {
        if (!tempString.isEmpty()) {
            result.append(ENGLISH_DIGIT_TO_BANGLA_TEXT_MAP.get(tempString.toString())).append(' ').append(groupText).append(' ');
            tempString.setLength(0);
        } else if (isCroreGroup && !result.isEmpty()) {
            result.append(groupText).append(' ');
        }
    }

    private static String removeRedundantSpace(String result) {
        return result.trim().replaceAll(" +", " ");
    }

    private static Map<String, String> createEnglishDigitToBanglaTextMap() {
        Map<String, String> englishDigitToBanglaTextMap = new HashMap<>();
        englishDigitToBanglaTextMap.put("00", "শূন্য");
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
}
