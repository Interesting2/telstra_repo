Feature: Sim Card Activator
  We want to know if the sim card activation is successful

  Scenario Outline: Is Sim Card Activated?
    Given I have a Sim Card with iccid is "<iccid>"
    When I ask whether the Sim Card is activated
    Then querying the Sim Card with id "<id>" should show active "<isactive>"

  Examples:
  | iccid                       | id | isactive |
  | 1255789453849037777         | 1  | true     |
  | 8944500102198304826         | 2  | false    |