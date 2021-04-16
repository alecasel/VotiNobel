package it.polito.tdp.nobel.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import it.polito.tdp.nobel.db.EsameDAO;

public class Model {
	
	private List<Esame> partenza;
	private Set<Esame> soluzioneMigliore;
	private double mediaSoluzioneMigliore;
	int L = 0;
	
	public Model() {
		EsameDAO dao = new EsameDAO();
		this.partenza = dao.getTuttiEsami();
		//	Soluzione migliore non qui perché altrimenti dovrei pulirla ogni volta
	}

	
	public Set<Esame> calcolaSottoinsiemeEsami(int numeroCrediti) {
		
		Set<Esame> parziale = new HashSet<Esame>();
		soluzioneMigliore = new HashSet<Esame>();
		mediaSoluzioneMigliore = 0;
		
		cerca(parziale, 0, numeroCrediti);
		
		return soluzioneMigliore;	
	}

	
	private void cerca(Set<Esame> parziale, int i, int m) {
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
		
		//	sicuramente crediti < m
		//	L = N => non ci sono più esami da aggiungere
		if (L == partenza.size()) {
			return ;	//	non posso aggiungere esami perché non ne ho più
		}
		
		//	Genero i sottoproblemi
		for (Esame esame : partenza) {
			if (!parziale.contains(esame)) {
				parziale.add(esame);
				cerca(parziale, L+1, m);
				parziale.remove(esame);
			}
		}
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
