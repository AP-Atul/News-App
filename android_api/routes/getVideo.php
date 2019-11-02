<?php
    include_once '../config/database.php';

    // header("Content-Type: application/json; charset=UTF-8");

    $stmt = $conn->prepare("SELECT * FROM video ORDER BY video_id DESC;");
    $stmt->execute();
    $result = $stmt->get_result();
    $outp = $result->fetch_all(MYSQLI_ASSOC);

    echo json_encode($outp);
?>

