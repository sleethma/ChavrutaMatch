<?php
$host="localhost";
$database="n5n0l7n9_ChavrutaHostEntry";
$username="n5n0l7n9_sleethm";
$password="SleethMasterUser00";
$user_id_requesting_chavruta= $_GET["user_id"];

$con = mysqli_connect($host,$username,$password,$database);

if(!connection){
  echo "connection unsuccessful";
}

$sql = "select * from chavruta_hosts WHERE chavruta_request_1 = '$user_id_requesting_chavruta';";

$result = mysqli_query($con, $sql);
$response = array();

while($row = mysqli_fetch_array($result))
{
	array_push($response, array("chavruta_id"=>$row[0], 
	"hostFirstName"=>$row[1], "hostLastName"=>$row[2],"sessionMessage"=>$row[3], "sessionDate"=>$row[4], "startTime"=>$row[5], "endTime"=>$row[6],"sefer"=>$row[7], "location"=>$row[8], "host_id"=>$row[9], "chavruta_request_1"=>$row[10], "chavruta_request_2"=>$row[11], "chavruta_request_3"=>$row[12]
	)
		);
}
echo  json_encode(array("server_response"=> $response));
mysqli_close($con);
?>