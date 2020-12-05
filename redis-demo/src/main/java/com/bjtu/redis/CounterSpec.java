package com.bjtu.redis;

public class CounterSpec {
    private String countername;
    private String counterindex;
    private String type;
    private String keyfields;
    private String field;
    private String valuefields;

    public String getCountername() {
        return countername;
    }

    public void setCountername(String countername) {
        this.countername = countername;
    }

    public String getCounterindex() {
        return counterindex;
    }

    public void setCounterindex(String counterindex) {
        this.counterindex = counterindex;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getKeyfields() {
        return keyfields;
    }

    public void setKeyfields(String keyfields) {
        this.keyfields = keyfields;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getValuefields() {
        return valuefields;
    }

    public void setValuefields(String valuefields) {
        this.valuefields = valuefields;
    }
}
