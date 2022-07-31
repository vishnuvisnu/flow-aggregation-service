package com.netflix.demo.fas;

import com.netflix.demo.db.FlowsDB;
import com.netflix.demo.exceptions.FlowsNotFoundException;
import com.netflix.demo.model.Flow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class QueryController {

	private final FlowsDB flowsDB;

	@Autowired
	public QueryController() {
		this.flowsDB = FlowsDB.getInstance();
	}

	@GetMapping("/flows")
	@ResponseBody
	public List<Flow> flows(@RequestParam final int hour) throws FlowsNotFoundException {
		final List<Flow> flows = flowsDB.get(hour);
//		for (final Flow flow : flows) {
//			System.out.println("Flow is " + flow.toString());
//		}

		return flows;
	}
}
