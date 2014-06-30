<?php

///
//This file is used to import some sample data from LCBO to local DB.
//It will read the product ids from an array and get product information of each product using LCBO API then insert it into database
///
set_time_limit(0);
require_once('Product.php');
require_once('ProductCategory.php');
$product_ids = [353995, 344226, 289801, 946996, 965293, 349423, 112185, 297580, 335356, 890772, 362905, 372227, 372037, 351478, 335778, 89045, 371351, 147504, 367565, 367573, 177824, 361394, 364299, 344358, 147512, 270504, 272617, 275834, 142679, 249458, 230862, 344325, 207340, 361360, 226522, 280859, 164566, 354068, 208918, 113217, 227678, 226530, 369777, 292672, 303222, 303198, 292706, 303214, 279570, 278788, 292490, 292508, 279604, 279612, 278770, 890780, 6452, 332577, 6395, 146829, 156240, 4705, 223545, 223537, 60707, 36434, 273771, 146837, 294041, 890798, 15495, 362061, 362053, 218859, 170019, 209056, 135459, 153213, 890806, 192344, 59311, 143123, 151928, 133009, 279620, 330365, 279703, 118927, 665430, 269258, 229781, 268938, 141804, 154302, 291989, 276071, 265397, 16329, 255281, 348490, 352104, 250365, 573352, 116251, 604348, 157974, 320218, 316117, 257238, 359471, 276287, 363010, 550749];
//$product_ids = [16329];
// asort($product_ids);
// print_r($product_ids);
// exit(0);

$objProductCategory = new ProductCategory();

$objProduct = new Product();
$i = 0;
foreach ($product_ids as $product_id)
{
	$url = 'http://lcboapi.com/products/' . $product_id;

	$cURL = curl_init();

	curl_setopt($cURL, CURLOPT_URL, $url);
	curl_setopt($cURL, CURLOPT_HTTPGET, true);
	curl_setopt($cURL, CURLOPT_HEADER, 0);
	curl_setopt($cURL, CURLOPT_RETURNTRANSFER, true);

	$result = curl_exec($cURL);	

	curl_close($cURL);

	$json = json_decode($result);
	if ($json->status == 200)
	{
		$objProduct->ID = $json->result->id;
		$objProduct->Name = $json->result->name;
		$objProduct->Price = $json->result->price_in_cents;
		$objProduct->Volume = $json->result->volume_in_milliliters;
		$objProduct->ImageURL = $json->result->image_url;

		$objProductCategory->Name = $json->result->primary_category;

		$objProduct->ProductCategoryID = $objProductCategory->getIDByName();

		$objProduct->save();
		$i++;
	}
	else
	{
		echo $json->status;
		echo '<br>';
	}
}


echo "Done! " . $i . " product(s) imported or updated!";

?>