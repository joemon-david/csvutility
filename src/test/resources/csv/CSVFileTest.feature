Feature: Test CSV Operation


  Scenario: Test
    Given Read file "data//input//people.csv" to get all records where coulumn "country" equals "Iraq" and create file "data//output//iraq.csv" with columns "name,age,country"


   Scenario Outline: Test
     Given  Read file <inputFilePath> to get all records where coulumn <headercolumn> equals <matchingvalue> and create file <outputFilePath> with columns <OutputColumns>

     Examples:
     |inputFilePath|headercolumn|matchingvalue|outputFilePath|OutputColumns|
     |"data//input//people.csv"|"country"|"Monaco"|"data//output//monaco.csv"|"name,age,country"|
     |"data//input//people.csv"|"country"|"Benin"|"data//output//benin.csv"|"name,age,country"|

  @RunNow
  Scenario: Multiple Filter Test
      Given Read xls file "data//input//people.csv"
      And Select all the xls records that matches below condition
      When the xls column header "Country" equals "Syria"
      And the header "Age" equals "30"
      Then create a csv file "data//output//syria.csv" from xls file with fields "name,age,country"
