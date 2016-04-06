<?php

// ################################################
//  Config
// ################################################

define('IncludeGuard', TRUE);
//DB-Config
require_once("db_credentials.php");

// ################################################
//  Functions
// ################################################

// ++++++++++++++++++++++++++++++++++++++++++++++++
// Make DB connection
// ++++++++++++++++++++++++++++++++++++++++++++++++
function connectDb(){
  
  // Import settings
  global $db_name; global $db_user; global $db_password; global $db_host;
  
  // Make connection
  $link = mysqli_connect($db_host, $db_user, $db_password, $db_name);

  // Check if error
  if (!$link || mysqli_connect_errno()) {
    die ("Holy shit, the Database is fucked up.");
  }

  return $link;
}

// ++++++++++++++++++++++++++++++++++++++++++++++++
// Handle requests for LCs
// ++++++++++++++++++++++++++++++++++++++++++++++++
function getLc($how_much){

  // Get array of whole DB Table
  if($how_much == "all") {

    $link = connectDb();
    $query = "SELECT * FROM `t_learning_center` WHERE 1;";

    if ($result = mysqli_query($link, $query)) {
      $rows = array();
      while($r = mysqli_fetch_assoc($result)) {
          $rows[] = $r;
      }

      // Encode and send results
      echo json_encode($rows);

      mysqli_free_result($result);
    }
    
     mysqli_close($link);
  }
  
  // Get only one row by id
  else{
    echo "TODO: lc id" . $how_much;
  }

}

// ++++++++++++++++++++++++++++++++++++++++++++++++
// Handle requests for Statistics
// ++++++++++++++++++++++++++++++++++++++++++++++++
function getStat($how_much){
  
  // Get array of whole DB Table
  if($how_much == "all") {
    
    $link = connectDb();
    $query = "SELECT * FROM `t_statistics` WHERE 1;";
    
    if ($result = mysqli_query($link, $query)) {
      $rows = array();
      while($r = mysqli_fetch_assoc($result)) {
          $rows[] = $r;
      }

      // Encode and send results
      echo json_encode($rows);

      mysqli_free_result($result);
    }
    
     mysqli_close($link);
  }

  // Get only one row by id
  else{
    echo "TODO: stat id";
  }
}

// ++++++++++++++++++++++++++++++++++++++++++++++++
// Handle requests for current Data
// ++++++++++++++++++++++++++++++++++++++++++++++++
function getCurrent($how_much){
  
  // Get array of whole DB Table
  if($how_much == "all") {
    $link = connectDb();
    $query = "SELECT * FROM `t_current_data` WHERE 1;";

    if ($result = mysqli_query($link, $query)) {
      $rows = array();
      while($r = mysqli_fetch_assoc($result)) {
          $rows[] = $r;
      }

      // Encode and send results
      echo json_encode($rows);

      mysqli_free_result($result);
    }
    
     mysqli_close($link);
  }

  // Get only one row by id
  else{
    echo "TODO: current id";
  }
}

// ################################################
// Select right function
// ################################################

//Handle Arguments
$what = $_GET["what"];
$how_much = $_GET["how_much"];

//Send Requests to right request function
switch ($what) {
    case "lc":
      getLc($how_much);
      break;

    case "curr":
      getCurrent($how_much);
      break;

    case "stat":
      getStat($how_much);
      break;

    default:
      echo "Permission denied";
      break;
}

?>
