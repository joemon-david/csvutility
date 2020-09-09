Feature: Test CSV Operation


  Scenario: Test
    Given Read file "data//input//people.csv" to get all records where coulumn "country" equals "Iraq" and create file "data//output//iraq.csv" with columns "name,age,country"

  @RunNow
   Scenario Outline: Test
     Given  Read file <inputFilePath> to get all records where coulumn <headercolumn> equals <matchingvalue> and create file <outputFilePath> with columns <OutputColumns>

     Examples:
     |inputFilePath|headercolumn|matchingvalue|outputFilePath|OutputColumns|
     |"data//input//people.csv"|"country"|"Monaco"|"data//output//monaco.csv"|"name,age,country"|
     |"data//input//people.csv"|"country"|"Benin"|"data//output//benin.csv"|"name,age,country"|