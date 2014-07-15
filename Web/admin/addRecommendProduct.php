<?php
require_once 'checkSession.php';

require_once '../API/RecommendCategory.php';
require_once '../API/RecommendProductList.php';
header('Content-type: application/json');
$objRecommendProductList = new RecommendProductList();
$objRecommendCategory = new RecommendCategory();
$objRecommendCategory->Mood_Category_ID = $_POST['mood_category_id'];
$objRecommendCategory->Weekday = $_POST['weekday'];
$recommend_category_id = $objRecommendCategory->getIDByWeekdayAndMood();

$objRecommendProductList->Product_ID = $_POST['product_id'];
$objRecommendProductList->Recommend_Category_ID = $recommend_category_id;

$result = $objRecommendProductList->add();

echo json_encode(array("result" => $result));

?>