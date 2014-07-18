<?php
require_once 'checkSession.php';


require_once "../API/MoodCategory.php";
$objMoodCategory = new MoodCategory();
$allMoodCategories = $objMoodCategory->all();
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
        <h3>Mood Categories</h3>
        <table class="small-12" id="tblMoodCategories">
            <tr>
                <th>Name</th>
                <th>Description</th>
                <th>&nbsp;</th>
                <th>&nbsp;</th>
            </tr>
    <?php
    foreach ($allMoodCategories as $category)
    {
    ?>           
            <tr>
                <td><span class="moodCategoryName"><?php echo $category["name"]; ?></span></td>
                <td><span class="moodCategoryDescription"><?php echo $category["description"]; ?></span></td>
                <td><input type="hidden" class="moodCategoryID" value="<?php echo $category["mood_category_id"]; ?>"><a class="editMoodCategory">Edit</a></td>
                <td><a class="deleteMoodCategory">Delete</a></td>
            </tr>
    <?php
    }
    ?>
        </table>
        <a class="addMoodCategory">Add</a>
    </div>

<div id="editCategryContainer" class="reveal-modal" data-reveal>
    <form id="editCategoryForm">
        <input type="hidden" id="mood_category_id" name="mood_category_id" value="">
        <div class="row">
            <div class="small-3 inline columns">Name</div>
            <div class="small-9 columns"><input type="text" id="mood_category_name" name="name"></div>
        </div>
        <div class="row">
            <div class="small-3 inline columns">Description</div>
            <div class="small-9 columns"><textarea id="mood_category_description" name="description"></textarea></div>
        </div>        
        <div class="row">
            <div class="small-12"><button>Submit</button></div>
        </div> 
    </form>
    <a class="close-reveal-modal">&#215;</a>
</div>
<div id="addCategryContainer" class="reveal-modal" data-reveal>
    <form id="addCategoryForm">
        <div class="row">
            <div class="small-3 inline columns">Name</div>
            <div class="small-9 columns"><input type="text" id="mood_category_name_a" name="name"></div>
        </div>
        <div class="row">
            <div class="small-3 inline columns">Description</div>
            <div class="small-9 columns"><textarea id="mood_category_description_a" name="description"></textarea></div>
        </div>        
        <div class="row">
            <div class="small-12"><button>Submit</button></div>
        </div> 
    </form>
    <a class="close-reveal-modal">&#215;</a>
</div>    
    <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
    <script src="js/foundation.min.js"></script>
    <script src="js/foundation/foundation.reveal.js"></script>    
    <script>
      $(document).foundation();
    </script>
    <script src="js/moodmanagement.js"></script>
</body>
</html>