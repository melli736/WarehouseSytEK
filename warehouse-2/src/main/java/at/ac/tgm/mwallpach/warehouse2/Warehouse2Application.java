package at.ac.tgm.mwallpach.warehouse2;

import at.ac.tgm.mwallpach.warehouse2.io.LocalListener;
import at.ac.tgm.mwallpach.warehouse2.io.Receiver;
import at.ac.tgm.mwallpach.warehouse2.io.Sender;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.*;

@SpringBootApplication
public class Warehouse2Application {

	// Map zur Speicherung der Sender für jede Lagerhaus-ID
	public static Map<String, Sender> senders = new HashMap<>();

	// Map zur Speicherung der Empfänger für jede Lagerhaus-ID
	public static Map<String, Receiver> receivers = new HashMap<>();

	/**
	 * Hauptmethode zum Starten der Anwendung.
	 *
	 * @param args Kommandozeilenargumente
	 * @throws InterruptedException Ausnahme, die auftritt, wenn ein Thread während des Wartens unterbrochen wird
	 */
	public static void main(String[] args) throws InterruptedException {
		// Erstellt eine SpringApplication-Instanz für die Warehouse2Application-Klasse
		SpringApplication app = new SpringApplication(Warehouse2Application.class);

		// Setzt die Standardeigenschaften der Anwendung, einschließlich des Serverports
		app.setDefaultProperties(Collections.singletonMap("server.port", Integer.valueOf(args[1])));

		// Definiert die Lagerhausstandorte
		String[] warehouseLocations = {"war1", "war2", "war3"};

		// Initialisiert Empfänger und Sender für jedes Lagerhaus
		for (String location : warehouseLocations) {
			receivers.put(location, new Receiver(location, false, senders.get(location)));
			senders.put(location, new Sender(location));
			// Jeder Lagerstandort hat seine eigene Liste für empfangene Daten
			LocalListener.dataMap.put(location, new ArrayList<>());
		}

		// Initialisiert Empfänger und Sender für das Hauptlagerhaus
		receivers.put("main", new Receiver("main", true, senders.get("main")));
		senders.put("main", new Sender("main"));
		// Das Hauptlager hat ebenfalls seine eigene Liste für empfangene Daten
		LocalListener.dataMap.put("main", new ArrayList<>());

		// Startet die Anwendung mit den angegebenen Argumenten
		app.run(args);
	}
}
