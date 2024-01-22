// Warehouse2Application.java
package at.ac.tgm.mwallpach.warehouse2;

import at.ac.tgm.mwallpach.warehouse2.model.WarehouseData;
import at.ac.tgm.mwallpach.warehouse2.warehouse.WarehouseSimulation;
import at.ac.tgm.mwallpach.warehouse2.io.Receiver;
import at.ac.tgm.mwallpach.warehouse2.io.Sender;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SpringBootApplication
public class Warehouse2Application implements CommandLineRunner {

	public static Sender[] senders;

	public static List<String> savedData;

	public static Set<String> timestampsToAcknowledge;

	public static void main(String[] args) {
		SpringApplication.run(Warehouse2Application.class, args);
	}

	@Override
	public void run(String... args) {
		savedData = new ArrayList<>();
		timestampsToAcknowledge = new HashSet<>();
		senders = new Sender[] {
				new Sender("WarehouseTopic_Main", true),
				new Sender("WarehouseTopic_001", false),
				new Sender("WarehouseTopic_002", false),
				new Sender("WarehouseTopic_003", false)
		};
		var receivers = new Receiver[]{
				new Receiver("WarehouseTopic_Main", true),
				new Receiver("WarehouseTopic_001", false),
				new Receiver("WarehouseTopic_002", false),
				new Receiver("WarehouseTopic_003", false),
		};

	}
}
