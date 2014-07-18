<?php

require_once 'checkSession.php';

require_once '../API/MoodCategory.php';
header('Content-type: application/json');
$objMoodCategory = new MoodCategory();
$objMoodCategory->ID = $_POST["mood_category_id"];
$result = $objMoodCategory->delete();
echo json_encode(array("result" => $result));
?>