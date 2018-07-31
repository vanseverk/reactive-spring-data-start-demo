package be.reactiveprogramming.demo.reactivespringdatastart.repository;

import be.reactiveprogramming.demo.reactivespringdatastart.data.ConcertTicket;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface ConcertTicketRepository extends ReactiveMongoRepository<ConcertTicket, Long> {
}
