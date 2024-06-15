package edu.esprit.tests;

import edu.esprit.entities.Publicite;
import edu.esprit.services.ServiceActualite;
import edu.esprit.services.ServicePublicite;

import java.util.Date;

public class MainA {
    public static void main(String[] args) {
        ServicePublicite sp1 = new ServicePublicite();
        ServiceActualite sp2 = new ServiceActualite();
        //sp1.ajouter(new Publicite(5, "aaaaaa", "bdann", 1263863963, "aa", "aaa", 12, 3));
        //sp1.modifier(new Publicite(464,"aaaaaaaaaaaaaaaa", "bbbbbbbbbbbbbbb", 00000, "la petite ariana","afzgzeg",1,24));
        // sp1.modifier(new Publicite(453,"amine", "yah", 4, "","",12,24));
        // sp1.supprimer(461);
        //System.out.println(sp1.getAll());
        java.sql.Date sqlDate = new java.sql.Date(new Date().getTime());
        // sp2.ajouter(new Actualite("yahya", "aa", sqlDate, "daazza",1));
        //sp2.supprimer(1);
        java.sql.Date sqlDate1 = new java.sql.Date(new Date().getTime());
        // sp2.modifier(new Actualite(26,"aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa","bbbbbbbbbbbbbbbbb",sqlDate1,"asss",1));
        //System.out.println(sp2.getAll());

    }
}