package asw.instagnam.ricette.domain;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import asw.instagnam.api.events.ricette.RicettaCreataEvent;
import asw.instagnam.ricette.messaging.RicetteEventNotifier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.*; 

@Service
@Transactional
public class RicetteService {

	@Autowired
	private RicetteRepository ricetteRepository;
	
	@Autowired
	@Qualifier("kafkaRicetteEventNotifier")
	private RicetteEventNotifier notifier;

 	public RicettaCompleta createRicetta(String autore, String titolo, String preparazione) {
		RicettaCompleta ricetta = new RicettaCompleta(autore, titolo, preparazione); 
		ricetta = ricetteRepository.save(ricetta);
		notifier.notify(new RicettaCreataEvent(ricetta.getId(), ricetta.getAutore(), ricetta.getTitolo()));
		return ricetta;
	}

 	public RicettaCompleta getRicetta(Long id) {
		RicettaCompleta ricetta = ricetteRepository.findById(id).orElse(null);
		return ricetta;
	}

	public Collection<RicettaCompleta> getRicette() {
		Collection<RicettaCompleta> ricette = ricetteRepository.findAll();
		return ricette;
	}

	public Collection<RicettaCompleta> getRicetteByAutore(String autore) {
		Collection<RicettaCompleta> ricette = ricetteRepository.findAllByAutore(autore);
		return ricette;
	}

}
