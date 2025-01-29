//package com.cleartax.training_superheroes.services;
//
//import com.cleartax.training_superheroes.config.SqsConfig;
//import com.cleartax.training_superheroes.entities.Superhero;
//import com.cleartax.training_superheroes.repos.SuperheroRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Service;
//import software.amazon.awssdk.services.sqs.SqsClient;
//import software.amazon.awssdk.services.sqs.model.*;
//import io.awspring.cloud.sqs.annotation.SqsListener;
package com.cleartax.training_superheroes.services;

import com.cleartax.training_superheroes.config.SqsConfig;
import io.awspring.cloud.sqs.annotation.SqsListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
//import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.cleartax.training_superheroes.entities.Superhero;
import com.cleartax.training_superheroes.repos.SuperheroRepository;

import java.util.List;

@Service
public class SuperheroConsumer {
    @Autowired
    private SqsConfig sqsConfig;

    @Autowired
    private SuperheroRepository superheroRepository;

    @Value("${sqs.queue.url}")
    private String queueUrl;

    @SqsListener("${sqs.queue.name}")
    public void listenToQueue(String messageBody) {
        System.out.println("Received message: " + messageBody);
        if (messageBody.contains("vihanagarwal")) {
            messageBody = messageBody.replace("vihanagarwal", "vihanagarwal updated");
            System.out.println("Updated message: " + messageBody);
            List<Superhero> superheroes = superheroRepository.findAllByName("vihanagarwal");
            if (!superheroes.isEmpty()) {
                for (Superhero superhero : superheroes) {
                    superhero.setName("vihanagarwal updated");
                    superheroRepository.save(superhero);
                }
                System.out.println("Superhero name updated in the database.");
            } else {
                System.out.println("Superhero with name 'vihan' not found in the database.");
            }
        } else {
            System.out.println("Original message: " + messageBody);
        }
    }
}
//@Service
//public class SuperheroConsumer {
//  @Autowired
//  private SqsConfig sqsConfig;
//  @Autowired
//  private SqsClient sqsClient;
//  @Autowired
//  private SuperheroRepository superheroRepository;
//
////  @Scheduled(fixedDelay = 1000)
////  public void consumeSuperhero() {
////    try {
////      ReceiveMessageResponse receiveMessageResponse = sqsClient.receiveMessage(ReceiveMessageRequest.builder()
////              .queueUrl(sqsConfig.getQueueUrl())
////              .maxNumberOfMessages(1)  // Adjust according to your needs
////              .waitTimeSeconds(10) // Enable long polling
////              .build());
////
////      receiveMessageResponse.messages().forEach(message -> {
////        String body = message.body();
////        System.out.println("Received message: " + body);
////        if (body.contains("vihanagarwal")) {
////          body = body.replace("vihanagarwal", "vihanagarwal updated");
////          System.out.println("Updated message: " + body);
////          List<Superhero> superheroes = superheroRepository.findAllByName("vihanagarwal");
////          if (!superheroes.isEmpty()) {
////            for (Superhero superhero : superheroes) {
////              superhero.setName("vihanagarwal updated");
////              superheroRepository.save(superhero);
////            }
////            System.out.println("Superhero name updated in the database.");
////          } else {
////            System.out.println("Superhero with name 'vihan' not found in the database.");
////          }
////        } else {
////          System.out.println("Original message: " + body);
////        }
////        sqsClient.deleteMessage(DeleteMessageRequest.builder()
////                .queueUrl(sqsConfig.getQueueUrl())
////                .receiptHandle(message.receiptHandle())
////                .build());
////      });
////    } catch (SqsException e) {
////      System.err.println("SQS error: " + e.awsErrorDetails().errorMessage());
////      System.out.println("The queue might be empty!!");
////    }
////  }
//    @SqsListener("${sqs.queue.url}")
//    public void listenmessage(String name){
//        System.out.println(name);
//    }
//}