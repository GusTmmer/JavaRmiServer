package Consultas;

/**
 *
 * A class responsible for processing user inputted dates and returning easy to process data types.
 */
public class Date {
        
    public int day;
    public int month;
    public int year;

    public int reprDay;
    
    public static final int[] daysInMonths = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

    public Date(int day, int month, int year) {
        this.day = day;
        this.month = month;
        this.year = year;

        reprDay = day + month * daysInMonths[month - 1] + year * 365;
    }

    /**
     *
     * @param date : A string containing the date in the format DD/MM/YYYY
     */
    public Date(String date) {

        String[] dateParts = date.split("/");

        this.day = Integer.parseInt(dateParts[0]);
        this.month = Integer.parseInt(dateParts[1]);
        this.year = Integer.parseInt(dateParts[2]);

        reprDay = day + month * daysInMonths[month - 1] + year * 365;
    }
}
