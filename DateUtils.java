
public class DateUtils {
    public static String getLocalizedDateTime(LocalDateTime localDateTime, Locale locale) {
        if (localDateTime == null) {
            return null;
        }
        DateTimeFormatter formatter
                = DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm a")
                .localizedBy(locale)
                .withDecimalStyle(DecimalStyle.of(locale));

        return localDateTime.format(formatter);
    }


    public static String getLocalizedDate(LocalDate localDate, Locale locale) {
        if (localDate == null) {
            return null;
        }
        DateTimeFormatter formatter
                = DateTimeFormatter.ofPattern("dd/MM/yyyy")
                .localizedBy(locale)
                .withDecimalStyle(DecimalStyle.of(locale));

        return localDate.format(formatter);
    }

    public static String getLocalizedTime(LocalTime localTime, Locale locale) {
        if (localTime == null) {
            return null;
        }
        DateTimeFormatter formatter
                = DateTimeFormatter.ofPattern("hh:mm a")
                .localizedBy(locale)
                .withDecimalStyle(DecimalStyle.of(locale));

        return localTime.format(formatter);
    }


    public static String getLocalizedDateFromTimeStamp(LocalDateTime localDateTime, Locale locale) {
        if (localDateTime == null) {
            return null;
        }
        DateTimeFormatter formatter
                = DateTimeFormatter.ofPattern("dd/MM/yyyy")
                .localizedBy(locale)
                .withDecimalStyle(DecimalStyle.of(locale));

        return localDateTime.format(formatter);
    }

    public static LocalDateTime addDaysToCurrentDate(Long numOfDays) {
        return LocalDateTime.now().plusDays(numOfDays);
    }

    public static String getLocalizedDayOfMonth(LocalDate localDate, Locale locale) {
        if (localDate == null) {
            return null;
        }
        DateTimeFormatter formatter
                = DateTimeFormatter.ofPattern("dd")
                .localizedBy(locale)
                .withDecimalStyle(DecimalStyle.of(locale));

        return localDate.format(formatter);
    }

    public static String getLocalizedMonthValue(LocalDate localDate, Locale locale) {
        if (localDate == null) {
            return null;
        }
        DateTimeFormatter formatter
                = DateTimeFormatter.ofPattern("MM")
                .localizedBy(locale)
                .withDecimalStyle(DecimalStyle.of(locale));

        return localDate.format(formatter);
    }

    public static String getLocalizedYear(LocalDate localDate, Locale locale) {
        if (localDate == null) {
            return null;
        }
        DateTimeFormatter formatter
                = DateTimeFormatter.ofPattern("yyyy")
                .localizedBy(locale)
                .withDecimalStyle(DecimalStyle.of(locale));

        return localDate.format(formatter);
    }

    public static String getBanglaDate(LocalDate localDate) {
        if (localDate == null) {
            return null;
        }
        Locale locale = new Locale.Builder().setLanguage("bn").build();
        DateTimeFormatter formatter
                = DateTimeFormatter.ofPattern("dd/MM/yyyy")
                .localizedBy(locale)
                .withDecimalStyle(DecimalStyle.of(locale));

        return localDate.format(formatter);
    }

    public static String getBanglaDayOfMonth(LocalDate localDate) {
        if (localDate == null) {
            return null;
        }
        Locale locale = new Locale.Builder().setLanguage("bn").build();
        DateTimeFormatter formatter
                = DateTimeFormatter.ofPattern("dd")
                .localizedBy(locale)
                .withDecimalStyle(DecimalStyle.of(locale));

        return localDate.format(formatter);
    }

    public static String getBanglaMonthValue(LocalDate localDate) {
        if (localDate == null) {
            return null;
        }
        Locale locale = new Locale.Builder().setLanguage("bn").build();
        DateTimeFormatter formatter
                = DateTimeFormatter.ofPattern("MM")
                .localizedBy(locale)
                .withDecimalStyle(DecimalStyle.of(locale));

        return localDate.format(formatter);
    }

    public static String getBanglaYear(LocalDate localDate) {
        if (localDate == null) {
            return null;
        }
        Locale locale = new Locale.Builder().setLanguage("bn").build();
        DateTimeFormatter formatter
                = DateTimeFormatter.ofPattern("yyyy")
                .localizedBy(locale)
                .withDecimalStyle(DecimalStyle.of(locale));

        return localDate.format(formatter);
    }

    public static String getBanglaTime(LocalTime localTime) {
        if (localTime == null) {
            return null;
        }
        Locale locale = new Locale.Builder().setLanguage("bn").build();
        DateTimeFormatter formatter
                = DateTimeFormatter.ofPattern("hh:mm")
                .localizedBy(locale)
                .withDecimalStyle(DecimalStyle.of(locale));

        return localTime.format(formatter);
    }
}