<?php

require_once 'checkSession.php';

require_once '../API/Product.php';
header('Content-type: application/json');
$objProduct = new Product();
$objProduct->ID = $_POST["product_id"];

$result = $objProduct->getByID();
echo $result;

?>