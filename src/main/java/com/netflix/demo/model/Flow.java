package com.netflix.demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public class Flow {
    @JsonProperty("src_app")
    private String srcApp;

    @JsonProperty("dest_app")
    private String destApp;

    @JsonProperty("vpc_id")
    private String vpcId;

    @JsonProperty("hour")
    private int hour;

    @Setter
    @JsonProperty("bytes_rx")
    @EqualsAndHashCode.Exclude
    private int bytesRx;

    @Setter
    @EqualsAndHashCode.Exclude
    @JsonProperty("bytes_tx")
    private int bytesTx;
}
