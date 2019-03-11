
package services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import repositories.GPSRepository;

@Service
@Transactional
public class GPSService {

	@Autowired
	private GPSRepository	GPSRepository;

}
