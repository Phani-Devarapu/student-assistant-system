package com.adrinofast.sa4;

import java.util.List;

public class UserSubscriptions {

    private List<String> intrests;

    public UserSubscriptions() {
    }

    public UserSubscriptions(List<String> intrests) {
        this.intrests = intrests;
    }

    public List<String> getIntrests() {
        return intrests;
    }

    public void setIntrests(List<String> intrests) {
        this.intrests = intrests;
    }

    @Override
    public String toString() {
        return "UserSubscriptions{" +
                "intrests=" + intrests +
                '}';
    }
}
