<?php
require_once('Product.php');
require_once('RecommendCategory.php');
require_once('ProductCategory.php');
require_once('MoodCategory.php');

if (!isset($_GET['action']))
{
	header("HTTP/1.1 400 Bad Request", true, 400);
	exit ('Invalid call');
}

$action = $_GET['action'];

switch ($action) {
	//
	case 'GetRecommendProduct':
		if (!isset($_GET['w']) || !isset($_GET['mc']) || !isset($_GET['pc']))
		{
			header("HTTP/1.1 400 Bad Request", true, 400);
			exit ('Invalid call');
		}		
		$weekday = $_GET['w'];
		$mood_category_id = $_GET['mc'];
		$product_category_id = $_GET['pc'];
		$objRecommendCategory = new RecommendCategory();
		$objRecommendCategory->Weekday = $weekday;
		$objRecommendCategory->Mood_Category_ID = $mood_category_id;
		$objRecommendCategory->Product_Category_ID = $product_category_id;
		echo $objRecommendCategory->getRecommendProductList();
		break;
	
	//	
	case 'GetProductCategory':
		
		$objProductCategory = new ProductCategory();
		echo $objProductCategory->getProductCategoryList();
		break;

	//
	case 'GetProductDetail':
		if (!isset($_GET['id']))
		{
			header("HTTP/1.1 400 Bad Request", true, 400);
			exit ('Invalid call');
		}
		$product_id = $_GET['id'];
		$objProduct = new Product();
		$objProduct->ID = $product_id;
		echo $objProduct->getByID();
		break;
	//
	case 'GetMoodCategory':
		$objMoodCategory = new MoodCategory();
		echo $objMoodCategory->getMoodCategoryList();
		break;
		break;
	
	default:
		header("HTTP/1.1 400 Bad Request", true, 400);
		exit ('Invalid call');
		break;
}

?>