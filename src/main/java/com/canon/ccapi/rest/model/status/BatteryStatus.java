package com.canon.ccapi.rest.model.status;


import com.canon.ccapi.rest.interfaces.*;

@ResponseType(type = ResponseTypes.JSON)
@RestCommand(restcommand = "devicestatus/battery",restverb = RestVerbs.GET)
public class BatteryStatus implements CCAPIPojos {

    private String name;
    private String kind;
    private String level;
    private String quality;

    public BatteryStatus(){}

    public BatteryStatus(String name, String kind, String level, String quality) {
        this.name = name;
        this.kind = kind;
        this.level = level;
        this.quality = quality;
    }

    @Override
    public String toString() {
        return "BatteryStatus{" +
                "name='" + name + '\'' +
                ", kind='" + kind + '\'' +
                ", level='" + level + '\'' +
                ", quality='" + quality + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }


}
