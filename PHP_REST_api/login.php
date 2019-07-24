<?php
$reply = array();
include 'db_conn.php';

if(isset($_POST['email']) && isset($_POST['password'])){
	$email = $_POST['email'];
    $password = $_POST['password'];
    
    $query =
    "SELECT 
        user_id, password_hash
    FROM 
        users 
    WHERE 
        email = ?";

	if($stmt = $conn->prepare($query)){
		$stmt->bind_param("s",$email);
		$stmt->execute();
		$stmt->bind_result($user_id, $password_hash);
		if($stmt->fetch()){
			if(password_verify($password,$password_hash)){
				$response["status"] = 0;
				$response["message"] = "Login successful";
				$response["user_id"] = $user_id;
			}
			else{
				$response["status"] = 1;
				$response["message"] = "Invalid username and password combination";
			}
		}
		else{
			$response["status"] = 1;
			$response["message"] = "Invalid username and password combination";
		}
		
		$stmt->close();
	}
}
else{
	$response["status"] = 2;
	$response["message"] = "Missing mandatory parameters";
}
//Display the JSON response
echo json_encode($response);
?>