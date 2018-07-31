package be.reactiveprogramming.demo.reactivespringdatastart;

import be.reactiveprogramming.demo.reactivespringdatastart.data.ConcertTicket;
import be.reactiveprogramming.demo.reactivespringdatastart.repository.ConcertTicketRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@SpringBootApplication
@EnableReactiveMongoRepositories
public class ReactiveSpringDataStartApplication {

    public static void main(String[] args) throws InterruptedException {
        ConfigurableApplicationContext application = SpringApplication.run(ReactiveSpringDataStartApplication.class, args);

        final ConcertTicketRepository concertTicketRepository = application.getBean(ConcertTicketRepository.class);

        final Mono<Void> emptyDatabaseFlow =
                concertTicketRepository.deleteAll();

        final Mono<Void> saveTicketsFlow =
                concertTicketRepository.save((new ConcertTicket("ABBA", "Jerry")))
                .and(concertTicketRepository.save((new ConcertTicket("Hawkwind", "Kris"))));

        final Flux<ConcertTicket> printConcertTicketsOverviewFlow =
                concertTicketRepository.findAll()
                .flatMap(ticket -> printTicketInformation(ticket));

        emptyDatabaseFlow
        .then(saveTicketsFlow)
        .thenMany(printConcertTicketsOverviewFlow)
        .subscribe();

        System.out.println("Will wait for the information to be printed from the database");
        Thread.sleep(1000);
    }

    private static Mono<ConcertTicket> printTicketInformation(ConcertTicket concertTicket) {
        System.out.println(String.format("Ticket Artist: %s Buyer: %s", concertTicket.getArtist(), concertTicket.getBuyer()));
        return Mono.just(concertTicket);
    }
}
