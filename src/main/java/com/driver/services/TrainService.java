package com.driver.services;

import com.driver.EntryDto.AddTrainEntryDto;
import com.driver.EntryDto.SeatAvailabilityEntryDto;
import com.driver.model.Passenger;
import com.driver.model.Station;
import com.driver.model.Ticket;
import com.driver.model.Train;
import com.driver.repository.TrainRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TrainService {

    @Autowired
    TrainRepository trainRepository;

    public Integer addTrain(AddTrainEntryDto trainEntryDto){

        //Add the train to the trainRepository
        //and route String logic to be taken from the Problem statement.
        //Save the train and return the trainId that is generated from the database.
        //Avoid using the lombok library

        Train train = new Train();
        train.setDepartureTime(trainEntryDto.getDepartureTime());
        train.setNoOfSeats(trainEntryDto.getNoOfSeats());

        // Make the Route String
        String route = "";
        List<Station> stations = trainEntryDto.getStationRoute();
        for(int i=0;i<stations.size();i++){
            route += stations.get(i);
            if(i != stations.size() - 1){
                route += ",";
            }
        }

        // Add it to the train
        train.setRoute(route);

        // Save the train to db
        Train savedTrain = trainRepository.save(train);
        return savedTrain.getTrainId();
    }

    public Integer calculateAvailableSeats(SeatAvailabilityEntryDto seatAvailabilityEntryDto){

        //Calculate the total seats available
        //Suppose the route is A B C D
        //And there are 2 seats avaialble in total in the train
        //and 2 tickets are booked from A to C and B to D.
        //The seat is available only between A to C and A to B. If a seat is empty between 2 station it will be counted to our final ans
        //even if that seat is booked post the destStation or before the boardingStation
        //Inshort : a train has totalNo of seats and there are tickets from and to different locations
        //We need to find out the available seats between the given 2 stations.



       return null;
    }

    public Integer calculatePeopleBoardingAtAStation(Integer trainId,Station station) throws Exception{

        //We need to find out the number of people who will be boarding a train from a particular station
        //if the trainId is not passing through that station
        //throw new Exception("Train is not passing from this station");
        //  in a happy case we need to find out the number of such people.

        // Get the train
        Optional<Train> optionalTrain = trainRepository.findById(trainId);
        if(!optionalTrain.isPresent()){
            throw new Exception("Invalid TrainId");
        }
        Train train = optionalTrain.get();

        // Get the route
        String[] stations = train.getRoute().split(",");
        boolean isStationPresent = false;
        for(String route : stations){
            if(route.equals(station.toString())){
                isStationPresent = true;
                break;
            }
        }

        if(!isStationPresent){
            throw new Exception("Train is not passing from this station");
        }

        // Get the Number of Passengers boarding at Particular station
        int count = 0;
        List<Ticket> bookedTickets = train.getBookedTickets();
        for(Ticket bookedTicket : bookedTickets){
            if(bookedTicket.getFromStation().toString().equals(station.toString())){
                count++;
            }
        }

        return count;
    }

    public Integer calculateOldestPersonTravelling(Integer trainId){

        //Throughout the journey of the train between any 2 stations
        //We need to find out the age of the oldest person that is travelling the train
        //If there are no people travelling in that train you can return 0

        // Get the train
        Optional<Train> optionalTrain = trainRepository.findById(trainId);
        if(!optionalTrain.isPresent()){
            return 0;
        }
        Train train = optionalTrain.get();

        // Get the Oldest person
        int maxAge = Integer.MIN_VALUE;
        List<Ticket> bookedTickets = train.getBookedTickets();
        for(Ticket bookedTicket : bookedTickets){
            List<Passenger> passengers = bookedTicket.getPassengersList();
            for(Passenger passenger : passengers){
                if(passenger.getAge() > maxAge){
                    maxAge = passenger.getAge();
                }
            }
        }

        if(maxAge == Integer.MIN_VALUE) return 0;

        return maxAge;
    }

    public List<Integer> trainsBetweenAGivenTime(Station station, LocalTime startTime, LocalTime endTime){

        //When you are at a particular station you need to find out the number of trains that will pass through a given station
        //between a particular time frame both start time and end time included.
        //You can assume that the date change doesn't need to be done ie the travel will certainly happen with the same date (More details
        //in problem statement)
        //You can also assume the seconds and milli seconds value will be 0 in a LocalTime format.



        return null;
    }

}
