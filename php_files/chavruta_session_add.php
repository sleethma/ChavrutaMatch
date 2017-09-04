<?php
require "chavruta_init.php";
$userId = $_POST["user_id"];
$userName = $_POST["user_name"];
$userAvatarNumber=$_POST["user_avatar_number"];
$userFirstName=$_POST["user_first_name"];
$userLastName=$_POST["user_last_name"];
$userPhoneNumber=$_POST["user_phone_number"];
$userEmail=$_POST["user_email"];
$userBio=$_POST["user_bio"];




 $sql= "insert into user_profiles values(NULL, $userId','$userName','$userAvatarNumber','$userFirstName',
'$userLastName','$userPhoneNumber','$userEmail','$userBio');";

  if(mysqli_query($connection, $sql))
  {
  echo " values added";
  }else{
    echo "values not added";
  } 
 ?>