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
      Given Read xls file provided in the FilePath interface
      And Select all the xls records that matches below condition
      When the xls column header "timestamp" equals "9/11/2020"
      And the xls column header "name" equals "login"
      And the xls column header "client_OS" equals "iOS 13.1.3"
      Then create a csv file "data//output//mhb.csv" from xls file with fields "timestamp,name,user_Id,client_OS" and an additional field "Build One"
