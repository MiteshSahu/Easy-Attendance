package com.example.easy_attendance;

public class StudentsStats {
    String rollno ;
    String stats;

    public StudentsStats(String rollno, String stats) {
        this.rollno = rollno;
        this.stats = stats;
    }

    public String getRollno() {
        return rollno;
    }

    public void setRollno(String rollno) {
        this.rollno = rollno;
    }

    public String getStats() {
        return stats;
    }

    public void setStats(String stats) {
        this.stats = stats;
    }
}
