<?php

require_once 'checkSession.php';

require_once '../API/Product.php';
header('Content-type: application/json');
$objProduct = new Product();
$objProduct->ID = null;
$objProduct->Name = $_POST["name"];
$objProduct->Volume = $_POST["volume"];
$objProduct->Price = $_POST["price"];
$objProduct->ImageURL = $_POST["imageURL"];
$objProduct->LCBOProductID = $_POST["lcbo_product_id"];
$objProduct->ProductCategoryID = $_POST["product_category_id"];
$result = $objProduct->add();
echo json_encode(array("result" => $result));

?>