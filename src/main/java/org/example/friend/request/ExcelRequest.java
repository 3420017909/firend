package org.example.friend.request;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import org.example.friend.converter.RoleConverter;
import org.example.friend.converter.StatusConverter;

public class ExcelRequest {
    @ExcelProperty("用户id")
    @ColumnWidth(20)
private long id;

    @ExcelProperty("手机号")
    @ColumnWidth(20)
    private String phone;

    @ExcelProperty("用户名")
    @ColumnWidth(20)
    private String username;

    @ExcelProperty("个性")
    @ColumnWidth(20)
    private String profile;

    @ExcelProperty(value = "性别")
    @ColumnWidth(20)
    private String gender;

    @ExcelProperty(value = "用户状态", converter = StatusConverter.class)
    @ColumnWidth(20)
    private int status;

    @ExcelProperty(value = "权限", converter = RoleConverter.class)
    @ColumnWidth(20)
    private int role;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }
}
