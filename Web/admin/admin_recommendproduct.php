<?php
require_once 'checkSession.php';

require_once '../API/MoodCategory.php';
require_once '../API/ProductCategory.php';

$objMoodCategory = new MoodCategory();
$allMoodCategories = $objMoodCategory->all();

$objProductCategory = new ProductCategory();
$allProductCategories = $objProductCategory->all();



?>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title></title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">
        <link rel="stylesheet" href="css/normalize.css" />
        <link rel="stylesheet" href="css/foundation.css" />  
</head>
<body>
    <?php
    include 'menu.php';
    ?>
    <div class="row">
        <h3>Recommend Product</h3>        
    </div>
    <div class="row recommendProductWizard" id="weedays">
        <h4>Weekday</h4>
        <div class="small-4 columns"><input type="radio" value="1" name="weekday" class="weekday">Monday</div>
        <div class="small-4 columns"><input type="radio" value="2" name="weekday" class="weekday">Tuesday</div>
        <div class="small-4 columns"><input type="radio" value="3" name="weekday" class="weekday">Wednesday</div>
        <div class="small-4 columns"><input type="radio" value="4" name="weekday" class="weekday">Thursday</div>
        <div class="small-4 columns end"><input type="radio" value="0" name="weekday" class="weekday">Weekends</div>
    </div>
    <div class="row recommendProductWizard hide" id="moodCategories">
        <h4>Mood</h4>
        <?php
            $last_key = end(array_keys($allMoodCategories));
            foreach ($allMoodCategories as $key => $mood)
            {
        ?>
        <div class="small-4 columns <?php if ($key == $last_key) echo ' end ' ?>"><input type="radio" class="moodCategoryID" value="<?php echo $mood['mood_category_id']; ?>" name="mood_category_id"><?php echo $mood['name']; ?></div>
        <?php
            }
        ?>
    </div>
    <div class="row recommendProductWizard hide" id="productCategories">
        <h4>Category</h4>
        <?php
            $last_key = end(array_keys($allProductCategories));
            foreach ($allProductCategories as $key => $productCategory)
            {
        ?>
        <div class="small-4 columns <?php if ($key == $last_key) echo ' end ' ?>"><input type="radio" class="productCategoryID" value="<?php echo $productCategory['product_category_id']; ?>" name="product_category_id"><?php echo $productCategory['name']; ?></div>
        <?php
            }
        ?>        
    </div>
    <div class="row recommendProductWizard hide" id="products">
        <div class="row">
            <div class="small-9 columns"><h4>Product</h4></div>
            <div class="small-3 columns"><a class="addProduct inline">Add new product</a></div>
        </div>
        <table id="productlist" class="small-12">
            <tr>
                <th>Name</th>
                <th>Volume</th>
                <th>Price</th>
                <th>LCBO ID</th>                
                <th></th>
                <th></th>
            </tr>
        </table>
    </div>
    <div id="addProductContainer" class="reveal-modal" data-reveal>
        <form id="addProductForm">
            <div class="row">
                <div class="small-3 columns"><label class="inline">LCBO Product ID</label></div>
                <div class="small-9 columns">
                    <div class="row collapse">
                        <div class="small-10 columns"><input type="text" id="lcbo_product_id_a" name="lcbo_product_id"></div>
                        <div class="small-2 columns"><a class="button postfix getLCBOProduct">Get</a></div>
                    </div>
                </div>
            </div>            
            <div class="row">
                <div class="small-3 columns"><label class="inline">Name</label></div>
                <div class="small-9 columns"><input type="text" id="product_name_a" name="name"></div>
            </div>
            <div class="row">
                <div class="small-3 columns"><label class="inline">Volume</label></div>
                <div class="small-9 columns">
                    <div class="row collapse">
                        <div class="small-10 columns"><input type="text" id="product_volume_a" name="volume"></div>
                        <div class="small-2 columns"><span class="postfix">ml</span></div>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="small-3 columns"><label class="inline">Price</label></div>
                <div class="small-9 columns">
                    <div class="row collapse">                    
                        <div class="small-10 columns"><input type="text" id="product_price_a" name="price"></div>
                        <div class="small-2 columns"><span class="postfix">cents</span></div>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="small-3 columns"><label class="inline">Image URL</label></div>
                <div class="small-9 columns"><input type="text" id="product_imageURL_a" name="imageURL"></div>
            </div>            
            <div class="row">
                <div class="small-12"><button type="submit">Submit</button></div>
            </div> 
        </form>
        <a class="close-reveal-modal">&#215;</a>
    </div> 

    <div id="editProductContainer" class="reveal-modal" data-reveal>
        <form id="editProductForm">
            <input type="hidden" id="product_id_e" name="product_id">
            <input type="hidden" id="product_inlist_e" name="product_inlist">
            <div class="row">
                <div class="small-3 columns"><label class="inline">LCBO Product ID</label></div>
                <div class="small-9 columns">
                    <div class="row collapse">
                        <div class="small-10 columns"><input type="text" disabled="disabled" id="lcbo_product_id_e" name="lcbo_product_id"></div>
                        <div class="small-2 columns"><a class="button postfix updateLCBOProduct">Get</a></div>
                    </div>
                </div>
            </div>            
            <div class="row">
                <div class="small-3 columns"><label class="inline">Name</label></div>
                <div class="small-9 columns"><input type="text" id="product_name_e" name="name"></div>
            </div>
            <div class="row">
                <div class="small-3 columns"><label class="inline">Volume</label></div>
                <div class="small-9 columns">
                    <div class="row collapse">
                        <div class="small-10 columns"><input type="text" id="product_volume_e" name="volume"></div>
                        <div class="small-2 columns"><span class="postfix">ml</span></div>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="small-3 columns"><label class="inline">Price</label></div>
                <div class="small-9 columns">
                    <div class="row collapse">                    
                        <div class="small-10 columns"><input type="text" id="product_price_e" name="price"></div>
                        <div class="small-2 columns"><span class="postfix">cents</span></div>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="small-3 columns"><label class="inline">Image URL</label></div>
                <div class="small-9 columns"><input type="text" id="product_imageURL_e" name="imageURL"></div>
            </div>            
            <div class="row">
                <div class="small-12"><button type="submit">Submit</button>&nbsp;<button type="button" class="delteProduct">Delete</button></div>
            </div> 
        </form>
        <a class="close-reveal-modal">&#215;</a>
    </div>     
    
    <div id="loading_progress">

    </div>    
    <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
    <script src="js/vendor/jquery.validate.js"></script>    
    <script src="js/foundation.min.js"></script>
    <script src="js/foundation/foundation.reveal.js"></script>    
    <script>
      $(document).foundation();
    </script>        
    <script src="js/recommendproduct.js"></script>
</body>
</html>
