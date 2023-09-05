package com.example.ridebuddy23;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class JourneyData implements Parcelable {
    public String dateTime, startCity, endCity, description, journeyID, submittedDate;
    public String status = "null";

    public String getSubmittedDate() {
        return submittedDate;
    }

    public double sLat, sLog, eLat, eLog;

    public JourneyData(){}

    public String getJourneyID() {
        return journeyID;
    }

    public double getsLat() {
        return sLat;
    }

    public double getsLog() {
        return sLog;
    }

    public double geteLat() {
        return eLat;
    }

    public double geteLog() {
        return eLog;
    }

    public void setCoordinates(double sLat, double sLog, double eLat, double eLog) {
        this.sLat = sLat;
        this.sLog = sLog;
        this.eLat = eLat;
        this.eLog = eLog;
    }

    protected JourneyData(Parcel in) {
        dateTime = in.readString();
        startCity = in.readString();
        endCity = in.readString();
        description = in.readString();
        status = in.readString();
    }

    public static final Creator<JourneyData> CREATOR = new Creator<JourneyData>() {
        @Override
        public JourneyData createFromParcel(Parcel in) {
            return new JourneyData(in);
        }

        @Override
        public JourneyData[] newArray(int size) {
            return new JourneyData[size];
        }
    };

    public void setPage01(String dateTime, String startCity, String endCity) {
        this.dateTime = dateTime;
        this.startCity = startCity;
        this.endCity = endCity;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public String toString() {
        return dateTime + "\n" + startCity + "\n" + endCity + "\n" + status + "\n" + description;
    }

    public String getDateTime() {
        return dateTime;
    }

    public String getStartCity() {
        return startCity;
    }

    public String getEndCity() {
        return endCity;
    }

    public String getStatus() {
        return status;
    }

    public String getDescription() {return description;}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(dateTime);
        dest.writeString(startCity);
        dest.writeString(endCity);
        dest.writeString(description);
        dest.writeString(status);
    }
}
