package moe.gkd.smsassistant.entity;

public class ConfigStatusEntity {
    private String name;
    private boolean enable;

    public ConfigStatusEntity(String name, boolean enable) {
        this.name = name;
        this.enable = enable;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }
}
