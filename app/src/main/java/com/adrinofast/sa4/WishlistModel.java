package com.adrinofast.sa4;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class WishlistModel {



    private String userId;
    private List<String> programId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<String> getProgramId() {
        return programId;
    }

    public void setProgramId(List<String> programId) {
        this.programId = programId;
    }

    @Override
    public String toString() {
        return "WishlistModel{" +
                "userId='" + userId + '\'' +
                ", programId=" + programId +
                '}';
    }
}
