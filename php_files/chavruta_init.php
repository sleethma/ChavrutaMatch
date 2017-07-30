<?php
$host="localhost";
$database="n5n0I7n9_ChavrutaHostEntry";
$username="sleethm";
$password="SleethMasterUser00";

$connection = mysqli_connect($host,$username,$password,$database);

if(!connection){
  echo "connection unsuccessful";
}else{
  echo "connection successful";
}
?>
