<?php
///
//This file is used to insert some random recommend product to the database.
//It will read all products and then randomly recommend 10 products to each recommend category (Weekday/Mood_Category)
///
set_time_limit(0);
require_once('Product.php');
require_once('ProductCategory.php');
require_once('RecommendCategory.php');
require_once('RecommendProductList.php');

$objRecommendCategory = new RecommendCategory();
$allRecommendCategories = $objRecommendCategory->all();

$objProduct = new Product();
$allProducts = $objProduct->all();

$objRecommendProductList = new RecommendProductList();

foreach ($allRecommendCategories as $category)
{
	$objRecommendProductList->Recommend_Category_ID = $category['recommend_category_id'];
	$rand_keys = array_rand($allProducts, 10);
	// var_dump($rand_keys);
	foreach ($rand_keys as $rand_key)
	{
		// echo $allProducts[$rand_key]['name'] . '<br>';
		$objRecommendProductList->Product_ID = $allProducts[$rand_key]['product_id'];
		$objRecommendProductList->add();
	}
}

echo 'Done! Random recommendation setup!';

?>