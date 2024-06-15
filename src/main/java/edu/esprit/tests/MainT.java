package edu.esprit.tests;

import edu.esprit.entities.CommentaireTache;
import edu.esprit.entities.EndUser;
import edu.esprit.entities.Tache;
import edu.esprit.services.*;
import edu.esprit.utils.DataSource;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainT {
    public static void main(String[] args) {

        try {
            DataSource ds = new DataSource();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            // Create service instances
            ServiceUser serviceUser = new ServiceUser();
            ServiceTache serviceTache = new ServiceTache();
            ServiceCommentaireTache serviceCommentaireTache = new ServiceCommentaireTache();
            //ServiceCategorieT scat = new ServiceCategorieT();

            // Add a task
            EndUser user01 = serviceUser.getOneByID(14);
            //CategorieT categ01 = serviceTache.getOneByID(13).getCategorie();
            Tache nouvelleTache = new Tache("categ01", "Titre de la tâche", "fichier.txt",
                    new SimpleDateFormat("yyyy-MM-dd HH:mm").parse("2024-02-12 12:00"),
                    new SimpleDateFormat("yyyy-MM-dd HH:mm").parse("2024-02-20 18:00"), "description", EtatTache.TODO, user01);
            serviceTache.ajouter(nouvelleTache);
            System.out.println(serviceTache.getAll());

            // Modify a task
            Tache tacheModifiee = new Tache(97, "categ01", "Titre de la tâche 02", "fichier",
                    new SimpleDateFormat("yyyy-MM-dd HH:mm").parse("2024-02-18 20:00"),
                    new SimpleDateFormat("yyyy-MM-dd HH:mm").parse("2024-02-20 18:00"), "description", EtatTache.TODO, user01);
            serviceTache.modifier(tacheModifiee);
            System.out.println(serviceTache.getAll());

            // Add a comment
            Tache tache = serviceTache.getOneByID(97);
            CommentaireTache nouveauCommentaire = new CommentaireTache(user01, tache, new Date(), "555");
            serviceCommentaireTache.ajouter(nouveauCommentaire);
            System.out.println(serviceCommentaireTache.getAll());

            // Modify a comment
            CommentaireTache commentaireModifie = serviceCommentaireTache.getOneByID(40);
            commentaireModifie.setText_C("6969");
            serviceCommentaireTache.modifier(commentaireModifie);
            System.out.println(serviceCommentaireTache.getAll());

            // Delete a comment
            serviceCommentaireTache.supprimer(14);
            System.out.println(serviceCommentaireTache.getAll());

        } catch (Exception e) {
            System.out.println("Erreur : " + e.getMessage());
        }

    }
}
