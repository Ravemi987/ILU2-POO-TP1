package histoire;

import personnages.Chef;
import personnages.Druide;
import personnages.Gaulois;
import villagegaulois.Etal;
import villagegaulois.Village;

public class ScenarioCasDegrade {
    public static void main(String[] args) {
        Etal etal = new Etal();
        Gaulois obelix = new Gaulois("Obélix", 25);
        System.out.println(etal.libererEtal());
        etal.occuperEtal(obelix, "truc", 0);
        try {
            System.out.println(etal.acheterProduit(5, null));
        } catch (IllegalArgumentException | IllegalStateException e) {
            e.printStackTrace();
        }
        System.out.println("Fin du test");
    }
}
