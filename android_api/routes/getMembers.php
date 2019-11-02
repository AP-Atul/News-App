<?php
    include_once '../config/database.php';

    // header("Content-Type: application/json; charset=UTF-8");

    $stmt = $conn->prepare("SELECT * FROM members ORDER BY member_id DESC;");
    $stmt->execute();
    $result = $stmt->get_result();
    $outp = $result->fetch_all(MYSQLI_ASSOC);

    echo json_encode($outp);
?>