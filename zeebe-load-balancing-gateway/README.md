# Zeebe Load Balancing Gateway

## Motivation
I want to have more than one active gateway at the same time. So, I have to balance requests between Zeebe Gateways.

## Hypothesis
Zeebe Load Balancing Gateway can balance worker requests to the same gateway.

## Requirements
1. JDK 11
2. Docker
3. Docker Compose
4. Camunda Modeler with Camunda Cloud features

## How to run
1. `mvn clean package`
2. Run the `App` class
3. Run the `docker-compose.yml`
4. Deploy `test.bpmn` via `localhost:9000`
5. Run a new task (again via `localhost:9000`)
6. Pay attention to the `We choose this client` log. It should contain pointers to the different clients!
7. This is working.

## Result
This hypothesis could be implemented. Consider using different frameworks. For example, Quarks is not so flexible for this task.