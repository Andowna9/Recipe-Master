package com.codecooks.serialize;

import java.time.LocalDateTime;

public class RecentRecipeFeedData extends RecipeFeedData {

    private LocalDateTime dateTime;

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }
}
