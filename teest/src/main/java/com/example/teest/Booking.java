package com.example.teest;

import java.time.LocalDate;

public class Booking {
    private String bookingID;
    private String guestName;
    private String roomType;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private double price;

    private int paymentStatus;

    // Constructor
    public Booking(String bookingID , String guestName, String roomType, LocalDate checkInDate, LocalDate checkOutDate, double price , int paymetStatus) {
        this.bookingID = bookingID;
        this.guestName = guestName;
        this.roomType = roomType;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.price = price;
        this.paymentStatus= paymetStatus;
    }

    // Getter and Setter methods
    public String getBookingID() {
        return bookingID;
    }
    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }

    public double getPrice() {
        return price;
    }

    public int getPaymentStatus() {
        return paymentStatus;
    }
}
