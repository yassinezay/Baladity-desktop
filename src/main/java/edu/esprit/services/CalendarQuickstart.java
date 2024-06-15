package edu.esprit.services;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.store.MemoryDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import edu.esprit.entities.Evenement;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class CalendarQuickstart {
    private static final String APPLICATION_NAME = "Google Calendar API Java Quickstart";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";
    private static final List<String> SCOPES = Arrays.asList(CalendarScopes.CALENDAR_READONLY, CalendarScopes.CALENDAR_EVENTS);
    private static final String CREDENTIALS_FILE_PATH = "/evenementGui/client_secret_911099648307-6odh40p2fvq34jnmeskras2ecqcq8ho1.apps.googleusercontent.com.json";

    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT)
            throws IOException {
        InputStream in = CalendarQuickstart.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets =
                GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(MemoryDataStoreFactory.getDefaultInstance()) // Utilisation de MemoryDataStoreFactory
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    public static void main(String... args) throws IOException, GeneralSecurityException {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Calendar service = new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();

        ServiceEvenement serviceEvenement = new ServiceEvenement();
        Set<Evenement> evenements = serviceEvenement.getAll();

        Map<String, String> addedEventIds = new HashMap<>();

        for (Evenement evenement : evenements) {
            if (evenement.getNomEvent() == null || evenement.getDateEtHeureDeb() == null || evenement.getDateEtHeureFin() == null) {
                System.err.println("L'événement est incomplet : " + evenement);
                continue;
            }

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate startDate = LocalDate.parse(evenement.getDateEtHeureDeb(), formatter);
            LocalDate endDate = LocalDate.parse(evenement.getDateEtHeureFin(), formatter);

            ZonedDateTime startDateTime = startDate.atStartOfDay(ZoneId.of("GMT+2"));
            ZonedDateTime endDateTime = endDate.atStartOfDay(ZoneId.of("GMT+2")).plusDays(1).minusNanos(1);

            Event event = new Event()
                    .setSummary(evenement.getNomEvent())
                    .setLocation("Lieu de l'événement")
                    .setDescription("Description de l'événement.");

            DateTime start = new DateTime(startDateTime.toInstant().toEpochMilli());
            DateTime end = new DateTime(endDateTime.toInstant().toEpochMilli());

            EventDateTime startEventDateTime = new EventDateTime().setDateTime(start);
            EventDateTime endEventDateTime = new EventDateTime().setDateTime(end);

            event.setStart(startEventDateTime);
            event.setEnd(endEventDateTime);

            try {
                if (addedEventIds.containsKey(evenement.getNomEvent())) {
                    System.out.println("L'événement " + evenement.getNomEvent() + " existe déjà dans Google Calendar.");
                    continue;
                }

                // Vérifier si un événement avec le même nom existe déjà dans le calendrier
                String eventExists = checkEventExists(service, evenement.getNomEvent());
                if (eventExists != null) {
                    addedEventIds.put(evenement.getNomEvent(), eventExists);
                    System.out.println("L'événement " + evenement.getNomEvent() + " existe déjà dans Google Calendar.");
                    continue;
                }

                // Si l'événement n'existe pas, l'ajouter
                Event createdEvent = service.events().insert("primary", event).execute();
                addedEventIds.put(evenement.getNomEvent(), createdEvent.getId());

                System.out.println("Événement créé : " + createdEvent.getHtmlLink());
            } catch (IOException e) {
                System.err.println("Erreur lors de l'insertion de l'événement : " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    // Méthode pour vérifier si un événement avec le même nom existe déjà dans le calendrier
    private static String checkEventExists(Calendar service, String eventName) throws IOException {
        String pageToken = null;
        do {
            com.google.api.services.calendar.model.Events events = service.events().list("primary")
                    .setQ(eventName)
                    .setPageToken(pageToken)
                    .execute();
            for (Event event : events.getItems()) {
                // Si un événement avec le même nom est trouvé, retourner son ID
                if (event.getSummary().equals(eventName)) {
                    return event.getId();
                }
            }
            pageToken = events.getNextPageToken();
        } while (pageToken != null);
        // Si aucun événement avec le même nom n'est trouvé, retourner null
        return null;
    }
}
