package com.driver.services;


import com.driver.EntryDto.BookTicketEntryDto;
import com.driver.model.Passenger;
import com.driver.model.Ticket;
import com.driver.model.Train;
import com.driver.repository.PassengerRepository;
import com.driver.repository.TicketRepository;
import com.driver.repository.TrainRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TicketService {

    @Autowired
    TicketRepository ticketRepository;

    @Autowired
    TrainRepository trainRepository;

    @Autowired
    PassengerRepository passengerRepository;


    public Integer bookTicket(BookTicketEntryDto bookTicketEntryDto)throws Exception{

        //Check for validity
        //Use bookedTickets List from the TrainRepository to get bookings done against that train
        // Incase the there are insufficient tickets
        // throw new Exception("Less tickets are available");
        //otherwise book the ticket, calculate the price and other details
        //Save the information in corresponding DB Tables
        //Fare System : Check problem statement
        //Incase the train doesn't pass through the requested stations
        //throw new Exception("Invalid stations");
        //Save the bookedTickets in the train Object
        //Also in the passenger Entity change the attribute bookedTickets by using the attribute bookingPersonId.
       //And the end return the ticketId that has come from db

        // Get the Train
        Optional<Train> optionalTrain = trainRepository.findById(bookTicketEntryDto.getTrainId());
        if(!optionalTrain.isPresent()){
            throw new Exception("Invalid trainId");
        }
        Train train = optionalTrain.get();

        // Get the BookingPerson
        Optional<Passenger> optionalBookingPerson = passengerRepository.findById(bookTicketEntryDto.getBookingPersonId());
        if(!optionalBookingPerson.isPresent()){
            throw new Exception("Invalid stations");
        }
        Passenger bookingPerson = optionalBookingPerson.get();

        // Check if there is enough seats
        int requiredSeats = bookTicketEntryDto.getNoOfSeats();
        int availableSeats = train.getNoOfSeats() - train.getBookedTickets().size();
        if(requiredSeats > availableSeats){
            throw new Exception("Less tickets are available");
        }

        // Check if the train passes through the route
        String[] stations = train.getRoute().split(",");
        boolean startStation = false;
        boolean endStation = false;
        int totalCost = 0;
        for(String station : stations){
            if(station.equals(bookTicketEntryDto.getFromStation().toString())){
                startStation = true;
            }
            if(station.equals(bookTicketEntryDto.getToStation().toString())){
                endStation = true;
            }
            // Calculating the total fare
            if(startStation && !endStation){
                totalCost += 300;
            }
        }
        if(!startStation || !endStation){
            throw new Exception("Invalid stations");
        }

        // Get all the Passengers
        List<Passenger> passengers = new ArrayList<>();
        for(Integer id : bookTicketEntryDto.getPassengerIds()){
            Optional<Passenger> optionalPassenger = passengerRepository.findById(id);
            if(optionalPassenger.isPresent()){
                passengers.add(optionalPassenger.get());
            }
        }


        Ticket ticket = new Ticket();
        ticket.setFromStation(bookTicketEntryDto.getFromStation());
        ticket.setToStation(bookTicketEntryDto.getToStation());
        ticket.setTotalFare(totalCost);
        ticket.setPassengersList(passengers);

        // Save the ticket into db
        Ticket savedTicket = ticketRepository.save(ticket);

        // Save the ticket into Train and BookingPerson tables
        train.getBookedTickets().add(savedTicket);
        Train savedTrain = trainRepository.save(train);

        bookingPerson.getBookedTickets().add(savedTicket);
        Passenger savedBookingPerson = passengerRepository.save(bookingPerson);

       return savedTicket.getTicketId();

    }
}
