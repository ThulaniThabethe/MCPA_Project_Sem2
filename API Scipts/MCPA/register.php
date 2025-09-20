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

// Check if the required keys are set before accessing them
if (!isset($_POST['email']) || !isset($_POST['password'])) {
    die(json_encode(["success" => false, "message" => "Required email and password fields are missing."]));
}

// Get the posted data from the Android app
$email = $_POST['email'];
$password = $_POST['password'];

// Hash the password securely
$hashed_password = password_hash($password, PASSWORD_DEFAULT);

// Check if email already exists
$sql_check = "SELECT user_id FROM users WHERE email = ?";
$stmt_check = $conn->prepare($sql_check);
$stmt_check->bind_param("s", $email);
$stmt_check->execute();
$stmt_check->store_result();

if ($stmt_check->num_rows > 0) {
    echo json_encode(["success" => false, "message" => "Email already registered."]);
    $stmt_check->close();
    $conn->close();
    exit();
}
$stmt_check->close();

// Prepare and bind the SQL INSERT statement with only email and password
$sql_insert = "INSERT INTO users (email, password_hash) VALUES (?, ?)";
$stmt_insert = $conn->prepare($sql_insert);
$stmt_insert->bind_param("ss", $email, $hashed_password);

// Execute the statement
if ($stmt_insert->execute()) {
    echo json_encode(["success" => true, "message" => "Registration successful!"]);
} else {
    echo json_encode(["success" => false, "message" => "Registration failed: " . $stmt_insert->error]);
}

$stmt_insert->close();
$conn->close();
?> 
