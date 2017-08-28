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




 $sql= "insert into chavruta_hosts values('$hostFirstName' ,'$hostLastName','$sessionMessage','$sessionDate', '$startTime', '$endTime','$sefer', '$location');";

  if(mysqli_query($connection, $sql))
  {
  echo " values added";
  }else{
    echo "values not added";
  } 
 ?>

