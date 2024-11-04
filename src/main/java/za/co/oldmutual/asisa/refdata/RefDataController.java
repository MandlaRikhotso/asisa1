package za.co.oldmutual.asisa.refdata;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/refdata")
@PostAuthorize(value="hasAnyAuthority(@roles.refData)")
public class RefDataController {

	@Autowired
	RefDataDAO refDataDAO;
	
	 @Autowired
	 private ReferenceDataCache referenceDataCache;

	@GetMapping("/claimTypes")
	public ResponseEntity<Map<String, Object>> getClaimTypes() {
		Map<String, Object> responseObject = new HashMap<>();
		responseObject.put("claimTypes", refDataDAO.getClaimTypes());
		return returnResponseObject(responseObject);
	}

	@GetMapping("/claimCategories")
	public ResponseEntity<Map<String, Object>> getClaimCategories() {
		Map<String, Object> responseObject = new HashMap<>();
		responseObject.put("claimCategories", refDataDAO.getClaimCategories());
		return returnResponseObject(responseObject);
	}

	@GetMapping("/claimStatuses")
	public ResponseEntity<Map<String, Object>> getClaimStatuses() {
		Map<String, Object> responseObject = new HashMap<>();
		responseObject.put("claimStatuses", refDataDAO.getClaimStatuses());
		return returnResponseObject(responseObject);
	}

	@GetMapping("/claimCauses")
	public ResponseEntity<Map<String, Object>> getClaimCauses() {
		Map<String, Object> responseObject = new HashMap<>();
		responseObject.put("claimCauses", refDataDAO.getClaimCauses());
		return returnResponseObject(responseObject);
	}

	@GetMapping("/impairmentCodes")
	public ResponseEntity<Map<String, Object>> getImpairmentCodes() {
		Map<String, Object> responseObject = new HashMap<>();
		responseObject.put("impairmentCodes", refDataDAO.getImpairmentCodes());
		return returnResponseObject(responseObject);
	}

	@GetMapping("/paymentMethods")
	public ResponseEntity<Map<String, Object>> getPaymentMethods() {
		Map<String, Object> responseObject = new HashMap<>();
		responseObject.put("paymentMethods", refDataDAO.getPaymentMethods());
		return returnResponseObject(responseObject);
	}

	@GetMapping("/policyTypes")
	public ResponseEntity<Map<String, Object>> getPolicyTypes() {
		Map<String, Object> responseObject = new HashMap<>();
		responseObject.put("policyTypes", refDataDAO.getPolicyTypes());
		return returnResponseObject(responseObject);
	}

	@GetMapping("/lifeSpecs")
	public ResponseEntity<Map<String, Object>> getLifeSpecs() {
		Map<String, Object> responseObject = new HashMap<>();
		responseObject.put("lifeSpecs", refDataDAO.getLifeSpecs());
		return returnResponseObject(responseObject);
	}

	@GetMapping("/lifeSymbols")
	public ResponseEntity<Map<String, Object>> getLifeSymbols() {
		Map<String, Object> responseObject = new HashMap<>();
		responseObject.put("symbols", refDataDAO.getLifeSymbols());
		return returnResponseObject(responseObject);
	}

	@GetMapping("/identityTypes")
	public ResponseEntity<Map<String, Object>> getIdentityTypes() {
		Map<String, Object> responseObject = new HashMap<>();
		responseObject.put("identityTypes", refDataDAO.getIdentityTypes());
		return returnResponseObject(responseObject);
	}

	@GetMapping("/titles")
	public ResponseEntity<Map<String, Object>> getTitles() {
		Map<String, Object> responseObject = new HashMap<>();
		responseObject.put("titles", refDataDAO.getTitles());
		return returnResponseObject(responseObject);
	}

	@GetMapping("/genders")
	public ResponseEntity<Map<String, Object>> getGenders() {
		Map<String, Object> responseObject = new HashMap<>();
		responseObject.put("genders", refDataDAO.getGenders());
		return returnResponseObject(responseObject);
	}

	@GetMapping("/notificationTypes")
	public ResponseEntity<Map<String, Object>> getNotificationTypes() {
		Map<String, Object> responseObject = new HashMap<>();
		responseObject.put("notificationTypes", refDataDAO.getNotificationTypes());
		return returnResponseObject(responseObject);
	}
	
	@GetMapping("/readings")
	public ResponseEntity<Map<String, Object>> getReadings() {
		Map<String, Object> responseObject = new HashMap<>();
		responseObject.put("reading", refDataDAO.getReadingsCriteria());
		return returnResponseObject(responseObject);
	}
	

	@GetMapping("/readings/{impairmentCode}")
	public ResponseEntity<Map<String, Object>> getReadingsCriteria(@PathVariable String impairmentCode) {
		Map<String, Object> responseObject = new HashMap<>();
		responseObject.put("readings", referenceDataCache.populateDescFromCode(impairmentCode, RefTypeEnum.READINGS));
		return returnResponseObject(responseObject);
	}

	
	public ResponseEntity<Map<String, Object>> returnResponseObject(Map<String, Object> responseObject) {
		return new ResponseEntity<>(responseObject, HttpStatus.OK);
	}
}
