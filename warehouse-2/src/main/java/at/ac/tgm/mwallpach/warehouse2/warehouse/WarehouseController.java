package at.ac.tgm.mwallpach.warehouse2.warehouse;

import at.ac.tgm.mwallpach.warehouse2.Warehouse2Application;
import at.ac.tgm.mwallpach.warehouse2.io.LocalListener;
import at.ac.tgm.mwallpach.warehouse2.io.Receiver;
import at.ac.tgm.mwallpach.warehouse2.io.Sender;
import at.ac.tgm.mwallpach.warehouse2.model.WarehouseData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class WarehouseController {
    // SLF4J-Logger für die Protokollierung von Nachrichten
    private final static Logger logger = LoggerFactory.getLogger(WarehouseController.class);

    // Autowired WarehouseService für die Geschäftslogik
    @Autowired
    private WarehouseService service;

    // Map zur Speicherung der Sender für jede Lagerhaus-ID
    private static final Map<String, Sender> senders = Warehouse2Application.senders;

    /**
     * Behandelt die Anfrage für die Wurzel-URL ("/") und stellt Links zu den Datenendpunkten jedes Lagers bereit.
     *
     * @return HTML-Seite mit Links zu den einzelnen Datenendpunkten der Lagerhäuser.
     */
    @RequestMapping("/")
    public String warehouseMain() {
        StringBuilder mainPage = new StringBuilder("Warehouse Application<br/><br/>");

        // Fügt Links für jedes Lagerhaus in sortierter Reihenfolge hinzu
        for (String warehouseID : Warehouse2Application.senders.keySet()) {
            mainPage.append("<a href='http://localhost:8080/warehouse/")
                    .append(warehouseID)
                    .append("/data'>Link zu Lagerhaus/")
                    .append(warehouseID)
                    .append("/data</a><br/>");
        }

        return mainPage.toString();
    }

    /**
     * Behandelt Anfragen für Lagerhausdaten basierend auf der bereitgestellten Lagerhaus-ID.
     *
     * @param inID Lagerhaus-ID
     * @return WarehouseData im JSON-Format
     */
    @CrossOrigin(origins = "http://localhost:3000")
    @RequestMapping(value = "/warehouse/{inID}/data", produces = MediaType.APPLICATION_JSON_VALUE)
    public WarehouseData warehouseData(@PathVariable String inID) {
        logger.info("getting data from warehouse id: " + inID);
        WarehouseData data = service.getWarehouseData(inID);

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = null;
        try {
            jsonString = objectMapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        logger.info(jsonString);

        // Use the sender corresponding to the warehouse ID
        Sender sender = Warehouse2Application.senders.get(inID);
        if (sender != null) {
            logger.info("sending to id " + inID);
            sender.sendMessageToTopic("Success: Data received and processed.");

            // Send ACK automatically using LocalListener
            LocalListener localListener = Warehouse2Application.receivers.get(inID).getLocalListener();
            if (localListener != null) {
                localListener.sendAcknowledgement();
            } else {
                logger.warn("No LocalListener found for warehouse ID: " + inID);
            }
        }

        return data;
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @RequestMapping(value = "/warehouse/main/data", produces = MediaType.APPLICATION_JSON_VALUE)
    public String warehouseMainData() throws JsonProcessingException {
        // Erstellen Sie eine Map für alle Daten
        Map<String, List<String>> allData = new HashMap<>();

        // Iterieren Sie über alle Empfänger und fügen Sie die Daten zur Map hinzu
        for (Receiver receiver : Warehouse2Application.receivers.values()) {
            LocalListener localListener = receiver.getLocalListener();
            if (localListener != null) {
                allData.put(receiver.getLocation(), LocalListener.dataMap.get(receiver.getLocation()));
            }
        }

        // Konvertiere die Map in einen JSON-String
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(allData);
    }
}
