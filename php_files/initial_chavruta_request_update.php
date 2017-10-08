<?php
require "chavruta_init.php";
//replace this with userId and chavrutaId number vars
/*
$user_id = "0505050505";
$chavruta_id = 17;
$openRequestSlot = "chavruta_request_1";
$requesterAvatarColumn = "chavruta_request_1_avatar";
$requesterNameColumn = "chavruta_request_1_name";
$requesterAvatar = "777";
$requesterName = "Boingo";
*/
//if error, it is in top vars!
$user_id = $_POST["user_id"];
$chavruta_id = $_POST["chavruta_id"];
$openRequestSlot = $_POST["open_request_slot"];
$requesterAvatarColumn = $_POST["requester_avatar_column"];
$requesterNameColumn = $_POST["requester_name_column"];
$requesterAvatar = $_POST["requester_avatar"];
$requesterName = $_POST["requester_name"];


  $sql=  
   "UPDATE chavruta_hosts SET $openRequestSlot = '$user_id', $requesterAvatarColumn = '$requesterAvatar', $requesterNameColumn = '$requesterName'   WHERE auto_inc = '$chavruta_id';";


  if(mysqli_query($connection, $sql))
  {
  echo " values added: request match # " + '$openRequestSlot';
  }else{
    echo "values not added";
  } 
 ?>
