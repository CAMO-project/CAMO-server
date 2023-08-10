/*
package team.moca.camo.data;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.stereotype.Component;
import team.moca.camo.domain.Cafe;
import team.moca.camo.domain.Location;
import team.moca.camo.domain.User;
import team.moca.camo.domain.embedded.Address;
import team.moca.camo.repository.CafeLocationRepository;
import team.moca.camo.repository.CafeRepository;
import team.moca.camo.repository.UserRepository;

import javax.annotation.PostConstruct;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class CafeDataInitializer {

    private static final String CSV_FILE_PATH = "/Users/jcw/cafe.csv";

    private final CafeRepository cafeRepository;
    private final CafeLocationRepository cafeLocationRepository;
    private final UserRepository userRepository;

    public CafeDataInitializer(CafeRepository cafeRepository, CafeLocationRepository cafeLocationRepository, UserRepository userRepository) {
        this.cafeRepository = cafeRepository;
        this.cafeLocationRepository = cafeLocationRepository;
        this.userRepository = userRepository;
    }

    @PostConstruct
    protected void postConstruct() {
        this.initializeCafeData();
    }

    public void initializeCafeData() {
        User user = userRepository.findById("user_6974a0aa92834b1d944ffd4d90e8cbf9").get();
        List<CSVRow> cafeRow = readCSVFile();
        cafeRow.forEach(csvRow -> {
            Cafe cafe = convertRowToCafe(csvRow);
            cafe.updateOwner(user);
            Location location = convertRowToLocation(cafe, csvRow);
            cafeRepository.save(cafe);
            cafeLocationRepository.save(location);
        });
    }

    private List<CSVRow> readCSVFile() {
        List<CSVRow> rows = new ArrayList<>();

        try (CSVReader reader = new CSVReaderBuilder(new FileReader(CafeDataInitializer.CSV_FILE_PATH)).withSkipLines(1).build()) {
            String[] header = {"name", "category", "state", "city", "town", "road_address", "address_details", "latitude", "longitude"};
            String[] data;

            while ((data = reader.readNext()) != null) {
                CSVRow row = new CSVRow();
                for (int i = 0; i < header.length; i++) {
                    String value = (i < data.length) ? data[i] : "";
                    switch (header[i]) {
                        case "name":
                            row.setName(value);
                            break;
                        case "state":
                            row.setState(value);
                            break;
                        case "city":
                            row.setCity(value);
                            break;
                        case "town":
                            row.setTown(value);
                            break;
                        case "road_address":
                            row.setRoadAddress(value);
                            break;
                        case "address_details":
                            row.setAddressDetails(value);
                            break;
                        case "latitude":
                            row.setLatitude(Double.parseDouble(value));
                            break;
                        case "longitude":
                            row.setLongitude(Double.parseDouble(value));
                            break;
                    }
                }
                rows.add(row);
            }
        } catch (CsvValidationException | IOException e) {
            e.printStackTrace();
        }
        return rows;
    }

    private Cafe convertRowToCafe(CSVRow csvRow) {
        return Cafe.builder()
                .name(csvRow.getName())
                .introduction("맛있는 커피~")
                .contact("010" + (int) (Math.random() * 10000) + (int) (Math.random() * 10000))
                .address(Address.builder()
                        .state(csvRow.getState())
                        .city(csvRow.getCity())
                        .town(csvRow.getTown())
                        .roadAddress(csvRow.getRoadAddress())
                        .addressDetail(csvRow.getAddressDetails())
                        .build())
                .businessRegistrationNumber("8735632945")
                .build();
    }

    private Location convertRowToLocation(Cafe cafe, CSVRow csvRow) {
        return Location.builder()
                .id(cafe.getId())
                .coordinates(new GeoJsonPoint(csvRow.getLongitude(), csvRow.getLatitude()))
                .build();
    }
}

@Getter
@Setter
class CSVRow {

    private String name;
    private String state;
    private String city;
    private String town;
    private String roadAddress;
    private String addressDetails;
    private double latitude;
    private double longitude;

    public CSVRow() {
    }

    public CSVRow(String name, String state, String city, String town, String roadAddress, String addressDetails, double latitude, double longitude) {
        this.name = name;
        this.state = state;
        this.city = city;
        this.town = town;
        this.roadAddress = roadAddress;
        this.addressDetails = addressDetails;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
*/
