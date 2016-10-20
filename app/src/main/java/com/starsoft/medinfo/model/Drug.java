package com.starsoft.medinfo.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Aashish on 10/1/2016.
 */
@IgnoreExtraProperties
public class Drug implements Parcelable {
    private String brand_id;
    private String brand_name;
    private String generic_name;
    private String brand_image;
    private String brand_contains;
    private String brand_manufacturer_name;
    private String brand_type;
    private String category;
    private String diseases;
    private String precautions;
    private String side_effects;
    private String storage;
    private String use_of_drug;
    private String adult;
    private String child;

    public Drug(String brand_id, String brand_name, String generic_name, String brand_image, String brand_contains, String brand_manufacturer_name, String brand_type, String category, String diseases, String precautions, String side_effects, String storage, String use_of_drug, String adult, String child) {
        this.brand_id = brand_id;
        this.brand_name = brand_name;
        this.generic_name = generic_name;
        this.brand_image = brand_image;
        this.brand_contains = brand_contains;
        this.brand_manufacturer_name = brand_manufacturer_name;
        this.brand_type = brand_type;
        this.category = category;
        this.diseases = diseases;
        this.precautions = precautions;
        this.side_effects = side_effects;
        this.storage = storage;
        this.use_of_drug = use_of_drug;
        this.adult = adult;
        this.child = child;
    }

    protected Drug(Parcel in) {
        brand_id = in.readString();
        brand_name = in.readString();
        generic_name = in.readString();
        brand_image = in.readString();
        brand_contains = in.readString();
        brand_manufacturer_name = in.readString();
        brand_type = in.readString();
        category = in.readString();
        diseases = in.readString();
        precautions = in.readString();
        side_effects = in.readString();
        storage = in.readString();
        use_of_drug = in.readString();
        adult = in.readString();
        child = in.readString();
    }

    public static final Creator<Drug> CREATOR = new Creator<Drug>() {
        @Override
        public Drug createFromParcel(Parcel in) {
            return new Drug(in);
        }

        @Override
        public Drug[] newArray(int size) {
            return new Drug[size];
        }
    };

    public String getBrand_id() {
        return brand_id;
    }

    public void setBrand_id(String brand_id) {
        this.brand_id = brand_id;
    }

    public String getBrand_name() {
        return brand_name;
    }

    public void setBrand_name(String brand_name) {
        this.brand_name = brand_name;
    }

    public String getGeneric_name() {
        return generic_name;
    }

    public void setGeneric_name(String generic_name) {
        this.generic_name = generic_name;
    }

    public String getBrand_image() {
        return brand_image;
    }

    public void setBrand_image(String brand_image) {
        this.brand_image = brand_image;
    }

    public String getBrand_contains() {
        return brand_contains;
    }

    public void setBrand_contains(String brand_contains) {
        this.brand_contains = brand_contains;
    }

    public String getBrand_manufacturer_name() {
        return brand_manufacturer_name;
    }

    public void setBrand_manufacturer_name(String brand_manufacturer_name) {
        this.brand_manufacturer_name = brand_manufacturer_name;
    }

    public String getBrand_type() {
        return brand_type;
    }

    public void setBrand_type(String brand_type) {
        this.brand_type = brand_type;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDiseases() {
        return diseases;
    }

    public void setDiseases(String diseases) {
        this.diseases = diseases;
    }

    public String getPrecautions() {
        return precautions;
    }

    public void setPrecautions(String precautions) {
        this.precautions = precautions;
    }

    public String getSide_effects() {
        return side_effects;
    }

    public void setSide_effects(String side_effects) {
        this.side_effects = side_effects;
    }

    public String getStorage() {
        return storage;
    }

    public void setStorage(String storage) {
        this.storage = storage;
    }

    public String getUse_of_drug() {
        return use_of_drug;
    }

    public void setUse_of_drug(String use_of_drug) {
        this.use_of_drug = use_of_drug;
    }

    public String getAdult() {
        return adult;
    }

    public void setAdult(String adult) {
        this.adult = adult;
    }

    public String getChild() {
        return child;
    }

    public void setChild(String child) {
        this.child = child;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(brand_id);
        dest.writeString(brand_name);
        dest.writeString(generic_name);
        dest.writeString(brand_image);
        dest.writeString(brand_contains);
        dest.writeString(brand_manufacturer_name);
        dest.writeString(brand_type);
        dest.writeString(category);
        dest.writeString(diseases);
        dest.writeString(precautions);
        dest.writeString(side_effects);
        dest.writeString(storage);
        dest.writeString(use_of_drug);
        dest.writeString(adult);
        dest.writeString(child);
    }
}
