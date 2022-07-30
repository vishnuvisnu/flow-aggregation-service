package com.netflix.demo.fas;

import com.netflix.demo.db.FlowsDB;
import com.netflix.demo.model.Flow;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class IngestionController {
    private final FlowsDB flowsDB;

    public IngestionController() {
        this.flowsDB = FlowsDB.getInstance();
    }

    @PostMapping(value = "/flows", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void flows(@RequestBody final List<Flow> flows) {
        for (Flow flow : flows) {
            flowsDB.put(flow);
        }
    }
}
