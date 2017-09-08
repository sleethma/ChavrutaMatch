<?php
require "chavruta_init.php";
//$hostFirstName = $_POST["host_first_name"];


 $sql= "UPDATE chavruta_hosts SET chavruta_request_1 = "1352475" WHERE auto_inc = 5;";

  if(mysqli_query($connection, $sql))
  {
  echo " values added";
  }else{
    echo "values not added";
  } 
 ?>

