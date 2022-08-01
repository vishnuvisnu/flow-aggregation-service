package com.netflix.demo.fas;

import com.netflix.demo.db.FlowsDB;
import com.netflix.demo.model.Flow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class QueryController {

	private static final Logger logger = LoggerFactory.getLogger(QueryController.class);
	private final FlowsDB flowsDB;

	@Autowired
	public QueryController() {
		this.flowsDB = FlowsDB.getInstance();
	}

	@GetMapping("/flows")
	@ResponseBody
	public List<Flow> flows(@RequestParam final int hour) {
		try {
			final List<Flow> flows = flowsDB.get(hour);
			return flows;
		} catch (final Exception e) {
			logger.error("Failed to fetch flows for hour " + hour);
		}

		return new ArrayList<>();
	}
}
