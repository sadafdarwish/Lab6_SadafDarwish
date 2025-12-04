Lab 6 – Lotto App

A JavaFX application that generates random lotto numbers, runs up to 5 times using threads, and saves each result to a MySQL database.

Features

Generates unique random numbers (0–100)

Uses SecureRandom

Runs up to 5 lotto draws with Threads

Saves results into MySQL (lotto_db)

Shows final result in the JavaFX window

Uses Hashtable to store values

Database Setup

Run in MySQL Workbench:

CREATE DATABASE lotto_db;
USE lotto_db;

CREATED TABLE lotto_results (
  id INT AUTO_INCREMENT PRIMARY KEY,
  run_number INT,
  result VARCHAR(20)
);

How to Run

Open project in Eclipse

Add JavaFX libraries

Add MySQL JDBC driver

Update DB credentials in LottoApp.java

Run app → click Run Lotto (max 5 runs)

Sample Output
Run 1 -> 84
Run 2 -> 19
Run 3 -> 1
Run 4 -> 49
Run 5 -> 46
Final Result: [84, 19, 1, 49, 46]

Author

Sadaf Darwish
