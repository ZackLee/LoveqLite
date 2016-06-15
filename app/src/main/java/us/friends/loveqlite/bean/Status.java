package us.friends.loveqlite.bean;

import java.util.HashMap;
import java.util.Map;

public class Status {

    private Integer succeed;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * @return The succeed
     */
    public Integer getSucceed() {
        return succeed;
    }

    /**
     * @param succeed The succeed
     */
    public void setSucceed(Integer succeed) {
        this.succeed = succeed;
    }

    public Status withSucceed(Integer succeed) {
        this.succeed = succeed;
        return this;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    public Status withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

}