# Spring Boot with Reactor and Spring Data
Sample of a simple use of Reactor.io, Spring Data and Spring Boot.

#### How to use?

To build a simple Docker image to this application, use the following command:

    docker build -f Dockerfile -t flavio/reactorapp:1.0.0 .

To run the container, use the following command:

    docker run -it --cpuset-cpus="1" -m 256M --name flavio/reactorapp -p 80:3000 flavio/reactorapp

To test the app you can do a POST to:

    http://{your_domain}/task

With a JSON:

    {"description":"This is a test task!"}

And to retrieve the added tasks request a GET to:

    http://{your_domain}/tasks
