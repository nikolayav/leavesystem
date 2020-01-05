package com.leavesystem.service;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.Events;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;

import java.time.Period;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.joda.time.Days;
import org.joda.time.Duration;
//import org.springframework.format.datetime.joda.*;

public class GoogleCal {
    private static final String APPLICATION_NAME = "LeaveSystem Calendar";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";

    /**
     * Global instance of the scopes required by this quick start.
     * If modifying these scopes, delete your previously saved tokens/ folder.
     */
    private static final List<String> SCOPES = Collections.singletonList("https://www.googleapis.com/auth/calendar");
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";

    /**
     * Creates an authorized Credential object.
     * @param HTTP_TRANSPORT The network HTTP Transport.
     * @return An authorized Credential object.
     * @throws IOException If the credentials.json file cannot be found.
     */
    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        // Load client secrets.
        InputStream in = GoogleCal.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }
    
    public void getTokens( ) throws IOException, GeneralSecurityException{
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Calendar service = new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    public static void test() throws IOException, GeneralSecurityException {
        // Build a new authorized API client service.
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Calendar service = new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();

        
        // List the next 10 events from the primary calendar.
        DateTime now = new DateTime(System.currentTimeMillis());
        Events events = service.events().list("leavesystemcal@gmail.com")
                .setMaxResults(10)
                .setTimeMin(now)
                .setOrderBy("startTime")
                .setSingleEvents(true)
                .execute();
        List<Event> items = events.getItems();
        if (items.isEmpty()) {
            System.out.println("No upcoming events found.");
        } else {
            System.out.println("Upcoming events");
            for (Event event : items) {
                DateTime start = event.getStart().getDateTime();
                if (start == null) {
                    start = event.getStart().getDate();
                }
                System.out.printf("%s (%s)\n", event.getSummary(), start);
            }
        }
        
        for (Event event : items) {
        	System.out.println(event.getSummary());
        	if (event.getSummary().equals("usertest usertest")) {
            System.out.println(event.getId());
        	break;
        	}
        }
    }
    
    public String createEvent(String name, DateTime dateFrom, DateTime dateTo) throws GeneralSecurityException, IOException {
    	final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Calendar service = new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();
        
    	Event event = new Event()
    		    .setSummary(name)
    		    .setDescription("Absence.");

    		EventDateTime start = new EventDateTime()
    		    .setDateTime(dateFrom)
    		    .setTimeZone("Europe/Sofia");
    		event.setStart(start);

    		EventDateTime end = new EventDateTime()
    		    .setDateTime(dateTo)
    		    .setTimeZone("Europe/Sofia");
    		event.setEnd(end);
    		
    		String calendarId = "primary";
    		event = service.events().insert(calendarId, event).execute();
    		
			return event.getId();
			
    }
    
    public void deleteEvent(String calendarId) throws GeneralSecurityException, IOException {
    	final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
    	Calendar service = new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();

    		service.events().delete("primary", calendarId).execute();
    }
    
}