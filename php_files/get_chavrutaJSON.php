<?php
$host="localhost";
$database="n5n0l7n9_ChavrutaHostEntry";
$username="n5n0l7n9_sleethm";
$password="SleethMasterUser00";

$con = mysqli_connect($host,$username,$password,$database);

if(!connection){
  echo "connection unsuccessful";
}else{
  echo "connection successful";
}

$sql = "select * from chavruta_hosts;";

$result = mysqli_query($con, $sql);
$response = array();

while($row = mysqli_fetch_array($result))
{
	array_push($response, array(
	"hostFirstName"=>$row[0], "hostLastName"=>$row[1],"sessionMessage"=>$row[2], "sessionDate"=>$row[3], "startTime"=>$row[4], "endTime"=>$row[5],"sefer"=>$row[6], "location"=>$row[7]
	)
		);
}
echo  json_encode(array("server_response"=> $response));
mysqli_close($con);
?>