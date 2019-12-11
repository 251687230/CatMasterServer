package com.zous.catmaster.bean;

import java.util.ArrayList;
import java.util.List;

public class FrequencyBean {
    private String uniqueKey;
    private long start;
    private long end;
    private int time;
    private int limit;
    List<Long> accessPoints = new ArrayList<>();

    public void reset(long timeMillis) {

        start = end = timeMillis;
        accessPoints.clear();
        accessPoints.add(timeMillis);
    }

    @Override
    public String toString() {
        return "FrequencyStruct [uniqueKey=" + uniqueKey + ", start=" + start
                + ", end=" + end + ", time=" + time + ", limit=" + limit
                + ", accessPoints=" + accessPoints + "]";
    }

    public String getUniqueKey() {
        return uniqueKey;
    }

    public void setUniqueKey(String uniqueKey) {
        this.uniqueKey = uniqueKey;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public List<Long> getAccessPoints() {
        return accessPoints;
    }

    public void setAccessPoints(List<Long> accessPoints) {
        this.accessPoints = accessPoints;
    }
}
