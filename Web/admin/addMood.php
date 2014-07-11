<?php
require_once 'checkSession.php';

require_once '../API/MoodCategory.php';
header('Content-type: application/json');
$objMoodCategory = new MoodCategory();
$objMoodCategory->Name = $_POST["name"];
$objMoodCategory->Description = $_POST["description"];
$result = $objMoodCategory->add();
echo json_encode(array("result" => $result));
?>