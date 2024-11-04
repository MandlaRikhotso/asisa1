package za.co.oldmutual.asisa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import za.co.oldmutual.asisa.refdata.RefTypeEnum;
import za.co.oldmutual.asisa.refdata.ReferenceDataCache;

@RestController
@RequestMapping("/api")
public class HealthCheckAPI {

	@Autowired
	ReferenceDataCache referenceDataCache;

	@Autowired
	BuildProperties buildProperties;

	@GetMapping("/readiness")
	@ResponseBody
	public boolean readiness() {
		return true;
	}

	@GetMapping("/liveness")
	@ResponseBody
	public boolean liveness() {
		Assert.isTrue(referenceDataCache.isCodeValid("1", RefTypeEnum.POLICY_TYPE), "Reference Cache is ready");
		return true;
	}

	@GetMapping("/buildProperties")
	@ResponseBody
	public BuildProperties buildProperties() {
		return buildProperties;
	}
}
