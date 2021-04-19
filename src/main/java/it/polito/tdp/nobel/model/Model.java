package it.polito.tdp.nobel.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import it.polito.tdp.nobel.db.EsameDAO;

public class Model {
	
	private List<Esame> partenza;
	private Set<Esame> soluzioneMigliore;
	private double mediaSoluzioneMigliore;
	
	public Model() {
		EsameDAO dao = new EsameDAO();
		this.partenza = dao.getTuttiEsami();
		//	Soluzione migliore non qui perché altrimenti dovrei pulirla ogni volta
	}

	
	public Set<Esame> calcolaSottoinsiemeEsami(int numeroCrediti) {
		
		Set<Esame> parziale = new HashSet<Esame>();
		soluzioneMigliore = new HashSet<Esame>();
		mediaSoluzioneMigliore = 0;
		
		cercaFurbo(parziale, 0, numeroCrediti);
		
		return soluzioneMigliore;	
	}

	/**
	 * COMPLESSITA' = N!
	 * @param parziale
	 * @param L
	 * @param m
	 */
	private void cerca(Set<Esame> parziale, int L, int m) {
		//	casi terminali
		
		int crediti = sommaCrediti(parziale);
		
		if (crediti > m) {
			return ;
		}
		
		if (crediti == m) {
			double media = calcolaMedia(parziale);
			if (media > mediaSoluzioneMigliore) {
				soluzioneMigliore = new HashSet<>(parziale);
				mediaSoluzioneMigliore = media;
			}
			
			return ;	//	Ho trovato la soluzione migliore, non vado oltre
		}
		
		//	se arrivo qui: sicuramente abbiamo crediti < m
		//	se L = N => non ci sono più esami da aggiungere
		if (L == partenza.size()) {
			return ;	//	non posso aggiungere esami perché non ne ho più
		}
		
		//	Genero i sottoproblemi
		for (Esame esame : partenza) {
			//	Non possiamo avere soluzione parziale che contiene due volte lo stesso esame. Non devo aggiungere di nuovo lo stesso,
			//	quindi salto quel giro del ciclo
			if (!parziale.contains(esame)) {
				parziale.add(esame);
				cerca(parziale, L+1, m);
				parziale.remove(esame);
			}
		}
	}
	
	/**
	 * Complessità 2^N
	 * @param parziale
	 * @param L
	 * @param m
	 */
	private void cercaFurbo(Set<Esame> parziale, int L, int m) {
		// casi terminali

		int crediti = sommaCrediti(parziale);

		if (crediti > m) {
			return;
		}

		if (crediti == m) {
			double media = calcolaMedia(parziale);
			if (media > mediaSoluzioneMigliore) {
				soluzioneMigliore = new HashSet<>(parziale);
				mediaSoluzioneMigliore = media;
			}

			return; // Ho trovato la soluzione migliore, non vado oltre
		}
		
		//	se arrivo qui: sicuramente abbiamo crediti < m
		//	se L = N => non ci sono più esami da aggiungere
		if (L == partenza.size()) {
			return ;	//	non posso aggiungere esami perché non ne ho più
		}
		
		//	Generazione sottoproblemi
		//	partenza[L] è da aggiungere o no? Provo entrambi i casi. Ho due strade, cioè aggiungo o non aggiungo
		parziale.add(partenza.get(L));	//	Al primo giro della ricorsione, aggiungo l'indice 0 e così via...
		cercaFurbo(parziale, L+1, m);
		
		parziale.remove(partenza.get(L));
		cercaFurbo(parziale, L+1, m);
	}


	public double calcolaMedia(Set<Esame> esami) {
		
		int crediti = 0;
		int somma = 0;
		
		for(Esame e : esami){
			crediti += e.getCrediti();
			somma += (e.getVoto() * e.getCrediti());
		}
		
		return somma/crediti;
	}
	
	public int sommaCrediti(Set<Esame> esami) {
		int somma = 0;
		
		for(Esame e : esami)
			somma += e.getCrediti();
		
		return somma;
	}

}
