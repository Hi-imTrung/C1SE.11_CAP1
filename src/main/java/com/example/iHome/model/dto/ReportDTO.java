package com.example.iHome.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReportDTO {

    private String startDate;
    private String endDate;

    private int totalBooking;
    private int totalPending;
    private int totalApproved;

}
