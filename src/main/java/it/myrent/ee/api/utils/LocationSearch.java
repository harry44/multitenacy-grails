package it.myrent.ee.api.utils;

import it.myrent.ee.db.MROldSede;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shivangani on 30/08/2019.
 */
public class LocationSearch {

    /**
     *
     * @param sx
     * @param description
     * @return
     */
    public static MROldSede[] byExactDescription(Session sx, String description) {
        if(description == null) {
            return new MROldSede[0];
        }
        return ((List<MROldSede>) sx.createQuery(
                "select s from MROldSede s where lower(s.descrizione) = :description").
                setParameter("description", description.toLowerCase()).
                list()).toArray(new MROldSede[0]);
    }

    /**
     *
     * @param sx
     * @param codice
     * @return
     */
    public static MROldSede[] byExactCode(Session sx, String codice) {
        if(codice == null) {
            return new MROldSede[0];
        }
        return ((List<MROldSede>) sx.createQuery(
                "select s from MROldSede s where lower(s.codice) = :codice").
                setParameter("codice", codice.toLowerCase()).
                list()).toArray(new MROldSede[0]);
    }


    /**
     * Search location by description
     * @param sx
     * @param locationDescription
     * @return
     */
    public static MROldSede[] byDescription(Session sx, String locationDescription) {
        MROldSede[] result = new MROldSede[0];
        if(locationDescription == null) {
            return result;
        }
        String address = extractAdress(locationDescription);
        String cityName = LocationSearch.extractItalianCityName(locationDescription);
        Boolean airport = LocationSearch.isAirport(locationDescription);
        Boolean station = LocationSearch.isTrainStation(locationDescription);

        if (address != null) {
            result = LocationSearch.byAddressCityType(sx, cityName, address, airport, station);
        } else {
            String description = LocationSearch.extractItalianDescription(locationDescription);
            result = LocationSearch.byExactDescription(sx, description);
            if (result.length == 0) {
                result = LocationSearch.byCityType(sx, cityName, airport, station);
            }
        }
        return result;
    }


    private static String extractAdress(String keywords) {
        keywords = keywords.toLowerCase().trim();
        int firstSpace = keywords.indexOf(' ');
        int lastSpace = keywords.lastIndexOf(' ');
        if (0 < firstSpace && firstSpace < lastSpace && lastSpace < keywords.length()) {
            return keywords.substring(firstSpace, lastSpace).trim();
        }
        return null;
    }


    /**
     * Location search by city, address and type.
     * @param sx
     * @param cityName
     * @param address
     * @param airport
     * @param station
     * @return
     */
    public static MROldSede[] byAddressCityType(Session sx, String cityName, String address, Boolean airport, Boolean station) {
        if (address == null || cityName == null) {
            return new MROldSede[0];
        }
        return ((List<MROldSede>) sx.createQuery(
                "select s from MROldSede s where " +
                        "(lower(s.citta) = :cityName or lower (s.descrizione) like :wildCityName) and " +
                        "(lower(s.via) like :wildAddress or lower(s.descrizione) like :wildAddress) and " +
                        "s.aeroporto = :airport and " +
                        "s.ferrovia = :station").
                setParameter("cityName", cityName.toLowerCase()).
                setParameter("wildCityName", "%" + cityName.toLowerCase() + "%").
                setParameter("wildAddress", "%" + address.toLowerCase() + "%").
                setParameter("airport", airport).
                setParameter("station", station).
                list()).toArray(new MROldSede[0]);
    }

    /**
     *
     * @param sx
     * @param cityName
     * @param airport
     * @param station
     * @return
     */
    public static MROldSede[] byCityType(Session sx, String cityName, Boolean airport, Boolean station) {
        if (cityName == null) {
            return new MROldSede[0];
        }
        return ((List<MROldSede>) sx.createQuery(
                "select s from MROldSede s where " +
                        "(lower(s.citta) = :cityName or lower (s.descrizione) like :wildCityName) and " +
                        "s.aeroporto = :airport and " +
                        "s.ferrovia = :station").
                setParameter("cityName", cityName.toLowerCase()).
                setParameter("wildCityName", "%" + cityName.toLowerCase() + "%").
                setParameter("airport", airport).
                setParameter("station", station).
                list()).toArray(new MROldSede[0]);
    }

    //    private static MROldSede searchLocationByCode(Session sx, String code) {
//        if (code == null) {
//            return null;
//        }
//        return (MROldSede) sx.createQuery(
//                "select s from MROldSede s where lower(s.codice) = :code").
//                setParameter("code", code.toLowerCase()).
//                setMaxResults(1).
//                uniqueResult();
//    }

    /**
     *
     * @param locationDescription
     * @return
     */

    public static String extractItalianCityName(String locationDescription) {
        return locationDescription.trim().
                toLowerCase().
                split("[\\s]")[0].replaceAll("^milan$", "milano").
                replaceAll("^rome$", "roma").
                replaceAll("^turin$", "torino").
                replaceAll("^venice$", "venezia");
    }

    /**
     *
     * @param locationDescription
     * @return
     */
    public static String extractItalianDescription(String locationDescription) {
        return locationDescription.trim().
                toLowerCase().
                replaceAll("^milan", "milano").
                replaceAll("^rome", "roma").
                replaceAll("^turin", "torino").
                replaceAll("^venice", "venezia").
                replaceAll("^naples", "napoli").
                replaceAll("airport$", "aeroporto").
                replaceAll("station$", "stazione");
    }

    /**
     *
     * @param locationList
     * @return
     */
    public static MROldSede[] extractEnabledLocations(MROldSede[] locationList) {
        List<MROldSede> enabledLocations = new ArrayList<MROldSede>();
        for(int i = 0; i < locationList.length; i++) {
            if(Boolean.TRUE.equals(locationList[i].getAttiva())) {
                enabledLocations.add(locationList[i]);
            }
        }
        return enabledLocations.toArray(new MROldSede[0]);
    }

    /**
     *
     * @param locationDescription
     * @return
     */
    public static Boolean isAirport(String locationDescription) {
        return locationDescription.trim().toLowerCase().endsWith("airport");
    }

    /**
     *
     * @param locationDescription
     * @return
     */
    public static Boolean isDowntown(String locationDescription) {
        return locationDescription.trim().toLowerCase().endsWith("downtown");
    }

    /**
     *
     * @param locationDescription
     * @return
     */
    public static Boolean isTrainStation(String locationDescription) {
        return locationDescription.trim().toLowerCase().endsWith("station");
    }
}
