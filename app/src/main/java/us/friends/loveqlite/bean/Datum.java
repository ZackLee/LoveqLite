package us.friends.loveqlite.bean;


import java.util.HashMap;
import java.util.Map;

public class Datum {

    private String lang;
    private String id;
    private String file_name;
    private String attach_file;
    private String down_url;
    private String download_num;

    private int year;
    private int month;
    private String day;

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public Datum setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
        return this;
    }

    public String getLang() {
        return lang;
    }

    public Datum setLang(String lang) {
        this.lang = lang;
        return this;
    }

    public String getId() {
        return id;
    }

    public Datum setId(String id) {
        this.id = id;
        return this;
    }

    public String getFile_name() {
        return file_name;
    }

    public Datum setFile_name(String file_name) {
        this.file_name = file_name;
        return this;
    }

    public String getAttach_file() {
        return attach_file;
    }

    public Datum setAttach_file(String attach_file) {
        this.attach_file = attach_file;
        return this;
    }

    public String getDown_url() {
        return down_url;
    }

    public Datum setDown_url(String down_url) {
        this.down_url = down_url;
        return this;
    }

    public String getDownload_num() {
        return download_num;
    }

    public Datum setDownload_num(String download_num) {
        this.download_num = download_num;
        return this;
    }

    public int getYear() {
        return year;
    }

    public Datum setYear(int year) {
        this.year = year;
        return this;
    }

    public int getMonth() {
        return month;
    }

    public Datum setMonth(int month) {
        this.month = month;
        return this;
    }

    public String getDay() {
        return day;
    }

    public Datum setDay(String day) {
        this.day = day;
        return this;
    }

    private int dayOfWeek;



}
