<?php
require_once 'checkSession.php';

require_once '../API/RecommendCategory.php';

header('Content-type: application/json');
$objRecommendCategory = new RecommendCategory();
$objRecommendCategory->Weekday = $_POST["weekday"];
$objRecommendCategory->Mood_Category_ID = $_POST["mood_category_id"];
$objRecommendCategory->Product_Category_ID = $_POST["product_category_id"];
$result = $objRecommendCategory->getProductsForRecommend();
echo json_encode(array("result" => $result));

?>