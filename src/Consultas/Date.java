/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Consultas;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author a1729756
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

    public Date(String date) {

        String[] dateParts = date.split("/");

        this.day = Integer.parseInt(dateParts[0]);
        this.month = Integer.parseInt(dateParts[1]);
        this.year = Integer.parseInt(dateParts[2]);

        reprDay = day + month * daysInMonths[month - 1] + year * 365;
    }
}
