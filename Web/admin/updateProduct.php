<?php


require_once 'checkSession.php';

require_once '../API/Product.php';
header('Content-type: application/json');
$objProduct = new Product();
$objProduct->ID = $_POST["product_id"];
$objProduct->Name = $_POST["name"];
$objProduct->Volume = $_POST["volume"];
$objProduct->Price = $_POST["price"];
$objProduct->ImageURL = $_POST["imageURL"];
$result = $objProduct->update();
echo json_encode(array("result" => $result));

?>