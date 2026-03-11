package com.example.roomrentalmanagementapp;

import java.io.Serializable;

// Lớp Room dùng để biểu diễn thông tin của một phòng trọ
// implements Serializable để có thể truyền đối tượng Room giữa các Activity qua Intent
public class Room implements Serializable {

    // Mã phòng
    private String id;

    // Tên phòng
    private String name;

    // Giá thuê phòng
    private double price;

    // Trạng thái phòng: true = đã thuê, false = còn trống
    private boolean isRented;

    // Tên người thuê phòng
    private String tenantName;

    // Số điện thoại người thuê
    private String phoneNumber;

    // Constructor dùng để khởi tạo đầy đủ thông tin cho một phòng
    public Room(String id, String name, double price, boolean isRented, String tenantName, String phoneNumber) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.isRented = isRented;
        this.tenantName = tenantName;
        this.phoneNumber = phoneNumber;
    }

    // Getters and Setters

    // Lấy mã phòng
    public String getId() { return id; }

    // Gán mã phòng
    public void setId(String id) { this.id = id; }

    // Lấy tên phòng
    public String getName() { return name; }

    // Gán tên phòng
    public void setName(String name) { this.name = name; }

    // Lấy giá thuê phòng
    public double getPrice() { return price; }

    // Gán giá thuê phòng
    public void setPrice(double price) { this.price = price; }

    // Kiểm tra phòng đã được thuê hay chưa
    public boolean isRented() { return isRented; }

    // Cập nhật trạng thái thuê của phòng
    public void setRented(boolean rented) { isRented = rented; }

    // Lấy tên người thuê
    public String getTenantName() { return tenantName; }

    // Gán tên người thuê
    public void setTenantName(String tenantName) { this.tenantName = tenantName; }

    // Lấy số điện thoại người thuê
    public String getPhoneNumber() { return phoneNumber; }

    // Gán số điện thoại người thuê
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
}