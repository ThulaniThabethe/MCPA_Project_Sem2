<?php
ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
error_reporting(E_ALL);

// Database credentials
$servername = "localhost";
$username = "root";
$password = "";
$dbname = "Woolworths";

// Create connection
$conn = new mysqli($servername, $username, $password, $dbname);

// Check connection
if ($conn->connect_error) {
    die(json_encode(["success" => false, "message" => "Connection failed: " . $conn->connect_error]));
}

// Check if the required keys are set
if (!isset($_POST['email']) || !isset($_POST['password'])) {
    die(json_encode(["success" => false, "message" => "Required email and password fields are missing."]));
}

// Get the posted data
$email = $_POST['email'];
$password = $_POST['password'];

// Prepare and execute the query to find the user by email
$sql_check = "SELECT password_hash FROM users WHERE email = ?";
$stmt_check = $conn->prepare($sql_check);
$stmt_check->bind_param("s", $email);
$stmt_check->execute();
$stmt_check->store_result();

// Check if a user with that email was found
if ($stmt_check->num_rows > 0) {
    $stmt_check->bind_result($hashed_password);
    $stmt_check->fetch();

    // Verify the password with the stored hash
    if (password_verify($password, $hashed_password)) {
        echo json_encode(["success" => true, "message" => "Login successful!"]);
    } else {
        echo json_encode(["success" => false, "message" => "Invalid password."]);
    }
} else {
    echo json_encode(["success" => false, "message" => "Email not found."]);
}

$stmt_check->close();
$conn->close();
?>
