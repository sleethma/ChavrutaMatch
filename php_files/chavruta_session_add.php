<?php
require "chavruta_init.php";
$sefer=$_POST["sefer"];


 $sql= "insert into CHAVRUTA_HOSTS values('$sefer');";

  if(mysqli_query($connection, $sql))
  {
  echo " values added";
  }else{
    echo "values not added";
  } 
 ?>
