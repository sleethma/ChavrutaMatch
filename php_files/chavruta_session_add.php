<?php
require "chavruta_init.php";
$hostFirstName = $_POST["host_first_name"];
$hostLastName = $_POST["host_last_name"];
$sessionMessage=$_POST["session_message"];
$sessionDate=$_POST["session_date"];
$startTime=$_POST["start_time"];
$endTime=$_POST["end_time"];
$sefer=$_POST["sefer"];
$location=$_POST["location"];
$hostId = $_POST["host_id"];
$chavrutaRequest1 = $_POST["chavruta_request_1"];
$chavrutaRequest2 = $_POST["chavruta_request_2"];
$chavrutaRequest3 = $_POST["chavruta_request_3"];






 $sql= "insert into chavruta_hosts values(NULL, '$hostFirstName' ,'$hostLastName','$sessionMessage','$sessionDate', '$startTime', '$endTime','$sefer', '$location', '$hostId','$chavrutaRequest1', '$chavrutaRequest2', '$chavrutaRequest3');";

  if(mysqli_query($connection, $sql))
  {
  echo " values added";
  }else{
    echo "values not added";
  } 
 ?>

