<?php
require "chavruta_init.php";
$requester_id = $_POST["requester_id"];
$chavruta_id = $_POST["chavruta_id"];
//get string host_id for auto_inc 
$chavruta_id_int = (int)$chavruta_id;



 $sql= "UPDATE chavruta_hosts SET confirmed = '$requester_id' WHERE auto_inc = '$chavruta_id_int';";

  if(mysqli_query($connection, $sql))
  {
  echo "chavruta confirmed: " + $requester_id ;
  }else{
    echo "values not added";
  } 	
 ?>