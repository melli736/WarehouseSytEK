# MidEng 7.2 Warehouse Message Oriented Middleware

**Author:** Melissa Wallpach 4BHIT
**Datum:** 2023-01-02

## Lösungsweg

**Schritt 1**
Installation von Apache ActiveMQ

**Schritt 2**
Weitere Attribute zum Warehouse hinzugefügt:

```
    private String warehouseID;
    private String warehouseName;
    private String warehouseAddress;
    private int warehousePostalCode;
    private String warehouseCity;
    private String warehouseCountry;
    private String timestamp;
```

toString methode angepasst.

**Schritt 3**
Die Methode WarehouseData getData(String inID) in der Simulation Klasse an die neuen Attribute angepasst. (Hart gecodet)

**Schritt 4**
Klasse Product erstellt:
Ein Produktobjekt, das Produktinformationen wie ID, Name, Category, Quantity, Amount und UnitType enthält.

**Schritt 5**
Der Klasse WarehouseData ein Produktarray als attribut hinzugefügt.

**Schritt 6**
Einen Konstruktor für die Klasse WarehouseSimulation erstellt, der den Pool (Produkt-Array) an möglichen Produkten (10) füllt.

**Schritt 7**
Eine Methode getRandomProducts in die Klasse WarehouseSimulation hinzugefügt, die 3 zufällige Produkte aus dem Pool auswählt.

**Schritt 8**
Die Methode getData in WarehouseSimulation so angepasst, dass sie ein Produkt-Array erstellt und mittels der methode getRandomProducts mit 3 Produkten füllt und an den Array-Setter des WarehouseData-Objektes übergibt.

### GKv

**Schritt 9**
Neuen Ordner erstellen und im terminal ein react projekt erstellen und axios installieren
```
npx create-react-app my-app
npm intsall axios  
```

**Schritt 10**
```
import { useState, useEffect } from "react";
import axios from 'axios';
```
*useEffect*: useEffect ist ein in React integrierter Hook, der es ermöglicht, Nebeneffekte in funktionalen Komponenten zu verwalten. Nebeneffekte umfassen typischerweise Datenabfragen, das manuelle Ändern des DOM oder Interaktionen mit externen Ressourcen wie APIs. Mit useEffect kann man diese Nebeneffekte steuern und sicherstellen, dass sie zur richtigen Zeit während des Lebenszyklus einer Komponente ausgeführt werden.

**Schritt 11**
* Component Table erstellen
* reload-Funktion definieren: Die reload-Funktion ist eine asynchrone Funktion, die Daten von einer API abruft. Sie verwendet Axios, um eine GET-Anfrage an die URL 'http://localhost:8080/warehouse/001/data' zu senden. Wenn die Anfrage erfolgreich ist, werden die erhaltenen Daten in die Zustandsvariablen gespeichert.
* Rendern der Komponente: Die Komponente rendert eine Schaltfläche "Reload", um die reload-Funktion manuell auszulösen, sowie verschiedene Informationen über das Lagerhaus, einschließlich Lagerhaus-ID, -Name, -Adresse, -Postleitzahl, -Stadt und -Land, sowie den Zeitstempel.
* Tabellenansicht der Daten: Unterhalb der Lagerhausinformationen wird eine Tabelle gerendert, die die Produktdaten anzeigt. Die Produktdaten werden aus der data-Zustandsvariablen genommen und in einer Tabellenansicht dargestellt. Jede Zeile der Tabelle zeigt die Produktattribute wie ID, Name, Kategorie, Menge, Betrag und Einheitstyp an.
* Schutz vor null-Werten: Bevor Daten in der Tabelle angezeigt werden, wird überprüft, ob data nicht null oder undefined ist. Dies verhindert Fehler, wenn die Daten noch nicht geladen wurden.
* Manuelles Neuladen der Daten: Durch Klicken auf die "Reload"-Schaltfläche oder beim ersten Rendern der Komponente werden die Daten von der API erneut geladen.

```
function Table(props) {

  //http://localhost:8080/warehouse/001/data

  const [data, setData] = useState(null);

  const [warehouseID, setWarehouseID] = useState(null);
  const [warehouseName, setWarehouseName] = useState(null);
  const [warehouseAddress, setWarehouseAddress] = useState(null);
  const [warehousePostalCode, setWarehousePostalCode] = useState(null);
  const [warehouseCity, setWarehouseCity] = useState(null);
  const [warehouseCountry, setWarehouseCountry] = useState(null);
  const [timestamp, setTimestamp] = useState(null);

  useEffect(() => {
    reload();
  }, []);

  async function reload() {
    const apiUrl = 'http://localhost:8080/warehouse/001/data';
    axios.get(apiUrl)
      .then(function (response) {
        const warehouseData = response.data;
        setData(warehouseData.products);
        setWarehouseID(warehouseData.warehouseID);
        setWarehouseName(warehouseData.warehouseName);
        setWarehouseAddress(warehouseData.warehouseAddress);
        setWarehousePostalCode(warehouseData.warehousePostalCode);
        setWarehouseCity(warehouseData.warehouseCity);
        setWarehouseCountry(warehouseData.warehouseCountry);
        setTimestamp(warehouseData.timestamp);
      })
      .catch(function (error) {
        console.error('Error:', error);
      });
  }
  

  return (
    <div className="consumer">
      <button onClick={reload}>Reload</button>
      <br />
      <span>Warehouse ID: {warehouseID}</span>
      <br />
      <span>Warehouse Name: {warehouseName}</span>
      <br />
      <span>Warehouse Address: {warehouseAddress}</span>
      <br />
      <span>Warehouse Postal Code: {warehousePostalCode}</span>
      <br />
      <span>Warehouse City: {warehouseCity}</span>
      <br />
      <span>Warehouse Country: {warehouseCountry}</span>
      <br />
      <span>Timestamp: {timestamp}</span>
      <br />
      
      <table>
        <thead>
          <tr>
            <th>ID</th>
            <th>Name</th>
            <th>Category</th>
            <th>Quantity</th>
            <th>Amount</th>
            <th>Unit</th>
          </tr>
        </thead>
        <tbody>
          {data && data.map((item, index) => (
            <tr key={index}>
              <td>{item.id}</td>
              <td>{item.name}</td>
              <td>{item.category}</td>
              <td>{item.quantity}</td>
              <td>{item.amount}</td>
              <td>{item.unitType}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
    );
}
```

## EK implementierung

Dieses Java-Anwendungsprojekt implementiert eine Kommunikationsplattform für eine Handelskette mithilfe des Java Message Service (JMS) mit Apache ActiveMQ als Message Broker. Das System umfasst mehrere Lagerstandorte, die über Message Queues mit einem zentralen Server kommunizieren. Hier ist eine kurze Beschreibung jeder Klasse:

1. **LocalListener:**
   - Implementiert das `MessageListener`-Interface, um Nachrichten von den Message Queues zu verarbeiten.
   - Bestätigt und verarbeitet Nachrichten basierend auf ihren Themen (ACKs oder JSON-Nachrichten).
   - Speichert empfangene Daten in geeigneten Datenstrukturen in der Klasse `Warehouse2Application`.

2. **Receiver:**
   - Stellt eine Verbindung zum ActiveMQ-Broker her.
   - Richtet einen Message Consumer ein, um einem bestimmten Thema oder einer Warteschlange zuzuhören.
   - Nutzt den `LocalListener`, um eingehende Nachrichten zu verarbeiten.

3. **Sender:**
   - Stellt eine Verbindung zum ActiveMQ-Broker her.
   - Richtet einen Message Producer ein, um Nachrichten an ein bestimmtes Thema oder eine Warteschlange zu senden.

4. **Product:**
   - Stellt ein Produkt mit Eigenschaften wie ID, Name, Kategorie, Menge und Einheit dar.
   - Generiert bei der Instantiierung zufällig Produktdaten.

5. **WarehouseData:**
   - Stellt Daten für ein Lager dar, einschließlich einer ID, eines Namens, eines Zeitstempels, von Standortdetails und einer Liste von Produkten.
   - Verwendet den Jackson ObjectMapper für die JSON-Serialisierung.

6. **WarehouseConsumer:**
   - Controller zur Bearbeitung von Anfragen im Zusammenhang mit dem Lagerverbraucher (Benutzeroberfläche).

7. **WarehouseController:**
   - Hauptcontroller zur Bearbeitung von REST-Endpunkten für die Lageranwendung.
   - Verwendet `WarehouseService`, um Lagerdaten abzurufen.
   - Stellt Endpunkte bereit, um Lagerdaten in JSON- oder XML-Format abzurufen und zu übertragen.

8. **WarehouseService:**
   - Serviceklasse zur Bereitstellung von Begrüßungen und zum Abrufen von Lagerdaten.
   - Verwendet eine `WarehouseSimulation`, um simulierte Lagerdaten zu generieren.

9. **WarehouseSimulation:**
   - Simuliert die Generierung von Lagerdaten basierend auf vordefinierten Städten.

10. **Warehouse2Application:**
    - Hauptanwendungsklasse, die `CommandLineRunner` implementiert.
    - Initialisiert Sender- und Empfängerinstanzen für jeden Lagerstandort und das Hauptthema.

Die Anwendung startet, indem man Verbindungen und Listener für jeden Lagerstandort herstellt. Der zentrale Server verarbeitet eingehende Daten, sendet Bestätigungen und bietet REST-Endpunkte zum Abrufen und Übertragen von Lagerdaten. Die simulierten Daten umfassen Produkte und Lagerinformationen.


## Fragenstellungen

**Eigenschaften der Message Oriented Middleware (MOM):**

1. **Nachrichtenorientiert:**
   - MOM behandelt die Kommunikation zwischen verteilten Anwendungen durch den Austausch von Nachrichten.

2. **Asynchron:**
   - Die Kommunikation zwischen den Komponenten erfolgt asynchron, d.h., Absender und Empfänger sind zeitlich entkoppelt.

3. **Zuverlässigkeit:**
   - MOM gewährleistet die zuverlässige Übermittlung von Nachrichten, selbst wenn bestimmte Komponenten vorübergehend nicht verfügbar sind.

4. **Nachrichtenvermittlung:**
   - Nachrichten werden nicht direkt zwischen den Anwendungen, sondern über einen zentralen Message Broker oder eine Middleware-Plattform übermittelt.

**Transiente und synchrone Kommunikation:**

- **Transiente Kommunikation:**
  - Bei transienter Kommunikation erfolgt der Austausch von Nachrichten ohne dauerhafte Speicherung. Die Nachricht wird nur für die Dauer der Übermittlung gespeichert.

- **Synchrone Kommunikation:**
  - Bei synchroner Kommunikation wartet der Absender auf eine unmittelbare Antwort des Empfängers, bevor der Prozess fortgesetzt wird.

**Funktionsweise einer JMS Queue:**

- Eine JMS Queue ist eine Warteschlange für die asynchrone, point-to-point-Kommunikation.
- Sender senden Nachrichten an die Queue, und Empfänger erhalten die Nachrichten in der Reihenfolge, in der sie in der Queue eintreffen.
- Jeder Empfänger liest eine Nachricht und bestätigt deren Empfang, um sie aus der Queue zu entfernen.

**JMS Overview - Wichtige JMS-Klassen und deren Zusammenhang:**

1. **ConnectionFactory:**
   - Erstellt eine Connection zu einem JMS-Broker.

2. **Connection:**
   - Bietet eine Verbindung zum JMS-Broker.

3. **Session:**
   - Sitzung für die Erstellung von Producern und Consumern.

4. **Destination:**
   - Ziel, wohin Nachrichten gesendet oder von wo sie abgerufen werden.

5. **MessageProducer:**
   - Sendet Nachrichten an ein Ziel.

6. **MessageConsumer:**
   - Empfängt und verarbeitet Nachrichten von einem Ziel.

**Funktionsweise eines JMS Topic:**

- Ein JMS Topic ermöglicht die asynchrone, publish/subscribe-Kommunikation.
- Sender (Publisher) senden Nachrichten an ein Topic, und alle Abonnenten (Subscriber), die auf dieses Topic hören, erhalten eine Kopie der Nachricht.

**Lose gekoppeltes verteiltes System:**

- In einem lose gekoppelten verteilten System sind die Komponenten unabhängig voneinander und kommunizieren über Nachrichten ohne direkte Abhängigkeiten.
- Beispiel: Ein verteiltes System, bei dem verschiedene Mikroservices mithilfe von Nachrichten oder APIs kommunizieren.
- Es wird als "lose gekoppelt" bezeichnet, da Änderungen an einem System die anderen Systeme nicht unmittelbar beeinflussen, was Flexibilität und Skalierbarkeit ermöglicht.

