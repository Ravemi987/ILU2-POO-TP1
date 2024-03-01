package villagegaulois;

import histoire.VillageSansChefException;
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

			for (int i = 0; i < nbEtals; i++) {
				etals[i] = new Etal();
			}
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
				if (!(etals[i].isEtalOccupe())) {
					return i;
				}
			}
			return -1;
		}

		Etal[] trouverEtals(String produit) {
			int nbEtalsContenantProduit = 0;
            for (Etal etal : etals) {
                if (etal.isEtalOccupe() && etal.contientProduit(produit)) {
                    nbEtalsContenantProduit++;
                }
            }

			Etal[] etalContenantProduit = new Etal[nbEtalsContenantProduit];
			int indiceProduit = 0;
            for (Etal etal : etals) {
                if (etal.isEtalOccupe() && etal.contientProduit(produit)) {
                    etalContenantProduit[indiceProduit] = etal;
                    indiceProduit++;
                }
            }

			return etalContenantProduit;
		}

		Etal trouverVendeur(Gaulois gaulois) {
            for (Etal etal : etals) {
                if (etal.isEtalOccupe() && etal.getVendeur().getNom().equals(gaulois.getNom())) {
                    return etal;
                }
            }
			return null;
		}

		String afficherMarche() {
			StringBuilder chaine = new StringBuilder();
			int nbEtalVide = 0;
            for (Etal etal : etals) {
				if (etal.isEtalOccupe()) {
					chaine.append(etal.afficherEtal());
				} else {
					nbEtalVide++;
				}
            }
			if (nbEtalVide > 0) {
				chaine.append( "Il reste " + nbEtalVide + " étals non utilisés dans le marché.\n");
			}
			return chaine.toString();
		}
	}

	public Village(String nom, int nbVillageoisMaximum, int nbEtals) {
		this.nom = nom;
		villageois = new Gaulois[nbVillageoisMaximum];
		marche = new Marche(nbEtals);
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
		if (chef == null) {
			throw new VillageSansChefException("Le village n'a pas de chef.");
		}
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
		StringBuilder chaine = new StringBuilder(vendeur.getNom() + " cherche un endroit pour vendre " + nbProduit + " " + produit + ".\n");
		int indiceEtal = marche.trouverEtatlLibre();
		marche.utiliserEtal(indiceEtal, vendeur, produit, nbProduit);
		chaine.append("Le vendeur " + vendeur.getNom() + " vend des " + produit + " à l'étal n°" + (indiceEtal+1) +".\n");

		return chaine.toString();
	}

	public String rechercherVendeursProduit(String produit) {
		StringBuilder chaine = new StringBuilder();
		Etal[] etalProduit = marche.trouverEtals(produit);
		switch (etalProduit.length) {
			case 0:
				chaine.append("Il n'y a pas de vendeur qui proposent des " + produit + " au marché.\n");
				break;
			case 1:
				chaine.append("Seul le vendeur " + etalProduit[0].getVendeur().getNom() + " vend des " + produit + " au marché.\n");
				break;
			default:
				chaine.append("Les vendeurs qui proposent des " + produit + " sont :\n");
                for (Etal etal : etalProduit) {
                    chaine.append("- " + etal.getVendeur().getNom() + "\n");
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
		StringBuilder chaine1 = new StringBuilder("Le marche du village " + "\"" + nom + "\" ");
		StringBuilder chaine2 = new StringBuilder(marche.afficherMarche());
		if (marche.etals[1].isEtalOccupe()) {
			return chaine1.append("possède plusieurs étales :\n").append(chaine2).toString();
		} else  {
			if (marche.etals[0].isEtalOccupe()) {
				return chaine1.append("possède une étale :\n").append(chaine2).toString();
			} else {
				return chaine1.append("ne possède pas d'étale :\n").append(chaine2).toString();
			}
		}
	}
}