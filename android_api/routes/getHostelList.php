<?php
    include_once '../config/database.php';

    // header("Content-Type: application/json; charset=UTF-8");

    
    if(isset($_GET['city'])){
        $city = $_GET['city'];
    }
    
    $stmt = $conn->prepare("SELECT * FROM dharamshalas WHERE  d_city LIKE  '%{$city}%' ORDER BY d_id ;");
    $stmt->execute();
    $result = $stmt->get_result();
    $outp = $result->fetch_all(MYSQLI_ASSOC);

    echo json_encode($outp);
?>

