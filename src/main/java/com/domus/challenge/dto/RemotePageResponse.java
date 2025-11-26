package com.domus.challenge.dto;

import java.util.List;
import lombok.Data;

@Data
public class RemotePageResponse {

    private int page;
    private int per_page;
    private int total;
    private int total_pages;
    private List<RemoteMovie> data;
}
