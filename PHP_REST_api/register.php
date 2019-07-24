<?php

include 'db_conn.php';

function emailExists($email){

    $query = 
    "SELECT 
        email 
    FROM 
        users 
    WHERE 
        email = ?";
    
	global $conn;
	if($stmt = $conn->prepare($query)){
		$stmt->bind_param("s",$email);
		$stmt->execute();
		$stmt->store_result();
		$stmt->fetch();
		if($stmt->num_rows == 1){
			$stmt->close();
			return true;
		}
		$stmt->close();
	}

	return false;
}

$reply = array();

if(isset($_POST['email']) && isset($_POST['password'])){
	$email = $_POST['email'];
	$password_hash = password_hash($_POST['password'], PASSWORD_DEFAULT);
	if(!emailExists($email)){
		$regquery =
		"INSERT INTO 
			users(email, password_hash)
		VALUES
			(?,?)";
		
		if($stmt = $conn->prepare($regquery)){
			$stmt->bind_param("ss",$email,$password_hash);
			$stmt->execute();
			$reply["status"] = 0;
			$reply["message"] = "User created";
			$stmt->close();
		}
	} else {
		$reply["status"] = 1;
		$reply["message"] = "Email exist in system";
	}
} else {
	$reply["status"] = 2;
	$reply["message"] = "Missing email or password";
}

echo json_encode($reply);
?>