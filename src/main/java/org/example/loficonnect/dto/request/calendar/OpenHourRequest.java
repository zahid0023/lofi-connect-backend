package org.example.loficonnect.dto.request.calendar;

import lombok.Data;

@Data
public class OpenHourRequest {
    private Integer openHour;    // required, 0 <= openHour <= 23
    private Integer openMinute;  // required, 0 <= openMinute <= 60
    private Integer closeHour;   // required, 0 <= closeHour <= 23
    private Integer closeMinute; // required, 0 <= closeMinute <= 60
}