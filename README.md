## ThreadStorm

This is the final projet of module Java Advanced

The goal of this project is to create a distributed task scheduling system that allows multiple clients to connect to a server via sockets. Specialized workers will be used to perform tasks submitted by clients and connected to the server via RMI

The goal of this project is to create a distributed task scheduling system that allows multiple clients to connect to a server via sockets. Specialized workers will be used to perform tasks submitted by clients and connected to the server via RMI. Proposed tasks include using filters and convolution to process images, as well as displaying a curve of temperature and humidity variation read from a sensor. A special worker will be dedicated to reading data from an Arduino sensor.

## Folder Structure

The workspace contains two folders by default, where:

> **MainServer**
>> **`MainServer.java`**: where is the main function  
>> **`ServerThread.java`**: the class that execute tasks from taskQueue  
>>
>> `Task.java`: task object that contains taskId and client's socket  
>> `TaskQueue.java`: a blockingQueue to add and take tasks  
>> `ImageFilters.java`: Filter functions that can be used by the thread (ServerThread)  
>> `ImageDivider.java`: functions to divide and merge images  

___

> **Worker**
>> **`Worker.java`**: main function of a worker  
>>
>> `ImplFilters.java`: implimentation class for the Filter interface that is shared between worker and ServerThread  

___

> **ArduinoSen**
>> `SliverHandT`: folder that contains programs  
>> `test.txt`: exemple the text file with data recieved from the sensor

## Run programs

MainServer

```bash
java MainServer/MainServer.java
```

Worker

```bash
java Worker/Worker.java
```

Client

```bash
java Client/Client.java
```
