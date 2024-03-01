package villagegaulois;

import personnages.Chef;
import personnages.Gaulois;

public class Village {
	private String nom;
	private Chef chef;
	private Gaulois[] villageois;
	private int nbVillageois = 0;
	private Marche marche;
	
	private static class Marche {
		private Etal[] etals;
		
		private Marche(int nbEtals) {
			etals = new Etal[nbEtals];
		}
		
		void utiliserEtal(int indiceEtal, Gaulois vendeur, String produit, int nbProduit) {
			if (0 <= indiceEtal && indiceEtal < etals.length) {
				if (!(etals[indiceEtal].isEtalOccupe())) {
					etals[indiceEtal].occuperEtal(vendeur, produit, nbProduit);
				}
			}
		}
		
		int trouverEtatlLibre() {
			for (int i = 0; i < etals.length; i++) {
				if (etals[i] != null && !(etals[i].isEtalOccupe())) {
					return i;
				}
			}
			return -1;
		}
		
		Etal[] trouverEtals(String produit) {
			int nbEtalsContenantProduit = 0;
			for (int i = 0; i < etals.length; i++) {
				if (etals[i] != null && etals[i].contientProduit(produit)) {
					nbEtalsContenantProduit++;
				}
			}

			Etal[] etalContenantProduit = new Etal[nbEtalsContenantProduit];
			int indiceProduit = 0;
			for (int j = 0; j < etals.length; j++) {
				if (etals[j] != null && etals[j].contientProduit(produit)) {
					etalContenantProduit[indiceProduit] = etals[j];
					indiceProduit++;
				}
			}
	
			return etalContenantProduit;
		}
		
		Etal trouverVendeur(Gaulois gaulois) {
			for (int i = 0; i < etals.length; i++) {
				if (etals[i] != null && (etals[i].getVendeur().getNom().equals(gaulois.getNom()))) {
					return etals[i];
				}
			}
			return null;
		}
		
		String afficherMarche() {
			StringBuilder chaine = new StringBuilder();
			int nbEtalVide = 0;
			for (int i = 0; i < etals.length; i++) {
				if (etals[i] != null) {
					if (etals[i].isEtalOccupe()) {
						chaine.append(etals[i].afficherEtal());
					} else {
						nbEtalVide++;
					}
				}
			}
			if (nbEtalVide > 0) {
				chaine.append( "Il reste" + nbEtalVide + " étals non utilisés dans le marché.\n");
			}
			return chaine.toString();
		}
	}

	public Village(String nom, int nbVillageoisMaximum, int nbEtals) {
		this.nom = nom;
		villageois = new Gaulois[nbVillageoisMaximum];
		Marche marche = new Marche(nbEtals);
	}

	public String getNom() {
		return nom;
	}

	public void setChef(Chef chef) {
		this.chef = chef;
	}

	public void ajouterHabitant(Gaulois gaulois) {
		if (nbVillageois < villageois.length) {
			villageois[nbVillageois] = gaulois;
			nbVillageois++;
		}
	}

	public Gaulois trouverHabitant(String nomGaulois) {
		if (nomGaulois.equals(chef.getNom())) {
			return chef;
		}
		for (int i = 0; i < nbVillageois; i++) {
			Gaulois gaulois = villageois[i];
			if (gaulois.getNom().equals(nomGaulois)) {
				return gaulois;
			}
		}
		return null;
	}

	public String afficherVillageois() {
		StringBuilder chaine = new StringBuilder();
		if (nbVillageois < 1) {
			chaine.append("Il n'y a encore aucun habitant au village du chef "
					+ chef.getNom() + ".\n");
		} else {
			chaine.append("Au village du chef " + chef.getNom()
					+ " vivent les légendaires gaulois :\n");
			for (int i = 0; i < nbVillageois; i++) {
				chaine.append("- " + villageois[i].getNom() + "\n");
			}
		}
		return chaine.toString();
	}
	
	public String installerVendeur(Gaulois vendeur, String produit, int nbProduit) {
		StringBuilder chaine = new StringBuilder(vendeur.getNom() + " cherche un endroit pour vendre " + nbProduit + " " + produit + "\n");
		int indiceEtal = marche.trouverEtatlLibre();
		marche.utiliserEtal(indiceEtal, vendeur, produit, nbProduit);
		chaine.append("Le vendeur " + vendeur.getNom() + " vend des " + produit + " à l'étal n°" + indiceEtal +".\n");
		
		return chaine.toString();
	}
	
	public String rechercherVendeursProduit(String produit) {	
		StringBuilder chaine = new StringBuilder();
		Etal[] etalProduit = marche.trouverEtals(produit);
		switch (etalProduit.length) {
			case 0:
				chaine.append("Il n'y a pas de vendeur qui propose " + produit + " au marché.");
				break;
			case 1:
				chaine.append("Seul le vendeur " + etalProduit[0].getVendeur().getNom() + " vend des " + produit + " au marché.\n");
				break;
			default:
				chaine.append("Les vendeurs qui proposent" + produit + "sont :\n");
				for (int i = 0; i < etalProduit.length; i++) {
					chaine.append("- " + etalProduit[i].getVendeur().getNom());
				}
				break;
		}
		return chaine.toString();
	}
	
	public Etal rechercherEtal(Gaulois vendeur) {
		return marche.trouverVendeur(vendeur);
	}
	
	public String partirVendeur(Gaulois vendeur) {
		return rechercherEtal(vendeur).libererEtal();

	}
	
	public String afficherMarche() {
		return "Le marche du village " + nom + "possède plusieurs étales :\n" + marche.afficherMarche();
	}
}