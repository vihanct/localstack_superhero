package com.cleartax.training_superheroes.services;

import com.cleartax.training_superheroes.config.SqsConfig;
import com.cleartax.training_superheroes.entities.Superhero;
import com.cleartax.training_superheroes.repos.SuperheroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.*;

import java.util.List;

@Service
public class SuperheroConsumer {
  @Autowired
  private SqsConfig sqsConfig;
  @Autowired
  private SqsClient sqsClient;
  @Autowired
  private SuperheroRepository superheroRepository;

  @Scheduled(fixedDelay = 1000)
  public void consumeSuperhero() {
    try {
      ReceiveMessageResponse receiveMessageResponse = sqsClient.receiveMessage(ReceiveMessageRequest.builder()
              .queueUrl(sqsConfig.getQueueUrl())
              .maxNumberOfMessages(1)  // Adjust according to your needs
              .waitTimeSeconds(10) // Enable long polling
              .build());

      receiveMessageResponse.messages().forEach(message -> {
        // Process the message
        String body = message.body();
        System.out.println("Received message: " + body);

        // Check if the message contains "vihan" and update it
        if (body.contains("vihanagarwal")) {
          body = body.replace("vihanagarwal", "vihanagarwal updated");
          System.out.println("Updated message: " + body);
          List<Superhero> superheroes = superheroRepository.findAllByName("vihanagarwal");
          if (!superheroes.isEmpty()) {
            for (Superhero superhero : superheroes) {
              superhero.setName("vihanagarwal updated");
              superheroRepository.save(superhero);
            }
            System.out.println("Superhero name(s) updated in the database.");
          } else {
            System.out.println("Superhero with name 'vihan' not found in the database.");
          }
        } else {
          // Print the original message if it does not contain "vihan"
          System.out.println("Original message: " + body);
        }

        // Delete the message after processing
        sqsClient.deleteMessage(DeleteMessageRequest.builder()
                .queueUrl(sqsConfig.getQueueUrl())
                .receiptHandle(message.receiptHandle())
                .build());
      });
    } catch (SqsException e) {
      System.err.println("SQS error: " + e.awsErrorDetails().errorMessage());
      System.out.println("The queue might be empty!!");
    }
  }
}