package org.example.loficonnect.dto.request.calendar;

import lombok.Data;

import java.util.List;

@Data
public class OpenHoursRequest {

    private List<Integer> daysOfTheWeek; // required, e.g., [0,1,2]

    private List<OpenHourRequest> openHours; // required
}
