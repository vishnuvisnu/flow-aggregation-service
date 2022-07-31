package com.netflix.demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class Flow {
    @JsonProperty("src_app")
    private String srcApp;

    @JsonProperty("dest_app")
    private String destApp;

    @JsonProperty("vpc_id")
    private String vpcId;

    @JsonProperty("hour")
    private int hour;

    @JsonProperty("bytes_rx")
    private int bytesRx;

    @JsonProperty("bytes_tx")
    private int bytesTx;
}
