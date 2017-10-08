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

user_name
user_avatar_number
user_first_name
user_last_name
user_phone_number
user_email
user_bio


 $sql= "UPDATE user_profiles SET 'user_name' = '$userName', 'user_avatar_number' = '$userAvatarNumber', 'userFirstName' = '$userFirstName', 'user_last_name' = '$userLastName', 'user_phone_number' = '$userPhoneNumber', 
 'user_email' = '$userEmail', 'user_bio' = '$userBio' WHERE 'user_id' = '$userId';";

 // $sql=  
 //  "UPDATE chavruta_hosts SET $openRequestSlot = '$user_id', $requesterAvatarColumn = '$requesterAvatar', $requesterNameColumn = '$requesterName'   WHERE auto_inc = '$chavruta_id';";


  if(mysqli_query($connection, $sql))
  {
  echo " values added";
  }else{
    echo "values not added";
  } 
 ?>