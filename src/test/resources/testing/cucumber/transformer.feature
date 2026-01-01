@transformer
Feature: Message Transformer
  As a developer
  I want to transform messages to uppercase
  To ensure consistent data formatting

  Scenario: Transform a simple message
    Given I have a transformer service
    When I transform the message "hello cucumber"
    Then the result should be "HELLO CUCUMBER"

  Scenario Outline: Transform multiple messages
    Given I have a transformer service
    When I transform the message "<input>"
    Then the result should be "<output>"

    Examples:
      | input          | output          |
      | test           | TEST            |
      | bdd is cool    | BDD IS COOL     |
      | junit 5        | JUNIT 5         |
