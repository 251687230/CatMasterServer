package com.zous.catmaster.entity;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Store {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    private String id;
    @NotBlank
    private String name;
    private String contactPhone;
    private String contact;
    private String fixedPhone;
    @NotBlank
    private String areaCode;
    @NotBlank
    private String detailAddr;
    private String introduce;
    private String storeIcon;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> imageUrls = new ArrayList<>();
    @ManyToOne(fetch = FetchType.EAGER)
    Manager manager;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getFixedPhone() {
        return fixedPhone;
    }

    public void setFixedPhone(String fixedPhone) {
        this.fixedPhone = fixedPhone;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getDetailAddr() {
        return detailAddr;
    }

    public void setDetailAddr(String detailAddr) {
        this.detailAddr = detailAddr;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public Manager getManager() {
        return manager;
    }

    public void setManager(Manager manager) {
        this.manager = manager;
    }

    public String getStoreIcon() {
        return storeIcon;
    }

    public void setStoreIcon(String storeIcon) {
        this.storeIcon = storeIcon;
    }
}
