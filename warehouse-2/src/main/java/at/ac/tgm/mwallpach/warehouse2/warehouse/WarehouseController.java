// WarehouseController.java
package at.ac.tgm.mwallpach.warehouse2.warehouse;

import at.ac.tgm.mwallpach.warehouse2.Warehouse2Application;
import at.ac.tgm.mwallpach.warehouse2.io.LocalListener;
import at.ac.tgm.mwallpach.warehouse2.model.WarehouseData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
public class WarehouseController {

    private static final Logger logger = LoggerFactory.getLogger(WarehouseController.class);
    @Autowired
    private WarehouseService service;

    @CrossOrigin(origins = "http://localhost:3000")
    @RequestMapping("/")
    public String warehouseMain() {
        String mainPage = "Warehouse Application<br/><br/>" +
                "<a href='http://localhost:8080/warehouse/main'>Link to warehouse/main</a><br/>" +
                "<a href='http://localhost:8080/warehouse/001/data'>Link to warehouse/001/data</a><br/>" +
                "<a href='http://localhost:8080/warehouse/002/data'>Link to warehouse/002/data</a><br/>" +
                "<a href='http://localhost:8080/warehouse/003/data'>Link to warehouse/003/data</a><br/>";
        return mainPage;
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @RequestMapping(value="/warehouse/main", produces = MediaType.APPLICATION_JSON_VALUE)
    public String warehouseMainData() {
        for(var data : Warehouse2Application.savedData) {
            logger.info("parsing from: " + data);
            String timestamp = data.split("\"")[11];
            logger.info("timestamp parsed: " + timestamp);

            Warehouse2Application.senders[0].sendMessageToTopic(timestamp);
        }
        return Warehouse2Application.savedData.toString();
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @RequestMapping(value="/warehouse/{inID}/data", produces = MediaType.APPLICATION_JSON_VALUE)
    public WarehouseData warehouseData(@PathVariable String inID ) {
        WarehouseData data = service.getWarehouseData( inID );

        String timestamp = data.getTimestamp();
        logger.info("created timestampToAcknowledge: " + timestamp);
        Warehouse2Application.timestampsToAcknowledge.add(timestamp);

        switch (inID)   {
            case "001":
                Warehouse2Application.senders[1].sendMessageToTopic(data.toString());
                break;
            case "002":
                Warehouse2Application.senders[2].sendMessageToTopic(data.toString());
                break;
            case "003":
                Warehouse2Application.senders[3].sendMessageToTopic(data.toString());
                break;
            default:
                logger.error("Ungültige ID!");
                break;
        }
        return data;
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @RequestMapping(value="/warehouse/{inID}/xml", produces = MediaType.APPLICATION_XML_VALUE)
    public WarehouseData warehouseDataXML( @PathVariable String inID ) {
        return service.getWarehouseData( inID );
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @RequestMapping("/warehouse/{inID}/transfer")
    public String warehouseTransfer( @PathVariable String inID ) {
        return service.getGreetings("Warehouse.Transfer!");
    }

}
