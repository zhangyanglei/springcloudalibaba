package com.itmuch.gateway;

import java.time.LocalTime;
import lombok.Data;

@Data
public class TimeBetweenConfig {

    private LocalTime start;
    private LocalTime end;
}
