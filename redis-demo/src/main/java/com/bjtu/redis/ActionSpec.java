package com.bjtu.redis;

import java.util.List;

public class ActionSpec {
    /**
     * 操作类型-枚举
     */
    private enum ActionType {
        USER_LIKE_INCR,
        USER_LIKE_DECR,
        USER_LIKE_FREQ_INCR,
        USER_LIKE_FREQ_DECR,
    }

    private ActionType type;
    private List<String> feature_retrieve;
    private List<String> save_counter;

    public String getType() {
        return type.name();
    }

    public void setType(String type) {
        switch (type) {
            case "USER_LIKE_INCR":
                this.type = ActionType.USER_LIKE_INCR;
                break;
            case "USER_LIKE_DECR":
                this.type = ActionType.USER_LIKE_DECR;
                break;
            case "USER_LIKE_FREQ_INCR":
                this.type = ActionType.USER_LIKE_FREQ_INCR;
                break;
            case "USER_LIKE_FREQ_DECR":
                this.type = ActionType.USER_LIKE_FREQ_DECR;
                break;
            default:
                System.out.println("指令类别无效！");
                break;
        }
    }

    public List<String> getFeature_retrieve() {
        return feature_retrieve;
    }

    public void setFeature_retrieve(List<String> feature_retrieve) {
        this.feature_retrieve = feature_retrieve;
    }

    public List<String> getSave_counter() {
        return save_counter;
    }

    public void setSave_counter(List<String> save_counter) {
        this.save_counter = save_counter;
    }

}
